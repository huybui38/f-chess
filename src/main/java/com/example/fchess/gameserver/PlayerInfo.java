package com.example.fchess.gameserver;

public class PlayerInfo {
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    private String userID;
    PlayerInfo(String nickName, String userID){
        this.nickName = nickName;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
