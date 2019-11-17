package com.stars.controller.protocol.inf;

public enum PicklesGameState
{
  NULL(-1), 
  INITED(0), 
  XIA_ZHU(1), 
  COMPARE_CARD(2), 
  SYSTEM_COMPARE(3), 
  TEMP_COMPARE(4), 
  LIANG_PAI(5), 
  GAME_OVER(6), 
  GAME_STOPED(7);

  private byte value;

  private PicklesGameState(int value)
  {
    this.value = ((byte)value);
  }

  public byte getValue()
  {
    return this.value;
  }

  public static PicklesGameState parse(byte type)
  {
    for (PicklesGameState gameState : values())
    {
      if (type == gameState.value)
        return gameState;
    }
    return NULL;
  }
}