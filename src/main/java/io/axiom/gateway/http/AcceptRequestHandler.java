package io.axiom.gateway.http;

import io.axiom.gateway.service.AbstractSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptRequestHandler extends AbstractSocketHandler {

	private static Logger log= LoggerFactory.getLogger(AcceptRequestHandler.class);

	private final ServerSocketChannel serverSocketChannel;

	AcceptRequestHandler(Selector selector, ServerSocketChannel serverSocketChannel) {
		super(selector);
		this.serverSocketChannel = serverSocketChannel;
	}

	@Override
	public SocketChannel handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = serverSocketChannel.accept();
			channel.configureBlocking(false);
			channel.finishConnect();
			channel.register(selector, SelectionKey.OP_READ);
		} catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			closeRequest(channel);
		}
		return null;
	}
}
