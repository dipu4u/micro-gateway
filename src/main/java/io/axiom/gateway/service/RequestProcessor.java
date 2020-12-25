package io.axiom.gateway.service;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public interface RequestProcessor {
	
	void handleRequest(SelectionKey key) throws Exception;

	SelectableChannel process(SelectionKey key) ;
	
}
