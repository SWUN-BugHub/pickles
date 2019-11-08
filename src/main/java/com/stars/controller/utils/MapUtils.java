package com.stars.controller.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils
{
  private Map<String, Object> objects;
  
  public MapUtils()
  {
    this.objects = new HashMap();
  }
  
  public MapUtils setMap(String key, Object value)
  {
    this.objects.put(key, value);
    return this;
  }
  
  public Map<String, Object> getObjects()
  {
    return this.objects;
  }
  
  public static MapUtils getMap()
  {
    MapUtils result = new MapUtils();
    return result;
  }
}
