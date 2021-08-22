package com.example.fchess.gameobjects.engine;

import java.util.Locale;

import static com.example.fchess.gameobjects.engine.EngineConstant.*;

public enum EngineUtils {
    INSTANCE;

    public static int getPieceType(int piece){
        if (BLACK_PAWN == piece || RED_PAWN == piece)
            return PAWN;
        if (BLACK_ROOK == piece || RED_ROOK == piece)
            return ROOK;
        if (BLACK_ADVISOR == piece || RED_ADVISOR == piece)
            return ADVISOR;
        if (BLACK_CANNON== piece || RED_CANNON == piece)
            return CANNON;
        if (BLACK_ELEPHANT == piece || RED_ELEPHANT == piece)
            return ELEPHANT;
        if (BLACK_HORSE == piece || RED_HORSE == piece)
            return HORSE;
        if (BLACK_KING == piece || RED_KING == piece)
            return KING;
        return EMPTY;
    }
    public static int getPieceSide(int piece){
        if (piece >= RED_PAWN && piece <= RED_KING)
            return RED;
        if (piece >= BLACK_PAWN && piece <= BLACK_KING)
            return BLACK;
        return EMPTY;
    }
    public static int getPieceByChar(char c){
        switch (c){
            case 'r':
                return BLACK_ROOK;
            case 'k':
                return BLACK_KING;
            case 'c':
                return BLACK_CANNON;
            case 'n':
                return BLACK_HORSE;
            case 'b':
                return BLACK_ELEPHANT;
            case 'a':
                return BLACK_ADVISOR;
            case 'p':
                return BLACK_PAWN;
            case 'K':
                return RED_KING;
            case 'R':
                return RED_ROOK;
            case 'C':
                return RED_CANNON;
            case 'N':
                return RED_HORSE;
            case 'A':
                return RED_ADVISOR;
            case 'B':
                return RED_ELEPHANT;
            case 'P':
                return RED_PAWN;
        }
        return EMPTY;
    }
    public static boolean isOverBank(int square, int side){
//        int pieceSide = EngineUtils.getPieceSide(board[square]);
        if (side == RED){
            if ((square >= 23 && square <=31) || (square >= 34 && square <= 42) || (square >= 45 && square <=53) || (square >= 56 && square <= 64) || (square >= 67 && square <= 75) ) return true;
        }else {
            if ((square >= 78 && square <= 86) || (square >= 89 && square <= 97) || (square >= 100 && square <= 108) || (square >= 111 && square <= 119) || (square >= 122 && square <= 130) ) return true;
        }
        return false;
    }
    public static int convertPosition(String position){
        if (position.length() != 2)
            return -1;
        position = position.toUpperCase(Locale.ROOT);
        int file = position.charAt(0) - 65;
        int rank = position.charAt(1) - 48;
        if (file < 0 || file > 8)
            return -1;
        if (rank < 0 || rank > 9)
            return -1;
        int basis;
        switch (rank){
            case 0:
                basis = 122;
                break;
            case 1:
                basis = 111;
                break;
            case 2:
                basis = 100;
                break;
            case 3:
                basis = 89;
                break;
            case 4:
                basis = 78;
                break;
            case 5:
                basis = 67;
                break;
            case 6:
                basis = 56;
                break;
            case 7:
                basis = 45;
                break;
            case 8:
                basis = 34;
                break;
            default:
                basis = 23;
                break;
        }
        return basis + file;
    }
    public static boolean isInPlace(int square, int side){
        if (side == RED){
            if ((square >= 125 && square <= 127) || (square >= 114 && square <= 116) || (square >= 103 && square <= 105)){
                return true;
            }
        }else {
            if ((square >= 26 && square <= 28) || (square >= 37 && square <= 39) || (square >= 48 && square <= 50)){
                return true;
            }
        }
        return false;
    }
    public static int encode(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece, int capture){
        return (sourceSquare) |
                (targetSquare << 8) |
                (sourcePiece << 16) |
                (targetPiece << 20) |
                (capture << 24);
    }
    public static  int getSourceSquare(int move){
        return move & 0xFF;
    }
    public static  int getTargetSquare(int move){
        return (move >> 8) & 0xFF;
    }
    public static  int getSourcePiece(int move){
        return (move >> 16) & 0xF;
    }
    public static  int getTargetPiece(int move){
        return (move >> 20) & 0xF;
    }
    public static  int getCaptureFlag(int move){
        return (move >> 24) & 0x1;
    }
}
