package com.styf.netty;

public abstract class IConnectionHolder {
    public abstract void onDisconnect();

    public abstract IClientConnection getClientConnection();

    public abstract void setClientConnection(IClientConnection paramIClientConnection);
}
