package io.axiom.gateway;

import io.axiom.gateway.bootstrap.BootstrapHttpServer;

public class StartHttpServer {

	public static void main(String[] arg) {
		new BootstrapHttpServer().bootServer();
	}
}
