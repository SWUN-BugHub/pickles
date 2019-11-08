package com.styf.netty;

public abstract class IMessageHandler {
    public abstract void process(IClientConnection paramIClientConnection, Object paramObject);
}
