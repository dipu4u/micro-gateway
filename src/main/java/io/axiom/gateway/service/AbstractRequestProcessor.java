package io.axiom.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;



public abstract class AbstractRequestProcessor implements RequestProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractRequestProcessor.class);
	
	protected Selector selector;
	private int nextOps;
	
	protected AbstractRequestProcessor(Selector sel, int ops) {
		selector = sel;
		nextOps = ops;
		log.info("Abstract Processor " + nextOps + " : " + this.getClass().getName());
	}
	
	@Override
	public void handleRequest(SelectionKey key) throws Exception {
		SelectableChannel channel = process(key);
		if(Objects.nonNull(channel)) {
			channel.register(selector, nextOps);
		}
	}
	
	@Override
	public abstract SelectableChannel process(SelectionKey key);
	
	protected void closeRequest(SelectionKey key, SocketChannel channel) {
		key.cancel();
		try {
			channel.close();
		} catch (IOException e) {}
	}

}
