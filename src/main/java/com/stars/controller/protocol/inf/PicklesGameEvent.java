package com.stars.controller.protocol.inf;

public enum PicklesGameEvent {
	  NULL(-1), 
	  STOP(0), 
	  KAN_PAI(1), 
	  XIA_ZHU(2), 
	  COMPARE_CARD(3), 
	  QI_PAI(4), 
	  GAME_OVER(5), 
	  CHANGE_STATE(6),
	  QIANG_ZHUANG(7),		//抢庄
	  LAO_PAI(8),			//捞牌
	  ZHUANG_LAO_PAI(9),		//庄捞牌
	  EXPLODE(10),			//炸开
	  ZHUANG_EXPLODE(11);	//庄炸开
	  private byte value;

	  private PicklesGameEvent(int value)
	  {
	    this.value = ((byte)value);
	  }

	  public byte getValue()
	  {
	    return this.value;
	  }

	  public static PicklesGameEvent parse(byte type)
	  {
	    for (PicklesGameEvent gameState : values())
	    {
	      if (type == gameState.value)
	        return gameState;
	    }
	    return NULL;
	  }
}
