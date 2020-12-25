package io.axiom.gateway.http;

import io.axiom.gateway.constants.AppConstants;
import io.axiom.gateway.exception.InvalidRequestException;
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
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestReader extends AbstractRequestProcessor implements RequestProcessor {

	private static final Logger log = LoggerFactory.getLogger(RequestReader.class);
	
	private static final AtomicInteger COUNTER = new AtomicInteger(1);
	
	private final Charset charset;
	
	private static final int bufferLimit = 256;
	
	private ApplicationProperties props = ApplicationProperties.getInstance();
	
	RequestReader(Selector sel) {
		super(sel, SelectionKey.OP_WRITE);
		String charStr =  props.getProperty(AppConstants.CHARSET_KEY);
		charset = Charset.forName(charStr);
	}
	
	@Override
	public SelectableChannel process(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			byte[] data = readHttpMessage(channel);
			if(data.length == 1) throw new InvalidRequestException();
			log.info("Requester Address {}", channel.getRemoteAddress().toString());
			log.info("Request {}",  + COUNTER.getAndIncrement());
		} catch(IOException | InvalidRequestException e) {
			closeRequest(key,  channel);
			if(e.getClass().equals(IOException.class))
				log.error(e.getMessage(), e);
		}
		return channel;
	}
	
	private byte[] readHttpMessage(SocketChannel channel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(bufferLimit);
		int state;
		byte[] data = {1};
		int position = 0;
		while( (state = channel.read(buffer)) > 0 ) {
			buffer.flip();
			byte[] d = Arrays.copyOf(buffer.array(), state);
			data = Arrays.copyOf(data, position + state);
			System.arraycopy(d, 0, data, position, state);
			position = position + state;
			buffer.clear();
		}
		return data;
	}

}
