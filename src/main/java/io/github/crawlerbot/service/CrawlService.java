package io.github.crawlerbot.service;

import io.github.crawlerbot.domain.Crawl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Crawl.
 */
public interface CrawlService {

    /**
     * Save a crawl.
     *
     * @param crawl the entity to save
     * @return the persisted entity
     */
    Crawl save(Crawl crawl);

    /**
     * Get all the crawls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Crawl> findAll(Pageable pageable);


    /**
     * Get the "id" crawl.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Crawl> findOne(String id);

    /**
     * Delete the "id" crawl.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the crawl corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Crawl> search(String query, Pageable pageable);
}
