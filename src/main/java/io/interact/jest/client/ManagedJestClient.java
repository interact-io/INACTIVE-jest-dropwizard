package io.interact.jest.client;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.lifecycle.Managed;
import io.interact.jest.configuration.JestConfiguration;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

public class ManagedJestClient implements Managed {
	
	private JestClient jestClient;

	public JestClient getClient(){
		return jestClient;
	}
	
	public ManagedJestClient(final JestConfiguration config) {
        checkNotNull(config, "JestConfiguration must not be null");
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                               .Builder(config.getConnectionURL())
                               .multiThreaded(true)
                               .build());
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