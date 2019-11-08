package com.stars.controller.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
public class ManageBufferSession
{
  public ChannelHandlerContext ctx;
  public String method;
  public Object[] result;
  public ByteBuf buffer;
  
  public ManageBufferSession() {}
  
  public ManageBufferSession(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    this.ctx = ctx;
    this.buffer = buffer;
  }
  
  public ManageBufferSession(ChannelHandlerContext ctx, String method, Object[] result)
  {
    this.ctx = ctx;
    this.method = method;
    this.result = result;
  }
}
