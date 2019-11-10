package com.styf.netty.net;

public abstract class AbstractServerConnector
        implements IServerConnector
{
  public static final int DEFAULT_MAX_RECONNECT_TIMES = 30;
  protected int reconnectedTimes;
  private String address;
  private int port;
  private int maxReconnectTimes;
  private IServerPacketHandler packetHandler = null;

  private boolean isServerClosed = false;
  protected byte[] readBytes;

  public AbstractServerConnector(String address, int port, IServerPacketHandler packetHandler)
  {
    this.address = address;
    this.port = port;
    this.maxReconnectTimes = 30;
    this.reconnectedTimes = 0;
    this.packetHandler = packetHandler;
    this.readBytes = new byte[65534];
  }

  public abstract void setChannelReadBytes();

  public byte[] getReadBytes()
  {
    return this.readBytes;
  }

  public String getAddress()
  {
    return this.address;
  }

  public int getPort()
  {
    return this.port;
  }

  public int getMaxReconnectTimes()
  {
    return this.maxReconnectTimes;
  }

  public void setMaxReconnectTimes(int maxReconnectTimes)
  {
    this.maxReconnectTimes = maxReconnectTimes;
  }

  public int getReconnectedTimes()
  {
    return this.reconnectedTimes;
  }

  public IServerPacketHandler getPacketHandler()
  {
    return this.packetHandler;
  }

  public boolean isServerClosed()
  {
    return this.isServerClosed;
  }

  public void setServerClosed(boolean isServerClosed)
  {
    this.isServerClosed = isServerClosed;
  }
}