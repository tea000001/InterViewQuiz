package com.test_project.interview_quiz.Bean;

import android.graphics.Bitmap;

public class InputBean {

    //region  //用於第一頁的recycleview
    String login = "";
    String txt_avatar = "";
    Boolean favor_status = false;

    public String getlogin() {
        return login;
    }
    public void setlogin(String login) {
        this.login = login;
    }

    public String gettxt_avatar() {
        return txt_avatar;
    }
    public void settxt_avatar(String txt_avatar) {
        this.txt_avatar = txt_avatar;
    }

    public Boolean getfavor_status(){ return favor_status;}
    public void setfavor_status(Boolean favor_status) {this.favor_status = favor_status;}
    //endregion


    //region //用於第三頁的following recycleview
    String loging_following = "";
    String txt_avatar_following = "";
    Boolean double_follow = false;

    public String getloging_following() {
        return loging_following;
    }
    public void setloging_following(String loging_following) {
        this.loging_following = loging_following;
    }

    public String gettxt_avatar_following() {
        return txt_avatar_following;
    }
    public void settxt_avatar_following(String txt_avatar_following) {
        this.txt_avatar_following = txt_avatar_following;
    }

    public Boolean getdouble_follow(){
        return double_follow;
    }
    public void setdouble_follow(Boolean double_follow){
        this.double_follow = double_follow;
    }
    //endregion

    //region //用於第三頁的foller recycleview
    String loging_follower = "";
    String txt_avatar_follower = "";

    public String getloging_follower() {
        return loging_follower;
    }
    public void setloging_follower(String loging_follower) {
        this.loging_follower = loging_follower;
    }

    public String gettxt_avatar_follower() {
        return txt_avatar_follower;
    }
    public void settxt_avatar_follower(String txt_avatar_follower) {
        this.txt_avatar_follower = txt_avatar_follower;
    }
    //endregion

}
