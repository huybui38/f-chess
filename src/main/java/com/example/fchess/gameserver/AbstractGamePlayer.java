package com.example.fchess.gameserver;

public abstract class AbstractGamePlayer {
    public  abstract void onWinning();
    public  abstract void onLosing();
    protected boolean isViewer;
    protected boolean isWin;
    protected int team;
    protected String userID;
    public boolean isWin() {
        return isWin;
    }
    public void setWin(boolean win) {
        isWin = win;
    }
    public int getTeam() {
        return team;
    }
    public void setTeam(int team) {
        this.team = team;
    }
    public boolean isViewer() {
        return isViewer;
    }
    public void setViewer(boolean viewer) {
        isViewer = viewer;
    }

    AbstractGamePlayer(String userID){
        this.userID = userID;
        this.isViewer = true;
        this.team = -1;
        this.isWin = false;
    }
}
