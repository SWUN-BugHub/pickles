package com.stars.controller.entity;

import com.stars.controller.protocol.inf.CardInfo;
import com.stars.controller.utils.ManageBufferSession;
import com.stars.controller.utils.MessageSend;

import java.util.List;

public class GameUser extends User{
    public GameUser(User user) {
        this.setCtx(user.getCtx());
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setNickname(user.getNickname());
        this.setName(user.getName());
        this.setSex(user.getSex());
        this.setPassword(user.getPassword());
        this.setPhone(user.getPhone());
        this.setCard(user.getCard());
        this.setQuestion(user.getQuestion());
        this.setAnswer(user.getAnswer());
        this.setRegistDate(user.getRegistDate());
        this.setLoginDate(user.getLoginDate());
        this.setStatus(user.getStatus());
        this.setOverflow(user.getOverflow());
        this.setGameGold(user.getGameGold());
        this.setExpeGold(user.getExpeGold());
        this.setLevelScore(user.getLevelScore());
        this.setGameScore(user.getGameScore());
        this.setExpeScore(user.getExpeScore());
        this.setLevel(user.getLevel());
        this.setPhotoId(user.getPhotoId());
        this.setLastDeskId(user.getLastDeskId());
        this.setShutupStatus(user.getShutupStatus());
        this.setLastGame(user.getLastGame());
        this.setType(user.getType());
        this.setExpiryNum(user.getExpiryNum());
        this.setPayMoney(user.getPayMoney());
        this.setPromoterId(user.getPromoterId());
        this.setPromoterName(user.getPromoterName());
        if(user.getBorrow()!=null)
        {
            this.setBorrow(user.getBorrow());
        }
        if(user.getDisplayStatus()!=null)
        {
            this.setDisplayStatus(user.getDisplayStatus());
        }
        if(user.getCurrentGameScore()!=null)
        {
            this.setCurrentGameScore(user.getCurrentGameScore());
        }



    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public int getCouncil() {
        return council;
    }

    public void setCouncil(int council) {
        this.council = council;
    }

    private int council;
    private boolean isAction=true;
    private int gameState;
    private int coin;
    private Boolean qiangZhuang;
    private List<Integer> cards;
    private boolean isGain;
    private int xiaZhu;


    public void reset() {
        council=0;
        isAction=true;
        gameState=0;
        qiangZhuang=false;
        cards=null;
        isGain=false;
        xiaZhu=0;
    }


    public Boolean getQiangZhuang() {
        return qiangZhuang;
    }

    public int getXiaZhu() {
        return xiaZhu;
    }

    public void setXiaZhu(int xiaZhu) {
        this.xiaZhu = xiaZhu;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    private CardInfo cardInfo;



    public boolean isGain() {
        return isGain;
    }

    public void setGain(boolean gain) {
        isGain = gain;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }


    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }





    public void sendPacket(String method,Object[] message) {
        MessageSend.al.requestNotify(new ManageBufferSession(this.getCtx(), method, message));
    }

    public boolean isActive() {
        return false;
    }

    public void drawCard(byte nextCard) {
    }

    public void setQiangZhuang(Boolean qiangZhuang) {
        this.qiangZhuang = qiangZhuang;
    }


    public Boolean isQiangZhuang() {
        return this.qiangZhuang;
    }


}
