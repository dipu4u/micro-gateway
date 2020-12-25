package io.axiom.gateway.http;

import io.axiom.gateway.service.RequestProcessor;
import io.axiom.gateway.service.AbstractRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class RequestAcceptor extends AbstractRequestProcessor implements RequestProcessor {

	private static Logger log= LoggerFactory.getLogger(RequestAcceptor.class);
	
	RequestAcceptor(Selector sel) {
		super(sel, SelectionKey.OP_READ);
	}
	
	@Override
	public SelectableChannel process(SelectionKey key) {
		SocketChannel channel = null;
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			channel = serverSocketChannel.accept();
			if(Objects.nonNull(channel)) {
				channel.configureBlocking(false);
			}
		}catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
		return channel;
	}

}
