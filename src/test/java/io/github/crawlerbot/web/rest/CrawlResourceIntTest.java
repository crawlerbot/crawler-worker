package io.github.crawlerbot.web.rest;

import io.github.crawlerbot.CrawlerWorkerApp;

import io.github.crawlerbot.domain.Crawl;
import io.github.crawlerbot.repository.CrawlRepository;
import io.github.crawlerbot.repository.search.CrawlSearchRepository;
import io.github.crawlerbot.service.CrawlService;
import io.github.crawlerbot.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;


import static io.github.crawlerbot.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.crawlerbot.domain.enumeration.BrowserOS;
import io.github.crawlerbot.domain.enumeration.MessageObject;
import io.github.crawlerbot.domain.enumeration.MessageAction;
/**
 * Test class for the CrawlResource REST controller.
 *
 * @see CrawlResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrawlerWorkerApp.class)
public class CrawlResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final BrowserOS DEFAULT_BROWSER_OS = BrowserOS.UBUNTU;
    private static final BrowserOS UPDATED_BROWSER_OS = BrowserOS.MAC;

    private static final MessageObject DEFAULT_MESSAGE_OBJECT = MessageObject.CHANNEL;
    private static final MessageObject UPDATED_MESSAGE_OBJECT = MessageObject.CRAWLLINE;

    private static final MessageAction DEFAULT_MESSAGE_ACTION = MessageAction.INNIT_CRAWL;
    private static final MessageAction UPDATED_MESSAGE_ACTION = MessageAction.CRAWL_LINE;

    @Autowired
    private CrawlRepository crawlRepository;

    

    @Autowired
    private CrawlService crawlService;

    /**
     * This repository is mocked in the io.github.crawlerbot.repository.search test package.
     *
     * @see io.github.crawlerbot.repository.search.CrawlSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrawlSearchRepository mockCrawlSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCrawlMockMvc;

    private Crawl crawl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CrawlResource crawlResource = new CrawlResource(crawlService);
        this.restCrawlMockMvc = MockMvcBuilders.standaloneSetup(crawlResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crawl createEntity() {
        Crawl crawl = new Crawl()
            .content(DEFAULT_CONTENT)
            .browserOS(DEFAULT_BROWSER_OS)
            .messageObject(DEFAULT_MESSAGE_OBJECT)
            .messageAction(DEFAULT_MESSAGE_ACTION);
        return crawl;
    }

    @Before
    public void initTest() {
        crawlRepository.deleteAll();
        crawl = createEntity();
    }

    @Test
    public void createCrawl() throws Exception {
        int databaseSizeBeforeCreate = crawlRepository.findAll().size();

        // Create the Crawl
        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isCreated());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeCreate + 1);
        Crawl testCrawl = crawlList.get(crawlList.size() - 1);
        assertThat(testCrawl.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCrawl.getBrowserOS()).isEqualTo(DEFAULT_BROWSER_OS);
        assertThat(testCrawl.getMessageObject()).isEqualTo(DEFAULT_MESSAGE_OBJECT);
        assertThat(testCrawl.getMessageAction()).isEqualTo(DEFAULT_MESSAGE_ACTION);

        // Validate the Crawl in Elasticsearch
        verify(mockCrawlSearchRepository, times(1)).save(testCrawl);
    }

    @Test
    public void createCrawlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = crawlRepository.findAll().size();

        // Create the Crawl with an existing ID
        crawl.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeCreate);

        // Validate the Crawl in Elasticsearch
        verify(mockCrawlSearchRepository, times(0)).save(crawl);
    }

    @Test
    public void checkMessageObjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlRepository.findAll().size();
        // set the field null
        crawl.setMessageObject(null);

        // Create the Crawl, which fails.

        restCrawlMockMvc.perform(post("/api/crawls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCrawls() throws Exception {
        // Initialize the database
        crawlRepository.save(crawl);

        // Get all the crawlList
        restCrawlMockMvc.perform(get("/api/crawls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawl.getId())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].browserOS").value(hasItem(DEFAULT_BROWSER_OS.toString())))
            .andExpect(jsonPath("$.[*].messageObject").value(hasItem(DEFAULT_MESSAGE_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].messageAction").value(hasItem(DEFAULT_MESSAGE_ACTION.toString())));
    }
    

    @Test
    public void getCrawl() throws Exception {
        // Initialize the database
        crawlRepository.save(crawl);

        // Get the crawl
        restCrawlMockMvc.perform(get("/api/crawls/{id}", crawl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(crawl.getId()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.browserOS").value(DEFAULT_BROWSER_OS.toString()))
            .andExpect(jsonPath("$.messageObject").value(DEFAULT_MESSAGE_OBJECT.toString()))
            .andExpect(jsonPath("$.messageAction").value(DEFAULT_MESSAGE_ACTION.toString()));
    }
    @Test
    public void getNonExistingCrawl() throws Exception {
        // Get the crawl
        restCrawlMockMvc.perform(get("/api/crawls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCrawl() throws Exception {
        // Initialize the database
        crawlService.save(crawl);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCrawlSearchRepository);

        int databaseSizeBeforeUpdate = crawlRepository.findAll().size();

        // Update the crawl
        Crawl updatedCrawl = crawlRepository.findById(crawl.getId()).get();
        updatedCrawl
            .content(UPDATED_CONTENT)
            .browserOS(UPDATED_BROWSER_OS)
            .messageObject(UPDATED_MESSAGE_OBJECT)
            .messageAction(UPDATED_MESSAGE_ACTION);

        restCrawlMockMvc.perform(put("/api/crawls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCrawl)))
            .andExpect(status().isOk());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeUpdate);
        Crawl testCrawl = crawlList.get(crawlList.size() - 1);
        assertThat(testCrawl.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCrawl.getBrowserOS()).isEqualTo(UPDATED_BROWSER_OS);
        assertThat(testCrawl.getMessageObject()).isEqualTo(UPDATED_MESSAGE_OBJECT);
        assertThat(testCrawl.getMessageAction()).isEqualTo(UPDATED_MESSAGE_ACTION);

        // Validate the Crawl in Elasticsearch
        verify(mockCrawlSearchRepository, times(1)).save(testCrawl);
    }

    @Test
    public void updateNonExistingCrawl() throws Exception {
        int databaseSizeBeforeUpdate = crawlRepository.findAll().size();

        // Create the Crawl

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCrawlMockMvc.perform(put("/api/crawls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawl)))
            .andExpect(status().isBadRequest());

        // Validate the Crawl in the database
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Crawl in Elasticsearch
        verify(mockCrawlSearchRepository, times(0)).save(crawl);
    }

    @Test
    public void deleteCrawl() throws Exception {
        // Initialize the database
        crawlService.save(crawl);

        int databaseSizeBeforeDelete = crawlRepository.findAll().size();

        // Get the crawl
        restCrawlMockMvc.perform(delete("/api/crawls/{id}", crawl.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Crawl> crawlList = crawlRepository.findAll();
        assertThat(crawlList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Crawl in Elasticsearch
        verify(mockCrawlSearchRepository, times(1)).deleteById(crawl.getId());
    }

    @Test
    public void searchCrawl() throws Exception {
        // Initialize the database
        crawlService.save(crawl);
        when(mockCrawlSearchRepository.search(queryStringQuery("id:" + crawl.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crawl), PageRequest.of(0, 1), 1));
        // Search the crawl
        restCrawlMockMvc.perform(get("/api/_search/crawls?query=id:" + crawl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawl.getId())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].browserOS").value(hasItem(DEFAULT_BROWSER_OS.toString())))
            .andExpect(jsonPath("$.[*].messageObject").value(hasItem(DEFAULT_MESSAGE_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].messageAction").value(hasItem(DEFAULT_MESSAGE_ACTION.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crawl.class);
        Crawl crawl1 = new Crawl();
        crawl1.setId("id1");
        Crawl crawl2 = new Crawl();
        crawl2.setId(crawl1.getId());
        assertThat(crawl1).isEqualTo(crawl2);
        crawl2.setId("id2");
        assertThat(crawl1).isNotEqualTo(crawl2);
        crawl1.setId(null);
        assertThat(crawl1).isNotEqualTo(crawl2);
    }
}
