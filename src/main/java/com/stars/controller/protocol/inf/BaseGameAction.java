package com.stars.controller.protocol.inf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseGameAction<T extends BaseGame>
{
  protected static final Logger logger = LoggerFactory.getLogger(BaseGameAction.class);
  protected long tick;
  protected long finishDelay;
  protected long finishTick;
  protected long remainTick;
  protected boolean check;

  public BaseGameAction()
  {
    this(0);
  }

  public BaseGameAction(int delay)
  {
    this(delay, 0);
  }

  public BaseGameAction(int delay, int finishDelay)
  {
    this.tick = (System.currentTimeMillis() + delay);
    this.finishDelay = finishDelay;
    this.finishTick = 9223372036854775807L;
    this.remainTick = finishDelay;
  }

  public void execute(T game, long tick)
  {
    if ((this.tick <= tick) && (this.finishTick == 9223372036854775807L))
    {
      executeImp(game, tick);
    }
  }

  public void executeImp(T game, long tick)
  {
    finish(tick);
  }

  protected void finish(long tick)
  {
    this.finishTick = (tick + this.finishDelay);
  }

  public boolean isFinished(T game, long tick)
  {
    return this.finishTick < tick;
  }

  public long getFinishTick()
  {
    return this.finishTick;
  }

  public long getTick()
  {
    return this.tick;
  }

  public void setTick(long tick)
  {
    this.tick = tick;
  }

  public long getRemainTick()
  {
    return this.remainTick;
  }

  public void setRemainTick(long tick)
  {
    this.remainTick = (this.tick - tick);
  }

  public boolean isCheck()
  {
    return this.check;
  }

  public void setCheck(boolean check)
  {
    this.check = check;
  }


}