package io.axiom.gateway.http;

import io.axiom.gateway.constants.AppConstants;
import io.axiom.gateway.config.ApplicationProperties;
import io.axiom.gateway.service.AbstractSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class WriteResponseHandler extends AbstractSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(WriteResponseHandler.class);
	
	private String response;
	
	private ApplicationProperties props = ApplicationProperties.getInstance();
	
	WriteResponseHandler(Selector selector) {
		super(selector);
		response = props.getProperty(AppConstants.HTTP_RESPONSE);
	}

	@Override
	public SocketChannel handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
			channel.write(buffer);
			channel.shutdownOutput();
		} catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		} finally {
			closeRequest(channel);
		}
		return channel;
	}
}
