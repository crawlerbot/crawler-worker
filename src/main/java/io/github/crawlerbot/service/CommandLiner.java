package io.github.crawlerbot.service;


import io.github.crawlerbot.domain.enumeration.BrowserOS;
import io.github.crawlerbot.domain.enumeration.MessageAction;
import io.github.crawlerbot.domain.enumeration.MessageObject;
import io.github.crawlerbot.exceptions.NotSupportBrowserException;
import io.github.crawlerbot.messaging.MessagePayLoad;
import io.github.crawlerbot.messaging.ProducerChannel;
import io.github.crawlerbot.models.Channel;
import io.github.crawlerbot.models.CrawlLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;


@Service
public class CommandLiner {

    private static final Logger log = LoggerFactory.getLogger(CommandLiner.class);

    private  MessageChannel channel;
    public CommandLiner(ProducerChannel channel) {
        this.channel = channel.messageChannel();
    }

    private void testTiki(BrowserOS browserOS) {
        Channel messageContent = new FileStoreService().readLocalChannel("sample_tiki.json");
        String content = new FileStoreService().convertChannelToString(messageContent);

        MessagePayLoad messagePayLoad = new MessagePayLoad();
        messagePayLoad.setMessage(content);
        messagePayLoad.setBrowserOS(browserOS);
        messagePayLoad.setMessageAction(MessageAction.INNIT_CRAWL);
        messagePayLoad.setMessageObject(MessageObject.CHANNEL);

        if(messagePayLoad.getMessageAction().equals(MessageAction.INNIT_CRAWL)) {
            Channel channel = new FileStoreService().convertStringToChannel(messagePayLoad.getMessage());
            try {
                Crawler crawler = new Crawler(channel, this);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, browserOS).start();

            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if(messagePayLoad.getMessageAction().equals(MessageAction.CRAWL_LINE)) {
            log.info("Crawlline......");
            try {
                CrawlLine crawlLine = DataTransformer.readFromString(messagePayLoad.getMessage());
                Crawler crawler = new Crawler(crawlLine, this);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, browserOS).start();
            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if(messagePayLoad.getMessageAction().equals(MessageAction.INDEX)) {
            log.info("Crawler Indexing Action");
        }
    }
    private void testYoutube(BrowserOS browserOS) {


        Channel messageContent = new FileStoreService().readLocalChannel("sample.json");
        String content = new FileStoreService().convertChannelToString(messageContent);
        MessagePayLoad messagePayLoad = new MessagePayLoad();
        messagePayLoad.setMessage(content);
        messagePayLoad.setBrowserOS(browserOS);
        messagePayLoad.setMessageAction(MessageAction.INNIT_CRAWL);
        messagePayLoad.setMessageObject(MessageObject.CHANNEL);
        if(messagePayLoad.getMessageAction().equals(MessageAction.INNIT_CRAWL)) {
            Channel channel = new FileStoreService().convertStringToChannel(messagePayLoad.getMessage());
            try {
                Crawler crawler = new Crawler(channel, this);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, browserOS).start();

            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if(messagePayLoad.getMessageAction().equals(MessageAction.CRAWL_LINE)) {
            log.info("Crawlline......");
            try {
                CrawlLine crawlLine = DataTransformer.readFromString(messagePayLoad.getMessage());
                Crawler crawler = new Crawler(crawlLine, this);
                crawler.config(Crawler.BrowserName.CHROME, Crawler.BrowserHost.LOCAL, browserOS).start();
            } catch (NotSupportBrowserException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        if(messagePayLoad.getMessageAction().equals(MessageAction.INDEX)) {
            log.info("Crawler Indexing Action");
        }

    }


    public void sendNextCrawlLine(CrawlLine nextInput) {
        log.info("Put to next queue here sendNextCrawlLine: {}", nextInput);
        String messageBody = DataTransformer.convertCrawlLineToString(nextInput);
        MessagePayLoad messagePayLoad = new MessagePayLoad();
        messagePayLoad.setMessage(messageBody);
        // need to be reractor
        messagePayLoad.setBrowserOS(nextInput.getBrowserOS());
        messagePayLoad.setMessageAction(MessageAction.CRAWL_LINE);
        messagePayLoad.setMessageObject(MessageObject.CRAWLLINE);
        channel.send(MessageBuilder.withPayload(messagePayLoad).build());
    }
    public void crawl(MessagePayLoad messagePayLoad) {
        if(messagePayLoad.getMessageObject().equals(MessageObject.CHANNEL)) {
            messagePayLoad.setMessageAction(MessageAction.INNIT_CRAWL);
            messagePayLoad.setMessageObject(MessageObject.CHANNEL);
            channel.send(MessageBuilder.withPayload(messagePayLoad).build());
        } else if(messagePayLoad.getMessageObject().equals(MessageObject.CRAWLLINE)){
            messagePayLoad.setBrowserOS(messagePayLoad.getBrowserOS());
            messagePayLoad.setMessageAction(MessageAction.CRAWL_LINE);
            messagePayLoad.setMessageObject(MessageObject.CRAWLLINE);
            channel.send(MessageBuilder.withPayload(messagePayLoad).build());
        }
    }
    public void consume(MessagePayLoad messagePayLoad) {
        log.info("start Received message.....");
        log.info("Received message: {}.", messagePayLoad.getMessage());

        if (messagePayLoad.getMessageAction().equals(MessageAction.INNIT_CRAWL)) {
            Channel channel = new FileStoreService().convertStringToChannel(messagePayLoad.getMessage());
            try {
                Crawler crawler = new Crawler(channel, this);
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
                Crawler crawler = new Crawler(crawlLine, this);
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
}
