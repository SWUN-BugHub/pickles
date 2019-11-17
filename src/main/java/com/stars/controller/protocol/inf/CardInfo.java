package com.stars.controller.protocol.inf;

import java.util.Arrays;
import java.util.List;


public class CardInfo {
	private List<Integer> cards;

	public List<Integer> getCards() {
		return cards;
	}

	public void setCards(List<Integer> cards) {
		this.cards = cards;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int count;
	private int type;

	  public void reset()
	  {
	    this.cards = null;
	    this.type = 0;
	    this.count = 0;
	  }

	@Override
	public String toString() {
		return "卡牌为"+ Arrays.asList(cards)+"点数为"+count+"type为"+type;
	}
}
