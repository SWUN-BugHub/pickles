package com.stars.controller.protocol;

import java.util.*;

import com.stars.controller.entity.*;
import com.stars.controller.protocol.inf.PicklesRoom;
import com.stars.controller.service.*;
import com.stars.controller.util.LocalMem;
import com.stars.controller.util.MyUtil;
import com.stars.controller.utils.*;
import com.styf.netty.CommonConst;
import com.styf.netty.net.IClientConnection;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService
{
  //@Autowired
  //private ActivityService activityService;
  @Autowired
  private VersionService versionService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private SystemConfigService systemConfigService;

  @Autowired
  private UserAwardService userAwardService;

  @Autowired
  private PicklesDeskService picklesDeskService;

  public Map<String, Object> checkVersion(String versionCode, ChannelHandlerContext ctx)
  {
    IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
    Integer id=(Integer) conn.getAttribute("id");
  //  System.out.println(id==null?"未登录":"登录了，id:"+id);
    MapUtils result =MapUtils.getMap();
    Version version = this.versionService.selectById(8);
    if (version == null)
    {
      result.setMap("success", Boolean.valueOf(false)).setMap("msg", ArrayOfclass.ar.getName()).setMap("msgCode", Integer.valueOf(ArrayOfclass.ar.getCode()));
      //this.logger.error(com.stars.controller.utils.ArrayOfclass.ar.getName());
    }
    else if (version.getVersionCode().equals(versionCode))
    {
      result.setMap("success", Boolean.valueOf(true)).setMap("haveNewVersion", Boolean.valueOf(false));
    }
    else
    {
      result.setMap("success", Boolean.valueOf(true)).setMap("haveNewVersion", Boolean.valueOf(true)).setMap("downloadAddress", version);
    }
    return result.getObjects();
  }
  public Map<String, Object> publicKey(String modulus, String exponent, ChannelHandlerContext ctx)
  {
    MapUtils result = MapUtils.getMap();
    IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
    conn.setAttribute("attack", "false");
    String key = MyUtil.getRandomChar(16);

    LocalMem.keySession.put(ctx, key);

    key = MyUtil.Base64EncodeToString(key, modulus, exponent);

    result.setMap("success", Boolean.valueOf(true)).setMap("key", key).setMap("time", Long.valueOf(System.currentTimeMillis()));

    return result.getObjects();
  }
  public Map<String, Object> userLogin(String username, String password, ChannelHandlerContext ctx)
  {
    Map<String, Object> loginResult = _login(username, password);
    boolean isLogin = ((Boolean)loginResult.get("success")).booleanValue();
    if (isLogin)
    {
      User user = this.roleService.selectByUserName(username);
      _dealRepeatLogin(user);    //删除已经在线连接
      _setLoginStatus(ctx, user);//存放session
      _sendLoginAward(user, ctx);//如果有奖品信息 发送玩家奖品
      _updateUserLastGameLog(user.getId(), 10);//修改玩家最后登录时间
      user.setCtx(ctx);//将玩家信息绑定
      LocalMem.userMap.put(user.getId(),user);//将玩家保存起来
     // ManageSocketConnect.requestRemotely("gameService/login", new Object[] { Integer.valueOf(user.getId()), 10 });
    }
    return loginResult;
  }
  public void heart(ChannelHandlerContext ctx)
  {
    if (LocalMem.timeSession.containsKey(ctx))
    {
      LocalMem.timeSession.put(ctx, Long.valueOf(System.currentTimeMillis()));
    }
    IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
    if (conn.isConnected()) {
      MessageSend.al.requestNotify(new com.stars.controller.utils.ManageBufferSession(ctx, "heart", new Object[0]));
    }
  }
  private void _updateUserLastGameLog(int userId, int gameType)
  {
    this.roleService.updateUserLastGame(userId, gameType);
  }
  private Map<String, Object> _login(String username, String password)
  {
    MapUtils result = MapUtils.getMap();

    User user = this.roleService.selectByUserName(username);
    if (user == null)
    {
      result.setMap("success", Boolean.valueOf(false)).setMap("msg", ArrayOfclass.at.getName()).setMap("msgCode", Integer.valueOf(ArrayOfclass.at.getCode()));
    }
    else if (user.getPassword().equals(DigestUtils.md5Hex(password)))
    {
      if (user.getStatus() == 0)
      {
        SystemConfig sc = this.systemConfigService.getSystemConfig();
        if ((sc.getGameStatus() == 1) && (user.getType() != 2)) {
          result.setMap("success", Boolean.valueOf(false)).setMap("msg", ArrayOfclass.ay.getName()).setMap("msgCode", Integer.valueOf(ArrayOfclass.ay.getCode()));
        } else {
          if(LocalMem.temporaryList.containsKey(user.getId()))
          {
            user.setRoomId(LocalMem.temporaryList.get(user.getId()));
          }
          result.setMap("success", Boolean.valueOf(true)).setMap("user", user);
        }
      }
      else
      {
        result.setMap("success", Boolean.valueOf(false)).setMap("msg", ArrayOfclass.ax.getName()).setMap("msgCode", ArrayOfclass.ax.getCode());
      }
    }
    else
    {
      result.setMap("success", Boolean.valueOf(false)).setMap("msg", ArrayOfclass.au.getName()).setMap("msgCode", ArrayOfclass.au.getCode());
    }
    return result.getObjects();
  }
  private void _sendLoginAward(User user, ChannelHandlerContext ctx)
  {
    UserAward[] awards = this.userAwardService.selectByUserId(user.getId());
    if (awards != null) {
      for (int i = 0; i < awards.length; i++) {
        MessageSend.al.requestNotify(new ManageBufferSession(ctx, "userAward", new Object[] { Integer.valueOf(awards[i].getGold()) }));
      }
    }
  }
  public synchronized void enterRoom(int roomId, ChannelHandlerContext ctx)
  {
    try
    {
      IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
      Integer id = (Integer)conn.getAttribute("id");
      int userId = Integer.parseInt(id.toString());
      User user = LocalMem.userMap.get(userId);
      if (user.getOverflow() == 1)
      {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("isOverflow", Boolean.valueOf(true));
        result.put("overflowValue", Integer.valueOf(this.systemConfigService.getSystemConfig2().getMoneyOverrun()));
        MessageSend.al.requestNotify(new ManageBufferSession(ctx, "roomInfo", new Object[] { result }));
        return;
      }
      if(LocalMem.temporaryList.keySet().contains(user.getId())&&LocalMem.temporaryList.get(user.getId())!=roomId)
      {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", "已经加入其他场次");
        result.put("roomId",LocalMem.temporaryList.get(user.getId()));
        //result.put("overflowValue", Integer.valueOf(this.systemConfigService.getSystemConfig2().getMoneyOverrun()));
        MessageSend.al.requestNotify(new ManageBufferSession(ctx, "roomInfo", new Object[] { result }));
        return;
      }

      List<PicklesDesk> desks = this.picklesDeskService.getPicklesDesks(roomId);
      for(PicklesDesk desk :desks)
      {
        if(LocalMem.roomMap.containsKey(desk.getId()))
        {
          List<User> list=new ArrayList<User>();
          if(LocalMem.roomMap.get(desk.getId()).getTakesUser()!=null)
          {
            Set<Integer> keySet=LocalMem.roomMap.get(desk.getId()).getTakesUser().keySet();
            for(Integer key:keySet)
            {
              list.add(LocalMem.roomMap.get(desk.getId()).getTakesUser().get(key));
            }
          }
          desk.setUserList(list);
        }
      }
      LocalMem.temporaryList.put(Integer.valueOf(userId), Integer.valueOf(roomId));
      if (conn.isConnected()) {
        if(LocalMem.deskUserList.keySet().contains(user.getId()))
        {
        //  result.put("deskId",);
          MessageSend.al.requestNotify(new ManageBufferSession(ctx, "roomInfo", new Object[] { desks.toArray(),LocalMem.deskUserList.get(user.getId()) }));
          return;
        }
        MessageSend.al.requestNotify(new ManageBufferSession(ctx, "roomInfo", new Object[] { desks.toArray() }));
      }
    }
    catch (Exception e)
    {
      System.err.println("进入房间失败");
      System.out.println(e);
    }
  }


  public synchronized void updateRoom(int roomId, ChannelHandlerContext ctx)
  {
    try
    {
      IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
      Integer id = (Integer)conn.getAttribute("id");
      int userId = Integer.parseInt(id.toString());
      User user = LocalMem.userMap.get(userId);
      List<PicklesDesk> desks = this.picklesDeskService.getPicklesDesks(roomId);
      for(PicklesDesk desk :desks)
      {
        if(LocalMem.roomMap.containsKey(desk.getId()))
        {
          List<User> list=new ArrayList<User>();
          if(LocalMem.roomMap.get(desk.getId()).getTakesUser()!=null)
          {
            Set<Integer> keySet=LocalMem.roomMap.get(desk.getId()).getTakesUser().keySet();
            for(Integer key:keySet)
            {
              list.add(LocalMem.roomMap.get(desk.getId()).getTakesUser().get(key));
            }
          }
          desk.setUserList(list);
        }
      }
      if (conn.isConnected()) {
        MessageSend.al.requestNotify(new ManageBufferSession(ctx, "updateRoomInfo", new Object[] { desks.toArray() }));
      }
    }
    catch (Exception e)
    {
      System.err.println("进入房间失败");
      System.out.println(e);
    }
  }


  public void leaveRoom(int roomId,ChannelHandlerContext ctx)
  {
    IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
    int userId = Integer.parseInt(conn.getAttribute("id").toString());
    LocalMem.temporaryList.remove(userId);
  }

  private void _setLoginStatus(ChannelHandlerContext ctx, User user)
  {
    IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
    conn.setAttribute("trueClient", Boolean.valueOf(true));
    conn.setAttribute("username", user.getUsername());
    conn.setAttribute("id", Integer.valueOf(user.getId()));
    LocalMem.userSessionList.put(Integer.valueOf(user.getId()), ctx);
  }
  private void _dealRepeatLogin(User user)
  {
    if (LocalMem.userSessionList.containsKey(Integer.valueOf(user.getId())))
    {
      ChannelHandlerContext ctx = (ChannelHandlerContext)LocalMem.userSessionList.get(user.getId());
      IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
      int userId = Integer.parseInt(conn.getAttribute("id").toString());

      MessageSend.al.requestNotify(new ManageBufferSession(ctx, "quitToLogin", new Object[] { Integer.valueOf(4) }));

      LocalMem.userSessionList.remove(Integer.valueOf(userId));

      conn.setAttribute("needRemove", Boolean.valueOf(true));
      LocalMem.timeSession.remove(conn);
    }
  }

  /**
   * 进入房间
   * @param deskId
   * @param ctx
   * @return
   */
  public synchronized Map<String, Object> enterDesk(int deskId, ChannelHandlerContext ctx)
  {

    Map<String, Object> result = new HashMap();
    try
    {
      //1、判断玩家是否登录
      IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
      int userId = Integer.parseInt(conn.getAttribute("id").toString());
      User user = LocalMem.userMap.get(userId);
      if(user==null)
      {
        result.put("success", Boolean.valueOf(false));
        result.put("messageStatus", Integer.valueOf(0));
        return result;
      }

      if(LocalMem.deskUserList.keySet().contains(user.getId())&&LocalMem.deskUserList.get(userId)!=deskId)
      {
        result.put("error", "已经加入其他桌子");
        result.put("deskId",LocalMem.deskUserList.get(user.getId()));
        //result.put("overflowValue", Integer.valueOf(this.systemConfigService.getSystemConfig2().getMoneyOverrun()));
        //MessageSend.al.requestNotify(new ManageBufferSession(ctx, "roomInfo", new Object[] { result }));
        return result;
      }
      //2、判断房间是否已经在map里面了 有就取出来 没有就从数据库中取，再没有就返回失败
      PicklesDesk desk = LocalMem.deskMap.get(deskId);
      if(desk==null)
      {
        desk = picklesDeskService.getPicklesDesksById(deskId);
        if (desk == null)
        {
          result.put("success", Boolean.valueOf(false));
          result.put("messageStatus", Integer.valueOf(0));
          return result;
        }
        if(!LocalMem.deskMap.keySet().contains(user.getId()))
        {
          LocalMem.deskMap.put(deskId,desk);
        }

      }
      //3、将房间信息添加到消息列表
      result.put("success", Boolean.valueOf(true));
      result.put("messageStatus", 1);
      result.put("roomInfo",desk);
      //4、判断房间对象是否已经创建过了
      BaseRoom room=LocalMem.roomMap.get(deskId);
      if(room==null)
      {
        room=new PicklesRoom(desk);
        LocalMem.roomMap.put(deskId,room);
      }
      //5、将玩家转换成游戏玩家对象赋以游戏分数
      GameUser gameUser=new GameUser(user);;
      gameUser.setGameState(0);
      gameUser.setCoin(0);
      //6、如果存在将已经坐下的玩家和观战中的玩家加入消息列表
      result.put("watchers",room.getWatchers());
      result.put("takes",room.getTakesUser());
      //7、将玩家添加进房间内的观战者列表
        if(!room.watchers.contains(gameUser))
        {
            room.addWatchers(gameUser);
        }
      //8、向房间内的其他玩家发送有玩家加入信息
      for(GameUser u:room.getWatchers())
      {
        if(LocalMem.deskUserList.keySet().contains(user.getId()))
        {
          break;
        }
        if(u.getId()!=user.getId())
        {
          MessageSend.al.requestNotify(new ManageBufferSession((ChannelHandlerContext)LocalMem.userMap.get(u.getId()).getCtx(), "enterDesk", new Object[] { gameUser }));
        }
      }
      Set<Integer> key=room.getTakesUser().keySet();
      for(Integer k:key)
      {
        if(LocalMem.deskUserList.keySet().contains(user.getId()))
        {
          break;
        }
        if(k!=user.getId())
        {
          MessageSend.al.requestNotify(new ManageBufferSession((ChannelHandlerContext)LocalMem.userMap.get(k).getCtx(), "enterDesk", new Object[] { gameUser }));
        }
      }
      if(!LocalMem.deskUserList.keySet().contains(user.getId()))
      {
        LocalMem.deskUserList.put(userId,deskId);
      }
      //9、返回消息
      return result;
    }
    catch (Exception e)
    {
      result.put("status", Integer.valueOf(999));
    }
    return result;
  }

}
