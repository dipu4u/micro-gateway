package io.axiom.gateway.bootstrap;

import io.axiom.gateway.service.NetworkService;
import io.axiom.gateway.config.ApplicationProperties;
import io.axiom.gateway.http.HttpNetworkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapHttpServer {

	private final static Logger log = LoggerFactory.getLogger(BootstrapHttpServer.class);
	
	public void bootServer() {
		ApplicationProperties properties = ApplicationProperties.getInstance();
		log.info("Server Configuration loaded");
		int port = Integer.parseInt(properties.getProperty("server.port").trim());
		
		NetworkService server = new HttpNetworkServiceImpl();
		server.initServer(port);
		server.startServer();
	}
	
}
