package io.axiom.gateway.http;

import io.axiom.gateway.config.ApplicationProperties;
import io.axiom.gateway.constants.AppConstant;
import io.axiom.gateway.service.AbstractSocketHandler;
import io.axiom.gateway.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class WriteResponseHandler extends AbstractSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(WriteResponseHandler.class);
	private static final AtomicInteger COUNTER = new AtomicInteger(0);

	private static final String response = ApplicationProperties.getInstance().getProperty("http.ok.response");

	@Override
	public void handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.wrap(String.format(response, COUNTER.incrementAndGet()).getBytes(AppConstant.UTF_8_CHARSET));
			channel.write(buffer);
			log.info("Request Count {}", COUNTER.get());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			SocketUtils.closeRequest(channel);
		}
	}

}
