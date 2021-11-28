package com.example.fchess.enums;

public enum eGameData {
    GAME_DATA(0),
    GAME_UNDO(1),
    GAME_PAUSE(2),
    GAME_SURRENDER(3),
    GAME_END(4),
    GAME_SYNC(5);
    public final int label;
    eGameData(int label) {
        this.label = label;
    }
    public int getValue(){
        return this.label;
    }
    public static eGameData fromId(int id) {
        for (eGameData type : values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }
}
