package com.styf.netty;


import com.styf.netty.net.AbstractClientConnection;
import com.styf.netty.net.IMessageHandler;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;


public class NettyClientConnection extends AbstractClientConnection
{
  private Channel channel = null;

  private boolean isServerClosed = false;

  public NettyClientConnection(IMessageHandler mesageHandler, Channel channel, int readByteslen)
  {
    super(mesageHandler, readByteslen);
    this.channel = channel;
    setChannelReadBytes();
  }

  public String getClientIP()
  {
    String ip = (String)this.channel.attr(CommonConst.CLIENT_IP).get();
    if (ip != null)
      return ip;
    InetSocketAddress insocket = (InetSocketAddress)this.channel.remoteAddress();
    return insocket.getAddress().getHostAddress();
  }

  public void send(Object packet)
  {
    if (isConnected())
    {
      this.channel.writeAndFlush(packet);
    }
  }

  public void setEncryptionKey(int[] key)
  {
    this.channel.attr(CommonConst.NETTY_ENCRYPTION_KEY).set(key);
  }

  public void setDecryptionKey(int[] key)
  {
    this.channel.attr(CommonConst.NETTY_DECRYPTION_KEY).set(key);
  }

  public void closeConnection(boolean immediately)
  {
    if (isConnected())
    {
      this.channel.close();
    }
  }

  public boolean isServerClosed()
  {
    return this.isServerClosed;
  }

  public void setIsServerClosed(boolean isServerClosed)
  {
    this.isServerClosed = isServerClosed;
  }

  public boolean isConnected()
  {
    if ((this.channel != null) && (this.channel.isActive()))
    {
      return true;
    }

    return false;
  }

  public void setChannelReadBytes()
  {
    this.channel.attr(CommonConst.READ_BYTES).set(this.readBytes);
  }
}