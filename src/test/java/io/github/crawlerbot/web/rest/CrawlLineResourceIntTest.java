package io.github.crawlerbot.web.rest;

import io.github.crawlerbot.CrawlerWorkerApp;

import io.github.crawlerbot.domain.CrawlLine;
import io.github.crawlerbot.repository.CrawlLineRepository;
import io.github.crawlerbot.repository.search.CrawlLineSearchRepository;
import io.github.crawlerbot.service.CrawlLineService;
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

/**
 * Test class for the CrawlLineResource REST controller.
 *
 * @see CrawlLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CrawlerWorkerApp.class)
public class CrawlLineResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private CrawlLineRepository crawlLineRepository;

    

    @Autowired
    private CrawlLineService crawlLineService;

    /**
     * This repository is mocked in the io.github.crawlerbot.repository.search test package.
     *
     * @see io.github.crawlerbot.repository.search.CrawlLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrawlLineSearchRepository mockCrawlLineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCrawlLineMockMvc;

    private CrawlLine crawlLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CrawlLineResource crawlLineResource = new CrawlLineResource(crawlLineService);
        this.restCrawlLineMockMvc = MockMvcBuilders.standaloneSetup(crawlLineResource)
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
    public static CrawlLine createEntity() {
        CrawlLine crawlLine = new CrawlLine()
            .content(DEFAULT_CONTENT);
        return crawlLine;
    }

    @Before
    public void initTest() {
        crawlLineRepository.deleteAll();
        crawlLine = createEntity();
    }

    @Test
    public void createCrawlLine() throws Exception {
        int databaseSizeBeforeCreate = crawlLineRepository.findAll().size();

        // Create the CrawlLine
        restCrawlLineMockMvc.perform(post("/api/crawl-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawlLine)))
            .andExpect(status().isCreated());

        // Validate the CrawlLine in the database
        List<CrawlLine> crawlLineList = crawlLineRepository.findAll();
        assertThat(crawlLineList).hasSize(databaseSizeBeforeCreate + 1);
        CrawlLine testCrawlLine = crawlLineList.get(crawlLineList.size() - 1);
        assertThat(testCrawlLine.getContent()).isEqualTo(DEFAULT_CONTENT);

        // Validate the CrawlLine in Elasticsearch
        verify(mockCrawlLineSearchRepository, times(1)).save(testCrawlLine);
    }

    @Test
    public void createCrawlLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = crawlLineRepository.findAll().size();

        // Create the CrawlLine with an existing ID
        crawlLine.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrawlLineMockMvc.perform(post("/api/crawl-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawlLine)))
            .andExpect(status().isBadRequest());

        // Validate the CrawlLine in the database
        List<CrawlLine> crawlLineList = crawlLineRepository.findAll();
        assertThat(crawlLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrawlLine in Elasticsearch
        verify(mockCrawlLineSearchRepository, times(0)).save(crawlLine);
    }

    @Test
    public void getAllCrawlLines() throws Exception {
        // Initialize the database
        crawlLineRepository.save(crawlLine);

        // Get all the crawlLineList
        restCrawlLineMockMvc.perform(get("/api/crawl-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawlLine.getId())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
    

    @Test
    public void getCrawlLine() throws Exception {
        // Initialize the database
        crawlLineRepository.save(crawlLine);

        // Get the crawlLine
        restCrawlLineMockMvc.perform(get("/api/crawl-lines/{id}", crawlLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(crawlLine.getId()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }
    @Test
    public void getNonExistingCrawlLine() throws Exception {
        // Get the crawlLine
        restCrawlLineMockMvc.perform(get("/api/crawl-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCrawlLine() throws Exception {
        // Initialize the database
        crawlLineService.save(crawlLine);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCrawlLineSearchRepository);

        int databaseSizeBeforeUpdate = crawlLineRepository.findAll().size();

        // Update the crawlLine
        CrawlLine updatedCrawlLine = crawlLineRepository.findById(crawlLine.getId()).get();
        updatedCrawlLine
            .content(UPDATED_CONTENT);

        restCrawlLineMockMvc.perform(put("/api/crawl-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCrawlLine)))
            .andExpect(status().isOk());

        // Validate the CrawlLine in the database
        List<CrawlLine> crawlLineList = crawlLineRepository.findAll();
        assertThat(crawlLineList).hasSize(databaseSizeBeforeUpdate);
        CrawlLine testCrawlLine = crawlLineList.get(crawlLineList.size() - 1);
        assertThat(testCrawlLine.getContent()).isEqualTo(UPDATED_CONTENT);

        // Validate the CrawlLine in Elasticsearch
        verify(mockCrawlLineSearchRepository, times(1)).save(testCrawlLine);
    }

    @Test
    public void updateNonExistingCrawlLine() throws Exception {
        int databaseSizeBeforeUpdate = crawlLineRepository.findAll().size();

        // Create the CrawlLine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCrawlLineMockMvc.perform(put("/api/crawl-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crawlLine)))
            .andExpect(status().isBadRequest());

        // Validate the CrawlLine in the database
        List<CrawlLine> crawlLineList = crawlLineRepository.findAll();
        assertThat(crawlLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrawlLine in Elasticsearch
        verify(mockCrawlLineSearchRepository, times(0)).save(crawlLine);
    }

    @Test
    public void deleteCrawlLine() throws Exception {
        // Initialize the database
        crawlLineService.save(crawlLine);

        int databaseSizeBeforeDelete = crawlLineRepository.findAll().size();

        // Get the crawlLine
        restCrawlLineMockMvc.perform(delete("/api/crawl-lines/{id}", crawlLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CrawlLine> crawlLineList = crawlLineRepository.findAll();
        assertThat(crawlLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrawlLine in Elasticsearch
        verify(mockCrawlLineSearchRepository, times(1)).deleteById(crawlLine.getId());
    }

    @Test
    public void searchCrawlLine() throws Exception {
        // Initialize the database
        crawlLineService.save(crawlLine);
        when(mockCrawlLineSearchRepository.search(queryStringQuery("id:" + crawlLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crawlLine), PageRequest.of(0, 1), 1));
        // Search the crawlLine
        restCrawlLineMockMvc.perform(get("/api/_search/crawl-lines?query=id:" + crawlLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawlLine.getId())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrawlLine.class);
        CrawlLine crawlLine1 = new CrawlLine();
        crawlLine1.setId("id1");
        CrawlLine crawlLine2 = new CrawlLine();
        crawlLine2.setId(crawlLine1.getId());
        assertThat(crawlLine1).isEqualTo(crawlLine2);
        crawlLine2.setId("id2");
        assertThat(crawlLine1).isNotEqualTo(crawlLine2);
        crawlLine1.setId(null);
        assertThat(crawlLine1).isNotEqualTo(crawlLine2);
    }
}
