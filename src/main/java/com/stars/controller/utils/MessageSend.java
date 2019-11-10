package com.stars.controller.utils;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import com.stars.controller.util.MyUtil;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

@Service("messageSend")
public class MessageSend
  extends Thread
{
  private static Logger logger = Logger.getLogger(MessageSend.class);
  public static ImplementRequest al = new ImplementRequest();
  private ThreadPoolExecutor am = new ThreadPoolExecutor(1, 1, 3L, TimeUnit.SECONDS, 
    new LinkedBlockingQueue(), new CallerRunsPolicy());
  
  @PostConstruct
  public void IllIlIlIlIIlIlIl()
  {
    start();
  }
  
  public void run()
  {
    for (ManageBufferSession data = al.waitImplement(); data != null; data = al.waitImplement()) {
      this.am.execute(new SendExecute(data));
    }
  }
  
  public void lIlIIlIIIIlIlIll(ManageBufferSession data)
  {
    new SendExecute(data).run();
  }
  
  private class SendExecute
    extends Thread
  {
    private ManageBufferSession an;
    
    public SendExecute(ManageBufferSession data)
    {
      this.an = data;
    }
    
    public void run()
    {
      try
      {
        Map<String, Object> msgContext = new HashMap();
        msgContext.put("method", this.an.method);
        msgContext.put("args", this.an.result);
        

        Gson gson = new Gson();
        String msgJson = gson.toJson(msgContext);
        System.out.println("返回:"+msgJson);
        byte[] content = msgJson.getBytes("utf-8");
        
        String sendContent = new String(content);
       // MessageSend.logger.info(" send json  " + sendContent);
        if (!this.an.method.equals("publicKey")) {
          content = MyUtil.AESencryption_1_UTF8(content, this.an.ctx);
        }
        ByteBuffer bb = ByteBuffer.allocate(content.length + 4);
        bb.putInt(content.length);
        bb.put(content);
        bb.flip();
        if (this.an.ctx != null)
        {
          this.an.ctx.write(bb);
          MessageSend.logger.info("send messgae success");
        }
      }
      catch (Exception e)
      {
        MessageSend.logger.error("send messgae fail  " + e.getMessage());
      }
    }
  }
}
