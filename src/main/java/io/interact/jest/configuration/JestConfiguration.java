package io.interact.jest.configuration;

import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JestConfiguration {
	
	@JsonProperty
	@NotNull
	String connectionURL = "http://localhost:9200";
	

	public String getConnectionURL() {
		return connectionURL;
	}

	@ValidationMethod
    public boolean isValidConfig() {
        return !StringUtils.isEmpty(connectionURL);
    }
}
