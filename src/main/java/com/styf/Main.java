package com.styf;

import com.styf.netty.HelloServerHandler;
import com.styf.netty.MyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ywy on 16/9/14.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        MyServer server = ctx.getBean(MyServer.class);
        server.bind(10031);
    }

}
