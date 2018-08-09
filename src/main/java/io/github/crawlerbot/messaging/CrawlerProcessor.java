package io.github.crawlerbot.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CrawlerProcessor {

    String CRAWL_INPUT = "crawler-in";
    String CRAWL_NEXT = "crawler-next";
    String INDEXING_OUTPUT = "indexing-out";


    @Input(CRAWL_INPUT)
    SubscribableChannel startCrawl();

    @Input(CRAWL_NEXT)
    SubscribableChannel startCrawlNext();

    @Input(INDEXING_OUTPUT)
    MessageChannel sendToIndexing();


}
