package io.interact.jest.configuration;

import io.dropwizard.validation.ValidationMethod;
import io.interact.jest.client.GsonAdapter;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;


public class JestConfiguration {
	
	@JsonProperty
	@NotNull
	List<String> connectionURLs = Lists.newArrayList("http://localhost:9200");
	
	List<String> adapters = Lists.newArrayList();
	
	
	public List<String> getConnectionURLS() {
		return connectionURLs;
	}
	
	
	public List<String> getAdapters() {
		return adapters;
	}


	@ValidationMethod
    public boolean isValidConfig() {
        return !connectionURLs.isEmpty();
    }
}


