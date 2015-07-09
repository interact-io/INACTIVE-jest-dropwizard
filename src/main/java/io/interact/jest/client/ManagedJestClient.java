package io.interact.jest.client;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.lifecycle.Managed;
import io.interact.jest.configuration.JestConfiguration;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.GsonBuilder;

/**
 * ManagedJestClient as the name implies is the {@link Managed} instance of a
 * JestClient hooked up into the dropwizard component lifecycle
 * 
 * @author matteo
 *
 */
public class ManagedJestClient implements Managed {

    private static final int READ_TIMEOUT = 60000;

    private static final int MAX_CONNE_IDLE = 10;

    private static final int CONN_TIMEOUT = 30000;

    private final JestClient jestClient;

    private static final Logger LOG = LoggerFactory.getLogger(ManagedJestClient.class);

    public ManagedJestClient(final JestConfiguration config) {
        checkNotNull(config, "JestConfiguration must not be null");

        GsonBuilder gsonBuilder = new GsonBuilder();
        for (String adapter : config.getAdapters()) {
            try {
                GsonAdapter<?> gsonAdapter = (GsonAdapter<?>) Class.forName(adapter).newInstance();
                gsonBuilder.registerTypeAdapter(gsonAdapter.getType(), gsonAdapter);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                LOG.error(String.format("Unable to instance class adapter %s", adapter), e);
                throw new IllegalArgumentException(e);
            }
        }

        Converters.registerDateTime(gsonBuilder);

        JestClientFactory factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig.Builder(config.getConnectionURLs()).multiThreaded(true)

        .readTimeout(READ_TIMEOUT).maxConnectionIdleTime(MAX_CONNE_IDLE, TimeUnit.SECONDS).connTimeout(CONN_TIMEOUT)
                .gson(gsonBuilder.create()).build());
        this.jestClient = factory.getObject();
    }

    public final JestClient getJestClient() {
        return this.jestClient;
    }

    @Override
    public void start() throws Exception {
        LOG.debug("starting the managed jest client");
    }

    @Override
    public void stop() throws Exception {
        this.jestClient.shutdownClient();
    }

}