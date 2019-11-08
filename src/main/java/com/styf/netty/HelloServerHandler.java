package com.styf.netty;


import com.stars.controller.util.LocalMem;
import com.stars.controller.util.MyApplicationContextUtil;
import com.stars.controller.utils.ManageBufferSession;
import com.stars.controller.utils.MessageSend;
import com.styf.pojo.AppVersion;
import com.styf.service.AppVersionService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ywy on 16/9/13.
 */
@Component
@ChannelHandler.Sharable
public class HelloServerHandler extends ChannelHandlerAdapter{


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("收到消息");
        try {
            ByteBuf buf = (ByteBuf)msg;
            //创建目标大小的数组
            byte[] barray = new byte[buf.readableBytes()];
            //把数据从bytebuf转移到byte[]
            buf.getBytes(0,barray);
            String jsonStr = new String(barray, "UTF-8");
            jsonStr=jsonStr.substring(jsonStr.indexOf("{"),jsonStr.length());
            System.out.println(jsonStr);
            buf.release();


            JSONObject json = null;
            JSONArray ja = null;
            Object[] args = new Object[0];
            json = JSONObject.fromObject(jsonStr);

            String url = json.getString("method");
            url = json.getString("method");
            ja = json.getJSONArray("args");
            args = ja.toArray();
            String[] urls = url.split("/");
            String protocol = urls[0];
            String method = urls[1];
            String version = json.getString("version");

            System.out.println(protocol+method+version);
            if ((version == null) || (LocalMem.version.compareTo(version) > 0))
            {
                ctx.close();
                return;
            }
            if (method.equals("checkVersion"))
            {
                long l1 = System.currentTimeMillis();
                json.put("time", Long.valueOf(l1));
            }
            if (method.equals("checkVersion"))
            {
                long l1 = System.currentTimeMillis();
                json.put("time", Long.valueOf(l1));
            }
            if ((!method.equals("publicKey")) && (!method.equals("heart")))
            {
                long time = json.getLong("time");
                if (System.currentTimeMillis() - time > 50000L)
                {
                    System.out.println("非秘钥交换和心跳包文  请求时间超过5秒,服务器将session主动断开  " + method + (System.currentTimeMillis() - time));
                    ctx.close();
                    return;
                }
            }
            Object[] args_login = new Object[args.length + 1];
            for (int i = 0; i < args.length; i++) {
                args_login[i] = args[i];
            }
            args_login[args.length] = ctx;
            args = args_login;
            Object service = MyApplicationContextUtil.getContext().getBean(protocol);
//            if(service==null)
//            {
//                System.err.println("找不到该类名"+protocol);
//                return;
//            }
            Method m = service.getClass().getDeclaredMethod(method, getClasses(args));
//            if(m==null)
//            {
//                System.err.println("找不到该方法"+args);
//                return;
//            }
            Object result = m.invoke(service, args);
            if (result != null) {
                //System.err.println("返回"+new ManageBufferSession(ctx, method, new Object[] { result }));
                MessageSend.al.requestNotify(new ManageBufferSession(ctx, method, new Object[] { result }));
                //ctx.writeAndFlush(new ManageBufferSession(ctx, method, new Object[] { result }));
            }
            /*
            String respMsg = "";
            List<AppVersion> list = appVersionService.selectAllApp();
            for (AppVersion a : list) {
                respMsg += a.getAppName() + ",";
            }
            respMsg += System.getProperty("line.separator");
            ByteBuf resp = Unpooled.copiedBuffer(respMsg.getBytes());
            ctx.writeAndFlush(resp);*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private Class<?>[] getClasses(Object[] args)
    {
        Class[] classArray = new Class[args.length];
        try
        {
            for (int i = 0; i < args.length; i++)
            {
                classArray[i] = args[i].getClass();
                if ((args[i] instanceof Integer)) {
                    classArray[i] = Integer.TYPE;
                } else if ((args[i] instanceof Boolean)) {
                    classArray[i] = Boolean.TYPE;
                } else if ((args[i] instanceof Map)) {
                    classArray[i] = Map.class;
                } else if ((args[i] instanceof Long)) {
                    classArray[i] = Long.TYPE;
                } else if ((args[i] instanceof Double)) {
                    classArray[i] = Double.TYPE;
                } else if ((args[i] instanceof ByteBuf)) {
                    classArray[i] = ByteBuf.class;
                } else if ((args[i] instanceof Float)) {
                    classArray[i] = Float.TYPE;
                }else if(args[i] instanceof String)
                {
                    classArray[i] = String.class;
                }
                else if(args[i] instanceof ChannelHandlerContext)
                {
                    classArray[i] = ChannelHandlerContext.class;
                }
            }
        }
        catch (Exception e)
        {

        }
        return classArray;
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功"+ctx.name());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接关闭"+ctx.name());
        super.channelInactive(ctx);
    }


}
