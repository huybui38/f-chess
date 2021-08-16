package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
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
    private final Logger log = LoggerFactory.getLogger(XiangqiBoard.class);
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor;
    protected String currentPosition;//FEN
    protected char[][] chessBoard = new char[11][10];

    public HashMap<eXiangqiNotion, XiangqiPiece> getProcessor() {
        return processor;
    }

    public void setProcessor(HashMap<eXiangqiNotion, XiangqiPiece> processor) {
        this.processor = processor;
    }

    public char[][] getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(char[][] chessBoard) {
        this.chessBoard = chessBoard;
    }

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
    public boolean onReceiveGameData(String source, String destination) {
        /*
        if this moving is valid then toggle turn
         */
        if (source.length() != 2) {
            return false;
        }
//        if (turn == 1 && board[source].matches("^r")){
//
//        }
        log.debug("Huy Ngu {} {}", source, destination);
        turn = 1 - turn;
        return true;
    }

    public XiangqiBoard(String fen, GameClient red, GameClient black) {
        this(red, black);
        setCurrentPosition(fen);
        convertFenToXiangqiBoard(currentPosition);
    }

    public XiangqiBoard(GameClient red, GameClient black) {
        this.red = red;
        this.black = black;
        initBoard();
        convertFenToXiangqiBoard(currentPosition);
    }

    private boolean checkValidFen(String fen) {
        return true;
    }

    public void addPiece(char piece, int x, int y) {
        chessBoard[x][y] = piece;
    }

    public void removePiece(int x, int y) {
        chessBoard[x][y] = '.';
    }

    private void convertFenToXiangqiBoard(String fen) {
        if (!checkValidFen(fen)) {
            System.out.print("Fen is invalid.");
            return;
        }

        int index = 0, countEmpty = 1;
        char charFen[] = fen.toCharArray();

        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 8; y++) {
                char fenChar = fen.charAt(index);
                if ((fenChar >= 'a' && fenChar <= 'z') || (fenChar >= 'A' && fenChar <= 'Z')) {
                    chessBoard[9 - x][y] = fenChar;
                    index++;
                } else chessBoard[9 - x][y] = '.';

                if (fenChar >= '1' && fenChar <= '9') {
                    if (countEmpty == (int) fenChar - 48) {
                        index++;
                        countEmpty = 1;
                    } else countEmpty++;
                }
                if (y == 8) index++;
            }
        }
    }

    public void showChessBoard() {
        for (int x = 9; x >= 0; x--) {
            for (int y = 0; y <= 8; y++) {
                System.out.print(chessBoard[x][y] + " ");
            }
            System.out.println();
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
