package com.styf.netty.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonMessageHandler
        implements IMessageHandler
{
  private String cmdCMPTName;
  private static Logger LOGGER = LoggerFactory.getLogger(CommonMessageHandler.class);

  public CommonMessageHandler(String cmdCMPTName)
  {
    this.cmdCMPTName = cmdCMPTName;
  }

  public void process(IClientConnection conn, Object packet)
  {
  /*  CommonMessage message = (CommonMessage)packet;
    short code = message.getCode();

    AbstractCommandComponent cm = (AbstractCommandComponent)ComponentManager.getInstance().getComponent(
            this.cmdCMPTName);
    if (cm == null)
    {
      LOGGER.error("CommandModule not found");
      return;
    }

    ICommand cmd = cm.getCommand(code);
    if (cmd == null)
    {
      LOGGER.error(" Can not found code = " + code + ",drop this packet.");
      return;
    }

    try
    {
      cmd.execute(conn, message.getBody());
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }*/
  }
}