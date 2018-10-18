package com.zhuzb.itsPet.model.login;

import java.io.Serializable;

/**
 * @author zhuzb
 * @date on 2018/5/24 0024
 * @email zhuzhibo2017@163.com
 */

public class UserDataBean implements Serializable {
    private String userId;
    private String userName;
    private String userImg;
    private String userTx;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserTx() {
        return userTx;
    }

    public void setUserTx(String userTx) {
        if (userTx==null||"".equals( userTx )||"null".equals( userTx )){
            userTx="无话可说";
        }
        this.userTx = userTx;
    }
}
