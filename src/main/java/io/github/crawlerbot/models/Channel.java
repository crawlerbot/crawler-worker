package io.github.crawlerbot.models;


import io.github.crawlerbot.enumerations.ChannelType;
import io.github.crawlerbot.enumerations.DocType;
import io.github.crawlerbot.enumerations.FetchEngine;
import io.github.crawlerbot.enumerations.PostType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Channel.
 */


public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String siteDomain;
    private String siteUrl;
    private String url;
    private DocType contentType;
    private String schedule;
    private String scheduleTimeZone;
    private Integer totalLevel;
    private Integer archiveLevel;
    private Boolean unlimitedLevel;
    private FetchEngine fetchEngine;
    private String category;
    private String tag;
    private String categorySlug;
    private String tagSlug;
    private String countryCode;
    private String languageCode;
    private String targetQueueChannel;
    private String topics;
    private String topicSlugs;
    private PostType postType;
    private Integer rankingCountry;
    private String logo;
    private String siteName;
    private ChannelType channelType;
    private FetchEngine levelOneFetchEngine;
    private FetchEngine levelTwoFetchEngine;
    private FetchEngine levelThreeFetchEngine;
    private FetchEngine levelFourFetchEngine;

    private DocType levelOneContentType;


    private DocType levelTwoContentType;


    private DocType levelThreeContentType;


    private DocType levelFourContentType;
    private Boolean allowExternalUrl;

    private Set<ConfigMapping> mappings = new HashSet<>();
    private Set<ConfigGroup> configGroups = new HashSet<>();

    private Set<SiteAction> actions = new HashSet<>();

    private Set<ConfigGroup> targetGroups = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Channel url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DocType getContentType() {
        return contentType;
    }

    public Channel contentType(DocType contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setContentType(DocType contentType) {
        this.contentType = contentType;
    }

    public String getSchedule() {
        return schedule;
    }

    public Channel schedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleTimeZone() {
        return scheduleTimeZone;
    }

    public Channel scheduleTimeZone(String scheduleTimeZone) {
        this.scheduleTimeZone = scheduleTimeZone;
        return this;
    }

    public void setScheduleTimeZone(String scheduleTimeZone) {
        this.scheduleTimeZone = scheduleTimeZone;
    }

    public Integer getTotalLevel() {
        return totalLevel;
    }

    public Channel totalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain;
    }

    public void setTotalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
    }

    public Integer getArchiveLevel() {
        return archiveLevel;
    }

    public Channel archiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
        return this;
    }

    public void setArchiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
    }

    public Boolean isUnlimitedLevel() {
        return unlimitedLevel;
    }

    public Channel unlimitedLevel(Boolean unlimitedLevel) {
        this.unlimitedLevel = unlimitedLevel;
        return this;
    }

    public Set<SiteAction> getActions() {
        return actions;
    }

    public void setActions(Set<SiteAction> actions) {
        this.actions = actions;
    }

    public void setUnlimitedLevel(Boolean unlimitedLevel) {
        this.unlimitedLevel = unlimitedLevel;
    }

    public FetchEngine getFetchEngine() {
        return fetchEngine;
    }

    public Channel fetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
        return this;
    }

    public void setFetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    public String getCategory() {
        return category;
    }

    public Channel category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public Channel tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public Channel categorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
        return this;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getTagSlug() {
        return tagSlug;
    }

    public Channel tagSlug(String tagSlug) {
        this.tagSlug = tagSlug;
        return this;
    }

    public void setTagSlug(String tagSlug) {
        this.tagSlug = tagSlug;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Channel countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Channel languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTargetQueueChannel() {
        return targetQueueChannel;
    }

    public Channel targetQueueChannel(String targetQueueChannel) {
        this.targetQueueChannel = targetQueueChannel;
        return this;
    }

    public void setTargetQueueChannel(String targetQueueChannel) {
        this.targetQueueChannel = targetQueueChannel;
    }

    public String getTopics() {
        return topics;
    }

    public Channel topics(String topics) {
        this.topics = topics;
        return this;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getTopicSlugs() {
        return topicSlugs;
    }

    public Channel topicSlugs(String topicSlugs) {
        this.topicSlugs = topicSlugs;
        return this;
    }

    public void setTopicSlugs(String topicSlugs) {
        this.topicSlugs = topicSlugs;
    }

    public PostType getPostType() {
        return postType;
    }

    public Channel postType(PostType postType) {
        this.postType = postType;
        return this;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public Integer getRankingCountry() {
        return rankingCountry;
    }

    public Channel rankingCountry(Integer rankingCountry) {
        this.rankingCountry = rankingCountry;
        return this;
    }

    public void setRankingCountry(Integer rankingCountry) {
        this.rankingCountry = rankingCountry;
    }


    public String getLogo() {
        return logo;
    }

    public Channel logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSiteName() {
        return siteName;
    }

    public Channel siteName(String siteName) {
        this.siteName = siteName;
        return this;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public Channel channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public FetchEngine getLevelOneFetchEngine() {
        return levelOneFetchEngine;
    }

    public Channel levelOneFetchEngine(FetchEngine levelOneFetchEngine) {
        this.levelOneFetchEngine = levelOneFetchEngine;
        return this;
    }

    public void setLevelOneFetchEngine(FetchEngine levelOneFetchEngine) {
        this.levelOneFetchEngine = levelOneFetchEngine;
    }

    public FetchEngine getLevelTwoFetchEngine() {
        return levelTwoFetchEngine;
    }

    public Channel levelTwoFetchEngine(FetchEngine levelTwoFetchEngine) {
        this.levelTwoFetchEngine = levelTwoFetchEngine;
        return this;
    }

    public void setLevelTwoFetchEngine(FetchEngine levelTwoFetchEngine) {
        this.levelTwoFetchEngine = levelTwoFetchEngine;
    }

    public FetchEngine getLevelThreeFetchEngine() {
        return levelThreeFetchEngine;
    }

    public Channel levelThreeFetchEngine(FetchEngine levelThreeFetchEngine) {
        this.levelThreeFetchEngine = levelThreeFetchEngine;
        return this;
    }

    public void setLevelThreeFetchEngine(FetchEngine levelThreeFetchEngine) {
        this.levelThreeFetchEngine = levelThreeFetchEngine;
    }

    public FetchEngine getLevelFourFetchEngine() {
        return levelFourFetchEngine;
    }

    public Channel levelFourFetchEngine(FetchEngine levelFourFetchEngine) {
        this.levelFourFetchEngine = levelFourFetchEngine;
        return this;
    }

    public void setLevelFourFetchEngine(FetchEngine levelFourFetchEngine) {
        this.levelFourFetchEngine = levelFourFetchEngine;
    }

    public DocType getLevelOneContentType() {
        return levelOneContentType;
    }

    public Channel levelOneContentType(DocType levelOneContentType) {
        this.levelOneContentType = levelOneContentType;
        return this;
    }

    public void setLevelOneContentType(DocType levelOneContentType) {
        this.levelOneContentType = levelOneContentType;
    }

    public DocType getLevelTwoContentType() {
        return levelTwoContentType;
    }

    public Channel levelTwoContentType(DocType levelTwoContentType) {
        this.levelTwoContentType = levelTwoContentType;
        return this;
    }

    public void setLevelTwoContentType(DocType levelTwoContentType) {
        this.levelTwoContentType = levelTwoContentType;
    }

    public DocType getLevelThreeContentType() {
        return levelThreeContentType;
    }

    public Channel levelThreeContentType(DocType levelThreeContentType) {
        this.levelThreeContentType = levelThreeContentType;
        return this;
    }

    public void setLevelThreeContentType(DocType levelThreeContentType) {
        this.levelThreeContentType = levelThreeContentType;
    }

    public DocType getLevelFourContentType() {
        return levelFourContentType;
    }

    public Channel levelFourContentType(DocType levelFourContentType) {
        this.levelFourContentType = levelFourContentType;
        return this;
    }

    public void setLevelFourContentType(DocType levelFourContentType) {
        this.levelFourContentType = levelFourContentType;
    }

    public Boolean isAllowExternalUrl() {
        return allowExternalUrl;
    }

    public Channel allowExternalUrl(Boolean allowExternalUrl) {
        this.allowExternalUrl = allowExternalUrl;
        return this;
    }

    public void setAllowExternalUrl(Boolean allowExternalUrl) {
        this.allowExternalUrl = allowExternalUrl;
    }


    public Set<ConfigMapping> getMappings() {
        return mappings;
    }

    public Channel mappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
        return this;
    }

    public Channel addMapping(ConfigMapping configMapping) {
        this.mappings.add(configMapping);
        return this;
    }

    public Channel removeMapping(ConfigMapping configMapping) {
        this.mappings.remove(configMapping);
        return this;
    }

    public void setMappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
    }

    public Set<ConfigGroup> getConfigGroups() {
        return configGroups;
    }

    public Channel configGroups(Set<ConfigGroup> configGroups) {
        this.configGroups = configGroups;
        return this;
    }

    public Channel addConfigGroup(ConfigGroup configGroup) {
        this.configGroups.add(configGroup);
        return this;
    }

    public Channel removeConfigGroup(ConfigGroup configGroup) {
        this.configGroups.remove(configGroup);
        return this;
    }

    public void setConfigGroups(Set<ConfigGroup> configGroups) {
        this.configGroups = configGroups;
    }

    public Set<ConfigGroup> getTargetGroups() {
        return targetGroups;
    }

    public Channel targetGroups(Set<ConfigGroup> configGroups) {
        this.targetGroups = configGroups;
        return this;
    }

    public Channel addTargetGroup(ConfigGroup configGroup) {
        this.targetGroups.add(configGroup);
        return this;
    }

    public Channel removeTargetGroup(ConfigGroup configGroup) {
        this.targetGroups.remove(configGroup);
        return this;
    }

    public void setTargetGroups(Set<ConfigGroup> configGroups) {
        this.targetGroups = configGroups;
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
        Channel channel = (Channel) o;
        if (channel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", url='" + getUrl() + "'" +
                ", contentType='" + getContentType() + "'" +
                ", schedule='" + getSchedule() + "'" +
                ", scheduleTimeZone='" + getScheduleTimeZone() + "'" +
                ", totalLevel=" + getTotalLevel() +
                ", archiveLevel=" + getArchiveLevel() +
                ", unlimitedLevel='" + isUnlimitedLevel() + "'" +
                ", fetchEngine='" + getFetchEngine() + "'" +
                ", category='" + getCategory() + "'" +
                ", tag='" + getTag() + "'" +
                ", categorySlug='" + getCategorySlug() + "'" +
                ", tagSlug='" + getTagSlug() + "'" +
                ", countryCode='" + getCountryCode() + "'" +
                ", languageCode='" + getLanguageCode() + "'" +
                ", targetQueueChannel='" + getTargetQueueChannel() + "'" +
                ", topics='" + getTopics() + "'" +
                ", topicSlugs='" + getTopicSlugs() + "'" +
                ", postType='" + getPostType() + "'" +
                ", rankingCountry=" + getRankingCountry() +
                ", name='" + getName() + "'" +
                ", logo='" + getLogo() + "'" +
                ", siteName='" + getSiteName() + "'" +
                ", channelType='" + getChannelType() + "'" +
                ", levelOneFetchEngine='" + getLevelOneFetchEngine() + "'" +
                ", levelTwoFetchEngine='" + getLevelTwoFetchEngine() + "'" +
                ", levelThreeFetchEngine='" + getLevelThreeFetchEngine() + "'" +
                ", levelFourFetchEngine='" + getLevelFourFetchEngine() + "'" +
                ", levelOneContentType='" + getLevelOneContentType() + "'" +
                ", levelTwoContentType='" + getLevelTwoContentType() + "'" +
                ", levelThreeContentType='" + getLevelThreeContentType() + "'" +
                ", levelFourContentType='" + getLevelFourContentType() + "'" +
                ", allowExternalUrl='" + isAllowExternalUrl() + "'" +
                "}";
    }
}
