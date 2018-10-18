package com.zhuzb.itsPet.config;

/**
 * @author zhuzb
 * @date on 2018/5/19 0019
 * @email zhuzhibo2017@163.com
 */

public class CONSTS {

    public static final String HOST = "http://39.107.117.122/its_server";
//    public static final String HOST = "http://anyongda.51vip.biz/its_server";

    public static final String LIST_CW_USER_URL = HOST + "/pet/listPetUser";//某用户宠物列表

    public static final String RM_DATA_URL = HOST + "/petNote/listRMpetNote";//热门列表数据地址
    public static final String GZ_DATA_URL = HOST + "/petNote/listGZpetNote";//关注列表数据地址
    public static final String MCB_DATA_URL = HOST + "/mcb/listMcb";//名宠榜列表

    public static final String ADD_PET_NOTE_URL = HOST + "/petNote/addPetNote";//添加宠物记录

    public static final String MY_DATA_URL = HOST + "/petNote/listMYpetNote";//我的宠物列表
    public static final String ADD_PET_URL = HOST + "/pet/addPet";//添加宠物
    public static final String UPDATA_PET_URL = HOST + "/pet/updataPet";//添加宠物

    public static final String ADD_FOLLOW_URL = HOST + "/user/addFollow";//添加关注
    public static final String DEL_FOLLOW_URL = HOST + "/user/delFollow";//取消关注
    public static final String ADD_UP_URL = HOST + "/user/addUpNote";//添加赞
    public static final String ADD_COMMENT_URL = HOST + "/user/addComment";//添加评论
    public static final String ADD_LOVENOTE_URL = HOST + "/user/addLoveNote";//添加收藏
    public static final String DEL_LOVENOTE_URL = HOST + "/user/delLoveNote";//取消收藏

    public static final String COMMENT_USER_NOTE_URL =HOST+ "/petNote/onePetNote";//消息页进记录

    public static final String COMMENT_NOTE_URL = HOST +"/petNote/listCommentNote";//记录评论列表
    public static final String COMMENT_USER_URL = HOST +"/user/listCommentUser";//关于我的评论列表
    public static final String LOVENOTE_USER_URL = HOST +"/user/listLoveUser";//收藏列表
    public static final String FOLLOW_USER_URL = HOST +"/user/listFollowUser";//关注宠物列表

}
