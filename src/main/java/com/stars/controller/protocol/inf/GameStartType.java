package com.stars.controller.protocol.inf;

public enum GameStartType
{
  HAND_START(0), 
  FIRST(-1), 
  PREPARE(-2), 
  OWNER(-3);

  private final byte value;

  private GameStartType(int value)
  {
    this.value = ((byte)value);
  }

  public byte getValue()
  {
    return this.value;
  }

  public static GameStartType parse(int value)
  {
    switch (value)
    {
    case 0:
      return HAND_START;
    case -1:
      return FIRST;
    case -2:
      return PREPARE;
    case -3:
      return OWNER;
    }
    return null;
  }
}