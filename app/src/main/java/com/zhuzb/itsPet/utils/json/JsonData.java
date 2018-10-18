package com.zhuzb.itsPet.utils.json;

import java.util.List;

/**
 * @author zhuzb
 * @date on 2018/5/20 0020
 * @email zhuzhibo2017@163.com
 */

public class JsonData<T> {
    private int errorCode;//错误码
    private List<T> data;//结果集
    private String errorMessage;//错误提示

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
