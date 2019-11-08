package com.stars.controller.utils;

import java.util.LinkedList;

public class ImplementRequest
{
  private LinkedList<ManageBufferSession> queue = new LinkedList();
  
  public synchronized void requestNotify(ManageBufferSession data)
  {
    this.queue.addFirst(data);
    notifyAll();
  }
  
  public synchronized ManageBufferSession waitImplement()
  {
    try
    {
      while (this.queue.isEmpty()) {
        wait();
      }
    }
    catch (Exception e)
    {
      return null;
    }
    return (ManageBufferSession)this.queue.pollLast();
  }
}
