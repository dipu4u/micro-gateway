package io.axiom.gateway.http;

import io.axiom.gateway.constants.SocketConstant;
import io.axiom.gateway.service.AbstractSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class AcceptRequestHandler extends AbstractSocketHandler {

	private static Logger log= LoggerFactory.getLogger(AcceptRequestHandler.class);

	private final ServerSocketChannel serverSocketChannel;

	AcceptRequestHandler(ServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}

	@Override
	public void handle(SelectionKey key) {
		SocketChannel channel = null;
		try {
			channel = serverSocketChannel.accept();
			channel.configureBlocking(false);
			Socket socket = channel.socket();
			socket.setReuseAddress(SocketConstant.RE_USE_ADDRESS);
			socket.setTcpNoDelay(SocketConstant.TCP_NO_DELAY);
			socket.setSoTimeout(SocketConstant.SOCKET_TIMEOUT);
			socket.setPerformancePreferences(1, 1, 2);

			if (channel.isConnectionPending())
				channel.finishConnect();
			channel.register(key.selector(), SelectionKey.OP_READ, UUID.randomUUID().toString());
		} catch(IOException ioe) {
			closeRequest(channel);
			log.error(ioe.getMessage(), ioe);
		}
	}

}
