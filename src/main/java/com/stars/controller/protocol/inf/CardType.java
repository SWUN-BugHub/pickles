package com.stars.controller.protocol.inf;
	/**
	 * 卡牌type
	 * @author Administrator
	 *
	 */
public enum CardType
{
  NULL(-1), 
  SAN_PAI(0), 
  DUI_ZI(1),  
  LIANG_DUI(2), 
  SAN_TIAO(3),  
  SHUN_ZI(4),  
  TONG_HUA(5),   
  HU_LU(6),  
  SI_TIAO(7),
  TONG_HUA_SHUN(8), 
  HUANG_JIA_TONG_HUA_SHUN(9)  ;

  private final byte value;

  private CardType(int value)
  {
    this.value = ((byte)value);
  }

  public byte getValue()
  {
    return this.value;
  }

  public static CardType parse(byte type)
  {
    CardType[] items = values();
    for (CardType item : items)
    {
      if (item.getValue() == type)
      {
        return item;
      }
    }
    return NULL;
  }
}