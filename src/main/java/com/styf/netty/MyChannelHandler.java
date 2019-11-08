package com.styf.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywy on 16/9/13.
 */
@Service
public class MyChannelHandler extends ChannelInitializer<SocketChannel>{


    @Autowired
    private HelloServerHandler helloServerHandler;



    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
       // socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
       // socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(helloServerHandler);
    }



}
