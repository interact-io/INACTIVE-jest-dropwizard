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

    public List<String> getConnectionURLs() {
        return this.connectionURLs;
    }

    public List<String> getAdapters() {
        return this.adapters;
    }

    @ValidationMethod
    public boolean isValidConfig() {
        return !this.connectionURLs.isEmpty();
    }

    /**
     * Default constructor
     */
    public JestConfiguration() {
    }
}
