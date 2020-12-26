package io.axiom.gateway.http;

import io.axiom.gateway.service.NetworkService;
import io.axiom.gateway.service.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class HttpNetworkServiceImpl implements NetworkService, Runnable {

	private final static Logger log = LoggerFactory.getLogger(HttpNetworkServiceImpl.class);
	
	private boolean listen = false;
	
	private ServerSocketChannel serverSocket = null;
	private Selector selector = null;

	private SocketHandler acceptRequestHandler = null;
	private SocketHandler readRequestHandler = null;
	private SocketHandler writeResponseHandler = null;

	public HttpNetworkServiceImpl() {}
	
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
							acceptRequestHandler.handle(key);
	                    }
	                    if (key.isReadable()) {
							SocketChannel channel = readRequestHandler.handle(key);
							if (null != channel) channel.register(selector, SelectionKey.OP_WRITE);
	                    }
	                    try {
							if (key.isWritable()) {
								writeResponseHandler.handle(key);
							}
						} catch(CancelledKeyException cke) {}
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
			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(false);
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverPort);
			serverSocket.socket().bind(inetSocketAddress);
			serverSocket.register(selector, serverSocket.validOps(), null);
			listen = true;

			acceptRequestHandler = new AcceptRequestHandler(selector, serverSocket);
			readRequestHandler = new ReadRequestHandler(selector);
			writeResponseHandler = new WriteResponseHandler(selector);
			log.info("Server initialised on port {}", serverPort);
		} catch(IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
