package com.ues.repository;

import com.ues.model.LocationIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationSearchRepository extends ElasticsearchRepository<LocationIndex, Long> {
}
