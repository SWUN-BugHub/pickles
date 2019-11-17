package com.stars.controller.protocol;

import com.stars.controller.entity.GameUser;
import com.stars.controller.entity.PicklesDesk;
import com.stars.controller.protocol.inf.*;
import com.stars.controller.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;

public abstract class BaseRoom
		implements IBaseRoom
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseRoom.class);
	protected static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	//protected static ThreadSafeRandom random = new ThreadSafeRandom();
	protected int MAX_PLAYER_COUNT;
	protected int roomID;
	protected boolean isUsing;
	protected BaseGame game;
	protected PlaceInfo[] places;
	protected PicklesDesk roomInfo;
	protected Set<GameUser> watchers;
	protected Map<Integer,GameUser> takesUser;
	protected int dealerID;
	protected long countDownTime = 0L;
	public IEventListener listener;
	public IEventListener exitListener;
	protected boolean startCountDown;
	protected Map<Integer, GameUser> mapPlayers;
//	protected DissolutionInfo dislutInfo;
	protected boolean isGameOver;
	private String[] processTime;

	public BaseRoom()
	{
		watchers=new HashSet<GameUser>();
		takesUser=new HashMap<Integer,GameUser>();
        this.isUsing = false;
        this.isGameOver = false;
        this.exitListener = new ExitListener();
        this.listener = new GameStopListener();
	}
	/**
	 * 获得观战玩家
	 */
	@Override
	public Set<GameUser> getWatchers() {
		return this.watchers;
	}
	public Map<Integer,GameUser> getTakesUser() {
		return this.takesUser;
	}
	public void addWatchers(GameUser gameUser) {
	   this.watchers.add(gameUser);
	}
	public void addTakesUser(GameUser gameUser) {
		this.takesUser.put(gameUser.getId(),gameUser);
	}

	public  GameUser getWatchersById(int userId)
	{
		Iterator<GameUser> iterator=watchers.iterator();
		while(iterator.hasNext())
		{
			GameUser user=iterator.next();
			if(user.getId()==userId)
			{
				return user;
			}
		}
		return null;
	}

	public void sendToAll(String method,Object[] message)
	{
		Iterator<GameUser> iterator=watchers.iterator();
		while(iterator.hasNext())
		{
			GameUser user=iterator.next();
			user.sendPacket( method,message);
		}
		Set<Integer> keySet=takesUser.keySet();
		for(Integer set:keySet)
		{
			takesUser.get(set).sendPacket(method,message);
		}

	}
	/**
	 * 获得game对象
	 * @return
	 */
	public BaseGame getGame()
	{
		return this.game;
	}

	/**
	 * 获得房间信息对象
	 * @return
	 */
	public PicklesDesk getRoomInfo()
	{
		return this.roomInfo;
	}

	public  void startGame()
	{
		if (this.roomInfo==null)//判断房间是否存在
		{
			return ;
		}
		if(canStartGame(takesUser.size()))
		{
			this.roomInfo.setStart(true);
			if (this.game == null)	//如果game等于空
			{
				game=new PicklesGame(this,takesUser,watchers);
                if (this.game != null)
                {
                    this.game.init();			//初始化游戏
                    this.game.initStopListener(this.listener, this.exitListener);//初始化停止监听器
                }
			}
			else
            {
                this.game.reset(takesUser, this.watchers);//游戏不等于null时，直接调用原本的游戏
            }
		}

	}
    /**
     * 获得地方
     * @param player
     * @return
     */
    public PlaceInfo getPlace(GameUser player)
    {
        if (player == null)
            return null;
        for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
        {
            if (this.places[i].getPlayer() == player)
                return this.places[i];
        }
        return null;
    }
    /**
     * 发送改变状态
     * @param placeInfo
     */
    private void sendChangeState(PlaceInfo placeInfo)
    {


    }
    /**
     * 获取准备玩家计数
     * @return
     */
    public int getPreparePlayerCount()
    {
        int count = 0;
        for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
        {
            GameUser player = this.places[i].getPlayer();
            if (player != null)
            {
                if (this.places[i].getState() == PlaceType.PREPARE)
                {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * 都准备好了吗
     * @return
     */
    private boolean iskAllPrepare()
    {
        int count = 0;
        for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
        {
            GameUser player = this.places[i].getPlayer();
            if (player != null)
            {
                if (this.places[i].getState() == PlaceType.UNPREPARE)
                {
                    return false;
                }
                count++;
            }
        }
        return count >= getAutoStartPlayerCount();
    }
    /**
     * 获得最低游戏玩家人数
     * @return
     */
    private int getAutoStartPlayerCount() {
        return 2;
    }
        /**
         * 获得游戏状态
         * @return
         */
    public boolean getGameState()
    {
        if (this.game == null)
            return false;
        if (!this.game.isStoped())
            return true;
        return false;
    }
    /**
     * 改变位置状态
     * @param value
     * @param placeInfo
     * @param check
     */
    public void changePlaceState(PlaceType value, PlaceInfo placeInfo, boolean check)
    {

        placeInfo.setState(value);
        sendChangeState(placeInfo);
        if (((value == PlaceType.PREPARE) && (check)) || (this.roomInfo.getAutoStart() == GameStartType.PREPARE.getValue()))
        {
            countDown(false);
        }
    }
    /**
     * 发送倒计时
     * @param time
     */
    private void sendCountDown(int time)
    {

    }
    /**
     * 准备倒计时
     */
    public int getPrepareCountDownTime()
    {
        return 10;
    }
    /**
     * 开始倒计时
     * @return
     */
    public boolean startGameCountDown()
    {
        if (this.startCountDown)
        {
            return true;
        }
        if (!iskAllPrepare())
        {
            return false;
        }
        long now = TimeUtil.getCurrentDate().getTime();
        this.startCountDown = true;


        this.countDownTime = (this.countDownTime == 0L ? (this.countDownTime = now) : this.countDownTime);
        sendCountDown(0);
        return true;
    }
    /**
     * 倒计时
     */
    public void countDown(boolean start)
    {

        if ((this.roomInfo.getAutoStart() == GameStartType.HAND_START.getValue()) ||
                (this.roomInfo.getAutoStart() == GameStartType.OWNER.getValue())) {
            return;
        }
        if ((!this.isUsing) || (getGameState()))
        {
            LOGGER.error("count_down: 1");
            return;
        }

        int prepareCount = getPreparePlayerCount();
        if ((!start) &&
                (this.roomInfo.getAutoStart() == 0 ? prepareCount < getAutoStartPlayerCount() : prepareCount < this.roomInfo.getAutoStart()))
        {
            LOGGER.error("count_down: 2");
            return;
        }

        if (iskAllPrepare())
        {
            this.countDownTime = 0L;
        }
        else if (!start)
        {
            LOGGER.error("place player not prepare");
            return;
        }

        if ((this.countDownTime != 0L) || (!start))
        {
            LOGGER.error("count_down: 3");
        }
        else if (!iskAllPrepare())
        {
            this.countDownTime = (TimeUtil.getCurrentDate().getTime() + getPrepareCountDownTime() * 1000);
        }

        if ((!startGameCountDown()) && (start))
        {
            sendCountDown(getPrepareCountDownTime());
        }
    }
    /**
     * 构造函数
     * @param
     */

	protected  boolean canStartGame(int size)
	{
		return size>=2;
	}


    /**
     * 退出侦听器
     * @author Administrator
     *
     */
    private class ExitListener
            implements IEventListener
    {
        /**
         * 构造函数
         */
        private ExitListener()
        {
        }
        /**
         * 事件
         */
        public void onEvent(EventArg arg)
        {
            PlaceInfo place = BaseRoom.this.getPlace((GameUser)arg.getSource());
            if (place != null)
            {
                BaseRoom.this.changePlaceState(PlaceType.UNPREPARE, place, false);
            }
        }
    }
    /**
     * 游戏停止侦听器
     * @author Administrator
     *
     */
    private class GameStopListener
            implements IEventListener
    {
        /**
         * 构造函数
         */
        private GameStopListener()
        {
        }
        /**
         * 事件
         */
        public void onEvent(EventArg arg)
        {

        }
    }

}

/**
   * 获取房间信息
   *//*

  public RoomMsgProto.RoomInfoPB.Builder getRoomInfoPB()
  {
    if (this.roomInfoPB == null)
    {
      this.roomInfoPB = getRoomInfoPB(this.roomInfo);
    }
    return this.roomInfoPB;
  }
  */
/**
   * 修改房间信息
   *//*

  public void updateRoomInfoPB()
  {
    this.roomInfoPB = getRoomInfoPB(this.roomInfo);
  }
  */

/**
   * 定时器组件
   * @return
   *//*

  protected ITimerComponent getTimer()
  {
    if (timer == null)
    {
      timer = (ITimerComponent)ComponentManager.getInstance().getComponent("TimerComponent");
    }
    return timer;
  }
  */
/**
   * 房间组件
   * @return
   *//*

  protected IRoomComponent getRoomC()
  {
    if (roomc == null)
    {
      roomc = (IRoomComponent)ComponentManager.getInstance().getComponent("RoomComponent");
    }
    return roomc;
  }
  */

/**
   * 地方玩家
   * @return
   *//*

  public Set<IGamePlayer> getPlacePlayers()
  {
    Set<IGamePlayer> placePlayers = new HashSet<IGamePlayer>();
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        placePlayers.add(player);
      }
    }
    return placePlayers;
  }
  */
/**
   * 获得ID
   * @return
   *//*

  public int getDealerID()
  {
    return this.dealerID;
  }
  */
/**
   * 设置id
   * @param dealerID
   *//*

  public void setDealerID(int dealerID)
  {
    this.dealerID = dealerID;
  }
  */


/**
   * 设置房间信息对象
   *//*
  public void setRoomInfo(RoomInfo roomInfo)
  {
    this.roomInfo = roomInfo;
    if (roomInfo != null)
    {
      this.MAX_PLAYER_COUNT = roomInfo.getPlayerCount();

      if (roomInfo.getRoomType() == RoomType.KUAI_SU_QIANG_ZHUANG.getValue())
      {
        this.processTime = roomInfo.getProcessTime().split(",");
        if ((this.processTime != null) && (this.processTime.length == 4))
        {
          for (int i = 0; i < this.processTime.length; i++)
          {
            if (!StringUtil.isNumeric(this.processTime[i]))
            {
              this.processTime[i] = "5";
            }
          }
        }
        else
        {
          this.processTime = new String[] { "5", "5", "5", "5" };
        }
      }
    }
  }
  */
/**
   * 获得是否使用
   * @return
   *//*

  public boolean getIsUsing()
  {
    return this.isUsing;
  }
  */
/**
   * 添加机器人
   *//*

  public abstract void addRobot();
*/
/**
 * 删除机器人
 *//*

  public abstract void removeRobot();
  */


/**
   * 初始化地方位置
   *//*

  private void inintPlaceSite()
  {
    this.places = new PlaceInfo[this.MAX_PLAYER_COUNT];
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      this.places[i] = new PlaceInfo(i);
      this.places[i].setState(PlaceType.NULL);
    }
  }
  */
/**
   * 获得房间id、
   * @return
   *//*

  public int getRoomID()
  {
    return this.roomInfo == null ? 0 : this.roomInfo.getRoomPassword();
  }
  */
/**
   * 房间能否停止
   * @return
   *//*

  protected boolean canRoomStop()
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        return false;
      }
    }
    if (this.watchers.size() > 0)
      return false;
    return true;
  }
  */
/**
   * 修改位置状态
   *//*

  public void updatePlaceState(IGamePlayer player)
  {
    PlaceInfo place = getPlace(player);
    if (place != null)
    {
      changePlaceState(place.getState(), place, false);
    }
  }
  */
/**
   * 改变位置状态
   * @param value
   * @param placeInfo
   *//*

  public void changePlaceState(PlaceType value, PlaceInfo placeInfo)
  {
    changePlaceState(value, placeInfo, true);
  }
  */
/**
   * 准备
   * @param value
   * @param placeInfo
   *//*

  public void aaaState(PlaceType value, PlaceInfo placeInfo)
  {*/
/*
	  if(value.getValue()==2)
	  {
		  //return;
	  }*//*

	  IGamePlayer player = placeInfo.getPlayer();
	  PlayerInfo playerInfo = player.getPlayerInfo();
	  //placeInfo.getPlayer().getPlayerInfo().setState(2);
	  RoomMsgProto.PlaceDataSC.Builder place = RoomMsgProto.PlaceDataSC.newBuilder();
	  place.setCoin(getPlayerCoin(player));
      place.setUserID(playerInfo.getId());
      place.setIndex(placeInfo.getPlace());
      place.setState(placeInfo.getState().getValue());
	  RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
	  builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_ChangeState);
	  builder.addPlaceData(place);
	    CommonMsgProto.CommonMsgPB.Builder message = CommonMsgProto.CommonMsgPB.newBuilder();
	    message.setCode(13333);
	    message.setBody(builder.build().toByteString());
	    sendToAll(message);
	    System.err.println(PrepareAll());
	    if(PrepareAll())
	    {	   
	    //	message.setCode(111);
	    //	sendToAll(message);;
	    }
  }
  */
/**
   * 改变位置状态
   * @param value
   * @param placeInfo
   * @param check
   *//*

  public void changePlaceState(PlaceType value, PlaceInfo placeInfo, boolean check)
  {
	 
    placeInfo.setState(value);
    sendChangeState(placeInfo);
    if (((value == PlaceType.PREPARE) && (check)) || (this.roomInfo.getAutoStart() == GameStartType.PREPARE.getValue()))
    {
      countDown(false);
    }
  }
  */
/**
   * 发送改变状态
   * @param placeInfo
   *//*

  private void sendChangeState(PlaceInfo placeInfo)
  {
    RoomMsgProto.PlaceDataSC.Builder place = RoomMsgProto.PlaceDataSC.newBuilder();
    place.setIndex(placeInfo.getPlace());
    place.setState(placeInfo.getState().getValue());

    IGamePlayer player = placeInfo.getPlayer();
    if (player != null)
    {
      PlayerInfo playerInfo = player.getPlayerInfo();
      place.setCoin(getPlayerCoin(player));
      place.setUserID(playerInfo.getId());
      place.setHead(playerInfo.getHead());
      place.setSex(playerInfo.getSex());
      place.setOnline(player.isOnline());
      place.setNickName(playerInfo.getNickName());
      place.setAutoPlay(player.isAutoFight());
      if (player.getLocation() != null)
      {
        place.setLocation(player.getLocation());
      }
      LOGGER.error("player, id: " + playerInfo.getId() + ", coin: " + getPlayerCoin(player));
    }

    RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
    builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_ChangeState);
    builder.addPlaceData(place);

    CommonMsgProto.CommonMsgPB.Builder message = CommonMsgProto.CommonMsgPB.newBuilder();
    message.setCode(16944);
    message.setBody(builder.build().toByteString());
    sendToAll(message);
  }
  */
/**
   * 获得房间coin
   *//*

  public float getPlayerCoin(IGamePlayer player)
  {
    float currCoin = 0.0F;
    if (this.roomInfo.getGameMode() == GameMode.CARD.getValue())
    {
      if (isClubCoinRoom())
      {
        currCoin = player.getCurPlayerClub().getCoin();
      }
      else
      {
        RoomCardCoinInfo info = getRoomC().getPlayerRoomCardInfo(this.roomInfo.getId(), 
          player.getPlayerInfo().getId());
        if (info != null)
          currCoin = info.getIntegral().floatValue();
      }
    }
    else
      currCoin = player.getPlayerInfo().getCoin().floatValue();
    return currCoin;
  }
  */
/**
   * 添加玩家
   * @param player
   * @param placeIndex
   * @param showSiteDown
   * @return
   *//*

  public boolean addPlayer(IGamePlayer player, int placeIndex, boolean showSiteDown)
  {
    if (getRoomC().getServerStatus() != 1)
    {
      RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ServerClose, player);
      return false;
    }

    player.getPlayerInfo().setRoomKey(this.roomInfo.getRoomPassword());
    int key = Integer.parseInt(getRedis().setNX("USERROOM" + player.getPlayerInfo().getId(), 
      String.valueOf(this.roomInfo.getRoomPassword())));
    if (key == this.roomInfo.getRoomPassword())
    {
      PlaceInfo info = getPlace(player);
      if (info == null)
      {
        this.watchers.add(player);
        if (this.game != null)
          this.game.addWatcher(player);
      }
      if (this.roomInfo.getClubId() > 0)
      {
        IClubModule cm = (IClubModule)player.getModule(ModuleType.CLUB);
        player.setCurPlayerClub(cm.getPlayerClubInfoByClubID(this.roomInfo.getClubId()));
      }
      player.setRoom(this);
      player.setGame(this.game);
      player.getPlayerInfo().setState(PlayerStatus.GAME.getValue());
      CommonMsgProto.CommonMsgPB.Builder message = getRoomDataMessage(RoomMsgProto.RoomCmdType.Cmd_EnterRoom, "EnterRoom.Success", player, 
        showSiteDown);

      player.sendPacket(message);
      */
/*
      //打印房间信息
      System.err.println(new StringBuffer()
    		  .append("Code-----").append(message.getCode())
    		  .append("\nBody-----").append(message.getBody())
    		  .append("\nDescriptor-----").append(message.getDescriptor())
    		  .append("\nDefaultInstanceForType-----").append(message.getDefaultInstanceForType())
    		  .append("\nDescriptorForType-----").append(message.getDescriptorForType())
    		  .append("\nInitializationErrorString-----").append(message.getInitializationErrorString())
    		  .append("\nAllFields-----").append(message.getAllFields())
    		  .append("\nUnknownFields-----").append(message.getUnknownFields())
    		  );
    		  *//*

      LOGGER.error("Enter room success, player: " + player.getPlayerInfo().getId() + ", room password: " + 
        this.roomInfo.getRoomPassword());
      return true;
    }

    LOGGER.error("Enter room fail, player: " + player.getPlayerInfo().getId() + ", room password: " + 
      this.roomInfo.getRoomPassword() + ", session key: " + key);
    return false;
  }
  */
/**
   * 获得房间日期信息
   * @param cmd
   * @param message
   * @param player
   * @param showSiteDown
   * @return
   *//*

  public CommonMsgProto.CommonMsgPB.Builder getRoomDataMessage(RoomMsgProto.RoomCmdType cmd, String message, IGamePlayer player, boolean showSiteDown)
  {
    RoomMsgProto.RoomMsgSC.Builder roomMsg = RoomMsgProto.RoomMsgSC.newBuilder();
    roomMsg.setCmdType(cmd);
    roomMsg.setGameStart(getGameState());
    roomMsg.setShowSite(showSiteDown);
    roomMsg.setShowStartButton(showStartButton(player.getPlayerInfo().getId()));
    int time = 0;
    if (this.countDownTime != 0L)
    {
      int temp = (int)((this.countDownTime - TimeUtil.getCurrentDate().getTime()) / 1000L);
      time = temp < 0 ? 0 : temp;
    }
    roomMsg.setCountDownTime(time);
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      PlaceInfo roomPlace = this.places[i];
      RoomMsgProto.PlaceDataSC.Builder placeData = getPlaceBuilder(roomPlace);
      roomMsg.addPlaceData(placeData);
    }

    RoomMsgProto.RoomInfoPB.Builder roomBuilder = getRoomInfoPB();
    roomMsg.setRoomInfo(roomBuilder);

    if (player != null)
    {
      RoomMsgProto.AutoFightPB autoFight = player.getAutoFight();
      if (autoFight != null) {
        roomMsg.setAutoFight(autoFight);
      }
      else {
        roomMsg.setAutoFight(initAutoFightParams(player));
      }
      if (this.roomInfo.getRoomType() == 8) {
        roomMsg.setJinHuaGenZhu(player.isJinHuaFollowBet());
      }
    }
	CommonMsgProto.CommonMsgPB.Builder commonMessage2 = CommonMsgProto.CommonMsgPB.newBuilder();
    commonMessage2.setCode(16666);
    sendToAll(commonMessage2);
    
    CommonMsgProto.CommonMsgPB.Builder commonMessage = CommonMsgProto.CommonMsgPB.newBuilder();
    commonMessage.setCode(16944);
    commonMessage.setBody(roomMsg.build().toByteString());
    
    return commonMessage;
  }
  */
/**
   * 获得游戏状态
   * @return
   *//*

  public boolean getGameState()
  {
    if (this.game == null)
      return false;
    if (!this.game.isStoped())
      return true;
    return false;
  }
  */
/**
   * 获得房间信息
   * @param roomInfo
   * @return
   *//*

  public RoomMsgProto.RoomInfoPB.Builder getRoomInfoPB(RoomInfo roomInfo)
  {
    RoomMsgProto.RoomInfoPB.Builder roomBuilder = RoomMsgProto.RoomInfoPB.newBuilder();
    roomBuilder.setGameType(roomInfo.getGameType());
    roomBuilder.setRoomType(roomInfo.getRoomType());
    roomBuilder.setPlayerCount(roomInfo.getPlayerCount());
    roomBuilder.setCurrentTurns(roomInfo.getCurrentTurns());
    roomBuilder.setCurrentPlayerCount(roomInfo.getCurPlayerCount());
    roomBuilder.setBaseCoin(roomInfo.getBaseCoin().floatValue());
    roomBuilder.setAutoStart(roomInfo.getGameStartType());
    roomBuilder.setTuiZhu(roomInfo.getTuiZhu());
    roomBuilder.setQiangZhuang(roomInfo.getQiangZhuang());
    roomBuilder.setFanBei(roomInfo.getFanBei());
    roomBuilder.setSpecialType(roomInfo.getSpecialType());
    roomBuilder.setAdvancedOption(roomInfo.getAdvancedOption());
    roomBuilder.setCreatorID(roomInfo.getCreatorId());
    roomBuilder.setBaseCoin(roomInfo.getBaseCoin().floatValue());
    roomBuilder.setId(roomInfo.getId());
    roomBuilder.setKey(roomInfo.getRoomPassword());
    roomBuilder.setRoomType(roomInfo.getRoomType());
    roomBuilder.setTotalGames(roomInfo.getTotalGames());
    roomBuilder.setGameType(roomInfo.getGameType());
    roomBuilder.setClubID(roomInfo.getClubId());
    roomBuilder.setHead(roomInfo.getHead());
    roomBuilder.setNickName(roomInfo.getNickName());
    roomBuilder.setIsSystem(roomInfo.getIsSystem());
    roomBuilder.setIsBegin(roomInfo.getIsBegin());
    roomBuilder.setCurrentGames(roomInfo.getCurrentGames());
    roomBuilder.setPayWay(roomInfo.getPayWay());
    roomBuilder.setGameMode(roomInfo.getGameMode());
    roomBuilder.setTemplateIndex(roomInfo.getTemplateIndex());
    roomBuilder.setLimitCoin(roomInfo.getLimitCoin());
    roomBuilder.setCardType(roomInfo.getCardType());
    roomBuilder.setMaxTurnTimes(roomInfo.getMaxTurnTimes());
    roomBuilder.setQiPaiTimes(roomInfo.getQiPaiTimes());
    roomBuilder.setTongHuaDeduction(roomInfo.getTongHuaDeduction());
    roomBuilder.setBaoZiDeduction(roomInfo.getBaoZiDeduction());
    roomBuilder.setMenPaiTimes(roomInfo.getMenPaiTimes());
    roomBuilder.setFirstPlayerRule(roomInfo.getFirstPlayerRule());
    roomBuilder.setBombBet(roomInfo.getBombBet());
    roomBuilder.setAccountMethod(roomInfo.getAccountMethod());
    roomBuilder.setKoupai(roomInfo.getKoupai());
    roomBuilder.setXiaZhuBet(roomInfo.getXiaZhuBet());
    roomBuilder.setKeChi(roomInfo.getKeChi());
    roomBuilder.setProcessTime(roomInfo.getProcessTime());
    roomBuilder.setGameAdvancedOption(roomInfo.getGameAdvancedOption());
    roomBuilder.setModel(roomInfo.getModel());
    if(roomInfo.getAdvanced_option_str()!=null)
    {
    	 roomBuilder.addAllAdvancedOption2(JSONObject.parseArray(roomInfo.getAdvanced_option_str(), Integer.class));
    }
    if(roomInfo.getSpecial_type_str()!=null)
    {
    	 roomBuilder.addAllSpecialType2(JSONObject.parseArray(roomInfo.getSpecial_type_str(), Integer.class));
    }
    return roomBuilder;
  }
  */
/**
   * 获得创建信息
   * @param roomPlace
   * @return
   *//*

  private RoomMsgProto.PlaceDataSC.Builder getPlaceBuilder(PlaceInfo roomPlace)
  {
    RoomMsgProto.PlaceDataSC.Builder placeData = RoomMsgProto.PlaceDataSC.newBuilder();
    placeData.setIndex(roomPlace.getPlace());
    placeData.setState(roomPlace.getState().getValue());
    IGamePlayer player = roomPlace.getPlayer();
    if (player != null)
    {
      placeData.setUserID(player.getPlayerInfo().getId());
      placeData.setNickName(player.getPlayerInfo().getNickName());
      placeData.setHead(player.getPlayerInfo().getHead());
      if(game instanceof JinHuaGame)
      {//
    	  JinHuaPlayer players=(JinHuaPlayer) ((JinHuaGame) game).getPlayer(player.getPlayerID());
    	  placeData.setCoin(getPlayerCoin(player)+ players.getTotalXiaZhu());
      }
      else
      {
    	  placeData.setCoin(getPlayerCoin(player));
      }
      
      placeData.setSex(player.getPlayerInfo().getSex());
      placeData.setOnline(player.isOnline());
      placeData.setAutoPlay(player.isAutoFight());
      if (player.getLocation() != null)
      {
        placeData.setLocation(player.getLocation());
      }
    }
    */
/*
    CommonMsgProto.CommonMsgPB.Builder commonMessage = CommonMsgProto.CommonMsgPB.newBuilder();
    Builder builder=PlayerModule.PBPlayer.newBuilder();
    builder.setCreatorId(13).setCurPlayerCount(15).setGameType(19).setTotalGames(1)
    .setRoomType(16).setRoomPassword(666666).setIsBegin(false).setIsExist(false).setPlayerCount(8);
    builder.addSpecialTypeList(15).addSpecialTypeList(12).addSpecialTypeList(13).addSpecialTypeList(14)
    .setPayWay(66).setPayDiamond(6).setId(666);
    commonMessage.setCode(12345);
    commonMessage.setBody(builder.build().toByteString());
    sendToAll(commonMessage);*//*

    return placeData;
  }
  */
/**
   * 发送
   * @param place
   *//*

  private void sendSiteDown(PlaceInfo place)
  {
    RoomMsgProto.RoomMsgSC.Builder roomMsg = RoomMsgProto.RoomMsgSC.newBuilder();
    roomMsg.setCmdType(RoomMsgProto.RoomCmdType.Cmd_SitDown);
    RoomMsgProto.PlaceDataSC.Builder placeData = getPlaceBuilder(place);
    if (placeData != null) {
      roomMsg.addPlaceData(placeData);
    }
    CommonMsgProto.CommonMsgPB.Builder commonMessage = CommonMsgProto.CommonMsgPB.newBuilder();
    commonMessage.setCode(16944);
    commonMessage.setBody(roomMsg.build().toByteString());
    sendToAll(commonMessage);
  }
  */
/**
   * 启动
   * @param info
   *//*

  public void start(RoomInfo info)
  {
    if (!this.isUsing)
    {
      reset();
      this.isUsing = true;
      setRoomInfo(info);
      setClubInfo();
      inintPlaceSite();
    }
  }
  */
/**
   * 停止
   *//*

  public void stop()
  {
    if (this.isUsing)
    {
      this.isUsing = false;
      if (this.game != null)
      {
        this.game.removeStopListener(this.listener, this.exitListener);
        this.game.allStop();
      }
      reset();
    }
  }
  */
/**
   * 重置
   *//*

  private void reset()
  {
    this.game = null;
    this.watchers.clear();
    this.dealerID = -1;
    this.roomInfoPB = null;
    if (this.roomInfo != null)
    {
      if (getTimer().isJobExist("RemoveRobot" + this.roomInfo.getId()))
      {
        getTimer().deleteJob("RemoveRobot" + this.roomInfo.getId());
      }
      if (getTimer().isJobExist("AddRobot" + this.roomInfo.getId()))
      {
        getTimer().deleteJob("AddRobot" + this.roomInfo.getId());
      }
      getRoomC().stopRoom(this.roomInfo.getRoomPassword());
      getRoomC().removeCountRoom(this);
      setRoomInfo(null);
    }
    this.countDownTime = 0L;
    this.startCountDown = false;
    this.isGameOver = false;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      this.places[i].setPlayer(null);
      this.places[i].setState(PlaceType.NULL);
    }
    if (this.dislutInfo != null)
    {
      this.dislutInfo.reset(null, null, false);
      this.dislutInfo = null;
    }
    if (this.clubInfo != null)
      this.clubInfo = null;
  }
  */
/**
   * 获得免费的地方
   * @return
   *//*

  protected PlaceInfo getFreePlace()
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      if (this.places[i].getState() == PlaceType.NULL)
        return this.places[i];
    }
    return null;
  }
  */
/**
   * 能添加玩家
   * @return
   *//*

  public abstract boolean canAddPlayer();
*/
/**
 * 能添加玩家
 * @param paramIGamePlayer
 * @return
 *//*

  public abstract boolean canAddPlayer(IGamePlayer paramIGamePlayer);
  */
/**
   * 获得地方
   * @param player
   * @return
   *//*

  public PlaceInfo getPlace(IGamePlayer player)
  {
    if (player == null)
      return null;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      if (this.places[i].getPlayer() == player)
        return this.places[i];
    }
    return null;
  }
*/
/**
 * 发送给所以玩家
 * @param commonMessage
 *//*

  public void sendToAll(CommonMsgProto.CommonMsgPB.Builder commonMessage)
  {
    sendToAllExcept(commonMessage, null);
	 //sendToAllExcept2(commonMessage, null);
  }
  */
/**
   * 发送给所以玩家除player以外
   * @param commonMessage
   * @param Player
   *//*

  public void sendToAllExcept(CommonMsgProto.CommonMsgPB.Builder commonMessage, IGamePlayer Player)
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      if ((this.places[i].getPlayer() != null) && 
        (this.places[i].getPlayer() != Player) && 
        (this.places[i].getPlayer().isOnline()))
        this.places[i].getPlayer().sendPacket(commonMessage);
    }
    for (IGamePlayer player : this.watchers)
    {
      if (player.isOnline())
        player.sendPacket(commonMessage);
    }
  }
  */
/**
   * 发送给所以玩家除player以外
   * @param commonMessage
   * @param Player
   *//*

  public void sendToAllExcept2(CommonMsgProto.CommonMsgPB.Builder commonMessage, IGamePlayer Player)
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      if ((this.places[i].getPlayer() != null) && 
        (this.places[i].getPlayer() != Player))
        this.places[i].getPlayer().sendPacket(commonMessage);
    }
    for (IGamePlayer player : this.watchers)
    {
      if (player.isOnline())
        player.sendPacket(commonMessage);
    }
  }
  */
/**
   * 变更地点状态
   *//*

  private void changePlaceStatePrepare()
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        if ((this.places[i].getState() == PlaceType.UNPREPARE) && 
          (getRoomC().getPlayerRoomCardInfo(this.roomInfo.getId(), player.getPlayerInfo().getId()) != null))
        {
          changePlaceState(PlaceType.PREPARE, this.places[i]);
        }
      }
    }
  }
  */
/**
   * 都准备好了吗
   * @return
   *//*

  private boolean iskAllPrepare()
  {
    int count = 0;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        if (this.places[i].getState() == PlaceType.UNPREPARE)
        {
          return false;
        }
        count++;
      }
    }
    return count >= getAutoStartPlayerCount();
  }
  
  private boolean PrepareAll() {
	  int list = 0;
	  for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
	    {
	      IGamePlayer player = this.places[i].getPlayer();
	      this.mapPlayers.put(Integer.valueOf(i), null);
	      if (player != null)
	      {
	        if (this.places[i].getState() != PlaceType.PREPARE)
	        {
	        	 list++;
	        }
	      }
	    }
	  return list>0?false:true;
  }
  */
/**
   * 获得准备好的玩家
   * @return
   *//*

  public List<IGamePlayer> getPreparePlayer()
  {
    List<IGamePlayer> list = new ArrayList<IGamePlayer>();
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      this.mapPlayers.put(Integer.valueOf(i), null);
      if (player != null)
      {
        if (this.places[i].getState() == PlaceType.PREPARE)
        {
          list.add(player);
          this.mapPlayers.put(Integer.valueOf(i), player);
        }
        else
        {
          standUp(player, false);
        }
      }
    }

    return list;
  }
  */
/**
   * 是否能启动游戏
   * @param prepareCount
   * @return
   *//*

  private boolean canStartGame(int prepareCount)
  {
    int autoStart = this.roomInfo.getAutoStart();
    if ((autoStart == 0) || (autoStart == -1) || (autoStart == -3))
    {
      return prepareCount >= getAutoStartPlayerCount();
    }
    if (autoStart == -2)
    {
      return iskAllPrepare();
    }
    if (autoStart > 0)
    {
      return prepareCount >= autoStart;
    }
    return false;
  }
*/
/**
 * 	开始游戏
 * @return
 *//*

  public boolean startGame()
  {
    if (!this.roomInfo.getIsExist())//判断房间是否存在
      {
    	return false;		//不存在返回false
      }
    List<IGamePlayer> players = getPreparePlayer();	//获得游戏准备的玩家
    this.countDownTime = 0L;
    if (!this.isUsing)	//判断房间是否正在使用
      {
    	return false;
      }
    this.startCountDown = false;		//开始倒计时
    if (canStartGame(players.size()))
    {
      this.roomInfo.setIsBegin(true);	//设置是否开始
      if (this.game == null)	//如果game等于空
      {
         switch (this.roomInfo.getRoomType())	//根据房间类型调用游戏
        {
        case 1:		//牛牛上庄											
        	//this.game = new WuHua(this, players, this.watchers);//TODO
        	this.game = new ShangZhuang(this, players, this.watchers);//TODO
          break;
        case 2:		//自由抢庄
          this.game = new QiangZhuang(this, players, this.watchers);
        	//this.game = new ShangZhuang(this, players, this.watchers);//TODO
          break;
        case 3:		//明牌抢庄
          this.game = new WuHua(this, players, this.watchers);
          break;
        case 4:		//三张弃牌
          this.game = new SanZhangQiPai(this, players, this.watchers);
          break;
        case 5:		//石头剪刀布
          this.game = new ShiTouJianDaoBu(this, players, this.watchers);
          break;
        case 6:		//轮流坐庄
          this.game = new LunZhuangGame(this, players, this.watchers, this.mapPlayers);
          break;
        case 7:		//五花
          this.game = new WuHua(this, players, this.watchers);//TODO
          break;
        case 8:		//德州扑克
        	//this.game=new DeZhouPuKeGame(this , players , this.watchers);
        //	
        	this.game=new LaDaCheGame(this , players , this.watchers);
          break;
        case 9:		//跑的快
          this.game = new PaoDeKuaiGame(this, players, this.watchers);
          break;
        case 10:	//设置总游戏，麻将
          this.roomInfo.setTotalGames(2147483647);
          this.game = new MahjongGame(this, players, this.watchers);
          break;
        case 11:	//推倒胡
          this.game = new TuiDaoHuGame(this, players, this.watchers);
          break;
        case 12:	//神速抢庄
          this.game = new KuaiSu(this, players, this.watchers);
          break;
        case 13:	// 珙县麻将
          this.game = new GongXianGame(this, players, this.watchers);
          break;
        case 14:	//邢台麻将
          this.game = new XingTaiGame(this, players, this.watchers);
          break;
        case 15:  //炸金花
        	this.game=new JinHuaGame(this, players, this.watchers);
          break;
        case 16:  //拉大车
        	this.game=new LaDaCheGame(this , players , this.watchers);
          break;
        case 17:  //大牌为庄
        	this.game=new DaPaiWeiZhuang(this , players , this.watchers);
          break;
        default:
          LOGGER.error("no such roomType: " + this.roomInfo.getRoomType());
        }

        if (this.game != null)
        {
          this.game.init();			//初始化游戏
          this.game.initStopListener(this.listener, this.exitListener);//初始化停止监听器
        }
      }
      else
      {
    	  this.game.reset(players, this.watchers);//游戏不等于null时，直接调用原本的游戏
      }
      IGamePlayer player;
      for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)//根据玩家数循环
      {
        player = this.places[i].getPlayer(); //获得第i个玩家
        if (player != null)
        {
          player.setGame(this.game);   //如果玩家不为null就将当前游戏设置为玩家的game
          changePlaceState(PlaceType.PLAYING, this.places[i]);	//改变位置状态
        }
      }
      for (IGamePlayer player1 : this.watchers)
      {
        player1.setGame(this.game);	//添加游戏对象
      }
      //创建gc对象
      IGameComponent gc = (IGameComponent)ComponentManager.getInstance().getComponent("GameComponent");
      gc.addGame(this.game);    //添加游戏对象

      if (this.roomInfo.getCurrentGames() == 0)	//判断当前房间的当前游戏是否等于0
      {
    	  //判断是否是牛牛房间或者当前房间的房间类型是否等于炸金花或者是否等于麻将
        if ((isNiuNiuRoom()) || (this.roomInfo.getRoomType() == RoomType.ZHA_JIN_HUA.getValue()) || 
          (this.roomInfo.getRoomType() == RoomType.MAHJONG.getValue()||this.roomInfo.getRoomType() ==17))//17为大庄为牛
        {
        	//如果是，设置最低启动为2
          this.roomInfo.setAutoStart(2);
        }
        //如果是跑得快
        else if (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue())
        {
        	//设置最低启动为3
          this.roomInfo.setAutoStart(3);
        }
        //如果是推倒胡或者形态
        else if ((this.roomInfo.getRoomType() == RoomType.TUI_DAO_HU.getValue()) || 
          (this.roomInfo.getRoomType() == RoomType.XING_TAI.getValue()))
        {
        	//设置最低启动为4
          this.roomInfo.setAutoStart(4);
        }
        //如果是贡献game
        else if (this.roomInfo.getRoomType() == RoomType.GONG_XIAN.getValue())
        {
        	//设置最低启动为3
          this.roomInfo.setAutoStart(3);
        }
        //设置是否开始为true
        this.roomInfo.setIsBegin(true);
        //修改房间信息
        updateRoomInfoPB();
        //发送修改房间信息
        sendUpdateRoomInfo();
      }

      if (this.roomInfo.getIsPrivate())//判断是否是私人房间
        {
    	  //设置过期时间为当前时间加1800000毫秒
    	  this.roomInfo.setExpireTime(new Date(System.currentTimeMillis() + 1800000L));
        }
      return true;	//返回true
    }
    return false;//返回false
  }
  */
/**
   * 获得最低游戏玩家人数
   * @return
   *//*

  private int getAutoStartPlayerCount()
  {
    if ((isNiuNiuRoom()) || (this.roomInfo.getRoomType() == RoomType.ZHA_JIN_HUA.getValue()))
    {
      return 2;	//牛牛和炸金花最少2人玩
    }
    if (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue())
    {
      return 3;	//跑得快   最少3人玩
    }
    if (this.roomInfo.getRoomType() == RoomType.MAHJONG.getValue())
    {
      return 2;	//麻将最少2人玩
    }
    if ((this.roomInfo.getRoomType() == RoomType.TUI_DAO_HU.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.XING_TAI.getValue()))
    {
      return 4;	//推倒胡形态最少4人
    }
    if (this.roomInfo.getRoomType() == RoomType.GONG_XIAN.getValue())
    {
      return 3;//攻心最少3人
    }
    return 0;
  }

  */
/**
   * 判断是否是牛牛房间
   *//*

  public boolean isNiuNiuRoom()
  {
    return (this.roomInfo.getRoomType() <= 7) || (this.roomInfo.getRoomType() == RoomType.KUAI_SU_QIANG_ZHUANG.getValue()||(this.roomInfo.getRoomType() <= 17) );
  }

  */
/**
   * 判断是否是麻将房间
   *//*

  public boolean isMahjongRoom()
  {
    return (this.roomInfo.getRoomType() == RoomType.MAHJONG.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.TUI_DAO_HU.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.GONG_XIAN.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.XING_TAI.getValue());
  }
  */
/**
   * 复位位置状态
   *//*

  private void resetPlaceState()
  {
    IGamePlayer player;
    int removeCount=0;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      player = this.places[i].getPlayer();
      if (player != null)
      {
        if (this.places[i].getState() == PlaceType.PLAYING)
        {
          if (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue())
          {
            player.cancleAutoFight();
          }
          if ((player.getAutoFight() != null) && (player.getAutoFight().getStatus()) && (player.isOnline()))
            changePlaceState(PlaceType.PREPARE, this.places[i], false);
          else
            changePlaceState(PlaceType.UNPREPARE, this.places[i], false);
        }
        if ((isClubCoinRoom()) && (!this.clubInfo.getRuleOptional()) && (getPlayerCoin(player) <= 0.0F))
        {
          roomc.addAction(new StandUpAction(this, player, false));
          removeCount++;
	          int type=this.getGame().getRoomInfo().getRoomType();
		        if(type==1||type==2||type==3||type==4||type==5||type==6||type==7||type==17)
		        {
		      	  BaseNiuniu niuniuGame=(BaseNiuniu) this.getGame();
		      	  if(niuniuGame.niuniuPlayers.size()-removeCount<=1)
		      	  {
		      		  IRoomComponent rc = (IRoomComponent)ComponentManager.getInstance().getComponent("RoomComponent");
		      		  rc.launchDislt(player);
		      		  //	  gameOver();
		      	  }
		        }
        }

        if (getRoomC().getServerStatus() != 1)
        {
          RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ServerClose, player);
        }
        player.setGame(null);
      }
    }

    for (IGamePlayer player1 : this.watchers)
    {
      if (getRoomC().getServerStatus() != 1)
      {
        RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ServerClose, player1);
      }
      player1.setGame(null);
    }
  }
  */
/**
   * 游戏结束
   *//*

  public void gameOver()
  {
    updateRoomInfoPB();
    boolean gameOver = false;
    if (this.roomInfo.getRoomType() == RoomType.MAHJONG.getValue())
    {
      if (((this.roomInfo.getCurrentTurns() == this.roomInfo.getMaxTurnTimes()) && 
        (this.roomInfo.getGameMode() == GameMode.CARD.getValue())) || 
        (!this.roomInfo.getIsExist()))
        gameOver = true;
    }
    else if (((this.roomInfo.getTotalGames() <= this.roomInfo.getCurrentGames()) && (
      (this.roomInfo.getRoomType() == 5) || (this.roomInfo.getGameMode() == GameMode.CARD.getValue()))) || 
      (!this.roomInfo.getIsExist())) {
      gameOver = true;
    }
    if (gameOver)
    {
      resetPlaceState();
      timeOut(true);
    }
    else
    {
      resetPlaceState();
      countDown(true);
    }
  }
  */
/**
   * 时间结束
   * @param immediately
   * @return
   *//*

  public synchronized boolean timeOut(boolean immediately)
  {
    if ((!immediately) && (getGameState()) && (this.roomInfo.getRoomType() != RoomType.SHITOUJIANDAOBU.getValue()))
    {
      if (this.roomInfo != null)
        RoomBussiness.updateRoomInfo(this.roomInfo);
      return false;
    }
    if (getGameState())
    {
      changeGameOverState();
      return false;
    }

    if (this.roomInfo != null)
    {
      this.roomInfo.setIsExist(false);
      RoomBussiness.updateRoomInfo(this.roomInfo);
      getRoomC().stopRoom(this.roomInfo.getRoomPassword());
    }

    RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
    if (this.roomInfo != null)
    {
      RoomMsgProto.RoomInfoPB.Builder roonInfo = RoomMsgProto.RoomInfoPB.newBuilder();
      roonInfo.setCreatorID(this.roomInfo.getCreatorId());
      builder.setRoomInfo(roonInfo);
    }
    builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_Fail);
    builder.setError(RoomMsgProto.ErrorType.Disslution);

    CommonMsgProto.CommonMsgPB.Builder msg = CommonMsgProto.CommonMsgPB.newBuilder();
    msg.setBody(builder.build().toByteString());
    msg.setCode(16944);

    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        player.sendPacket(msg);
        if (this.places[i].getState() == PlaceType.PLAYING)
        {
          changePlaceState(PlaceType.NULL, this.places[i], false);
        }
        removePlayer(player, 0);
        LOGGER.error("��ɢ���� player:" + player.getPlayerInfo().getId());
      }
    }
    IGamePlayer[] tempWatchers = (IGamePlayer[])this.watchers.toArray(new IGamePlayer[this.watchers.size()]);
    LOGGER.error("��ɢ���� Χ������:" + this.watchers.size());
    for (int i = 0; i < tempWatchers.length; i++)
    {
      IGamePlayer player = tempWatchers[i];
      player.sendPacket(msg);
      removePlayer(player, 0);
    }
    if (canRoomStop())
    {
      stop();
    }
    return true;
  }
  */
/**
   * 改变游戏结束状态
   *//*

  public void changeGameOverState()
  {
    if (isNiuNiuRoom())
    {
      this.game.addAction(new com.niuniu.action.ChangeStateAction(com.game.type.GameState.GameOver));
    }
    else if (this.roomInfo.getRoomType() == RoomType.ZHA_JIN_HUA.getValue())
    {
    //  this.game.addAction(new com.jinhua.action.ChangeStateAction(JinHuaGameState.GAME_OVER));	//TODO
    	 this.game.addAction(new com.dezhoupuke.action.ChangeStateAction(DeZhouPuKeGameState.GAME_OVER));
    }
    else if (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue())
    {
      this.game.addAction(new com.paodekuai.action.ChangeStateAction(com.paodekuai.type.GameState.GameOver));
    }
    else if (isMahjongRoom())
    {
      this.game.addAction(new com.mahjone.action.ChangeStateAction(com.mahjone.type.GameState.GameOver));
    }
  }

  */
/**
   * 坐下
   * @param player
   * @param placeIndex
   * @return
   *//*

  public boolean siteDown(IGamePlayer player, int placeIndex)
  {
	 CommonMsgProto.CommonMsgPB.Builder commonMessage2 = CommonMsgProto.CommonMsgPB.newBuilder();
     commonMessage2.setCode(16666);
     sendToAll(commonMessage2);
    if (getRoomC().getServerStatus() != 1)
    {
      RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ServerClose, player);
      return false;
    }
    if(getGame()!=null&&!getGame().getRoomInfo().getAdvancedOptionList().contains(101)&&getGame().getRoomInfo().getCurrentGames()>=1)
    {
    	return false;
    }
    if (placeIndex > this.MAX_PLAYER_COUNT)
      return false;
    if (getPlacePlyerCount() >= this.roomInfo.getPlayerCount())
    {
      RoomComponent.sendCommonMessage(this.roomInfo.getRoomPassword(), RoomMsgProto.ErrorType.EnterRoomNumberLimit, player);
      return false;
    }
    PlaceInfo place = getPlace(player);
    if (place != null)
    {
      return false;
    }
    place = null;
    if (placeIndex == -1)
    {
      place = getFreePlace();
    }
    else
    {
      place = this.places[placeIndex];
    }

    if ((place == null) || (place.getState() != PlaceType.NULL))
    {
      RoomComponent.sendCommonMessage(this.roomInfo.getRoomPassword(), RoomMsgProto.ErrorType.PlayerExist, player);
      return false;
    }

    this.roomInfo.setCurPlayerCount(this.roomInfo.getCurPlayerCount() + 1);
    this.watchers.remove(player);
    place.setPlayer(player);
    if (this.roomInfo.getAutoStart() == GameStartType.PREPARE.getValue())
    {
      place.setState(PlaceType.UNPREPARE);
    }
    else
    {
      place.setState(PlaceType.PREPARE);
    }
    if (this.dealerID == -1)
      this.dealerID = player.getPlayerInfo().getId();
    sendSiteDown(place);
    sendUpdaeClubRoomList();
    if (this.roomInfo.getAutoStart() != GameStartType.PREPARE.getValue())
    {
      countDown(false);
    }

    LOGGER.error("����:" + this.roomInfo.getRoomPassword() + " ���£�player:" + player.getPlayerInfo().getId());
    return true;
  }
  */
/**
   * 站起
   * @param player
   * @param checkCountDown
   *//*

  public void standUp(IGamePlayer player, boolean checkCountDown)
  {
    PlaceInfo place = getPlace(player);
    if (place != null)
    {
      this.roomInfo.setCurPlayerCount(this.roomInfo.getCurPlayerCount() - 1);
      changePlaceState(PlaceType.NULL, place);
      place.setPlayer(null);
    }
    player.setRoom(this);
    player.setAutoFight(null);
    player.setJinHuaFollowBet(false);
    this.watchers.add(player);
    if (this.game != null)
    {
      this.game.removePlayer(player.getPlayerInfo().getId());
    }

    LOGGER.error("房间号: " + this.roomInfo.getRoomPassword() + ", player: " + player.getPlayerInfo().getId());
  }

  */
/**
   * 玩家退出房间，删除玩家
   *//*

  public synchronized boolean removePlayer(IGamePlayer player, int exitType)
  {
	 CommonMsgProto.CommonMsgPB.Builder commonMessage2 = CommonMsgProto.CommonMsgPB.newBuilder();
     commonMessage2.setCode(16666);
     sendToAll(commonMessage2);
    PlaceInfo place = getPlace(player);
    if (place != null)
    {
      if (place.getState() == PlaceType.PLAYING)
        return false;
      changePlaceState(PlaceType.NULL, place);
      place.setPlayer(null);
    }

    int roomKey = 0;
    if (this.roomInfo != null)
    {
      roomKey = this.roomInfo.getRoomPassword();
      if (place != null)
      {
        this.roomInfo.setCurPlayerCount(this.roomInfo.getCurPlayerCount() - 1);
        sendUpdaeClubRoomList();
      }

    }

    if (player.getPlayerInfo().getId() == this.dealerID)
      this.dealerID = -1;
    this.watchers.remove(player);
    if (this.game != null)
    {
      this.game.removeWatcher(player);
      this.game.removePlayer(player.getPlayerInfo().getId());
    }
    player.setRoom(null);
    player.setGame(null);
    player.setJinHuaFollowBet(false);
    player.setAutoFight(null);
    player.setCurPlayerClub(null);
    player.setLastChatTime(null);
    player.setLastLanuchDisTime(null);
    player.setLocation(null);
    player.setPdkChangeCardIndex(-1);
    player.getPlayerInfo().setState(PlayerStatus.HALL.getValue());
    player.getPlayerInfo().setRoomKey(0);
    getRedis().del("USERROOM" + player.getPlayerInfo().getId());

    if (canRoomStop())
    {
      stop();
    }

    sendExitRoom(roomKey, player, exitType);

    if (this.roomInfo != null)
      LOGGER.error("����:" + this.roomInfo.getRoomPassword() + " �뿪, player:" + player.getPlayerInfo().getId());
    else {
      LOGGER.error("�����뿪��player:" + player.getPlayerInfo().getId());
    }
    return true;
  }
  */
/**
   * 解散房间
   *//*

  public void clearWatcher()
  {
    for (IGamePlayer player : this.watchers)
      removePlayer(player, 0);
  }
  */
/**
   * 退出房间
   * @param roomKey
   * @param player
   * @param exitType
   *//*

  public void sendExitRoom(int roomKey, IGamePlayer player, int exitType)
  {
    RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
    builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_ExitRoom);
    builder.setRoomKey(roomKey);
    builder.setExitType(exitType);

    CommonMsgProto.CommonMsgPB.Builder message = CommonMsgProto.CommonMsgPB.newBuilder();
    message.setCode(16944);
    message.setBody(builder.build().toByteString());
    player.sendPacket(message);
  }
*/
/**
 * 清空玩家
 *//*

  public void clearPlayer()
  {
    IGamePlayer player;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      player = this.places[i].getPlayer();
      if (player != null)
      {
        getRedis().del("USERROOM" + player.getPlayerInfo().getId());
      }
    }
    for (IGamePlayer player1 : this.watchers)
    {
      getRedis().del("USERROOM" + player1.getPlayerInfo().getId());
    }
  }

  public int getSiteDownPlaces()
  {
    int i = 0;
    for (int k = 0; k < this.MAX_PLAYER_COUNT; k++)
    {
      if (this.places[k].getState() != PlaceType.NULL)
      {
        i++;
      }
    }
    return i;
  }
  */
/**
   * 检查倒计时
   * @param now
   * @return
   *//*

  public boolean checkCountDown(long now)
  {
    if ((now >= this.countDownTime) || (!this.isUsing))
    {
      if (this.roomInfo != null)
      {
        LOGGER.error("---checkCountDown success!!! id: " + this.roomInfo.getRoomPassword());
        changePlaceStatePrepare();
        startGame();
      }
      return true;
    }
    return false;
  }
  */
/**
   * 获取准备玩家计数
   * @return
   *//*

  public int getPreparePlayerCount()
  {
    int count = 0;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        if (this.places[i].getState() == PlaceType.PREPARE)
        {
          count++;
        }
      }
    }
    return count;
  }
  */
/**
   * 倒计时
   *//*

  public void countDown(boolean start)
  {
    if (getRoomC().getServerStatus() != 1) {
      return;
    }
    if ((this.roomInfo.getAutoStart() == GameStartType.HAND_START.getValue()) || 
      (this.roomInfo.getAutoStart() == GameStartType.OWNER.getValue())) {
      return;
    }
    if ((!this.isUsing) || (getGameState()))
    {
      LOGGER.error("count_down: 1");
      return;
    }

    int prepareCount = getPreparePlayerCount();
    if ((!start) && 
      (this.roomInfo.getAutoStart() == 0 ? prepareCount < getAutoStartPlayerCount() : prepareCount < this.roomInfo.getAutoStart()))
    {
      LOGGER.error("count_down: 2");
      return;
    }

    if (iskAllPrepare())
    {
      this.countDownTime = 0L;
    }
    else if (!start)
    {
      LOGGER.error("place player not prepare");
      return;
    }

    if ((this.countDownTime != 0L) || (!start))
    {
      LOGGER.error("count_down: 3");
    }
    else if (!iskAllPrepare())
    {
      this.countDownTime = (TimeUtil.getCurrentDate().getTime() + getPrepareCountDownTime() * 1000);
    }

    if ((!startGameCountDown()) && (start))
    {
      getRoomC().addCountRoom(this);
      LOGGER.error("׼��2s����ʱ��" + this.roomInfo.getRoomPassword());
      sendCountDown(getPrepareCountDownTime());
    }
  }
  */
/**
   * 准备倒计时
   *//*

  public int getPrepareCountDownTime()
  {
    int result = 2;
    if ((this.roomInfo.getRoomType() == RoomType.MAHJONG.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.TUI_DAO_HU.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.XING_TAI.getValue()))
    {
      result = 10;
    }
    else if (this.roomInfo.getRoomType() == RoomType.KUAI_SU_QIANG_ZHUANG.getValue())
    {
      result = Integer.valueOf(this.processTime[0]).intValue();
    }
    else if (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue())
    {
      result = 5;
    }
    else if (this.roomInfo.getRoomType() == RoomType.GONG_XIAN.getValue())
    {
      return 30;
    }
    return result;
  }
  */
/**
   * 准备所有玩家计数
   * @return
   *//*

  private int getPrepareAllPlayerCount()
  {
    int count = 0;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        if (this.places[i].getState() == PlaceType.PREPARE)
        {
          count++;
        }
      }
    }
    return count;
  }
  */
/**
   * 开始倒计时
   * @return
   *//*

  public boolean startGameCountDown()
  {
    if (this.startCountDown)
    {
      return true;
    }
    if (!iskAllPrepare())
    {
      return false;
    }
    long now = TimeUtil.getCurrentDate().getTime();
    this.startCountDown = true;
    getRoomC().addCountRoom(this);

    this.countDownTime = (this.countDownTime == 0L ? (this.countDownTime = now) : this.countDownTime);
    sendCountDown(0);
    return true;
  }
  */
/**
   * 发送倒计时
   * @param time
   *//*

  private void sendCountDown(int time)
  {
    RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
    builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_CountDown);
    builder.setCountDownTime(time);

    CommonMsgProto.CommonMsgPB.Builder msg = CommonMsgProto.CommonMsgPB.newBuilder();
    msg.setCode(16944);
    msg.setBody(builder.build().toByteString());
    sendToAll(msg);
  }
  */
/**
   * 让玩家进入房间
   * @param userID
   * @return
   *//*

  public boolean getPlayerInRoom(int userID)
  {
    for (IGamePlayer player : this.watchers)
    {
      if (player.getPlayerInfo().getId() == userID) {
        return true;
      }
    }
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if ((player != null) && (player.getPlayerInfo().getId() == userID))
      {
        return true;
      }
    }
    if (getRoomC().getPlayerRoomCardInfo(this.roomInfo.getId(), userID) != null)
      return true;
    
    
    return false;
  }
  */
/**
   * 让玩家进入
   * @param userID
   * @return
   *//*

  public boolean getPlayerInSite(int userID)
  {
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if ((player != null) && (player.getPlayerInfo().getId() == userID))
      {
        return true;
      }
    }
    return false;
  }
  */
/**
   * 关闭房间
   * @param userID
   * @return
   *//*

  public boolean showSiteDown(int userID)
  {
    if (getPlayerInSite(userID))
      return false;
    return true;
  }
  */
/**
   * 显示启动按钮
   * @param userID
   * @return
   *//*

  public boolean showStartButton(int userID)
  {
    if (!this.roomInfo.getIsBegin())
    {
      if (this.roomInfo.getAutoStart() == GameStartType.OWNER.getValue())
      {
        if ((this.roomInfo.getClubId() > 0) && (this.roomInfo.getTemplateIndex() > 0))
        {
          return isClubAdminOrCreater(userID);
        }

        return this.roomInfo.getCreatorId() == userID;
      }

      if (this.roomInfo.getAutoStart() == GameStartType.HAND_START.getValue())
      {
        return true;
      }
    }
    return false;
  }
  //判断是否是俱乐部创建人或者管理员
  private boolean isClubAdminOrCreater(int userID)
  {
    IDataClubComponent dcc = (IDataClubComponent)ComponentManager.getInstance().getComponent(
      " ClubComponent");
    PlayerClubInfo pci = dcc.getPlayerClubInfoByClubID(userID, this.roomInfo.getClubId());
    if ((pci != null) && (pci.getDuty() > 0))
    {
      return true;
    }
    return false;
  }
  //获得房间玩家总人数
  public int getRoomPlyerCount()
  {
    int count = this.watchers.size();
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        count++;
      }
    }
    return count;
  }
  //获得玩家总人数
  public int getPlacePlyerCount()
  {
    int count = 0;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        count++;
      }
    }
    return count;
  }
*/
/**
 * 发送修改房间信息
 *//*

  public void sendUpdateRoomInfo()
  {
    CommonMsgProto.CommonMsgPB.Builder commonMsg = CommonMsgProto.CommonMsgPB.newBuilder();
    commonMsg.setCode(16944);

    RoomMsgProto.RoomMsgSC.Builder roomMsg = RoomMsgProto.RoomMsgSC.newBuilder();
    roomMsg.setCmdType(RoomMsgProto.RoomCmdType.Cmd_UpdateRoomInfo);
    RoomMsgProto.RoomInfoPB.Builder roomBuilder = getRoomInfoPB();
    roomMsg.setRoomInfo(roomBuilder);
    commonMsg.setBody(roomMsg.build().toByteString());
    sendToAll(commonMsg);
  }
  */
/**
   * 获得密码
   *//*

  public int getPassword()
  {
    return this.roomInfo.getRoomPassword();
  }
  */
/**
   * 发送错误信息
   * @param player
   * @param type
   *//*

  protected void sendErrorMessage(IGamePlayer player, RoomMsgProto.ErrorType type)
  {
    RoomMsgProto.RoomMsgSC.Builder msg = RoomMsgProto.RoomMsgSC.newBuilder();
    msg.setCmdType(RoomMsgProto.RoomCmdType.Cmd_Fail);
    msg.setError(type);

    CommonMsgProto.CommonMsgPB.Builder builder = CommonMsgProto.CommonMsgPB.newBuilder();
    builder.setCode(16944);
    builder.setBody(msg.build().toByteString());
    player.sendPacket(builder);
  }
  */
/**
   * 获得游戏记录
   * @param player
   *//*

  public void getGameRecord(IGamePlayer player)
  {
	  if(roomInfo.getRoomType()==8)
	  {
		  byte[] bytes= game.getGameRecordMsg();
		  player.sendPacket(bytes);
	  }
	  else  if(roomInfo.getRoomType()==15)
	  {
		  byte[] bytes= game.getGameRecordMsg();
		  player.sendPacket(bytes);
	  }
	  else
	  {
		  game.setPauseTime(player.getPlayerID());
	  }
		  
		 
	  */
/*
    List<byte[]> temps = getRedis().lrang(SafeEncoder.encode("CADR_ROOM_RECORD" + this.roomInfo.getId()), 0, 
      -1);

    CommonMsgProto.CommonMsgPB.Builder commonMsg = CommonMsgProto.CommonMsgPB.newBuilder();
    commonMsg.setCode(16944);

    RoomMsgProto.RoomMsgSC.Builder roomMsg = RoomMsgProto.RoomMsgSC.newBuilder();
    roomMsg.setCmdType(RoomMsgProto.RoomCmdType.Cmd_GetGameRecord);
    if (temps != null)
    {
      try
      {
        if ((this.roomInfo.getRoomType() == RoomType.ZHA_JIN_HUA.getValue()) || 
          (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue()))
        {
          for (byte[] i : temps)
          {
            roomMsg.addGameRecord(RoomMsgProto.CardGameRecordPB.parseFrom(i));
          }

        }
        else
        {
          int size = temps.size();
          if (size > 0)
          {
            roomMsg.addGameRecord(RoomMsgProto.CardGameRecordPB.parseFrom((byte[])temps.get(0)));
          }
        }

      }
      catch (InvalidProtocolBufferException e)
      {
        e.printStackTrace();
      }
    }
    commonMsg.setBody(roomMsg.build().toByteString());
    player.sendPacket(commonMsg);*//*

  }
  */
/**
   * 关闭俱乐部房间
   *//*

  public void clearClubCloseRoomPlayer()
  {
    for (IGamePlayer player : this.watchers)
    {
      if (removePlayer(player, 0)) {
        RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ClubClose, player);
      }
    }
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        if (removePlayer(player, 0))
          RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.ClubClose, player);
      }
    }
  }
  */
/**
   * 发送Reslt给所有人
   *//*

  public abstract void sendResltToAll();
  
  */
/**
   * 得到俱乐部的信息
   *//*

  public ClubInfo getClubInfo()
  {
    return this.clubInfo;
  }
  */
/**
   * 设置俱乐部的信息
   *//*

  public void setClubInfo()
  {
    if ((this.roomInfo != null) && (isClubCoinRoom()))
    {
      IDataClubComponent dcc = (IDataClubComponent)ComponentManager.getInstance().getComponent(
        " ClubComponent");
      this.clubInfo = dcc.getClubInfoByClubID(this.roomInfo.getClubId());
    }
  }
  */
/**
   * 是否是俱乐部基金支付房间
   *//*

  public boolean isClubCoinRoom()
  {
    if ((this.roomInfo.getClubId() > 0) && (this.roomInfo.getLimitCoin() > 0))
      return true;
    return false;
  }
  */
/**
   * 获得观察者列表
   * @param player
   *//*

  public void getWatcherList(IGamePlayer player)
  {
    RoomMsgProto.RoomMsgSC.Builder builder = RoomMsgProto.RoomMsgSC.newBuilder();
    builder.setCmdType(RoomMsgProto.RoomCmdType.Cmd_GetWatcherList);
    RoomMsgProto.WatcherListPB.Builder watcherListPB = null;
    for (IGamePlayer temp : this.watchers)
    {
      watcherListPB = RoomMsgProto.WatcherListPB.newBuilder();
      watcherListPB.setUserID(temp.getPlayerInfo().getId());
      watcherListPB.setNickName(temp.getPlayerInfo().getNickName());
      watcherListPB.setHead(temp.getPlayerInfo().getHead());
      builder.addWatcherList(watcherListPB);
    }
    sendMessage(player, builder);
  }
  
  
  */
/**
   * 发送信息
   * @param player
   * @param builder
   *//*

  public void sendMessage(IGamePlayer player, RoomMsgProto.RoomMsgSC.Builder builder)
  {
    CommonMsgProto.CommonMsgPB.Builder message = CommonMsgProto.CommonMsgPB.newBuilder();
    message.setCode(16944);
    message.setBody(builder.build().toByteString());
    player.sendPacket(message);
  }
*/
/**
 * 得到地方列表
 * @return
 *//*

  public List<RoomMsgProto.WatcherListPB> getPlaceList()
  {
    List<WatcherListPB> wi = new ArrayList<WatcherListPB>();
    RoomMsgProto.WatcherListPB.Builder watcherListPB = null;
    for (int i = 0; i < this.MAX_PLAYER_COUNT; i++)
    {
      IGamePlayer player = this.places[i].getPlayer();
      if (player != null)
      {
        watcherListPB = RoomMsgProto.WatcherListPB.newBuilder();
        watcherListPB.setUserID(player.getPlayerInfo().getId());
        watcherListPB.setNickName(player.getPlayerInfo().getNickName());
        watcherListPB.setHead(player.getPlayerInfo().getHead());
        wi.add(watcherListPB.build());
      }
    }
    return wi;
  }
  */
/**
   * 手动启动
   * @param player
   * @return
   *//*

  public boolean handStart(GameUser player)
  {
    if ((!getGameState()) && (getPrepareAllPlayerCount() >= getAutoStartPlayerCount()))
    {
      if (getRoomInfo().getAutoStart() == GameStartType.HAND_START.getValue())
      {
        sendCountDown(0);
        startGame();
        return true;
      }
      if (this.roomInfo.getAutoStart() == GameStartType.OWNER.getValue())
      {
        if ((this.roomInfo.getClubId() > 0) && (this.roomInfo.getTemplateIndex() > 0) && 
          (isClubAdminOrCreater(player.getPlayerInfo().getId())))
        {
          sendCountDown(0);
          startGame();
          return true;
        }
        if (player.getPlayerInfo().getId() == this.roomInfo.getCreatorId())
        {
          sendCountDown(0);
          startGame();
          return true;
        }
      }
    }
    else if (getPrepareAllPlayerCount() < getAutoStartPlayerCount())
    {
      RoomComponent.sendCommonMessage(0, RoomMsgProto.ErrorType.RoomNumberNotEnough, player);
    }
    return false;
  }

  */
/**
   * 玩回程游戏
   * @return
   *//*

  public boolean getPlayingReturnRoom()
  {
    if ((this.roomInfo.getRoomType() == RoomType.ZHA_JIN_HUA.getValue()) || 
      (this.roomInfo.getRoomType() == RoomType.PAO_DE_KUAI.getValue()))
    {
      return true;
    }
    return false;
  }
  */
/**
   * 游戏是否结束
   * @return
   *//*

  public boolean isGameOver()
  {
    return this.isGameOver;
  }
  */
/**
   * 设置游戏结束
   * @param isGameOver
   *//*

  public void setGameOver(boolean isGameOver)
  {
    this.isGameOver = isGameOver;
  }
  */
/**
   * 得到过程时间
   *//*

  public String[] getProcessTime()
  {
    return this.processTime;
  }
  */
/**
   * 得到下注赌注
   *//*

  public List<Integer> getXiaZhuBets()
  {
    switch (this.roomInfo.getXiaZhuBet())
    {
    case 1:
      return GlobalVariable.XIA_ZHU_BET_RULE_1;
    case 2:
      return GlobalVariable.XIA_ZHU_BET_RULE_2;
    case 3:
      return GlobalVariable.XIA_ZHU_BET_RULE_3;
    }
    return GlobalVariable.XIA_ZHU_BET_RULE_DEFAULT;
  }
  */
/**
   * 初始化自动押注赌注
   * @param player
   * @return
   *//*

  public RoomMsgProto.AutoFightPB.Builder initAutoFightParams(IGamePlayer player)
  {
    RoomMsgProto.AutoFightPB.Builder builder = RoomMsgProto.AutoFightPB.newBuilder();
    builder.setStatus(false);
    int xiaZhuBet = 1;
    if (getRoomInfo().getRoomType() == RoomType.KUAI_SU_QIANG_ZHUANG.getValue())
    {
      xiaZhuBet = ((Integer)getXiaZhuBets().get(0)).intValue();
    }
    builder.setQingZhuangCodition(-1);
    builder.setQiangZhuangMultiple(1);
    builder.setXiaZhuCodition(0);
    builder.setXiaZhuMultiple(xiaZhuBet);
    builder.setTuiZhuCodidition(-1);

    return builder;
  }
  */
/**
   * 添加当前游戏时间
   *//*

  public void addCurrentGameTimes()
  {
    this.roomInfo.setCurrentGames(this.roomInfo.getCurrentGames() + 1);
    LOGGER.info("add current game times, roomID: " + this.roomInfo.getId() + ", current times: " + 
      this.roomInfo.getCurrentGames());
  }
  */
/**
   * 退出侦听器
   * @author Administrator
   *
   *//*

  private class ExitListener
    implements IEventListener
  {	
	  */
/**
	   * 构造函数
	   *//*

    private ExitListener()
    {
    }
    */
/**
     * 事件
     *//*

    public void onEvent(EventArg arg)
    {
      PlaceInfo place = BaseRoom.this.getPlace((IGamePlayer)arg.getSource());
      if (place != null)
      {
        BaseRoom.this.changePlaceState(PlaceType.UNPREPARE, place, false);
      }
    }
  }
  */
/**
   * 游戏停止侦听器
   * @author Administrator
   *
   *//*

  private class GameStopListener
    implements IEventListener
  {
	  */
/**
	   * 构造函数
	   *//*

    private GameStopListener()
    {
    }
    */
/**
     * 事件
     *//*

    public void onEvent(EventArg arg)
    {
      BaseRoom.this.getRoomC().addAction(new GameStopAction(BaseRoom.this));
    }
  }
}*/
