package com.example.fchess.transmodel;

public class GameDataPackage {
    public GameDataPackage(String newPosition) {
        this.newPosition = newPosition;
    }
    public GameDataPackage(){};

    public String getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }

    private String newPosition;
}
