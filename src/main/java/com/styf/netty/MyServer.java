package com.styf.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ywy on 16/9/13.
 */
@Component
public class MyServer {

    @Autowired
    private MyChannelHandler myChannelHandler;

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(myChannelHandler);

            System.out.println("捞腌菜服务器启动成功 !!!");
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();


        }finally {
            System.out.println("server stop !!!");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

}
