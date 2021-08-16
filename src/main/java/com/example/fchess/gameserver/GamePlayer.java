package com.example.fchess.gameserver;

public class GamePlayer {

    public GamePlayer() {
        this.isViewer = true;
        this.team = -1;
    }
    private boolean isViewer;

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    private int team;
    public boolean isViewer() {
        return isViewer;
    }
    public void setViewer(boolean viewer) {
        isViewer = viewer;
    }
}
