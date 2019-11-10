package com.styf.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
    int beginIndex;
        while (true) {
            //包头开始游标点
            beginIndex = buffer.readerIndex();
            //标记初始读游标位置
            buffer.markReaderIndex();
            if (buffer.readInt() == -21415431) {
                break;
            }
            //未读到包头标识略过一个字节
            buffer.resetReaderIndex();
            buffer.readByte();

            //不满足
            if (buffer.readableBytes() < 8) {
                return ;
            }
        }
        //读取数据长度
        int lenth = buffer.readInt();
        if(lenth < 0 ){
            ctx.close();
        }
        //数据包还没到齐
        if(buffer.readableBytes() < lenth){
            buffer.readerIndex(beginIndex);
            return ;
        }
        //读数据部分
        byte[] data = new byte[lenth];
        buffer.readBytes(data);
        String jsonStr = new String(data, "UTF-8");
        System.out.println(jsonStr);
    }

}

