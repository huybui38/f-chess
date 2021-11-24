package com.example.fchess.gameobjects.engine;

public class EngineConstant {
    public static final int OUT = -1;
    public static final int EMPTY = 0;
    public static final int RED_PAWN = 1;
    public static final int RED_ADVISOR = 2;
    public static final int RED_ELEPHANT  = 3;
    public static final int RED_HORSE  = 4;
    public static final int RED_CANNON  = 5;
    public static final int RED_ROOK = 6;
    public static final int RED_KING = 7;
    public static final int BLACK_PAWN = 8;
    public static final int BLACK_ADVISOR = 9;
    public static final int BLACK_ELEPHANT  = 10;
    public static final int BLACK_HORSE  = 11;
    public static final int BLACK_CANNON  = 12;
    public static final int BLACK_ROOK = 13;
    public static final int BLACK_KING = 14;

    public static final int PAWN = 15;
    public static final int ADVISOR = 16;
    public static final int ELEPHANT = 17;
    public static final int HORSE = 18;
    public static final int CANNON = 19;
    public static final int ROOK = 20;
    public static final int KING = 21;


    public static final int BLACK = 0;
    public static final int RED = 1;

    /*
        DIRECTION
     */
    public static final int UP = 11;
    public static final int DOWN = -11;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;

    public static final int[] ORTHOGONALS = new int[]{
            UP, DOWN, LEFT, RIGHT
    };
    public static final int[] DIAGONALS = new int[]{
            UP + LEFT, UP + RIGHT, DOWN + LEFT, DOWN + RIGHT
    };

    /*

     */
    public static final int[][] PAWN_ATTACK_OFFSETS = new int[][] {
            { DOWN, LEFT, RIGHT},
            { UP, LEFT, RIGHT }

    };

    public static final int[][] HORSE_ATTACK_OFFSETS = new int[][] {
            { UP + UP + LEFT,  LEFT + LEFT + UP},
            { UP + UP + RIGHT, RIGHT + RIGHT + UP},
            { LEFT + LEFT + DOWN,  DOWN + DOWN + LEFT},
            { RIGHT + RIGHT + DOWN, DOWN + DOWN + RIGHT}
    };
    /*

     */
    public static final int[][] PAWN_MOVE_OFFSETS = new int[][] {
            { UP, LEFT, RIGHT },
            { DOWN, LEFT, RIGHT}
    };
    public static final int[] ELEPHANT_MOVE_OFFSETS = new int[] {
            (UP + LEFT) * 2, (UP + RIGHT) *2, (DOWN + LEFT) *2, (DOWN + RIGHT) *2
    };
    public static final int[][] HORSE_MOVE_OFFSETS = new int[][]{
            { UP * 2 + LEFT, UP *2 + RIGHT},
            { DOWN * 2 + LEFT, DOWN *2 + RIGHT },
            { LEFT *2 + UP, LEFT*2 + DOWN},
            { RIGHT *2 + UP, RIGHT*2 + DOWN},
    };
}
