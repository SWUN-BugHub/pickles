package com.stars.controller.entity;

public class Chat {
    Integer beUserId;
    Integer messageId;
    Integer type;
    public Chat(Integer beUserId,Integer messageId,Integer type)
    {
        this.beUserId=beUserId;
        this.messageId=messageId;
        this.type=type;
    }
    public Integer getBeUserId() {
        return beUserId;
    }

    public void setBeUserId(Integer beUserId) {
        this.beUserId = beUserId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
