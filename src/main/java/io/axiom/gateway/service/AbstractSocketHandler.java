package io.axiom.gateway.service;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public abstract class AbstractSocketHandler implements SocketHandler {

	protected void closeRequest(SocketChannel channel) {
		if (Objects.nonNull(channel)) {
			try {
				channel.close();
			} catch (IOException e) {}
		}
	}

}
