package io.axiom.gateway.constants;

public final class SocketConstant {

    public static final int SOCKET_BACKLOG = 20_000;
    public static final boolean RE_USE_ADDRESS = true;
    public static final int SOCKET_TIMEOUT = 120_000;
    public static final boolean TCP_NO_DELAY = false;
    public static final int SND_RCV_BUFFER_SIZE = 256;

    private SocketConstant() {}
}
