package io.github.crawlerbot.service;

import com.google.gson.Gson;
import io.github.crawlerbot.models.CrawlLine;

public class DataTransformer {
    public static CrawlLine readFromString(String crawlLineContent) {
        Gson gson = new Gson();
        CrawlLine crawlLine = gson.fromJson(crawlLineContent, CrawlLine.class);
        return crawlLine;
    }
    public static String convertCrawlLineToString(CrawlLine crawlLine) {
        Gson gson = new Gson();
        String content  = gson.toJson(crawlLine);
        return content;
    }

}

