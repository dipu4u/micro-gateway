package io.axiom.gateway.processor;

import io.axiom.gateway.config.ApplicationProperties;
import io.axiom.gateway.constants.AppConstant;
import io.axiom.gateway.model.SocketChannelData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestExecutorThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestExecutorThread.class);

    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final String RESPONSE = ApplicationProperties.getInstance().getProperty("http.ok.response");

    private final SelectionKey selectionKey;
    private final SocketChannelData data;

    public RequestExecutorThread(SelectionKey selectionKey, SocketChannelData data) {
        this.data = data;
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        SocketChannel channel = null;
        try {
            log.info("Request {}", COUNTER.incrementAndGet());
            //log.info("Request Address {}", data.getInetAddress().getHostName());
            channel = (SocketChannel) selectionKey.channel();
            channel.write(ByteBuffer.wrap(String.format(RESPONSE, COUNTER.get()).getBytes(AppConstant.UTF_8_CHARSET)));
            channel.shutdownOutput();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
