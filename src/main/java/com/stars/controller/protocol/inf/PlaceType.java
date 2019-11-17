package com.stars.controller.protocol.inf;

public enum PlaceType
{
  NULL(0), 
  UNPREPARE(1), //未准备
  PREPARE(2), 	//准备
  PLAYING(3);	//游戏

  private byte value;

  private PlaceType(int value)
  {
    this.value = ((byte)value);
  }

  public byte getValue()
  {
    return this.value;
  }

  public static PlaceType parse(int value)
  {
    switch (value)
    {
    case 0:
      return NULL;
    case 1:
      return UNPREPARE;
    case 2:
      return PREPARE;
    case 3:
      return PLAYING;
    }
    return NULL;
  }
}