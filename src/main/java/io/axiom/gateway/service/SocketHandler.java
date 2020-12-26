package io.axiom.gateway.service;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface SocketHandler {
	
	SocketChannel handle(SelectionKey key);

}
