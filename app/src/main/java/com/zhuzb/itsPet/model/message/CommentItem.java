package com.zhuzb.itsPet.model.message;

/**
 * @author zhuzb
 * @date on 2018/5/30 0030
 * @email zhuzhibo2017@163.com
 */

public class CommentItem {
    private String comment_text;
    private String comment_note_map_id;
    private String user_name;
    private String lastmodify_by;
    private String lastmodify_time;
    private String comment_user_id;
    private String creater_by;
    private String pet_note_id;
    private String status;
    private String creater_time;

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_note_map_id() {
        return comment_note_map_id;
    }

    public void setComment_note_map_id(String comment_note_map_id) {
        this.comment_note_map_id = comment_note_map_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLastmodify_by() {
        return lastmodify_by;
    }

    public void setLastmodify_by(String lastmodify_by) {
        this.lastmodify_by = lastmodify_by;
    }

    public String getLastmodify_time() {
        return lastmodify_time;
    }

    public void setLastmodify_time(String lastmodify_time) {
        this.lastmodify_time = lastmodify_time;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getCreater_by() {
        return creater_by;
    }

    public void setCreater_by(String creater_by) {
        this.creater_by = creater_by;
    }

    public String getPet_note_id() {
        return pet_note_id;
    }

    public void setPet_note_id(String pet_note_id) {
        this.pet_note_id = pet_note_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreater_time() {
        return creater_time;
    }

    public void setCreater_time(String creater_time) {
        this.creater_time = creater_time;
    }
}
