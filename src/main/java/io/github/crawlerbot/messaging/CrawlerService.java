package io.github.crawlerbot.messaging;

import io.github.crawlerbot.models.CrawlLine;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class CrawlerService {

    private final CrawlerProcessor crawlerProcessor;


    public CrawlerService(CrawlerProcessor crawlerProcessor) {
        this.crawlerProcessor = crawlerProcessor;
    }

    public void startCrawl(MessagePayLoad messagePayLoad) {
        MessageChannel messageChannel = crawlerProcessor.startCrawl();
        messageChannel.send(MessageBuilder
            .withPayload(messagePayLoad)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .build());

    }

    public void startCrawlNextLink(CrawlLine crawlLine) {
        MessageChannel messageChannel = crawlerProcessor.startCrawlNext();
        messageChannel.send(MessageBuilder
            .withPayload(crawlLine)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .build());
    }

    public void sendToIndexing(CrawlLine crawlLine) {
        MessageChannel messageChannel = crawlerProcessor.sendToIndexing();
        messageChannel.send(MessageBuilder
            .withPayload(crawlLine)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .build());

    }

}
