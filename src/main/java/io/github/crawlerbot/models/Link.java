package io.github.crawlerbot.models;


import io.github.crawlerbot.enumerations.CrawlStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Link.
 */

public class Link implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String url;
    private Long scrapeDataId;
    private Long scrapeId;
    private Integer currentLevel;
    private String scrapeUrl;
    private String parentUrl;
    private Long scrapeResultId;
    private String scrapeResultPath;
    private String scrapeResultContentType;

    private CrawlStatus crawlStatus;
    private String internalUrl;
    private String html;
    private String xml;
    private String json;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Link url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getScrapeDataId() {
        return scrapeDataId;
    }

    public Link scrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
        return this;
    }

    public void setScrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
    }

    public Long getScrapeId() {
        return scrapeId;
    }

    public Link scrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
        return this;
    }

    public void setScrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public Link currentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
        return this;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getScrapeUrl() {
        return scrapeUrl;
    }

    public Link scrapeUrl(String scrapeUrl) {
        this.scrapeUrl = scrapeUrl;
        return this;
    }

    public void setScrapeUrl(String scrapeUrl) {
        this.scrapeUrl = scrapeUrl;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public Link parentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
        return this;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public Long getScrapeResultId() {
        return scrapeResultId;
    }

    public Link scrapeResultId(Long scrapeResultId) {
        this.scrapeResultId = scrapeResultId;
        return this;
    }

    public void setScrapeResultId(Long scrapeResultId) {
        this.scrapeResultId = scrapeResultId;
    }

    public String getScrapeResultPath() {
        return scrapeResultPath;
    }

    public Link scrapeResultPath(String scrapeResultPath) {
        this.scrapeResultPath = scrapeResultPath;
        return this;
    }

    public void setScrapeResultPath(String scrapeResultPath) {
        this.scrapeResultPath = scrapeResultPath;
    }

    public String getScrapeREsultContentType() {
        return scrapeResultContentType;
    }

    public String getScrapeResultContentType() {
        return scrapeResultContentType;
    }

    public void setScrapeResultContentType(String scrapeResultContentType) {
        this.scrapeResultContentType = scrapeResultContentType;
    }

    public CrawlStatus getCrawlStatus() {
        return crawlStatus;
    }

    public Link crawlStatus(CrawlStatus crawlStatus) {
        this.crawlStatus = crawlStatus;
        return this;
    }

    public void setCrawlStatus(CrawlStatus crawlStatus) {
        this.crawlStatus = crawlStatus;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public Link internalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
        return this;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }

    public String getHtml() {
        return html;
    }

    public Link html(String html) {
        this.html = html;
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getXml() {
        return xml;
    }

    public Link xml(String xml) {
        this.xml = xml;
        return this;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getJson() {
        return json;
    }

    public Link json(String json) {
        this.json = json;
        return this;
    }

    public void setJson(String json) {
        this.json = json;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if (link.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), link.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Link{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", scrapeDataId=" + getScrapeDataId() +
            ", scrapeId=" + getScrapeId() +
            ", currentLevel=" + getCurrentLevel() +
            ", scrapeUrl='" + getScrapeUrl() + "'" +
            ", parentUrl='" + getParentUrl() + "'" +
            ", scrapeResultId=" + getScrapeResultId() +
            ", scrapeResultPath='" + getScrapeResultPath() + "'" +
            ", scrapeREsultContentType='" + getScrapeREsultContentType() + "'" +
            ", crawlStatus='" + getCrawlStatus() + "'" +
            ", internalUrl='" + getInternalUrl() + "'" +
            ", html='" + getHtml() + "'" +
            ", xml='" + getXml() + "'" +
            ", json='" + getJson() + "'" +
            "}";
    }
}
