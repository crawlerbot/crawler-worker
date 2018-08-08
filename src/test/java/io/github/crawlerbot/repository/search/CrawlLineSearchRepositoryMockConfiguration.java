package io.github.crawlerbot.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CrawlLineSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CrawlLineSearchRepositoryMockConfiguration {

    @MockBean
    private CrawlLineSearchRepository mockCrawlLineSearchRepository;

}
