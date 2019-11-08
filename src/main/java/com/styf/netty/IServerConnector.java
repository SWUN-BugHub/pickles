package com.styf.netty;

public abstract class IServerConnector {
    public abstract String getAddress();

    public abstract int getPort();

    public abstract int getMaxReconnectTimes();

    public abstract void setMaxReconnectTimes(int paramInt);

    public abstract int getReconnectedTimes();

    public abstract boolean connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract void send(Object paramObject);

    public abstract IServerPacketHandler getPacketHandler();

    public abstract byte[] getReadBytes();
}
