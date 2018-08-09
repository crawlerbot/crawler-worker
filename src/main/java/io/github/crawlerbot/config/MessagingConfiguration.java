package io.github.crawlerbot.config;


import io.github.crawlerbot.messaging.CrawlerProcessor;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * Configures Spring Cloud Stream support.
 *
 * This works out-of-the-box if you use the Docker Compose configuration at "src/main/docker/kafka.yml".
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for the official Spring Cloud Stream documentation.
 */
//@EnableBinding(value = {Source.class, ProducerChannel.class, ConsumerChannel.class})
@EnableBinding({CrawlerProcessor.class})
public class MessagingConfiguration {

}
