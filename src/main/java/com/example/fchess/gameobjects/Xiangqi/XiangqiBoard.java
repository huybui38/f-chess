package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eXiangqiNotion;
import com.example.fchess.gameobjects.AbstractBoard;
import com.example.fchess.gameobjects.AbstractPiece;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XiangqiBoard extends AbstractBoard {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor;
    private final  Logger log = LoggerFactory.getLogger(XiangqiBoard.class);

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
    public void onReceiveGameData(String newPosition) {
        /*
        if this moving is valid then toggle turn
         */
        currentPosition = newPosition;
        turn = 1 - turn;
        log.debug(newPosition);
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
    public XiangqiBoard(String fen, GameClient red, GameClient black) {
        this(red, black);
        setCurrentPosition(fen);
    }
    public XiangqiBoard(GameClient red, GameClient black){
        this.red = red;
        this.black =  black;
        initBoard();
    }

    private boolean checkValidFen(String fen) {
        return true;
    }

    public void addPiece(XiangqiPiece piece, int x, int y) {
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
