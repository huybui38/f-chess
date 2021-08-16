package com.example.fchess.gameserver;

public class PlayerInfo {
    private String userID;

    public String getUserID() {
        return userID;
    }

    private String fullName;
    PlayerInfo(String userID){
        this.userID = userID;
    }
}
