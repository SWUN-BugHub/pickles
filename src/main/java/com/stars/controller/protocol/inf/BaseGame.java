package com.stars.controller.protocol.inf;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stars.controller.entity.GameUser;
import com.stars.controller.entity.PicklesDesk;
import com.stars.controller.util.ThreadSafeRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseGame extends EventSource
  implements Runnable
{
  protected static final Logger LOGGER = LoggerFactory.getLogger(BaseGame.class.getName());
  protected static ThreadSafeRandom random = new ThreadSafeRandom();
  protected static final int DEFAULT_WAIT_TIME = 10000;
  protected static final int DEFAULT_WAIT_DELAY = 10;
  protected int CARD_COUNT = 0;
  protected int offset;
  protected Object curState;
  protected long id;
  protected IBaseRoom room;
  protected ArrayDeque<BaseGameAction<BaseGame>> addActions = new ArrayDeque();

  private ArrayDeque<BaseGameAction<BaseGame>> runActions = new ArrayDeque();
  protected Map<Integer, GameUser> allPlayers;
  protected Set<GameUser> watchers;
  protected Set<GameUser> offlineWatchers;
  protected Map<Integer,GameUser> players;
  protected LocalDateTime stateEndTime;
  protected int exceptionCount;
  private long waitTime;
  protected byte[] defaultCard;
  protected byte[] adminCard;
  protected boolean dissolutionLock;
  protected long pauseTime;
  protected int stateReaminTime;
  protected int placeIndex;

  public IBaseRoom getRoom()
  {
    return this.room;
  }

  public PicklesDesk getRoomInfo()
  {
    return this.room.getRoomInfo();
  }

  public void setAdminCard(byte[] card)
  {
    this.adminCard = card;
  }

  public long getWaitTime()
  {
    return this.waitTime;
  }

  public void waitTime(int delay)
  {
    this.waitTime = Math.max(this.waitTime, System.currentTimeMillis() + delay);
  }

  public void waitTimeDefault()
  {
    waitTime(10000);
  }

  public void sendToAll(String method,Object[] message)
  {
    sendToAll(method,message, null);
  }

  public void sendToAll(String method,Object[] message, GameUser except)
  {
    Iterator<GameUser> iterator=watchers.iterator();
    while(iterator.hasNext())
    {
      GameUser user=iterator.next();
      user.sendPacket( method,message);
    }
    Set<Integer> keySet=players.keySet();
    for(Integer set:keySet)
    {
      players.get(set).sendPacket(method,message);
    }
  }

  public void sendToWatch(String method,Object[] message)
  {
    for (GameUser player : this.watchers)
    {
      if (!this.offlineWatchers.contains(player))
        player.sendPacket(method,message);
    }
  }

  public BaseGame(IBaseRoom room, Map<Integer,GameUser> players, Set<GameUser> watchers)
  {
    this.allPlayers = new HashMap();
    this.watchers = new HashSet();
    this.offlineWatchers = new HashSet();
    this.room = room;
    this.curState = getCurState();
    this.players = players;
    this.watchers.addAll(watchers);
    this.dissolutionLock = false;
  }

  public boolean isDissolutionLock()
  {
    return this.dissolutionLock;
  }

  public void setDissolutionLock(boolean dissolutionLock)
  {
    LOGGER.error("是否在解散中: " + dissolutionLock);
    this.dissolutionLock = dissolutionLock;
  }

  public abstract Object getCurState();

  public abstract boolean isStoped();

  public void setCurState(Object curState, int time)
  {
    this.curState = curState;
    this.stateEndTime = LocalDateTime.now().plusSeconds(time);
    sendGameState(time);
    notifyPlayer();
    LOGGER.error("GameState -> " + this.curState);
  }

  public abstract void notifyPlayer();

  public abstract void notifyPlayer(GameUser paramBasePlayer);

  public abstract void sendGameState(int paramInt);

  public void setCurState(Object curState)
  {
    setCurState(curState, 10);
  }

  public long getId()
  {
    return this.id;
  }

  public int getPlaceIndex()
  {
    return this.placeIndex++;
  }

  public void addAction(BaseGameAction action)
  {
    synchronized (this.addActions)
    {
      this.addActions.add(action);
    }
  }

  public void addFirstAction(BaseGameAction action)
  {
    synchronized (this.addActions)
    {
      this.addActions.addFirst(action);
    }
  }

  private void addAction(ArrayList<BaseGameAction<BaseGame>> left)
  {
    synchronized (this.addActions)
    {
      this.addActions.addAll(left);
    }
  }

  public abstract void checkState(int paramInt);

  protected void dealCard(int count)
  {
	  for (Iterator iterator = allPlayers.values().iterator(); iterator.hasNext();)
		{
			GameUser player = (GameUser)iterator.next();
			for (int i = 0; i < count; i++)
				player.drawCard(getNextCard());

		}
  }

  protected byte getNextCard()
  {
    if (this.offset < this.CARD_COUNT)
    {
      if ((this.adminCard != null) && (this.adminCard.length > this.offset))
      {
        byte index = this.adminCard[(this.offset++)];
        LOGGER.error("----------offset: " + this.offset + "  Value: " + index + " ---------");
        return index;
      }

      byte index = this.defaultCard[(this.offset++)];

      return index;
    }

    return -1;
  }

  protected int getCardByIndex(int index)
  {
    if (index < this.CARD_COUNT)
    {
      if ((this.adminCard != null) && (this.adminCard.length > index))
      {
        return this.adminCard[index];
      }

      return this.defaultCard[index];
    }

    return -1;
  }

  protected byte[] getRemainCard(int count)
  {
    int total = this.CARD_COUNT - this.offset + count;
    byte[] remain = new byte[total];
    System.arraycopy(this.defaultCard, this.offset, remain, 0, this.CARD_COUNT - this.offset);
    return remain;
  }

  public abstract void init();

  public abstract void initStopListener(IEventListener paramIEventListener1, IEventListener paramIEventListener2);

  public abstract void removeStopListener(IEventListener paramIEventListener1, IEventListener paramIEventListener2);

  public void reset()
  {

  }
  public void reset(Map<Integer,GameUser> gamePlayers, Set<GameUser> watchers)
  {
    this.offset = 0;
    this.placeIndex = 0;
    this.addActions.clear();
    this.players.clear();
    this.watchers.clear();
    this.offlineWatchers.clear();
    this.watchers.addAll(watchers);
    this.players = gamePlayers;
    this.exceptionCount = 0;
    initReplayRecord();
    initPlayers();
    shuffleCard(this.defaultCard);
  }

  protected void initCard()
  {
    if (this.defaultCard == null)
    {
      this.defaultCard = new byte[this.CARD_COUNT];
    }
    for (byte i = 0; i < this.CARD_COUNT; i = (byte)(i + 1))
    {
      this.defaultCard[i] = ((byte)(i + 1));
    }

    shuffleCard(this.defaultCard);
  }

  protected abstract void initPlayers();

  protected abstract void initListener();

  protected abstract void removeListener();

  protected abstract void initReplayRecord();


  public abstract void reconnect(GameUser paramBasePlayer);

  public abstract void gameOver();

  public abstract void stop();

  public void pauseGame()
  {
    if (!isDissolutionLock())
    {
      long tick = System.currentTimeMillis();
      setDissolutionLock(true);
      setPauseTime(tick);
      this.stateReaminTime = ((int)Duration.between(LocalDateTime.now(), this.stateEndTime).getSeconds());
      for (BaseGameAction action : this.runActions)
      {
        action.setRemainTick(tick);
      }
    }
  }

  public void startPauseGame()
  {
    if (isDissolutionLock())
    {
      long tick = System.currentTimeMillis();
      for (BaseGameAction action : this.addActions)
      {
        action.setTick(tick + action.getRemainTick());
      }
      this.stateEndTime = LocalDateTime.now().plusSeconds(this.stateReaminTime);
      int count = (int)Duration.between(LocalDateTime.now(), this.stateEndTime).getSeconds();
      setCurState(this.curState, count);
      setDissolutionLock(false);
      setPauseTime(0L);
    }
  }


  public abstract byte[] getGameRecordMsg();

  public GameUser getPlayerByUserID(int userid)
  {

    return (GameUser)this.allPlayers.get(Integer.valueOf(userid));
  }



  public List<GameUser> getActivePlayers(GameUser player)
  {
    List list = new ArrayList();
    for (GameUser temp : this.allPlayers.values())
    {
      if ((temp.isActive()) && (temp != player))
        list.add(temp);
    }
    return list;
  }

  protected void shuffleCard(byte[] card)
  {
    int index = 0;
    byte temp = 0;
    int len = card.length - 1;
    for (int i = 0; i < len; i++)
    {
      index = random.next(len - i);
      temp = card[index];
      card[index] = card[(len - i)];
      card[(len - i)] = temp;
    }
    LOGGER.error("Shuffled card, gameID: " + this.id + ", default cards: " + Arrays.toString(this.defaultCard) + 
      ", admin cards: " + Arrays.toString(this.adminCard));
  }


  public abstract void playerAutoFight(GameUser paramIGamePlayer);

  public void addWatcher(GameUser player)
  {
    this.watchers.add(player);
  }

  public void removeWatcher(GameUser player)
  {
    this.watchers.remove(player);
  }

  public abstract void removePlayer(int paramInt);

  public void run()
  {
    try
    {
      long tick = System.currentTimeMillis();

      synchronized (this.addActions)
      {
        if (this.addActions.size() > 0)
        {
          ArrayDeque temp = this.runActions;
          this.runActions = this.addActions;
          this.addActions = temp;
        }
      }

      if (!isStoped())
      {
        if ((this.runActions != null) && (this.runActions.size() >= 1))
        {
          ArrayList left = new ArrayList();

          label334: for (BaseGameAction action : this.runActions)
          {
            try
            {
              if ((!this.dissolutionLock) || (action.isCheck()))
              {
                long start = System.currentTimeMillis();
                action.execute(this, tick);
                long end = System.currentTimeMillis();

                if (end - start > 100L)
                  LOGGER.error(String.format("Game Action time out - time: %d, action:%s ", new Object[] { 
                    Long.valueOf(end - start), action.getClass().getName() }));
                if (end - start <= 3000L) break label334;
                LOGGER.error(
                  String.format("Game Action time out over 3000ms - time: %d, action:%s ", new Object[] { 
                  Long.valueOf(end - start), action.getClass().getName() }));
              }
            }
            catch (Throwable ex)
            {
              this.exceptionCount += 1;
              if (this.exceptionCount > 300)
              {
                this.curState = getCurState();
                stop();
              }
            }
            finally
            {
              if (!action.isFinished(this, tick))
              {
                left.add(action);
              }
            }
          }
          if (left.size() > 0)
          {
            addAction(left);
          }
          this.runActions.clear();
        }
        else if (((this.runActions == null) || (this.runActions.size() == 0)) && (this.waitTime < tick))
        {
          checkState(0);
        }
      }
    }
    catch (Throwable e)
    {
      LOGGER.error("game update fail:", e);
    }
  }



  public long getPauseTime()
  {
    return this.pauseTime;
  }

  public void setPauseTime(long pauseTime)
  {
    this.pauseTime = pauseTime;
  }

  public abstract void dissloution();


  public abstract void kanPai();

  public abstract void xiaZhu();

  public abstract void compare();

  public abstract void qiPai();

  public abstract void qiangZhuang();

  public abstract void setContinue(boolean b);

    public abstract void biPai();

    public abstract PicklesGameEvent getPicklesGameEvent();

    public abstract void biPaiAll();

    public abstract GameUser getZhuang();
}