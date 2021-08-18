package com.example.fchess.enums;

public enum eTeam {
    RED(1),
    BLACK(0),
    ;

    public final int label;

    eTeam(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }

    public static eTeam fromId(int id) {
        for (eTeam type : values()) {
            if (type.getLabel() == id) {
                return type;
            }
        }
        return null;
    }
}
