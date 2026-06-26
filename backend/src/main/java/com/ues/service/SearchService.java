package com.ues.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.ues.dto.SearchResultDto;
import com.ues.model.LocationIndex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final Logger logger = LogManager.getLogger(SearchService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<SearchResultDto> search(String name, String description, String fileDescription,
                                         Integer reviewCountFrom, Integer reviewCountTo,
                                         Float performanceFrom, Float performanceTo,
                                         Float soundFrom, Float soundTo,
                                         Float lightingFrom, Float lightingTo,
                                         Float spaceFrom, Float spaceTo,
                                         Float experienceFrom, Float experienceTo,
                                         String boolOperator, String mltField,
                                         String sortDirection) {

        boolean useAnd = "AND".equalsIgnoreCase(boolOperator);

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        List<Query> textQueries = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            textQueries.add(buildTextQuery("name", name));
        }
        if (description != null && !description.isBlank()) {
            textQueries.add(buildTextQuery("description", description));
        }
        if (fileDescription != null && !fileDescription.isBlank()) {
            textQueries.add(buildTextQuery("fileDescription", fileDescription));
        }

        if (!textQueries.isEmpty()) {
            if (useAnd) {
                textQueries.forEach(boolBuilder::must);
            } else {
                textQueries.forEach(boolBuilder::should);
                boolBuilder.minimumShouldMatch("1");
            }
        }

        addRangeFilter(boolBuilder, "reviewCount", reviewCountFrom, reviewCountTo);
        addRangeFilter(boolBuilder, "avgPerformanceGrade", performanceFrom, performanceTo);
        addRangeFilter(boolBuilder, "avgSoundGrade", soundFrom, soundTo);
        addRangeFilter(boolBuilder, "avgLightingGrade", lightingFrom, lightingTo);
        addRangeFilter(boolBuilder, "avgSpaceGrade", spaceFrom, spaceTo);
        addRangeFilter(boolBuilder, "avgExperienceGrade", experienceFrom, experienceTo);

        if (mltField != null && !mltField.isBlank()) {
            Query mltQuery = MoreLikeThisQuery.of(mlt -> mlt
                    .fields("name", "description", "fileDescription")
                    .like(l -> l.text(mltField))
                    .minTermFreq(1)
                    .maxQueryTerms(25)
                    .minDocFreq(1)
            )._toQuery();
            boolBuilder.should(mltQuery);
        }

        Query finalQuery;
        BoolQuery built = boolBuilder.build();
        if (built.must().isEmpty() && built.should().isEmpty() && built.filter().isEmpty()) {
            finalQuery = MatchAllQuery.of(m -> m)._toQuery();
        } else {
            finalQuery = built._toQuery();
        }

        NativeQueryBuilder queryBuilder = NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(PageRequest.of(0, 100))
                .withHighlightQuery(buildHighlight());

        SortOrder order = "desc".equalsIgnoreCase(sortDirection) ? SortOrder.Desc : SortOrder.Asc;
        queryBuilder.withSort(s -> s.field(f -> f.field("name.sort").order(order)));

        NativeQuery nativeQuery = queryBuilder.build();

        SearchHits<LocationIndex> hits = elasticsearchOperations.search(nativeQuery, LocationIndex.class);

        logger.info("Search returned {} results", hits.getTotalHits());

        return hits.getSearchHits().stream()
                .map(this::toDto)
                .toList();
    }

    private Query buildTextQuery(String field, String value) {
        String trimmed = value.trim();

        // Phrase: "..."
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() > 2) {
            String phrase = trimmed.substring(1, trimmed.length() - 1);
            return MatchPhraseQuery.of(mp -> mp.field(field).query(phrase))._toQuery();
        }

        // Fuzzy: ~term
        if (trimmed.startsWith("~") && trimmed.length() > 1) {
            String term = trimmed.substring(1);
            return FuzzyQuery.of(fq -> fq.field(field).value(term).fuzziness("2"))._toQuery();
        }

        // Prefix: term*
        if (trimmed.endsWith("*") && trimmed.length() > 1) {
            String prefix = trimmed.substring(0, trimmed.length() - 1);
            return PrefixQuery.of(pq -> pq.field(field).value(prefix.toLowerCase()))._toQuery();
        }

        // Default: match
        return MatchQuery.of(mq -> mq.field(field).query(trimmed))._toQuery();
    }

    private void addRangeFilter(BoolQuery.Builder builder, String field, Number from, Number to) {
        if (from == null && to == null) return;

        builder.filter(RangeQuery.of(rq -> rq
                .number(nrq -> {
                    nrq.field(field);
                    if (from != null) nrq.gte(from.doubleValue());
                    if (to != null) nrq.lte(to.doubleValue());
                    return nrq;
                })
        )._toQuery());
    }

    private HighlightQuery buildHighlight() {
        HighlightParameters params = HighlightParameters.builder()
                .withPreTags("<em>")
                .withPostTags("</em>")
                .build();

        List<HighlightField> fields = List.of(
                new HighlightField("name"),
                new HighlightField("description")
        );

        return new HighlightQuery(new Highlight(params, fields), LocationIndex.class);
    }

    private SearchResultDto toDto(SearchHit<LocationIndex> hit) {
        LocationIndex source = hit.getContent();
        SearchResultDto dto = new SearchResultDto();
        dto.setId(source.getId());
        dto.setName(source.getName());
        dto.setDescription(source.getDescription());
        dto.setReviewCount(source.getReviewCount());
        dto.setAvgPerformanceGrade(source.getAvgPerformanceGrade());
        dto.setAvgSoundGrade(source.getAvgSoundGrade());
        dto.setAvgLightingGrade(source.getAvgLightingGrade());
        dto.setAvgSpaceGrade(source.getAvgSpaceGrade());
        dto.setAvgExperienceGrade(source.getAvgExperienceGrade());

        Map<String, String> highlights = new HashMap<>();
        hit.getHighlightFields().forEach((field, fragments) -> {
            if (!fragments.isEmpty()) {
                highlights.put(field, String.join(" ... ", fragments));
            }
        });
        dto.setHighlights(highlights);

        return dto;
    }
}
