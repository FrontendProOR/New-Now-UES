package com.ues.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ues.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private static final Logger logger = LogManager.getLogger(ElasticsearchConfig.class);

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUri;

    @Override
    public ClientConfiguration clientConfiguration() {
        logger.info("Configuring Elasticsearch client with URI: {}", elasticsearchUri);
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchUri.replace("http://", "").replace("https://", ""))
                .build();
    }
}
