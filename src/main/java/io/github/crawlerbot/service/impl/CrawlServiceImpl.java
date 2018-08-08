package io.github.crawlerbot.service.impl;

import io.github.crawlerbot.service.CrawlService;
import io.github.crawlerbot.domain.Crawl;
import io.github.crawlerbot.repository.CrawlRepository;
import io.github.crawlerbot.repository.search.CrawlSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Crawl.
 */
@Service
public class CrawlServiceImpl implements CrawlService {

    private final Logger log = LoggerFactory.getLogger(CrawlServiceImpl.class);

    private final CrawlRepository crawlRepository;

    private final CrawlSearchRepository crawlSearchRepository;

    public CrawlServiceImpl(CrawlRepository crawlRepository, CrawlSearchRepository crawlSearchRepository) {
        this.crawlRepository = crawlRepository;
        this.crawlSearchRepository = crawlSearchRepository;
    }

    /**
     * Save a crawl.
     *
     * @param crawl the entity to save
     * @return the persisted entity
     */
    @Override
    public Crawl save(Crawl crawl) {
        log.debug("Request to save Crawl : {}", crawl);        Crawl result = crawlRepository.save(crawl);
        crawlSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the crawls.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Crawl> findAll(Pageable pageable) {
        log.debug("Request to get all Crawls");
        return crawlRepository.findAll(pageable);
    }


    /**
     * Get one crawl by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Crawl> findOne(String id) {
        log.debug("Request to get Crawl : {}", id);
        return crawlRepository.findById(id);
    }

    /**
     * Delete the crawl by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Crawl : {}", id);
        crawlRepository.deleteById(id);
        crawlSearchRepository.deleteById(id);
    }

    /**
     * Search for the crawl corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Crawl> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Crawls for query {}", query);
        return crawlSearchRepository.search(queryStringQuery(query), pageable);    }
}
