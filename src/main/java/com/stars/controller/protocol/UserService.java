package com.stars.controller.protocol;

import java.util.Map;

import com.stars.controller.entity.SystemConfig;
import com.stars.controller.entity.User;
import com.stars.controller.entity.Version;
import com.stars.controller.service.ActivityService;
import com.stars.controller.service.RoleService;
import com.stars.controller.service.SystemConfigService;
import com.stars.controller.service.VersionService;
import com.stars.controller.util.LocalMem;
import com.stars.controller.util.MyUtil;
import com.stars.controller.utils.ArrayOfclass;
import com.stars.controller.utils.ManageBufferSession;
import com.stars.controller.utils.MapUtils;
import com.stars.controller.utils.MessageSend;
import com.styf.netty.CommonConst;
import com.styf.netty.IClientConnection;
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

  public Map<String, Object> checkVersion(String versionCode, ChannelHandlerContext session)
  {
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
  public Map<String, Object> userLogin(String username, String password, ChannelHandlerContext session)
  {
    Map<String, Object> loginResult = _login(username, password);
    boolean isLogin = ((Boolean)loginResult.get("success")).booleanValue();
    if (isLogin)
    {
      User user = this.roleService.selectByUserName(username);
      _dealRepeatLogin(user);
      //_setLoginStatus(session, user);
      //_sendLoginAward(user, session);
      //_updateUserLastGameLog(user.getId(), 7);
     // ManageSocketConnect.requestRemotely("gameService/login", new Object[] { Integer.valueOf(user.getId()), Integer.valueOf(7) });
    }
    return loginResult;
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

}
