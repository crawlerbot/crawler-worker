package io.github.crawlerbot.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.github.crawlerbot.domain.enumeration.BrowserOS;

import io.github.crawlerbot.domain.enumeration.MessageObject;

import io.github.crawlerbot.domain.enumeration.MessageAction;

/**
 * A Crawl.
 */
@Document(collection = "crawl")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crawl")
public class Crawl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("content")
    private String content;

    @Field("browser_os")
    private BrowserOS browserOS;

    @NotNull
    @Field("message_object")
    private MessageObject messageObject;

    @Field("message_action")
    private MessageAction messageAction;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Crawl content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BrowserOS getBrowserOS() {
        return browserOS;
    }

    public Crawl browserOS(BrowserOS browserOS) {
        this.browserOS = browserOS;
        return this;
    }

    public void setBrowserOS(BrowserOS browserOS) {
        this.browserOS = browserOS;
    }

    public MessageObject getMessageObject() {
        return messageObject;
    }

    public Crawl messageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
        return this;
    }

    public void setMessageObject(MessageObject messageObject) {
        this.messageObject = messageObject;
    }

    public MessageAction getMessageAction() {
        return messageAction;
    }

    public Crawl messageAction(MessageAction messageAction) {
        this.messageAction = messageAction;
        return this;
    }

    public void setMessageAction(MessageAction messageAction) {
        this.messageAction = messageAction;
    }
    // simlife-needle-entity-add-getters-setters - Simlife will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Crawl crawl = (Crawl) o;
        if (crawl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), crawl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Crawl{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", browserOS='" + getBrowserOS() + "'" +
            ", messageObject='" + getMessageObject() + "'" +
            ", messageAction='" + getMessageAction() + "'" +
            "}";
    }
}
