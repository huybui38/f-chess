package com.example.fchess.enums;

public enum ePieceNotation {
    BLACK_ADVISOR('a'),
    BLACK_CANNON('c'),
    BLACK_ELEPHANT('b'),
    BLACK_HORSE('n'),
    BLACK_KING('k'),
    BLACK_PAWN('p'),
    BLACK_ROOK('r'),
    RED_ADVISOR('A'),
    RED_CANNON('C'),
    RED_ELEPHANT('B'),
    RED_HORSE('N'),
    RED_KING('K'),
    RED_PAWN('P'),
    RED_ROOK('R'),
    ;

    public final char notation;

    ePieceNotation(char notation) {
        this.notation = notation;
    }
    public char getNotation() {return this.notation;}
    public static ePieceNotation fromNotation(char notation) {
        for (ePieceNotation type : values()) {
            if (type.getNotation() == notation) {
                return type;
            }
        }
        return null;
    }
}
