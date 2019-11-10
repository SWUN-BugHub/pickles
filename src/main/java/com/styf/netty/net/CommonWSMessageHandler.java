package com.styf.netty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonWSMessageHandler
        implements IMessageHandler
{
  private static Logger LOGGER = LoggerFactory.getLogger(CommonWSMessageHandler.class);

  public void process(IClientConnection conn, Object packet) {
   /* CommonMsgProto.CommonMsgPB pb = (CommonMsgProto.CommonMsgPB)packet;
    short code = (short)pb.getCode();					//获得请求code

    AbstractCommandComponent cm = (AbstractCommandComponent)ComponentManager.getInstance().getComponent(
            AbstractCommandComponent.NAME);
    if (cm == null)
    {
      LOGGER.error("CommandModule not found");
      return;
    }

    ICommand cmd = cm.getCommand(code);	//在这根据code区分请求信息
    if (cmd == null)
    {
      LOGGER.error(" Can not found code = " + code + ",drop this packet.");
      return;
    }

    try
    {
      cmd.execute(conn, pb.getBody());	//对请求进行处理
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }
  */
  }
}