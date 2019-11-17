package com.stars.controller.util;

import com.stars.controller.protocol.PicklesGame;
import com.stars.controller.protocol.inf.PicklesGameEvent;

public class SuspendUtil implements Runnable {
    public SuspendUtil(long stopTime, PicklesGameEvent event, PicklesGame game)
    {
        this.game=game;
        this.event=event;
        this.stopTime=System.currentTimeMillis()+stopTime;
    }
    private PicklesGameEvent event;

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    private boolean isStop=false;
    private long stopTime;
    private PicklesGame game;
    @Override
    public void run() {
        while(!isStop&&System.currentTimeMillis()<stopTime)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switch (event.getValue())
        {
            case 0:
                     game.stop();   //暂停
                break;
            case 1:
                    game.kanPai();//看牌（暂无）
                break;
            case 2:
                    game.xiaZhu();  //下注
                break;
            case 3:
                    game.compare(); //比牌
                break;
            case 4:
                game.qiPai();       //弃牌（暂无）
                break;
            case 7:
                game.qiangZhuang(); //抢庄
                break;
            case 8:
                game.idlefor();     //捞牌
                break;
            case 9:
                game.biPaiAll();       //比牌
                break;
            case 10:
                game.biPaiAll();       //炸开
                break;
            case 11:
                game.biPai();       //庄炸开
                break;
            default:
                break;
        }

    }

   /* public static void main(String[] args) {
        for(int i=0;i<=100000;i++)
        {
            Thread s=new Thread(new SuspendUtil());
            s.start();
            System.out.println("开启线程"+i);
        }
    }*/
}
