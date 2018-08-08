package io.github.crawlerbot.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CrawlSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CrawlSearchRepositoryMockConfiguration {

    @MockBean
    private CrawlSearchRepository mockCrawlSearchRepository;

}
