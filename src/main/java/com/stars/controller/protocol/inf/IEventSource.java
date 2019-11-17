package com.stars.controller.protocol.inf;

public abstract interface IEventSource
{
  public abstract void addListener(int paramInt, IEventListener paramIEventListener);

  public abstract void removeListener(int paramInt, IEventListener paramIEventListener);

  public abstract void notifyListeners(EventArg paramEventArg);

  public abstract void notifyListeners(int paramInt);
}