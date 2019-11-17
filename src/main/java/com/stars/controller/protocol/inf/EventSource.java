package com.stars.controller.protocol.inf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSource
  implements IEventSource
{
  private static final Logger LOGGER = LoggerFactory.getLogger(EventSource.class);
  private Map<Integer, Collection<IEventListener>> listeners;
  private ReadWriteLock lock;

  public EventSource()
  {
    this.listeners = new ConcurrentHashMap();
    this.lock = new ReentrantReadWriteLock();
  }

  public void addListener(int eventType, IEventListener listener)
  {
    if (listener == null) {
      LOGGER.error("listener is null", new NullPointerException());
    }
    Collection lstns = (Collection)this.listeners.get(Integer.valueOf(eventType));
    if (lstns == null)
    {
      try
      {
        this.lock.writeLock().lock();

        lstns = (Collection)this.listeners.get(Integer.valueOf(eventType));
        if (lstns == null)
        {
          lstns = new LinkedList();
          lstns.add(listener);
          this.listeners.put(Integer.valueOf(eventType), lstns);
        }
        else
        {
          lstns.add(listener);
        }
      }
      catch (Exception e)
      {
        LOGGER.error("", e);
      }
      finally
      {
        this.lock.writeLock().unlock();
      }
    }
    else
    {
      lstns.add(listener);
    }
    LOGGER.debug("Added a listener: {}, {}", Integer.valueOf(eventType), listener);
  }

  public void removeListener(int eventType, IEventListener listener)
  {
    try
    {
      this.lock.writeLock().lock();

      Collection lstns = (Collection)this.listeners.get(Integer.valueOf(eventType));
      if (lstns != null)
      {
        lstns.remove(listener);
      }
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }
    finally
    {
      this.lock.writeLock().unlock();
    }

    LOGGER.debug("Removed a listener: {}, {}", Integer.valueOf(eventType), listener);
  }

  /**
   * 比牌经过的方法
   */
  public void notifyListeners(EventArg arg)
  {
    try
    {
      this.lock.writeLock().lock();

      Collection<IEventListener> lstns = (Collection)this.listeners.get(Integer.valueOf(arg.getEventType()));
      if (lstns != null)
      {
        lstns = new ArrayList(lstns);
        for (IEventListener item : lstns)
        {
          item.onEvent(arg);
        }
      }
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }
    finally
    {
      this.lock.writeLock().unlock();
    }
  }

  public void notifyListeners(int eventType)
  {
    try
    {
      this.lock.writeLock().lock();
      Collection<IEventListener> lstns = (Collection)this.listeners.get(Integer.valueOf(eventType));
      if (lstns != null)
      {
        lstns = new ArrayList(lstns);
        for (IEventListener item : lstns)
        {
          item.onEvent(new EventArg(this, eventType));
        }
      }
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }
    finally
    {
      this.lock.writeLock().unlock();
    }
  }
}