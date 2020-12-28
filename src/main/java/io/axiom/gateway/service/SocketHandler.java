package io.axiom.gateway.service;

import java.nio.channels.SelectionKey;

public interface SocketHandler {
	
	void handle(SelectionKey key);

}
