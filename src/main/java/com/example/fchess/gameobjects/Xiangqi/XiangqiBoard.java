package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eXiangqiNotion;
import com.example.fchess.gameobjects.AbstractBoard;
import com.example.fchess.gameobjects.AbstractPiece;
import com.example.fchess.interfaces.IBoard;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XiangqiBoard extends AbstractBoard {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor;

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    protected void onStartGame() {

    }

    @Override
    protected void onEndGame() {

    }

    @Override
    protected void onPauseGame() {

    }

    @Override
    protected void onReceiveGameData(String orientation, String newPosition) {

    }

/*
      a b c d e f g h i
    0 R N B A K A B N R
    1 . . . . . . . . .
    2 . C . . . . . C .
    3 P . P . P . P . P
    4 . . . . . . . . .
    5 . . . . . . . . .
    6 p . p . p . p . p
    7 . c . . . . . c .
    8 . . . . . . . . .
    9 r n b a k a b n r
 */
    protected XiangqiPiece[][] chessBoard;
    protected char[][] displayBoard;
    protected char[] coordinates;

    protected String currentPosition;//FEN
    public XiangqiBoard(){
        initBoard();
    }
    public XiangqiBoard(String fen) {
        setCurrentPosition(fen);

    }

    private boolean checkValidFen(String fen) {
        return true;
    }

    public void addPiece (XiangqiPiece piece, int x, int y) {
        chessBoard[x][y] = piece;
    }

    private void convertFenToXiangqiBoard(String fen) {
        if (!checkValidFen(fen)) {
            System.out.print("Fen is invalid.");
            return;
        }

        int index = 0;
        char charFen[] = fen.toCharArray();
        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 8; y++) {
                if (charFen[index] > 'a') {

                }
            }
        }
    }

    @Override
    public boolean canEndGame() {
        return false;
    }

    @Override
    public void initBoard() {
        currentPosition = INITIAL_POSITION;
    }
}
