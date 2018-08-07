package io.github.crawlerbot.service;


public interface CrawlerEngine {
    Crawler fetch();

    Crawler parse(boolean splitData);

    Crawler index();

    Crawler upload();

    Crawler nextLinks();

    Crawler report();
    Crawler start();

}
