package com.example.fchess.enums;

public enum eChessPackage {
    GAME_ROOM("gameRoom"),
    GAME_DATA("gameData"),
    NOTIFY("notify");
    public final String label;
    eChessPackage(String label) {
        this.label = label;
    }
    public String getValue(){
        return this.label;
    }
}
