package com.zhuzb.itsPet.model.message;

/**
 * @author zhuzb
 * @date on 2018/5/13 0013
 * @email zhuzhibo2017@163.com
 */

public class MessageTVBitem {
    private int messageTVBplId;//私信id
    private int messageUserIdFrom;//私信者id
    private int messageUserIdTo;//被私信id
    private String messageUserFromImg;//私信者头像
    private String messageUserFromName;//私信者用户名
    private String messageTVBtime;//私信时间
    private String messageTextSX;//私信内容

    public int getMessageTVBplId() {
        return messageTVBplId;
    }

    public void setMessageTVBplId(int messageTVBplId) {
        this.messageTVBplId = messageTVBplId;
    }

    public int getMessageUserIdFrom() {
        return messageUserIdFrom;
    }

    public void setMessageUserIdFrom(int messageUserIdFrom) {
        this.messageUserIdFrom = messageUserIdFrom;
    }

    public int getMessageUserIdTo() {
        return messageUserIdTo;
    }

    public void setMessageUserIdTo(int messageUserIdTo) {
        this.messageUserIdTo = messageUserIdTo;
    }

    public String getMessageUserFromImg() {
        return messageUserFromImg;
    }

    public void setMessageUserFromImg(String messageUserFromImg) {
        this.messageUserFromImg = messageUserFromImg;
    }

    public String getMessageUserFromName() {
        return messageUserFromName;
    }

    public void setMessageUserFromName(String messageUserFromName) {
        this.messageUserFromName = messageUserFromName;
    }

    public String getMessageTVBtime() {
        return messageTVBtime;
    }

    public void setMessageTVBtime(String messageTVBtime) {
        this.messageTVBtime = messageTVBtime;
    }

    public String getMessageTextSX() {
        return messageTextSX;
    }

    public void setMessageTextSX(String messageTextSX) {
        this.messageTextSX = messageTextSX;
    }
}
