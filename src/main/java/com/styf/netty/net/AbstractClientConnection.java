package com.styf.netty.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractClientConnection
        implements IClientConnection
{
  private IConnectionHolder holder = null;

  private IMessageHandler messageHandler = null;

  private Map<Object, Object> attributes = null;
  protected byte[] readBytes;

  public AbstractClientConnection(IMessageHandler messageHandler, int readByteslen)
  {
    this.messageHandler = messageHandler;
    this.attributes = new ConcurrentHashMap<Object, Object>();
    this.readBytes = new byte[readByteslen];
  }

  public byte[] getReadBytes()
  {
    return this.readBytes;
  }

  public abstract void setChannelReadBytes();

  public IMessageHandler getPacketHandler()
  {
    return this.messageHandler;
  }

  public IConnectionHolder getHolder()
  {
    return this.holder;
  }

  public void setHolder(IConnectionHolder holder)
  {
    this.holder = holder;
    if (this.holder != null)
    {
      this.holder.setClientConnection(this);
    }
  }

  public void onDisconnect()
  {
    if (this.holder != null)
    {
      this.holder.onDisconnect();
    }
  }

  public void setAttribute(Object key, Object value)
  {
    this.attributes.put(key, value);
  }

  public Object getAttribute(Object key)
  {
    return this.attributes.get(key);
  }
}