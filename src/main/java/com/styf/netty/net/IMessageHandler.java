package com.styf.netty.net;
public abstract interface IMessageHandler
{
  public abstract void process(IClientConnection paramIClientConnection, Object paramObject);
}