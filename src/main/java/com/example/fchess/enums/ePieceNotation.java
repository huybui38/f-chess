package com.example.fchess.enums;

public enum ePieceNotation {
    EMPTY('.', 0),
    BLACK_ADVISOR('a', 1),
    BLACK_CANNON('c', 2),
    BLACK_ELEPHANT('b', 3),
    BLACK_HORSE('n', 4),
    BLACK_KING('k', 5),
    BLACK_PAWN('p', 6),
    BLACK_ROOK('r', 7),
    RED_ADVISOR('A', 8),
    RED_CANNON('C', 9),
    RED_ELEPHANT('B', 10),
    RED_HORSE('N', 11),
    RED_KING('K', 12),
    RED_PAWN('P', 13),
    RED_ROOK('R', 14),
    ;

    public final char notation;
    public final int id;

    ePieceNotation(char notation, int id) {
        this.notation = notation;
        this.id = id;
    }

    public char getNotation() {return this.notation;}
    public int getId() {return this.id;}
    public static ePieceNotation fromNotation(char notation) {
        for (ePieceNotation type : values()) {
            if (type.getNotation() == notation) {
                return type;
            }
        }
        return null;
    }
    public static ePieceNotation fromId(int id) {
        for (ePieceNotation type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
