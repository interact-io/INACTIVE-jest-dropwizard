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

    private JestClient jestClient;
    private final static Logger LOG = LoggerFactory.getLogger(ManagedJestClient.class);

    public JestClient getClient() {
        return jestClient;
    }

    public ManagedJestClient(final JestConfiguration config) {
        checkNotNull(config, "JestConfiguration must not be null");

        GsonBuilder gb = new GsonBuilder();
        for (String adapter : config.getAdapters()) {
            try {
                GsonAdapter<?> ga = (GsonAdapter<?>) Class.forName(adapter).newInstance();
                gb.registerTypeAdapter(ga.getType(), ga);
            } catch (Exception e) {
                LOG.error(String.format("Unable to instance class adapter %s", adapter), e);
                throw new RuntimeException(e);
            }
        }

        Converters.registerDateTime(gb);

        JestClientFactory factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig.Builder(config.getConnectionURLS()).multiThreaded(true)

        .readTimeout(60000).maxConnectionIdleTime(10, TimeUnit.SECONDS).connTimeout(30000).gson(gb.create()).build());
        this.jestClient = factory.getObject();
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        jestClient.shutdownClient();
    }

}