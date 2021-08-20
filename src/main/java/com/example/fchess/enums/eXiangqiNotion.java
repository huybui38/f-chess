package com.example.fchess.enums;

public enum eXiangqiNotion {
    ADVISOR('a'),
    CANNON('c'),
    ELEPHANT('b'),
    HORSE('n'),
    KING('k'),
    PAwN('p'),
    ROOK('r'),
    ;


    public final char notation;
    eXiangqiNotion(char notation) {
        this.notation = notation;
    }

    public char getNotation() {return  this.notation;}
    public static eXiangqiNotion fromNotation(char notation) {
        char formatNotation = Character.toLowerCase(notation);
        for (eXiangqiNotion type : values()) {
            if (type.getNotation() == formatNotation) {
                return type;
            }
        }
        return null;
    }
}
