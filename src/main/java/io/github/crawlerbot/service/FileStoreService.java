package io.github.crawlerbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.crawlerbot.models.Channel;
import io.github.crawlerbot.models.CrawlLine;
import io.github.crawlerbot.models.FileContent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileStoreService {
    private static final Logger logger = LoggerFactory.getLogger(FileStoreService.class);
    // read from properties
    private static String localPath = "/var/data/";

    public String getClassPath(String fileName) {
        //ClassLoader classLoader = getClass().getClassLoader();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return rootPath + "/" + fileName;
    }
    public CrawlLine readLocalCrawline(String fileName) {
        Gson gson = new Gson();
        CrawlLine result = null;
        BufferedReader br = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            br = new BufferedReader(new FileReader(getClassPath(fileName)));
            result = gson.fromJson(br, CrawlLine.class);
            if (result != null) {
                System.out.print(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        logger.info("channel is : {}", result);
        return result;
    }
    public Channel readLocalChannel(String fileName) {

        Gson gson = new Gson();
        Channel result = null;
        BufferedReader br = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            br = new BufferedReader(new FileReader(getClassPath(fileName)));
            result = gson.fromJson(br, Channel.class);
            if (result != null) {
                System.out.print(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        logger.info("channel is : {}", result);
        return result;

    }


    public String convertChannelToString(Channel channel) {
        if(channel == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(channel);
        return content;
    }
    public String convertCrawlLineToString(CrawlLine crawlLine) {
        if(crawlLine == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(crawlLine);
        return content;
    }



    public static String writeLocalFile(String fileName, CrawlLine resultData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String data = objectMapper.writeValueAsString(resultData);
            FileContent fileContent = new FileContent();
            fileContent.setContent(data);
            File file = new File(localPath + fileName);
            FileUtils.writeStringToFile(file, fileContent.getContent(), "utf-8");
            //String jsonPath = uploadJSONContent(fileName, fileContent);
            return fileName;
        } catch (Exception ex) {
            return null;
        }
    }

    public Channel convertStringToChannel(String message) {
        if(message == null || message.length() ==0) return null;
        Gson gson = new Gson();
        Channel result = gson.fromJson(message, Channel.class);
        return result;
    }
}
