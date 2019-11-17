package com.stars.controller.protocol;

import com.stars.controller.protocol.inf.PicklesGameEvent;
import com.stars.controller.util.LocalMem;
import com.styf.netty.CommonConst;
import com.styf.netty.net.IClientConnection;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service("gameService")
public class GameService {

    public Map<String, Object> qiangZhuang(int deskId,boolean qiangZhuang,ChannelHandlerContext ctx)
    {
        Map<String, Object> result = new HashMap();
        IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
        Integer userId=(Integer)conn.getAttribute("id");
        if(userId==null)
        {
            result.put("success", Boolean.valueOf(false));
            result.put("error", "请先登录");
            return result;
        }
        else {
            if (!LocalMem.deskUserList.containsKey(userId)) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "请先进入房间");
                return result;
            }
            if (LocalMem.deskUserList.get(userId) != deskId) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "进入的房间和发起站起的房间不一致");
                return result;
            }
            BaseRoom room = LocalMem.roomMap.get(deskId);
            if (room.getRoomInfo()==null||room.getRoomInfo().isStart()==false) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "房间未开始游戏");
                return result;
            }
            if(room.getGame().getPicklesGameEvent()!= PicklesGameEvent.QIANG_ZHUANG)
            {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "房间目前不处在抢庄状态");
                return result;
            }
            result.put("success", Boolean.valueOf(true));
            result.put("userId", userId);
            room.sendToAll("qiangZhuang",new Object[]{result});
            room.getTakesUser().get(userId).setQiangZhuang(qiangZhuang);
            Set<Integer> keySet=room.getTakesUser().keySet();
            for(Integer key:keySet)
            {
                if(room.getTakesUser().get(key).isQiangZhuang()==null)
                {
                    return null;
                }
            }
            room.getGame().setContinue(true);
            return null;
        }
    }
    public Map<String, Object> xiaZhu(int deskId,int coin,ChannelHandlerContext ctx)
    {
        Map<String, Object> result = new HashMap();
        IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
        Integer userId=(Integer)conn.getAttribute("id");
        if(userId==null)
        {
            result.put("success", Boolean.valueOf(false));
            result.put("error", "请先登录");
            return result;
        }
        else {
            if (!LocalMem.deskUserList.containsKey(userId)) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "请先进入房间");
                return result;
            }
            if (LocalMem.deskUserList.get(userId) != deskId) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "进入的房间和发起站起的房间不一致");
                return result;
            }
            BaseRoom room = LocalMem.roomMap.get(deskId);
            if (room.getRoomInfo()==null||room.getRoomInfo().isStart()==false) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "房间未开始游戏");
                return result;
            }
            if(room.getGame().getPicklesGameEvent()!= PicklesGameEvent.XIA_ZHU)
            {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "房间目前不处在下注状态");
                return result;
            }
            result.put("success", Boolean.valueOf(true));
            result.put("userId", userId);
            room.sendToAll("qiangZhuang",new Object[]{result});
            room.getTakesUser().get(userId).setXiaZhu(coin);
            Set<Integer> keySet=room.getTakesUser().keySet();
            for(Integer key:keySet)
            {
                if(room.getTakesUser().get(key).getXiaZhu()!=0&&room.getTakesUser().get(key)!=room.getGame().getZhuang())
                {
                    return null;
                }
            }
            room.getGame().setContinue(true);
            return null;
        }
    }
}
