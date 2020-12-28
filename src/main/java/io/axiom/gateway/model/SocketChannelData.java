package io.axiom.gateway.model;

import java.net.InetAddress;

public class SocketChannelData {

    private final String requestId;
    private byte[] requestData;
    private InetAddress inetAddress;

    public SocketChannelData(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
}
