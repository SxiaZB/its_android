package com.zhuzb.itsPet.model.pet;

/**
 * @author zhuzb
 * @date on 2018/5/22 0022
 * @email zhuzhibo2017@163.com
 */

public class PetItem {
    private double pet_id;
    private String user_id;
    private String pet_code;
    private String pet_name;
    private String pet_user_img_url;
    private String pet_breed;
    private String pet_grade;
    private String pet_year;
    private String pet_month;
    private String pet_day;
    private String pet_bz;
    private String pet_sex;
    private long pet_value;
    private String follow_name;
    private double status;//状态
    private String creation_date; //创建时间
    private String creation_by; //创建人
    private String lastmodified_date; //最后修改时间
    private String lastmodified_by; //修改人

    public String getPet_year() {
        return pet_year;
    }

    public void setPet_year(String pet_year) {
        this.pet_year = pet_year;
    }

    public String getPet_month() {
        return pet_month;
    }

    public void setPet_month(String pet_month) {
        this.pet_month = pet_month;
    }

    public String getPet_day() {
        return pet_day;
    }

    public void setPet_day(String pet_day) {
        this.pet_day = pet_day;
    }

    public String getPet_bz() {
        return pet_bz;
    }

    public void setPet_bz(String pet_bz) {
        this.pet_bz = pet_bz;
    }

    public String getPet_sex() {
        return pet_sex;
    }

    public void setPet_sex(String pet_sex) {
        this.pet_sex = pet_sex;
    }

    public double getPet_id() {
        return pet_id;
    }

    public void setPet_id(double pet_id) {
        this.pet_id = pet_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPet_code() {
        return pet_code;
    }

    public void setPet_code(String pet_code) {
        this.pet_code = pet_code;
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

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_grade() {
        return pet_grade;
    }

    public void setPet_grade(String pet_grade) {
        this.pet_grade = pet_grade;
    }

    public long getPet_value() {
        return pet_value;
    }

    public void setPet_value(long pet_value) {
        this.pet_value = pet_value;
    }

    public String getFollow_name() {
        return follow_name;
    }

    public void setFollow_name(String follow_name) {
        this.follow_name = follow_name;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getLastmodified_date() {
        return lastmodified_date;
    }

    public void setLastmodified_date(String lastmodified_date) {
        this.lastmodified_date = lastmodified_date;
    }

    public String getCreation_by() {
        return creation_by;
    }

    public void setCreation_by(String creation_by) {
        this.creation_by = creation_by;
    }

    public String getLastmodified_by() {
        return lastmodified_by;
    }

    public void setLastmodified_by(String lastmodified_by) {
        this.lastmodified_by = lastmodified_by;
    }
}
