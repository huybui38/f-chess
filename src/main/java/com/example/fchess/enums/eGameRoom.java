package com.example.fchess.enums;

public enum eGameRoom {
    JOIN_ROOM(1);

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

