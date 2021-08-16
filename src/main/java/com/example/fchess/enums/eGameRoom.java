package com.example.fchess.enums;

public enum eGameRoom {
    JOIN_ROOM(1),
    CREATE_ROOM(2),
    CHAT(3),
    SELECT_TEAM(4),
    ROOM_INFO (5),
    START_GAME (6),
    RESET_ROOM(7),
    EXIT_ROOM(8);

    public final int label;
    eGameRoom(int label) {
        this.label = label;
    }
    public int getValue(){
         return this.label;
    }
    public static eGameRoom fromId(int id) {
        for (eGameRoom type : values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }
}

