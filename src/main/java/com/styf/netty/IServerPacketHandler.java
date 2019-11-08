package com.styf.netty;

public abstract class IServerPacketHandler {
    public abstract void process(IServerConnector paramIServerConnector, Object paramObject);
}
