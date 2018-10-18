package com.zhuzb.itsPet.model.its;

import java.io.Serializable;

/**
 * @author zhuzb
 * @date on 2018/5/29 0029
 * @email zhuzhibo2017@163.com
 */

public class NoteDataItem implements Serializable {
    private String pet_grade;
    private String pet_user_img_url;
    private double follow_status;
    private double pet_note_id;
    private String pet_name;
    private String user_id;
    private String lastmodify_time;
    private String note_img_url;
    private String note_text;
    private String follow_name;
    private double pet_id;

    public NoteDataItem(NoteListItem noteListItem) {
        this.pet_grade = noteListItem.getPet_grade();
        this.pet_user_img_url = noteListItem.getPet_user_img_url();
        this.follow_status = noteListItem.getFollow_status();
        this.pet_note_id = noteListItem.getPet_note_id();
        this.pet_name = noteListItem.getPet_name();
        this.user_id = noteListItem.getUser_id();
        this.lastmodify_time = noteListItem.getLastmodify_time();
        this.note_img_url = noteListItem.getNote_img_url();
        this.note_text = noteListItem.getNote_text();
        this.follow_name = noteListItem.getFollow_name();
        this.pet_id = noteListItem.getPet_id();
    }

    public String getPet_grade() {
        return pet_grade;
    }

    public void setPet_grade(String pet_grade) {
        this.pet_grade = pet_grade;
    }

    public String getPet_user_img_url() {
        return pet_user_img_url;
    }

    public void setPet_user_img_url(String pet_user_img_url) {
        this.pet_user_img_url = pet_user_img_url;
    }

    public double getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(double follow_status) {
        this.follow_status = follow_status;
    }

    public double getPet_note_id() {
        return pet_note_id;
    }

    public void setPet_note_id(double pet_note_id) {
        this.pet_note_id = pet_note_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLastmodify_time() {
        return lastmodify_time;
    }

    public void setLastmodify_time(String lastmodify_time) {
        this.lastmodify_time = lastmodify_time;
    }

    public String getNote_img_url() {
        return note_img_url;
    }

    public void setNote_img_url(String note_img_url) {
        this.note_img_url = note_img_url;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public String getFollow_name() {
        return follow_name;
    }

    public void setFollow_name(String follow_name) {
        this.follow_name = follow_name;
    }

    public double getPet_id() {
        return pet_id;
    }

    public void setPet_id(double pet_id) {
        this.pet_id = pet_id;
    }
}
