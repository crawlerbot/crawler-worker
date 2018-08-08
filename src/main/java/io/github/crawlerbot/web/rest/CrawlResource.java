package io.github.crawlerbot.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.crawlerbot.domain.Crawl;
import io.github.crawlerbot.messaging.MessagePayLoad;
import io.github.crawlerbot.service.CommandLiner;
import io.github.crawlerbot.service.CrawlService;
import io.github.crawlerbot.web.rest.errors.BadRequestAlertException;
import io.github.crawlerbot.web.rest.util.HeaderUtil;
import io.github.crawlerbot.web.rest.util.PaginationUtil;
import io.github.simlife.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Crawl.
 */
@RestController
@RequestMapping("/api")
public class CrawlResource {

    private final Logger log = LoggerFactory.getLogger(CrawlResource.class);

    private static final String ENTITY_NAME = "crawl";

    private final CrawlService crawlService;

    private final CommandLiner commandLiner;

    public CrawlResource(CrawlService crawlService, CommandLiner commandLiner) {
        this.crawlService = crawlService;
        this.commandLiner = commandLiner;
    }

    /**
     * POST  /crawls : Create a new crawl.
     *
     * @param crawl the crawl to create
     * @return the ResponseEntity with status 201 (Created) and with body the new crawl, or with status 400 (Bad Request) if the crawl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/crawls")
    @Timed
    public ResponseEntity<Crawl> createCrawl(@Valid @RequestBody Crawl crawl) throws URISyntaxException {
        log.debug("REST request to save Crawl : {}", crawl);
        if (crawl.getId() != null) {
            throw new BadRequestAlertException("A new crawl cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Crawl result = crawlService.save(crawl);
        MessagePayLoad messagePayLoad = new MessagePayLoad();
        messagePayLoad.setMessage(result.getContent());
        messagePayLoad.setBrowserOS(result.getBrowserOS());
        messagePayLoad.setMessageAction(result.getMessageAction());
        messagePayLoad.setMessageObject(result.getMessageObject());
        commandLiner.crawl(messagePayLoad);
        return ResponseEntity.created(new URI("/api/crawls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /crawls : Updates an existing crawl.
     *
     * @param crawl the crawl to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated crawl,
     * or with status 400 (Bad Request) if the crawl is not valid,
     * or with status 500 (Internal Server Error) if the crawl couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/crawls")
    @Timed
    public ResponseEntity<Crawl> updateCrawl(@Valid @RequestBody Crawl crawl) throws URISyntaxException {
        log.debug("REST request to update Crawl : {}", crawl);
        if (crawl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Crawl result = crawlService.save(crawl);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, crawl.getId().toString()))
            .body(result);
    }

    /**
     * GET  /crawls : get all the crawls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of crawls in body
     */
    @GetMapping("/crawls")
    @Timed
    public ResponseEntity<List<Crawl>> getAllCrawls(Pageable pageable) {
        log.debug("REST request to get a page of Crawls");
        Page<Crawl> page = crawlService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/crawls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /crawls/:id : get the "id" crawl.
     *
     * @param id the id of the crawl to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the crawl, or with status 404 (Not Found)
     */
    @GetMapping("/crawls/{id}")
    @Timed
    public ResponseEntity<Crawl> getCrawl(@PathVariable String id) {
        log.debug("REST request to get Crawl : {}", id);
        Optional<Crawl> crawl = crawlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crawl);
    }

    /**
     * DELETE  /crawls/:id : delete the "id" crawl.
     *
     * @param id the id of the crawl to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/crawls/{id}")
    @Timed
    public ResponseEntity<Void> deleteCrawl(@PathVariable String id) {
        log.debug("REST request to delete Crawl : {}", id);
        crawlService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/crawls?query=:query : search for the crawl corresponding
     * to the query.
     *
     * @param query the query of the crawl search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/crawls")
    @Timed
    public ResponseEntity<List<Crawl>> searchCrawls(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Crawls for query {}", query);
        Page<Crawl> page = crawlService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/crawls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
