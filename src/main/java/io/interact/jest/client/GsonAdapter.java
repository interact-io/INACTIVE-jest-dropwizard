package io.interact.jest.client;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * 
 * The GsonAdapter interface wraps the functionality of a
 * serialization/deserialization process.
 * 
 * <p>
 * This is due to the fact that Jest library uses GSON while almost all interact
 * projects are using Jackson. With this functionality is possible to register
 * an adapter implementation that allows to hook into the gson ser/deser process
 * and replace with Jackson
 * </p>
 * 
 * @author matteo
 *
 * @param <T>
 *            the class generics
 */
public interface GsonAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * The getType method is used to extract the className of the generics of
     * the implementation of this interface to kickstart the deserialization
     * process of Jackson.
     * 
     * @return the ClassName of the type of the generic for the implementation
     */
    Class<T> getType();

}
