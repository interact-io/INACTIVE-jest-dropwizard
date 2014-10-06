package io.interact.jest.client;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public abstract class GsonAdapter<T> implements JsonSerializer<T>,JsonDeserializer<T>{
	public abstract Class<T> getType();
	
}
