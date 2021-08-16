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

    @Override
    public boolean canEndGame() {
        return false;
    }

    @Override
    public void initBoard() {
        currentPosition = INITIAL_POSITION;
    }

    protected XiangqiPiece[][] chessBoard = new XiangqiPiece[11][10];

    protected String currentPosition;//FEN
    public XiangqiBoard(){
        initBoard();
        convertFenToXiangqiBoard(currentPosition);
    }
    public XiangqiBoard(String fen) {
        setCurrentPosition(fen);
        convertFenToXiangqiBoard(currentPosition);
    }

    private boolean checkValidFen(String fen) {
        return true;
    }

    public void addPiece (XiangqiPiece piece, int x, int y) {
        chessBoard[x][y] = piece;
        piece.setCoordinatesX(x);
        piece.setCoordinatesY(y);
        piece.setChessBoard(this);
    }

    private void convertFenToXiangqiBoard(String fen) {
        if (!checkValidFen(fen)) {
            System.out.print("Fen is invalid.");
            return;
        }

        int index = 0, countEmpty = 1;
        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 8; y++) {
                char charPiece = fen.charAt(index);
                if ((charPiece >= 'a' && charPiece <= 'z') || (charPiece >= 'A' && charPiece <= 'Z')) {
                   XiangqiPiece piece = new XiangqiPiece().getPieceByCharFen(charPiece);
                   this.addPiece(piece, 9 - x, y);
                   index++;
                }

                if (charPiece >= '1' && charPiece <= '9') {
                    if (countEmpty == (int) charPiece - 48) {
                        index++;
                        countEmpty = 1;
                    } else countEmpty++;
                }
                if (y == 8) index ++;
            }
        }
    }

    public void showChessBoard() {
        for (int x = 9; x >= 0; x--){
            for (int y = 0; y <= 8; y++){
                if (chessBoard[x][y] != null) {
                    System.out.print(chessBoard[x][y].getLabel() + " ");
                } else System.out.print('.' + " ");
            }
            System.out.println();
        }
    }
}
