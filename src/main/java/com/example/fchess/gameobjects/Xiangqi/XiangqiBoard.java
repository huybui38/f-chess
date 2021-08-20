package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;
import com.example.fchess.enums.eXiangqiNotion;
import com.example.fchess.gameobjects.AbstractBoard;
import com.example.fchess.gameobjects.AbstractPiece;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XiangqiBoard extends AbstractBoard {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    private final Logger log = LoggerFactory.getLogger(XiangqiBoard.class);
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor;
    protected String currentPosition;//FEN
    public int[] kingPositionX = new int[2];
    public int[] kingPositionY = new int[2];
    protected char[][] chessBoard = new char[11][10];
    public ArrayList<Integer> moveList = new ArrayList<>(100);


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

    public int getCoordinateX(String coordinate) {
        return coordinate.charAt(1) - 48;
    }

    public int getCoordinateY(String coordinate) {
        return coordinate.charAt(0) - 97;
    }

    public String getSquareFromCoordinate(int x, int y) {
        char[] coordinateChar = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
        String result = String.valueOf(coordinateChar[y]) + String.valueOf(x);
        return result;
    }

    public boolean isAttacked(int x, int y, eTeam team, char[][] chessBoard) {
        System.out.println(team);

        XiangqiPiece pawn = processor.get(eXiangqiNotion.PAwN);
        if  (pawn.isCapture(x, y, team, chessBoard)) {
            System.out.println("PAWN attacked");
            return true;
        }


        XiangqiPiece horse = processor.get(eXiangqiNotion.HORSE);
        if (horse.isCapture(x, y, team, chessBoard)) {
            System.out.println("HORSE attacked");
            return true;
        }

        XiangqiPiece rook = processor.get(eXiangqiNotion.ROOK);
        if (rook.isCapture(x, y, team, chessBoard)) {
            System.out.println("ROOK attacked");
            return true;
        }

        XiangqiPiece cannon = processor.get(eXiangqiNotion.CANNON);
        if (cannon.isCapture(x, y, team, chessBoard)) {
            System.out.println("CANNON attacked");
            return true;
        }

        XiangqiPiece king = processor.get(eXiangqiNotion.KING);
        if (king.isCapture(x, y, team, chessBoard)) {
            System.out.println("KING attacked");
            return true;
        }

        System.out.println("False");
        return false;
    }

    public int encodeMove(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece, int captureFlag) {
        return (sourceSquare) |
                (targetSquare << 8) |
                (sourcePiece << 16) |
                (targetPiece << 20) |
                (captureFlag << 24);
    }

    public String getSourceSquare(int move) {
        int square = move & 0xFF;
        return getSquareFromCoordinate(square / 9, square % 9);
    }
    public String getTargetSquare(int move) {
        int square = (move >> 8) & 0xFF;
        return getSquareFromCoordinate(square / 9, square % 9);
    }
    public ePieceNotation getSourcePiece(int move) {
        int piece = (move >> 16) & 0xF;
        return ePieceNotation.fromId(piece);
    }
    public ePieceNotation getTargetPiece(int move) {
        int piece = (move >> 20) & 0xF;
        return ePieceNotation.fromId(piece);
    }
    public int getCaptureFlag(int move) {
        return (move >> 24) & 0x1;
    }

    public void pushMove(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece) {
        int move = 0;
        if (targetPiece == ePieceNotation.EMPTY.getId()) {
            move = encodeMove(sourceSquare, targetSquare, sourcePiece, targetPiece, 0);
        } else move = encodeMove(sourceSquare, targetSquare, sourcePiece, targetPiece, 1);

        this.moveList.add(move);
    }

    public boolean makeMove(String source, String destination) {
        int fromX = getCoordinateX(source);
        int fromY = getCoordinateY(source);
        int toX = getCoordinateX(destination);
        int toY = getCoordinateY(destination);
        char pieceFrom = chessBoard[fromX][fromY];
        char pieceTo = chessBoard[toX][toY];
        log.debug("Coordinate: x1={},y1={}  ;  x2={},y2={}", fromX, fromY, toX, toY);

        eXiangqiNotion pieceEnum = eXiangqiNotion.fromNotation(chessBoard[fromX][fromY]);
        XiangqiPiece pieceProcessor = processor.get(pieceEnum);
        int team = pieceProcessor.getTeam(chessBoard[fromX][fromY]).getLabel();

        if (turn != team) {
            log.debug("Not turn");
            return false;
        }

        boolean validateMove = pieceProcessor.validateMove(fromX, fromY, toX, toY, chessBoard);
        if (!validateMove) {
            log.debug("Invalid move");
            return false;
        }

        //move
        addPiece(chessBoard[fromX][fromY], toX, toY);
        removePiece(fromX, fromY);

        System.out.println("Team: " + eTeam.fromId(team) + " => " + kingPositionX[team] + "  " + kingPositionY[team]);
        if (this.isAttacked(kingPositionX[team], kingPositionY[team], eTeam.fromId(team), chessBoard)) {
            System.out.println("King is attacked");
            chessBoard[fromX][fromY] = pieceFrom;
            chessBoard[toX][toY] = pieceTo;
            return false;
        }

        int sourceSquare = fromX * 9 + fromY;
        int targetSquare = toX * 9 + toY;
        int sourcePiece = ePieceNotation.fromNotation(pieceFrom).getId();
        int targetPiece = ePieceNotation.fromNotation(pieceTo).getId();
        System.out.println(pieceTo + " " + targetPiece);
        pushMove(sourceSquare, targetSquare, sourcePiece, targetPiece);

        if (pieceEnum == eXiangqiNotion.KING) {
            kingPositionX[team] = toX;
            kingPositionY[team] = toY;
        }

        turn = 1 - turn;
        currentPosition = convertBoardToFen(chessBoard);
        return true;
    }

    public void takeBack() {
        int moveIndex = moveList.size() - 1;
        int move = moveList.get(moveIndex);

        String sourceSquare = getSourceSquare(move);
        String targetSquare = getTargetSquare(move);
        ePieceNotation sourcePiece = getSourcePiece(move);
        ePieceNotation targetPiece = getTargetPiece(move);

        chessBoard[getCoordinateX(sourceSquare)][getCoordinateY(sourceSquare)] = sourcePiece.getNotation();
        chessBoard[getCoordinateX(targetSquare)][getCoordinateY(targetSquare)] = targetPiece.getNotation();

        eXiangqiNotion pieceEnum = eXiangqiNotion.fromNotation(sourcePiece.getNotation());
        XiangqiPiece pieceProcessor = processor.get(pieceEnum);
        int team = pieceProcessor.getTeam(sourcePiece.getNotation()).getLabel();

        if (pieceEnum == eXiangqiNotion.KING) {
            kingPositionX[team] = getCoordinateX(sourceSquare);
            kingPositionY[team] = getCoordinateY(sourceSquare);
        }

        turn = team;
        moveList.remove(moveIndex);
    }



    @Override
    public boolean onReceiveGameData(String source, String destination) {
        /*
        if this moving is valid then toggle turn
         */
        if (source.length() != 2) {
            return false;
        }
        log.debug("From: {} , To: {}", source, destination);
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
        this.moveList.clear();
        this.processor = new HashMap<>();
        this.processor.put(eXiangqiNotion.HORSE, new Horse());
        this.processor.put(eXiangqiNotion.ELEPHANT, new Elephant());
        this.processor.put(eXiangqiNotion.ADVISOR, new Advisor());
        this.processor.put(eXiangqiNotion.PAwN, new Pawn());
        this.processor.put(eXiangqiNotion.KING, new King());
        this.processor.put(eXiangqiNotion.CANNON, new Cannon());
        this.processor.put(eXiangqiNotion.ROOK, new Rook());
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


//    public int Perft(int depth){
//        int nodes = 0;
//        if (depth == 0) return 1;
//        ArrayList<Integer> moves = generateMovesV1();
//        for (int i = 0; i < moves.size(); i++) {
//            boolean isValid = move(moves.get(i));
//            if (isValid){
//                nodes += Perft(depth -1);
//                undo();
//            }
//        }
//        return nodes;
//    }

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

                if (fenChar == ePieceNotation.RED_KING.getNotation()) {
                    System.out.println("RED: " + eTeam.RED.getLabel());
                    kingPositionX[eTeam.RED.getLabel()] = 9 - x;
                    kingPositionY[eTeam.RED.getLabel()] = y;
                }

                if (fenChar == ePieceNotation.BLACK_KING.getNotation()) {
                    System.out.println("BLACK: " + eTeam.BLACK.getLabel());
                    kingPositionX[eTeam.BLACK.getLabel()] = 9 - x;
                    kingPositionY[eTeam.BLACK.getLabel()] = y;
                }

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

    private String convertBoardToFen(char[][] board) {
        String result = "";
        for (int x = 9; x >= 0; x--) {
            int count = 0;
            for (int y = 0; y <= 8; y++) {
                if (board[x][y] == '.') {
                    count++;
                } else {
                    if (count != 0) result += count;
                    result += board[x][y];
                    count = 0;
                }
            }
            if (count != 0) result += count;
            if (x != 0) result += '/';
        }

        return result;
    }

    public void showChessBoard() {
        for (int x = 9; x >= 0; x--) {
            for (int y = 0; y <= 8; y++) {
                System.out.print(chessBoard[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println(convertBoardToFen(chessBoard));
    }

    @Override
    public boolean canEndGame() {
        return false;
    }

    @Override
    public void initBoard() {
        currentPosition = INITIAL_POSITION;
    }

    public ArrayList<Integer> generateMovesV2() {
        this.moveList.clear();

        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 8; y++) {
                if (chessBoard[x][y] != '.') {
                    eXiangqiNotion pieceEnum = eXiangqiNotion.fromNotation(chessBoard[x][y]);
                    XiangqiPiece pieceProcessor = processor.get(pieceEnum);
                    int team = pieceProcessor.getTeam(chessBoard[x][y]).getLabel();
                }
            }
        }

        return (ArrayList<Integer>) moveList.clone();
    }
}
