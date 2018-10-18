package com.zhuzb.itsPet.model.its;

/**
 * @author zhuzb
 * @date on 2018/5/29 0029
 * @email zhuzhibo2017@163.com
 */

public class McbItem {
    private String pet_grade;
    private String pet_name;
    private String pet_user_img_url;
    private String follow_status;
    private String up_sum;
    private String follow_name;
    private String pet_id;
    private String follow_sum;

    public String getFollow_sum() {
        return follow_sum;
    }

    public void setFollow_sum(String follow_sum) {
        this.follow_sum = follow_sum;
    }

    public String getPet_grade() {
        return pet_grade;
    }

    public void setPet_grade(String pet_grade) {
        this.pet_grade = pet_grade;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_user_img_url() {
        return pet_user_img_url;
    }

    public void setPet_user_img_url(String pet_user_img_url) {
        this.pet_user_img_url = pet_user_img_url;
    }

    public String getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }

    public String getUp_sum() {
        return up_sum;
    }

    public void setUp_sum(String up_sum) {
        this.up_sum = up_sum;
    }

    public String getFollow_name() {
        return follow_name;
    }

    public void setFollow_name(String follow_name) {
        this.follow_name = follow_name;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }
}
