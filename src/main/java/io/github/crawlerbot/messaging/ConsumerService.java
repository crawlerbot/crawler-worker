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

    private final CommandLiner commandLiner;
    public ConsumerService(CommandLiner commandLiner) {
        this.commandLiner = commandLiner;
    }
    // listen channel comming
    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(MessagePayLoad messagePayLoad) {
        commandLiner.consume(messagePayLoad);
    }
}
