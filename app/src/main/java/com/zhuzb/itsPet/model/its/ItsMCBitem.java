package com.zhuzb.itsPet.model.its;

/**
 * @author zhuzb
 * @date on 2018/4/26 0026
 * @email zhuzhibo2017@163.com
 */

public class ItsMCBitem {
    private String  petUserImg;//宠物头像
    private String petUserName;//宠物名
    private String PetUserTitle;//宠物等级
    public ItsMCBitem(){}

    public String getPetUserImg() {
        return petUserImg;
    }

    public void setPetUserImg(String petUserImg) {
        this.petUserImg = petUserImg;
    }

    public String getPetUserName() {
        return petUserName;
    }

    public void setPetUserName(String petUserName) {
        this.petUserName = petUserName;
    }

    public String getPetUserTitle() {
        return PetUserTitle;
    }

    public void setPetUserTitle(String petUserTitle) {
        PetUserTitle = petUserTitle;
    }
}
