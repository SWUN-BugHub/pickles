package com.styf.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ywy on 16/9/13.
 */
public class HelloClientHandler extends ChannelHandlerAdapter{


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();

        String body = "{\"method\":\"userService/checkVersion\",\"args\":[\"9.0.1\"],\"version\":\"1.2.15\"}";
        ByteBuf msg = Unpooled.buffer(body.getBytes().length);
        msg.writeInt(body.getBytes().length);
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
        //Thread.sleep(3000);
        body = "{\"method\":\"userService/publicKey\",\"args\":[\"9GsPEp2kSUzvJajn1mUCUoyCQ4IcNR/FuvN9LpiUvIQxQZMpleofI3P2kv2DpH1+UOMMfkzolvG+A0I45byU4vJsWE76RmNB1k5sejIXc8V6eRKzKSc08iAKtvrmvVOBNDiStgfPfQGIup0ZHrASKjkTTzJvtT2LQ1SbCakwLBu=\",\"AQAB\"],\"version\":\"1.2.15\"}";
        msg = Unpooled.buffer(body.getBytes().length);
        msg.writeInt(body.getBytes().length);
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
        //Thread.sleep(3000);
        body = "{\"method\":\"userService/userLogin\",\"time\":1573204136429,\"args\":[\"aaaaaa\",\"a11111\"],\"version\":\"1.2.15\"}";
        msg = Unpooled.buffer(body.getBytes().length);
        msg.writeInt(body.getBytes().length);
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
        Thread.sleep(3000);
        body = "{\"method\":\"userService/enterRoom\",\"time\":1573203828230,\"args\":[1],\"version\":\"1.2.15\"}";
        msg = Unpooled.buffer(body.getBytes().length);
        msg.writeInt(body.getBytes().length);
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
        Thread.sleep(3000);
        body = "{\"method\":\"userService/enterDesk\",\"time\":1573203828230,\"args\":[1],\"version\":\"1.2.15\"}";
        msg = Unpooled.buffer(body.getBytes().length);
        msg.writeInt(body.getBytes().length);
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
        Thread.sleep(3000);
        Thread t=new Thread(new ThreadConn(ctx));
        t.start();
    }
    class ThreadConn implements Runnable {
        ChannelHandlerContext ctx;

        public ThreadConn(ChannelHandlerContext ctx)
        {
            this.ctx=ctx;
        }
        @Override
        public void run() {
            while(true)
            {
                String body = "{\"method\":\"userService/heart\",\"time\":1573203136514,\"args\":[],\"version\":\"1.2.15\"}";
                ByteBuf msg = Unpooled.buffer(body.getBytes().length);
                msg.writeInt(body.getBytes().length);
                msg.writeBytes(body.getBytes());
                ctx.writeAndFlush(msg);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(1);
        ByteBuf buf = (ByteBuf)msg;
        //创建目标大小的数组
        byte[] barray = new byte[buf.readableBytes()];
        //把数据从bytebuf转移到byte[]
        buf.getBytes(0,barray);
        String jsonStr = new String(barray, "UTF-8");
        jsonStr=jsonStr.substring(jsonStr.indexOf("{"),jsonStr.length());
        System.out.println(jsonStr);
        buf.release();
    }
}
