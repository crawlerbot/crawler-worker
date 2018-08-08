package io.github.crawlerbot.repository.search;

import io.github.crawlerbot.domain.Crawl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Crawl entity.
 */
public interface CrawlSearchRepository extends ElasticsearchRepository<Crawl, String> {
}
