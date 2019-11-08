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
        msg.writeBytes(body.getBytes());
        ctx.writeAndFlush(msg);
//        String body2 = "{\"method\":\"userService/publicKey\",\"args\":[\"9GsPEp2kSUzvJajn1mUCUoyCQ4IcNR/FuvN9LpiUvIQxQZMpleofI3P2kv2DpH1+UOMMfkzolvG+A0I45byU4vJsWE76RmNB1k5sejIXc8V6eRKzKSc08iAKtvrmvVOBNDiStgfPfQGIup0ZHrASKjkTTzJvtT2LQ1SbCakwLBu=\",\"AQAB\"],\"version\":\"1.2.15\"}";
//        ByteBuf msg2 = Unpooled.buffer(body2.getBytes().length);
//        msg2.writeBytes(body2.getBytes());
//        ctx.writeAndFlush(msg2);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("client receive :" + body);
    }
}
