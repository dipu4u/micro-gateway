package io.axiom.gateway.utils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public final class SocketUtils {

    public static void closeRequest(SocketChannel channel) {
        if (Objects.nonNull(channel)) {
            try {
                channel.close();
            } catch (IOException e) {}
        }
    }

    private SocketUtils() {}
}
