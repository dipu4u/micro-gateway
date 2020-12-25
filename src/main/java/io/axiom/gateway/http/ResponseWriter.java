package io.axiom.gateway.http;

import io.axiom.gateway.constants.AppConstants;
import io.axiom.gateway.service.RequestProcessor;
import io.axiom.gateway.config.ApplicationProperties;
import io.axiom.gateway.service.AbstractRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ResponseWriter extends AbstractRequestProcessor implements RequestProcessor {

	private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);
	
	private String response;
	
	private ApplicationProperties props = ApplicationProperties.getInstance();
	
	ResponseWriter(Selector sel) {
		super(sel, -1);
		response = props.getProperty(AppConstants.HTTP_RESPONSE);
	}
	
	@Override
	public SelectableChannel process(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
			channel.write(buffer);
		} catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		} finally {
			closeRequest(key, channel);
		}
		return null;
	}

}
