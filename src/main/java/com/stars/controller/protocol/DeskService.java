package com.stars.controller.protocol;

import com.stars.controller.entity.Chat;
import com.stars.controller.entity.GameUser;
import com.stars.controller.util.LocalMem;
import com.styf.netty.CommonConst;
import com.styf.netty.net.IClientConnection;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("deskService")
public class DeskService {

    public Map<String, Object> leaveDesk(int roomId,ChannelHandlerContext ctx)
    {
        Map<String, Object> result = new HashMap();
        IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
        int userId = Integer.parseInt(conn.getAttribute("id").toString());
        if(LocalMem.roomMap.get(roomId).getRoomInfo().isStart()&&LocalMem.roomMap.get(roomId).getTakesUser().containsKey(userId))
        {
            result.put("success", Boolean.valueOf(false));
            result.put("error", "您已经开始游戏了");
            return result;
        }

        if(LocalMem.roomMap.get(roomId).getWatchersById(userId)!=null)
        {
            LocalMem.roomMap.get(roomId).getWatchers().remove(LocalMem.roomMap.get(roomId).getWatchersById(userId));
        }
        if(LocalMem.roomMap.get(roomId).getTakesUser().containsKey(userId))
        {
            LocalMem.roomMap.get(roomId).getTakesUser().remove(userId);
        }
        LocalMem.deskUserList.remove(userId);
        result.put("success", Boolean.valueOf(true));
        return result;
    }
    public Map<String, Object> watchers(int deskId,ChannelHandlerContext ctx)
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
            result.put("success", Boolean.valueOf(true));
            result.put("watchersList", room.getWatchers());
            return result;
        }
    }
    public Map<String, Object> standUp(Integer deskId, ChannelHandlerContext ctx)
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
            GameUser gameUser = room.getTakesUser().get(userId);
            if (gameUser == null) {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "您已经站起了");
                return result;
            }
            room.getTakesUser().remove(gameUser);
            room.getWatchers().add(gameUser);
            room.sendToAll("standUp",new Object[]{gameUser});
        }
        return null;
    }

    public Map<String, Object> addCoin(Integer deskId,int coin, ChannelHandlerContext ctx)
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
                result.put("error", "进入的房间和发起请求的房间不一致");
                return result;
            }
            //
            if(coin<-1000||coin>1000)
            {
                result.put("success", Boolean.valueOf(false));
                result.put("error", "操作分数过大");
                return result;
            }
            GameUser user=LocalMem.roomMap.get(deskId).getWatchersById(userId);
            if(user==null)
            {
                user=LocalMem.roomMap.get(deskId).getTakesUser().get(userId);
            }
            if(user==null)
            {
                return null;
            }
            if(coin>0)
            {
                if(user.getGameGold()<coin)
                {
                    coin=user.getGameGold();
                }
                user.setGameScore(coin*LocalMem.roomMap.get(deskId).getRoomInfo().getExchange()+user.getGameScore());
                user.setGameGold(user.getGameGold()-coin);
                LocalMem.roomMap.get(deskId).sendToAll("addCoin",new Object[]{user});
            }
            else if(coin<0)
            {
                int deleteCoin=LocalMem.roomMap.get(deskId).getRoomInfo().getExchange()*coin;
                if(user.getGameScore()<deleteCoin)
                {
                    deleteCoin=user.getGameScore();
                }
                user.setGameGold(deleteCoin/LocalMem.roomMap.get(deskId).getRoomInfo().getExchange()+user.getGameScore());
                user.setGameScore(user.getGameScore()-deleteCoin);
                LocalMem.roomMap.get(deskId).sendToAll("addCoin",new Object[]{user});
            }
        }
        return null;
    }

    public Map<String, Object> chat(int beUserId,int messageId,int type,int deskId,ChannelHandlerContext ctx)
    {
        Map<String, Object> result = new HashMap();
        IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
        Integer userId = Integer.parseInt(conn.getAttribute("id").toString());
        if(userId==null)
        {
            result.put("success", Boolean.valueOf(false));
            result.put("error", "请先登录");
            return result;
        }
        else
        {
            if(type==3)
            {
                if(!LocalMem.deskUserList.containsKey(userId))
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "请先进入房间");
                    return result;
                }
                if(LocalMem.deskUserList.get(userId)!=deskId)
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "进入的房间和发起表情的房间不一致");
                    return result;
                }
                BaseRoom room=LocalMem.roomMap.get(deskId);
                if(room!=null)
                {
                    room.sendToAll("chat",new Object[]{new Chat(beUserId,messageId,type)});
                }
            }
            else
            {
                if(beUserId==0)
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "请选择对谁发送");
                    return result;
                }
                if(!LocalMem.deskUserList.containsKey(userId))
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "请先进入房间");
                    return result;
                }
                if(LocalMem.deskUserList.get(userId)!=deskId)
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "进入的房间和发起表情的房间不一致");
                    return result;
                }
                if(LocalMem.deskUserList.get(userId)!=LocalMem.deskUserList.get(beUserId))
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "你们不处在同一个房间");
                    return result;
                }
                BaseRoom room=LocalMem.roomMap.get(deskId);
                GameUser gameUser=room.getTakesUser().get(beUserId);
                if(gameUser==null)
                {
                    result.put("success", Boolean.valueOf(false));
                    result.put("error", "对方处在观战列表中");
                    return result;
                }
                gameUser.sendPacket("chat",new Object[]{new Chat(beUserId,messageId,type)});
            }

        }
        return null;
    }
    public Map<String, Object> take(int deskId, ChannelHandlerContext ctx)
    {
        Map<String, Object> result = new HashMap();
        IClientConnection conn= (IClientConnection)ctx.channel().attr(CommonConst.CLIENT_CON).get();
        Integer userId=(Integer)conn.getAttribute("id");
        if(userId==null)
        {
            result.put("error", "请先登录");
            return result;
        }
        else
        {
            if(!LocalMem.deskUserList.containsKey(userId))
            {
                result.put("error", "请先进入房间");
                return result;
            }
            if(LocalMem.deskUserList.get(userId)!=deskId)
            {
                result.put("error", "进入的房间和发起坐下的房间不一致");
                return result;
            }
            if(LocalMem.roomMap.get(deskId).getRoomInfo()!=null&&LocalMem.roomMap.get(deskId).getRoomInfo().isStart())
            {
                result.put("error", "房间已经开始游戏了");
                return result;
            }
            BaseRoom room=LocalMem.roomMap.get(deskId);
            GameUser gameUser=room.getWatchersById(userId);
            if(gameUser==null)
            {
                result.put("error", "您已经坐下了");
                return result;
            }
            room.getWatchers().remove(gameUser);
            room.getTakesUser().put(userId,gameUser);
            room.sendToAll("take",new Object[]{gameUser});
            if(room.getTakesUser().size()>=room.getRoomInfo().getAutoStart())
            {
                //开始游戏
                room.startGame();
            }
        }
        return null;
    }
}
