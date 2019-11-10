package com.styf.netty.net;

public abstract interface IConnectionHolder
{
    public abstract void onDisconnect();

    public abstract IClientConnection getClientConnection();

    public abstract void setClientConnection(IClientConnection paramIClientConnection);
}