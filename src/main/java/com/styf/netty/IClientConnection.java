package com.styf.netty;

import org.aspectj.bridge.IMessageHandler;

public abstract class IClientConnection {
    public abstract String getClientIP();

    public abstract IMessageHandler getPacketHandler();

    public abstract IConnectionHolder getHolder();

    public abstract void setHolder(IConnectionHolder paramIConnectionHolder);

    public abstract void send(Object paramObject);

    public abstract void onDisconnect();

    public abstract void setEncryptionKey(int[] paramArrayOfInt);

    public abstract void setDecryptionKey(int[] paramArrayOfInt);

    public abstract void closeConnection(boolean paramBoolean);

    public abstract void setAttribute(Object paramObject1, Object paramObject2);

    public abstract Object getAttribute(Object paramObject);

    public abstract boolean isServerClosed();

    public abstract void setIsServerClosed(boolean paramBoolean);

    public abstract boolean isConnected();

    public abstract byte[] getReadBytes();
}
