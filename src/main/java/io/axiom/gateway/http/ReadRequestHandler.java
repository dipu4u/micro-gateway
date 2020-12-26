package io.axiom.gateway.http;

import io.axiom.gateway.constants.AppConstants;
import io.axiom.gateway.service.AbstractSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadRequestHandler extends AbstractSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(ReadRequestHandler.class);
	
	private static final AtomicInteger COUNTER = new AtomicInteger(1);

	ReadRequestHandler(Selector selector) {
		super(selector);
	}

	@Override
	public SocketChannel handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			byte[] data = readHttpMessage(channel);
			channel.shutdownInput();
			if(data.length > 1) {
				log.info("Requester Address {}", channel.getRemoteAddress().toString());
				log.info("Request {}", +COUNTER.getAndIncrement());
				return channel;
			}
		} catch(IOException e) {
			log.error(e.getMessage(), e);
		}
		closeRequest(channel);
		return null;
	}

	private byte[] readHttpMessage(SocketChannel channel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(AppConstants.BUFFER_LIMIT);
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
