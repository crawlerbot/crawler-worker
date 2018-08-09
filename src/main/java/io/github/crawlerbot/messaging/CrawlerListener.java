package io.github.crawlerbot.messaging;

import io.github.crawlerbot.domain.enumeration.MessageAction;
import io.github.crawlerbot.exceptions.NotSupportBrowserException;
import io.github.crawlerbot.models.Channel;
import io.github.crawlerbot.models.CrawlLine;
import io.github.crawlerbot.service.Crawler;
import io.github.crawlerbot.service.DataTransformer;
import io.github.crawlerbot.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class CrawlerListener {

    private final Logger logger = LoggerFactory.getLogger(CrawlerListener.class);
    @Autowired
    private CrawlerService crawlerService;

    @StreamListener(CrawlerProcessor.CRAWL_INPUT)
    public void handleCrawl(@Payload MessagePayLoad messagePayLoad) {

        logger.info("Received crawling: {}", messagePayLoad);
        if (messagePayLoad.getMessageAction().equals(MessageAction.INNIT_CRAWL)) {
            Channel channel = new FileStoreService().convertStringToChannel(messagePayLoad.getMessage());
            try {
                Crawler crawler = new Crawler(channel, crawlerService);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, messagePayLoad.getBrowserOS()).start();

            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (messagePayLoad.getMessageAction().equals(MessageAction.CRAWL_LINE)) {
            logger.info("Crawlline......");
            try {
                CrawlLine crawlLine = DataTransformer.readFromString(messagePayLoad.getMessage());
                Crawler crawler = new Crawler(crawlLine, crawlerService);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, messagePayLoad.getBrowserOS()).start();
            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (messagePayLoad.getMessageAction().equals(MessageAction.INDEX)) {
            logger.info("Crawler Indexing Action");
        }

    }

    @StreamListener(CrawlerProcessor.CRAWL_NEXT)
    public void handleCrawlNext(@Payload CrawlLine crawlLine) {
        try {
            Crawler crawler = new Crawler(crawlLine, crawlerService);
            crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, crawlLine.getBrowserOS()).start();
        } catch (NotSupportBrowserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
