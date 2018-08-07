package io.github.crawlerbot.models;


import io.github.crawlerbot.enumerations.ExpectResultType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ConfigGroup.
 */

public class ConfigGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String host;
    private Integer currentLevel;
    private ExpectResultType expectResultType;
    private Set<ConfigMapping> mappings = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ConfigGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public ConfigGroup host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public ConfigGroup currentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
        return this;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public ExpectResultType getExpectResultType() {
        return expectResultType;
    }

    public ConfigGroup expectResultType(ExpectResultType expectResultType) {
        this.expectResultType = expectResultType;
        return this;
    }

    public void setExpectResultType(ExpectResultType expectResultType) {
        this.expectResultType = expectResultType;
    }

    public Set<ConfigMapping> getMappings() {
        return mappings;
    }

    public ConfigGroup mappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
        return this;
    }

    public ConfigGroup addMapping(ConfigMapping configMapping) {
        this.mappings.add(configMapping);
        return this;
    }

    public ConfigGroup removeMapping(ConfigMapping configMapping) {
        this.mappings.remove(configMapping);
        return this;
    }

    public void setMappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
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
        ConfigGroup configGroup = (ConfigGroup) o;
        if (configGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", host='" + getHost() + "'" +
            ", currentLevel=" + getCurrentLevel() +
            ", expectResultType='" + getExpectResultType() + "'" +
            "}";
    }
}
