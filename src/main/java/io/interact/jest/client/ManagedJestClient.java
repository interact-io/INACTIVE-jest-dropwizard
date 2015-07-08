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

public class ManagedJestClient implements Managed {

    private static final int READ_TIMEOUT_MS = 60000;

    private static final int MAX_CONNECTION_IDLE_SECS = 10;

    private final JestClient jestClient;

    private final static Logger LOG = LoggerFactory.getLogger(ManagedJestClient.class);

    public JestClient getClient() {
        return this.jestClient;
    }

    public ManagedJestClient(final JestConfiguration config) {
        checkNotNull(config, "JestConfiguration must not be null");

        GsonBuilder gsonBuilder = new GsonBuilder();
        for (String adapter : config.getAdapters()) {
            try {
                GsonAdapter<?> ga = (GsonAdapter<?>) Class.forName(adapter).newInstance();
                gsonBuilder.registerTypeAdapter(ga.getType(), ga);
            } catch (Exception e) {
                LOG.error(String.format("Unable to instance class adapter %s", adapter), e);
                throw new RuntimeException(e);
            }
        }

        Converters.registerDateTime(gsonBuilder);

        JestClientFactory factory = new JestClientFactory();

        int CONNECTION_TIMEOUT_MS = 30000;
        factory.setHttpClientConfig(new HttpClientConfig.Builder(config.getConnectionURLS()).multiThreaded(true)

        .readTimeout(READ_TIMEOUT_MS).maxConnectionIdleTime(MAX_CONNECTION_IDLE_SECS, TimeUnit.SECONDS)
                .connTimeout(CONNECTION_TIMEOUT_MS).gson(gsonBuilder.create()).build());
        this.jestClient = factory.getObject();
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