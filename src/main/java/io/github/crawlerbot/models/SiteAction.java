package io.github.crawlerbot.models;



import io.github.crawlerbot.enumerations.Action;
import io.github.crawlerbot.enumerations.SeleniumActionGetContent;

import java.io.Serializable;

/**
 * A SiteAction.
 */

public class SiteAction implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String url;
    private String domain;
    private String host;

    private Integer level;
    private String selector;
    private String selectorAttr;
    private Action action;
    private Integer totalActions;

    private SeleniumActionGetContent getContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSelectorAttr() {
        return selectorAttr;
    }

    public void setSelectorAttr(String selectorAttr) {
        this.selectorAttr = selectorAttr;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Integer getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(Integer totalActions) {
        this.totalActions = totalActions;
    }

    public SeleniumActionGetContent getGetContent() {
        return getContent;
    }

    public void setGetContent(SeleniumActionGetContent getContent) {
        this.getContent = getContent;
    }

    @Override
    public String toString() {
        return "SiteAction{" +
            "id='" + id + '\'' +
            ", url='" + url + '\'' +
            ", domain='" + domain + '\'' +
            ", host='" + host + '\'' +
            ", level=" + level +
            ", selector='" + selector + '\'' +
            ", selectorAttr='" + selectorAttr + '\'' +
            ", action=" + action +
            ", totalActions=" + totalActions +
            ", getContent=" + getContent +
            '}';
    }
}
