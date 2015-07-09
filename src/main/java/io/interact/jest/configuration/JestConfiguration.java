package io.interact.jest.configuration;

import io.dropwizard.validation.ValidationMethod;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class JestConfiguration {

    @JsonProperty
    @NotNull
    protected List<String> connectionURLs = Lists.newArrayList("http://localhost:9200");

    protected List<String> adapters = Lists.newArrayList();

    /**
     * Default constructor. This is actually a not really useful comment for an
     * empty constructor but Sonar is complaining
     * 
     */
    public JestConfiguration() {
    }

    public List<String> getConnectionURLs() {
        return this.connectionURLs;
    }

    public List<String> getAdapters() {
        return this.adapters;
    }

    public void setConnectionURLs(List<String> connectionURLs) {
        this.connectionURLs = connectionURLs;
    }

    public void setAdapters(List<String> adapters) {
        this.adapters = adapters;
    }

    @ValidationMethod
    public boolean isValidConfig() {
        return !this.connectionURLs.isEmpty();
    }

}
