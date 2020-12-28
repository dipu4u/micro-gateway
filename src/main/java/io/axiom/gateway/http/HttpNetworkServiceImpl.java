package io.axiom.gateway.http;

import io.axiom.gateway.constants.SocketConstant;
import io.axiom.gateway.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class HttpNetworkServiceImpl implements NetworkService, Runnable {

	private final static Logger log = LoggerFactory.getLogger(HttpNetworkServiceImpl.class);
	
	private boolean listen = false;
	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;

	@Override
	public void startServer() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		log.info("Server listener started");
		while(listen) {
			try {
				int count = selector.select();
	            if (count > 0) {
	                for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
	                    SelectionKey key = i.next();
	                    i.remove();
	                    if (key.isAcceptable()) {
	                    	new AcceptRequestHandler(serverSocketChannel).handle(key);
	                    }
	                    if (key.isReadable()) {
	                    	new ReadRequestHandler().handle(key);
	                    }
						if (key.isValid() && key.isWritable()) {
							new WriteResponseHandler().handle(key);
						}
	                }
	            }
			} catch(Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	@Override
	public void initServer(int serverPort) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverPort);
			serverSocketChannel.socket().bind(inetSocketAddress, SocketConstant.SOCKET_BACKLOG);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.setSoTimeout(SocketConstant.SOCKET_TIMEOUT);
			serverSocket.setReuseAddress(SocketConstant.RE_USE_ADDRESS);
			serverSocket.setReceiveBufferSize(SocketConstant.SND_RCV_BUFFER_SIZE);
			serverSocket.setPerformancePreferences(1, 1, 2);
			listen = true;
			log.info("Server initialised on port {}", serverPort);
		} catch(IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
