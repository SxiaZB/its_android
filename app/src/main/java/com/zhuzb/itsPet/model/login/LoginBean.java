package com.zhuzb.itsPet.model.login;

/**
 * @author zhuzb
 * @date on 2018/5/23 0023
 * @email zhuzhibo2017@163.com
 */

public class LoginBean {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName==null||"".equals( userName )){
            userName="未登录";
        }
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password==null||"".equals( password )){
            password = "000000";
        }
        this.password = password;
    }
}
