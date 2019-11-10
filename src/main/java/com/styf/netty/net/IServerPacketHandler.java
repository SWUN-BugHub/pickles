package com.styf.netty.net;

public abstract interface IServerPacketHandler
{
  public abstract void process(IServerConnector paramIServerConnector, Object paramObject);
}