package io.github.crawlerbot.repository;

import io.github.crawlerbot.domain.Crawl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Crawl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrawlRepository extends MongoRepository<Crawl, String> {

}
