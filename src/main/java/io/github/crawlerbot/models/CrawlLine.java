package io.github.crawlerbot.models;

import io.github.crawlerbot.domain.enumeration.BrowserOS;
import io.github.crawlerbot.domain.enumeration.MessageAction;
import io.github.crawlerbot.domain.enumeration.MessageObject;
import io.github.crawlerbot.messaging.MessagePayLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * A CrawlLine.
 */

public class CrawlLine implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(CrawlLine.class);
    private static final long serialVersionUID = 1L;
    private String id;
    private Link link;
    private Channel channel;
    List<HashMap<String, List<String>>> data = new ArrayList<>();
    private String fileUrl;

    private String message;
    private MessageObject messageObject;
    private MessageAction messageAction;
    private BrowserOS browserOS;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageObject getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
    }

    public MessageAction getMessageAction() {
        return messageAction;
    }

    public void setMessageAction(MessageAction messageAction) {
        this.messageAction = messageAction;
    }

    public BrowserOS getBrowserOS() {
        return browserOS;
    }

    public void setBrowserOS(BrowserOS browserOS) {
        this.browserOS = browserOS;
    }

    public String getId() {
        return id;
    }

    public Link getLink() {
        return link;
    }


    public void setLink(Link link) {
        this.link = link;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<HashMap<String, List<String>>> getData() {
        return data;
    }

    public void setData(List<HashMap<String, List<String>>> data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public CrawlLine fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CrawlLine crawlLine = (CrawlLine) o;
        if (crawlLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), crawlLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CrawlLine{" +
            "id='" + id + '\'' +
            ", link=" + link +
            ", channel=" + channel +
            ", data=" + data +
            ", fileUrl='" + fileUrl + '\'' +
            '}';
    }

    private String getCurrentDataUrl(int index, List<HashMap<String, List<String>>> data) {
        try {
            return data.get(index).get("url").get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public CrawlLine updateData(List<HashMap<String, List<String>>> newData, ConfigGroup configGroup) {
        if (newData == null || newData.isEmpty()) return this;
        if (this.data.size() == 0) {
            this.data = newData;
            return this;
        }

        List<HashMap<String, List<String>>> remainData = new ArrayList<>();
        for (int i = 0; i < newData.size(); i++) {
            for (int j = 0; j < data.size(); j++) {

                String currentUrl = getCurrentDataUrl(j, data);
                String newDataUrl = getCurrentDataUrl(i, newData);

                if (newDataUrl != null && newDataUrl.equals(currentUrl)) {
                    Set<String> keys = newData.get(i).keySet();
                    for (String key : keys) {
                        List<String> dataValues = newData.get(i).get(key);
                        if (key != "url") {
                            // current data for this key is empty
                            if (data.get(j).get(key) == null || data.get(j).get(key).size() == 0) {
                                if (dataValues != null && dataValues.size() > 0) {
                                    data.get(j).put(key, newData.get(i).get(key));
                                }
                            } else if (data.get(j).get(key).size() > 0) { // add update to current data key values
                                for (String dataValue : dataValues) {
                                    data.get(j).get(key).add(dataValue);
                                }
                            }
                        }
                    }

                } else {
                    remainData.add(newData.get(i));
                }
            }
        }
        if (remainData.size() > 0) {
            for (HashMap<String, List<String>> dt : remainData) {
                data.add(dt);
            }
        }

        return this;
    }
}
