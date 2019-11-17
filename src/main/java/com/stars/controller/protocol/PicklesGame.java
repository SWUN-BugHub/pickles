package com.stars.controller.protocol;

import com.stars.controller.entity.GameUser;
import com.stars.controller.entity.PicklesDesk;
import com.stars.controller.entity.UserCard;
import com.stars.controller.protocol.inf.*;
import com.stars.controller.type.BaseCardType;
import com.stars.controller.util.CommandUtil;
import com.stars.controller.util.SuspendUtil;

import java.util.*;

public class PicklesGame extends BaseGame {
    private  long QIANG_ZHUANG_TIME;
    private IEventListener xiaZhuListener;
    private IEventListener compareListener;
    private GameUser zhuang;
    private long LAO_PAI_TIME;
    private PicklesGameEvent picklesGameEvent;
    private List<GameUser> laDaChePlayers;
    private List<BaseCardType> cardTypes;
    private GameUser turnPlayer;
    private GameUser winnerPlayer;
    private GameUser sourceComparePlayer;
    private GameUser targetComparePlayer;
    private GameUser changePlayer;

    public int getCurrOffice() {
        return currOffice;
    }

    public void setCurrOffice(int currOffice) {
        this.currOffice = currOffice;
    }

    private int currOffice;
    private int menPaiTimes;
    protected int totalIn;
    private  Thread t;
    public List<Integer> countCards;			//总牌
    public int THIS_CARD;							//总牌已经少了多少张牌
    private int XIA_ZHU_TIME;					//下注时间
    private int XIA_ZHU_DELAY;
    public static final int LIANG_PAI_TIME = 2000;	//亮牌时间
    public static final int LIANG_PAI_DEAL = 2;
    public static final int MXA_XIA_BET = 5;
    private int totalCoin;										//下注金币
    private int maxXiaZhuBet;								//最大下注金币
    private int maxTurnTimes;								//最大处理时间
    private int turnTime;
    private int turnFirstIndex;
    private boolean isChangePlayer;
    private int firstUserID;
    private SuspendUtil suspendUtil;
    public PicklesGame(IBaseRoom room, Map<Integer,GameUser> players, Set<GameUser> watchers) {
        super(room, players, watchers);
        this.xiaZhuListener = new XiaZhuListener();
        this.compareListener = new CompareListener();
        this.cardTypes = new ArrayList<BaseCardType>();
        initCardType();
    }
    @Override
    public void reset()
    {
        System.err.println("第"+(getCurrOffice()-1)+"局结束");
        if(getCurrOffice()>room.getRoomInfo().getTotalGame())
        {
            System.err.println("游戏局数已达到房间最大局数，游戏停止");
            System.err.println("发送总游戏结果");
            Set<Integer> keySet=new HashSet<Integer>(players.keySet());
            for(Integer key:keySet) {
                room.getWatchers().add(players.get(key));
                players.remove(key);
            }
            room.getRoomInfo().setStart(false);
            room.setGame(null);
            return;
        }
        System.err.println("开始第"+getCurrOffice()+"局");
        this.CARD_COUNT = 52;
        this.placeIndex = 0;
        this.turnFirstIndex = 0;
        this.turnTime = 1;
        this.isChangePlayer = false;
        this.totalCoin = 0;
        this.firstUserID = 0;
        this.winnerPlayer = null;
        this.targetComparePlayer = null;
        this.sourceComparePlayer = null;
        this.THIS_CARD=0;
        this.countCards=new ArrayList();
        //setCurrOffice(getCurrOffice()+1);
        initCards();

        initRoomSetting();
//        initReplayRecord(); //游戏记录
//        initPlayers();      //重新创建游戏对象
//        initPukePlayer();   //重新赋值游戏对象  这几步可以不用
        initListener();
        //initCard();
        setCurState(PicklesGameState.INITED);
        sendQiangZhuang();
    }
    @Override
    public void init() {
        Map<String,Boolean> map=new HashMap<String,Boolean>();
        map.put("success",true);
        sendToAll("gameStart",new Object[]{map});
        this.CARD_COUNT = 52;
        this.placeIndex = 0;
        this.turnFirstIndex = 0;
        this.turnTime = 1;
        this.isChangePlayer = false;
        this.totalCoin = 0;
        this.firstUserID = 0;
        this.winnerPlayer = null;
        this.targetComparePlayer = null;
        this.sourceComparePlayer = null;
        this.THIS_CARD=0;
        this.countCards=new ArrayList();
        setCurrOffice(1);
        initCards();

        initRoomSetting();
//        initReplayRecord(); //游戏记录
//        initPlayers();      //重新创建游戏对象
//        initPukePlayer();   //重新赋值游戏对象  这几步可以不用
        initListener();
        //initCard();
        setCurState(PicklesGameState.INITED);
        sendQiangZhuang();
    }


    private void sendQiangZhuang() {
        System.err.println("开始抢庄");
        CommandUtil send=new CommandUtil();
        send.setEvent(PicklesGameEvent.QIANG_ZHUANG);
        send.setTime(QIANG_ZHUANG_TIME);
        sendToAll("gameType",new Object[]{send});
        suspendUtil=new SuspendUtil(QIANG_ZHUANG_TIME,PicklesGameEvent.QIANG_ZHUANG,this);
        t=new Thread(suspendUtil);
        t.start();
        setPicklesGameEvent(PicklesGameEvent.QIANG_ZHUANG);
        players.get(players.keySet().iterator().next()).setQiangZhuang(true);
    }
    @Override
    public void qiangZhuang() {
        System.err.println("抢庄结束了");
        Set<Integer> keySet=players.keySet();
        for(Integer key:keySet)
        {
            if(players.get(key).isQiangZhuang()!=null&&players.get(key).isQiangZhuang())
            {
                System.err.println(key+"抢庄了");
                zhuang=players.get(key);
//                if(new Random().nextBoolean())
//                {
//                    break;
//                }
            }
            else
            {
                System.err.println(key+"没抢庄");
            }
        }
        if(zhuang==null)
        {
            zhuang=  players.get(players.keySet().iterator().next()+new Random().nextInt(1));
        }
        System.err.println("确认庄家"+zhuang.getId());
        System.err.println("发送下注");
        CommandUtil send=new CommandUtil();
        send.setEvent(PicklesGameEvent.XIA_ZHU);
        send.setTime(XIA_ZHU_TIME);
        sendToAll("gameType",new Object[]{send});
        suspendUtil=new SuspendUtil(XIA_ZHU_TIME,PicklesGameEvent.XIA_ZHU,this);
        t=new Thread(suspendUtil);
        t.start();
        setPicklesGameEvent(PicklesGameEvent.XIA_ZHU);
    }
    @Override
    public void xiaZhu() {
        System.err.println("下注结束了");
        System.err.println("发送卡牌");
        //if(玩家中存在高级玩家)
//        {
//        }
        Set<Integer> keySet=players.keySet();
        for(Integer key:keySet)
        {
            if(players.get(key)!=zhuang&&players.get(key).getXiaZhu()==0)
            {
                players.get(key).setXiaZhu(getRoomInfo().getBaseCoin());
                System.err.println("玩家"+key+"没有选择下注，设置默认下注"+getRoomInfo().getBaseCoin());
            }
            List<Integer> cards=new ArrayList();
            cards.add(countCards.get(THIS_CARD++));
            cards.add(countCards.get(THIS_CARD++));
            players.get(key).setCards(cards);
            System.err.println("玩家"+key+"的卡牌是"+Arrays.asList(cards));
            List<UserCard> userCardList=new ArrayList<UserCard>();
            UserCard userCard;
            for(Integer k:keySet)
            {
                userCard=new UserCard();
                userCard.setId(k);
                if(k==key)
                {
                    userCard.setCards(cards);
                }
                else
                {
                    cards=new ArrayList<Integer>();
                    cards.add(0);
                    cards.add(0);
                    userCard.setCards(cards);
                }
                userCardList.add(userCard);
            }
            players.get(key).sendPacket("cards",new Object[]{userCardList});
        }


        if(BaseCardType.isExplode(zhuang.getCards()))
        {
            System.err.println("庄家炸开");
            CommandUtil send=new CommandUtil();
            send.setEvent(PicklesGameEvent.ZHUANG_EXPLODE);
            send.setTime(1000);
            sendToAll("gameType",new Object[]{send});
            suspendUtil=new SuspendUtil(1000,PicklesGameEvent.ZHUANG_EXPLODE,this);
            t=new Thread(suspendUtil);
            t.start();
            setPicklesGameEvent(PicklesGameEvent.ZHUANG_EXPLODE);
        }
        else
        {
            System.err.println("庄家不炸开");
            System.err.println("顺序操作闲家");
            idlefor();
        }

    }

    public void idlefor() {
        Set<Integer> keySet=players.keySet();
        for(Integer key:keySet)
        {
            if(players.get(key)==zhuang||players.get(key).isGain())
            {
                continue;
            }
            if(BaseCardType.isExplode(players.get(key).getCards()))
            {
                System.err.println("闲家"+key+"可选择 捞牌 不捞 炸开");
                CommandUtil send=new CommandUtil();
                send.setEvent(PicklesGameEvent.LAO_PAI);
                send.setTime(LAO_PAI_TIME);
                send.setExplode(true);
                sendToAll("gameType",new Object[]{send});
                suspendUtil=new SuspendUtil(LAO_PAI_TIME,PicklesGameEvent.LAO_PAI,this);
                t=new Thread(suspendUtil);
                t.start();
                setPicklesGameEvent(PicklesGameEvent.LAO_PAI);
            }
            else
            {
                System.err.println("闲家"+key+"可选择 捞牌 不捞");
                CommandUtil send=new CommandUtil();
                send.setEvent(PicklesGameEvent.LAO_PAI);
                send.setTime(LAO_PAI_TIME);
                sendToAll("gameType",new Object[]{send});
                suspendUtil=new SuspendUtil(LAO_PAI_TIME,PicklesGameEvent.LAO_PAI,this);
                t=new Thread(suspendUtil);
                t.start();
            }
            players.get(key).setGain(true);
            return;
        }
        System.err.println("闲家操作结束，庄操作");
        System.err.println("庄家可选择 捞牌 不捞");
        CommandUtil send=new CommandUtil();
        send.setEvent(PicklesGameEvent.ZHUANG_LAO_PAI);
        send.setTime(LAO_PAI_TIME);
        sendToAll("gameType",new Object[]{send});
        suspendUtil=new SuspendUtil(LAO_PAI_TIME,PicklesGameEvent.ZHUANG_LAO_PAI,this);
        t=new Thread(suspendUtil);
        t.start();
    }

    @Override
    public void biPai() {
        System.err.println("开始比牌");
        setCurrOffice(getCurrOffice()+1);
        System.err.println("比牌结束,开始下一回合");
        reset();
    }

    public PicklesGameEvent getPicklesGameEvent() {
        return picklesGameEvent;
    }

    @Override
    public void biPaiAll() {
        System.err.println("比牌，并发送游戏结果");
        Set<Integer> keySet=players.keySet();
        zhuang.setCardInfo(getCardInfo(zhuang.getCards()));
        System.err.println(zhuang.getCardInfo().toString());
        for(Integer key:keySet)
        {
            if(players.get(key)!=zhuang&&players.get(key).isAction())
            {
                players.get(key).setCardInfo(getCardInfo(players.get(key).getCards()));
                System.err.println(players.get(key).getCardInfo().toString());
                if(derby(zhuang.getCardInfo(),players.get(key).getCardInfo())>0)
                {
                    //庄赢
                    zhuang.setCouncil(zhuang.getCouncil()+players.get(key).getXiaZhu());
                    players.get(key).setCouncil(players.get(key).getCouncil()-players.get(key).getXiaZhu());
                }
                else if(derby(zhuang.getCardInfo(),players.get(key).getCardInfo())<0)
                {
                    //闲赢
                    zhuang.setCouncil(zhuang.getCouncil()-players.get(key).getXiaZhu());
                    players.get(key).setCouncil(players.get(key).getCouncil()+players.get(key).getXiaZhu());
                }
                else
                {
                    //走水
                }
                players.get(key).setAction(false);
            }
        }
        sendToAll("gameOver",new Object[]{players});
        setCurrOffice(getCurrOffice()+1);
        for(Integer key:keySet)
        {
            players.get(key).getCardInfo().reset();
            players.get(key).reset();
        }
        reset();
    }

    @Override
    public GameUser getZhuang() {
        return this.zhuang;
    }

    private int derby(CardInfo cardInfo, CardInfo cardInfo1) {
        if(cardInfo.getCount()>cardInfo1.getCount())
        {
            return 1;
        }
        if(cardInfo.getCount()<cardInfo1.getCount())
        {
            return -1;
        }
        if(cardInfo.getType()>cardInfo1.getType())
        {
            return 1;
        }
        if(cardInfo.getType()<cardInfo1.getType())
        {
            return -1;
        }
        return 0;
    }

    public CardInfo getCardInfo(List<Integer> cards)
    {
        CardInfo info=new CardInfo();
        info.setCards(cards);
        if(BaseCardType.isBomo(cards))
        {
            info.setType(3);
        }
        else if(BaseCardType.isTripleSalt(cards))
        {
            info.setType(2);
        }
        else if(BaseCardType.isDoubleSalt(cards))
        {
            info.setType(1);
        }
        info.setCount(BaseCardType.getCount(cards));
        return info;
    }

    public void setPicklesGameEvent(PicklesGameEvent picklesGameEvent) {
        this.picklesGameEvent = picklesGameEvent;
    }


    private int getBaseCoin() {
        return getRoom().getRoomInfo().getBaseCoin();
    }
    /**
     *  初始化中 第二步：初始化卡牌属性
     */
    private void initCardType() {
       // this.cardTypes.add(new SiTiao());
      //  this.cardTypes.add(new SanTiao());
       // this.cardTypes.add(new SanPai());
    }
    /**
     * 初始化中 第一步：初始化房间设置
     */
    private void initRoomSetting() {

        PicklesDesk roomInfo = getRoomInfo();
        this.maxTurnTimes = 10000;
        this.menPaiTimes = 10000;
        this.maxXiaZhuBet = 10000;
        this.XIA_ZHU_TIME =roomInfo.getXiaZhuTime();
        this.QIANG_ZHUANG_TIME =roomInfo.getQiangZhuangTime();
        this.XIA_ZHU_DELAY = 10000;
        this.LAO_PAI_TIME=10000;
    }
    /**
     * 初始化底牌
     */
    private void initCards() {
	    for(int i=1;i<53;i++)
	    {
	    	countCards.add(i);
	    }
        Collections.shuffle(countCards);
    }
    @Override
    public Object getCurState() {
        return null;
    }

    @Override
    public boolean isStoped() {
        return false;
    }

    @Override
    public void notifyPlayer() {

    }

    @Override
    public void notifyPlayer(GameUser paramBasePlayer) {

    }

    @Override
    public void sendGameState(int paramInt) {

    }

    @Override
    public void checkState(int paramInt) {

    }



    @Override
    public void initStopListener(IEventListener listener, IEventListener exitListener) {
        addListener(PicklesGameEvent.STOP.getValue(), listener);
      //  addListener(PicklesGameEvent.PLAYER_EXIT.getValue(), exitListener);
    }

    @Override
    public void removeStopListener(IEventListener paramIEventListener1, IEventListener paramIEventListener2) {

    }

    @Override
    protected void initPlayers() {

    }

    @Override
    protected void initListener() {
        addListener(PicklesGameEvent.XIA_ZHU.getValue(), this.xiaZhuListener);
        addListener(PicklesGameEvent.COMPARE_CARD.getValue(), this.compareListener);
    }

    @Override
    protected void removeListener() {

    }

    @Override
    protected void initReplayRecord() {

    }

    @Override
    public void reconnect(GameUser paramBasePlayer) {

    }

    @Override
    public void gameOver() {

    }
    protected void sendOperation(Object cmd, Object builder)
    {
        sendOperation(cmd,builder,null);
    }
    /**
     * 发送操作
     * @param cmd
     * @param builder
     * @param cardInfo
     */
    protected void sendOperation(Object cmd, Object builder,Object cardInfo)
    {
        sendToAll("msgtest",new Object[]{cmd,builder});
    }
    @Override
    public void stop() {

    }

    @Override
    public byte[] getGameRecordMsg() {
        return new byte[0];
    }

    @Override
    public void playerAutoFight(GameUser paramIGamePlayer) {

    }

    @Override
    public void removePlayer(int paramInt) {

    }

    @Override
    public void dissloution() {

    }

    @Override
    public void kanPai() {

    }


    @Override
    public void compare() {

    }

    @Override
    public void qiPai() {

    }


    @Override
    public void setContinue(boolean b) {
        suspendUtil.setStop(true);
    }

    private class XiaZhuListener
            implements IEventListener
    {
        /**
         * 构造函数
         */
        private XiaZhuListener()
        {
        }
        /**
         * 事件
         */
        public void onEvent(EventArg arg)
        {
            GameUser player = (GameUser)arg.getSource();
            Object[] datas = (Object[])arg.getData();
//            GameMsgProto.CommonMsg.Builder builder = GameMsgProto.CommonMsg.newBuilder();
//            builder.setUserID(player.getGamePlayer().getPlayerInfo().getId());
//            builder.setXiaZhu(((Float)datas[0]).floatValue());
            PicklesGame.this.sendOperation(datas[1], datas[0]);
            PicklesGame tmp73_70 = PicklesGame.this;
            tmp73_70.totalIn = ((int)(tmp73_70.totalIn + ((Float)datas[0]).floatValue()));
        }
    }
    private class CompareListener
            implements IEventListener
    {
        /**
         * 构造函数
         */
        private CompareListener()
        {
        }
        /**
         *发送 事件
         */
        public void onEvent(EventArg arg)
        {
            GameUser player = (GameUser)arg.getSource();

        }
    }
}
