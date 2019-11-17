package com.stars.controller.protocol.inf;

import java.util.EventObject;

public class EventArg extends EventObject
{
  private static final long serialVersionUID = -3737140928956265655L;
  private int eventType;
  private Object data;
  private String triParams;

  public EventArg(Object source, int eventType)
  {
    super(source);
    this.eventType = eventType;
  }

  public EventArg(Object source, int eventType, Object data)
  {
    super(source);
    this.eventType = eventType;
    this.data = data;
  }

  public EventArg(Object source, int eventType, Object[] data)
  {
    super(source);
    this.eventType = eventType;
    this.data = data;
  }

  public int getEventType()
  {
    return this.eventType;
  }

  public Object getData()
  {
    return this.data;
  }

  public void setData(Object data)
  {
    this.data = data;
  }

  public String getTriParams()
  {
    return this.triParams;
  }

  public void setTriParams(String triParams)
  {
    this.triParams = triParams;
  }

  public void setSource(Object source)
  {
    this.source = source;
  }

  public void setEventType(int eventType)
  {
    this.eventType = eventType;
  }
}