package io.axiom.gateway.http;

import io.axiom.gateway.constants.AppConstant;
import io.axiom.gateway.service.AbstractSocketHandler;
import io.axiom.gateway.utils.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ReadRequestHandler extends AbstractSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(ReadRequestHandler.class);

	@Override
	public void handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) key.channel();
			byte[] data = readHttpMessage(channel);
			channel.shutdownInput();
			if (data.length > 1) {
				key.interestOps(SelectionKey.OP_WRITE);
			}
		} catch(IOException e) {
			SocketUtils.closeRequest(channel);
			log.error(e.getMessage(), e);
		}
	}

	private byte[] readHttpMessage(SocketChannel channel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(AppConstant.BUFFER_LIMIT);
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
