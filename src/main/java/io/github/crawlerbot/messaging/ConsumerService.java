package io.github.crawlerbot.messaging;

import io.github.crawlerbot.exceptions.NotSupportBrowserException;
import io.github.crawlerbot.models.Channel;
import io.github.crawlerbot.models.CrawlLine;
import io.github.crawlerbot.service.CommandLiner;
import io.github.crawlerbot.service.Crawler;
import io.github.crawlerbot.service.DataTransformer;
import io.github.crawlerbot.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class ConsumerService {

    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private CommandLiner commandLiner;


    // listen channel comming
    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(MessagePayLoad messagePayLoad) {

        log.info("start Received message.....");
        log.info("Received message: {}.", messagePayLoad.getMessage());

        if (messagePayLoad.getMessageAction().equals(MessageAction.INNIT_CRAWL)) {
            Channel channel = new FileStoreService().convertStringToChannel(messagePayLoad.getMessage());
            try {
                Crawler crawler = new Crawler(channel, commandLiner);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, messagePayLoad.getBrowserOS()).start();

            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (messagePayLoad.getMessageAction().equals(MessageAction.CRAWL_LINE)) {
            log.info("Crawlline......");
            try {
                CrawlLine crawlLine = DataTransformer.readFromString(messagePayLoad.getMessage());
                Crawler crawler = new Crawler(crawlLine, commandLiner);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, messagePayLoad.getBrowserOS()).start();
            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if (messagePayLoad.getMessageAction().equals(MessageAction.INDEX)) {
            log.info("Crawler Indexing Action");
        }
    }

    // listen crawline comming
    // implement here
}
