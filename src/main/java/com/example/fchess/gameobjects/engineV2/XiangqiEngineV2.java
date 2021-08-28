package com.example.fchess.gameobjects.engineV2;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;
import com.example.fchess.enums.eXiangqiNotion;
import com.example.fchess.gameobjects.Xiangqi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class XiangqiEngineV2 {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    private final Logger log = LoggerFactory.getLogger(XiangqiBoard.class);
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor = new PiecesProcessor().getProcessor();
    protected String currentPosition;//FEN
    public int[] kingPositionX = new int[2];
    public int[] kingPositionY = new int[2];
    protected char[][] chessBoard = new char[11][10];
    public ArrayList<Integer> moveList = new ArrayList<>();
    private int turn;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
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
        XiangqiPiece pawn = processor.get(eXiangqiNotion.PAwN);
        if (pawn.isCapture(x, y, team, chessBoard)) {
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

    public void pushMove(ArrayList moveList, int sourceSquare, int targetSquare, int sourcePiece, int targetPiece) {
        int move = 0;
        if (targetPiece == ePieceNotation.EMPTY.getId()) {
            move = encodeMove(sourceSquare, targetSquare, sourcePiece, targetPiece, 0);
        } else move = encodeMove(sourceSquare, targetSquare, sourcePiece, targetPiece, 1);

        moveList.add(move);
    }

    public boolean makeMove(String source, String destination) {
        int fromX = getCoordinateX(source);
        int fromY = getCoordinateY(source);
        int toX = getCoordinateX(destination);
        int toY = getCoordinateY(destination);
        char sourcePiece = chessBoard[fromX][fromY];
        char targetPiece = chessBoard[toX][toY];
        return makeMove(source, destination, sourcePiece, targetPiece);
    }

    public boolean makeMove(int move) {
        String source = getSourceSquare(move);
        String destination = getTargetSquare(move);
        char sourcePiece = getSourcePiece(move).getNotation();
        char targetPiece = getTargetPiece(move).getNotation();
        return makeMove(source, destination, sourcePiece, targetPiece);
    }

    public boolean makeMove(String source, String destination, char sourcePiece, char targetPiece) {
        int fromX = getCoordinateX(source);
        int fromY = getCoordinateY(source);
        int toX = getCoordinateX(destination);
        int toY = getCoordinateY(destination);

        eXiangqiNotion pieceEnum = eXiangqiNotion.fromNotation(sourcePiece);
        XiangqiPiece pieceProcessor = processor.get(pieceEnum);
        int team = pieceProcessor.getTeam(sourcePiece).getLabel();

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
        if (pieceEnum == eXiangqiNotion.KING) {
            kingPositionX[team] = toX;
            kingPositionY[team] = toY;
        }


//        System.out.println(getSquareFromCoordinate(fromX, fromY) + " => " + getSquareFromCoordinate(toX, toY));
//        System.out.println(ePieceNotation.fromNotation(sourcePiece) + " => " + ePieceNotation.fromNotation(targetPiece));
//        showChessBoard();
//        System.out.println("Team: " + eTeam.fromId(team) + " => " + kingPositionX[team] + "  " + kingPositionY[team]);

        if (this.isAttacked(kingPositionX[team], kingPositionY[team], eTeam.fromId(team), chessBoard)) {
            chessBoard[fromX][fromY] = sourcePiece;
            chessBoard[toX][toY] = targetPiece;
            if (pieceEnum == eXiangqiNotion.KING) {
                kingPositionX[team] = fromX;
                kingPositionY[team] = fromY;
            }
            return false;
        }

        int sourceSquare = fromX * 9 + fromY;
        int targetSquare = toX * 9 + toY;
        int sourcePieceID = ePieceNotation.getIdFromNotation(sourcePiece);
        int targetPieceID = ePieceNotation.getIdFromNotation(targetPiece);
        pushMove(this.moveList, sourceSquare, targetSquare, sourcePieceID, targetPieceID);

        turn = 1 - turn;
        return true;
    }

    public void takeBack() {
        if (moveList.size() == 0)
            return;

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

        this.turn = team;
        moveList.remove(moveIndex);
    }

    public XiangqiEngineV2() {
        this.turn = eTeam.RED.getLabel();
        initBoard();
        convertFenToXiangqiBoard(currentPosition);
        this.moveList.clear();
    }

    public XiangqiEngineV2(String fen) {
        this.turn = eTeam.RED.getLabel();
        setCurrentPosition(fen);
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

                if (fenChar == ePieceNotation.RED_KING.getNotation()) {
                    kingPositionX[eTeam.RED.getLabel()] = 9 - x;
                    kingPositionY[eTeam.RED.getLabel()] = y;
                }

                if (fenChar == ePieceNotation.BLACK_KING.getNotation()) {
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

    public String convertBoardToFen(char[][] board) {
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
    }

    public void checkToPushMove(ArrayList moveList,  int fromX, int fromY, int toX, int toY, XiangqiPiece pieceProcessor) {
        if (pieceProcessor.isOnChessBoard(fromX, fromY) && pieceProcessor.isOnChessBoard(toX, toY)) {
            char pieceSourceChar = chessBoard[fromX][fromY];
            char pieceTargetChar = chessBoard[toX][toY];
            int sourcePiece = ePieceNotation.getIdFromNotation(pieceSourceChar);
            int targetPiece = ePieceNotation.getIdFromNotation(pieceTargetChar);
            pushMove(moveList, fromX * 9 + fromY, toX * 9 + toY, sourcePiece, targetPiece);
        }
    }

    public ArrayList<Integer> generateMovesV2() {
        ArrayList<Integer> moveList = new ArrayList<>();
        for (int x = 0; x <= 9; x++) {
            for (int y = 0; y <= 8; y++) {
                if (chessBoard[x][y] != '.') {
                    eXiangqiNotion pieceEnum = eXiangqiNotion.fromNotation(chessBoard[x][y]);
                    XiangqiPiece pieceProcessor = processor.get(pieceEnum);
                    int team = pieceProcessor.getTeam(chessBoard[x][y]).getLabel();

                    int toX, toY;
                    if (team == turn) {
                        if (pieceEnum == eXiangqiNotion.PAwN) {
                            for (int i = 0; i < 3; i++) {
                                toX = x + Pawn.dx[team][i];
                                toY = y + Pawn.dy[team][i];
                                checkToPushMove(moveList, x, y, toX, toY, pieceProcessor);
                            }
                        }
                        if (pieceEnum == eXiangqiNotion.HORSE) {
                            for (int i = 0; i < 8; i++) {
                                toX = x + Horse.dx[i];
                                toY = y + Horse.dy[i];
                                checkToPushMove(moveList, x, y, toX, toY, pieceProcessor);
                            }
                        }
                        if (pieceEnum == eXiangqiNotion.ELEPHANT || pieceEnum == eXiangqiNotion.ADVISOR) {
                            for (int i = 0; i < 4; i++) {
                                if (pieceEnum == eXiangqiNotion.ELEPHANT) {
                                    toX = x + Elephant.dx[i];
                                    toY = y + Elephant.dy[i];
                                } else {
                                    toX = x + Advisor.dx[i];
                                    toY = y + Advisor.dy[i];
                                }
                                checkToPushMove(moveList, x, y, toX, toY, pieceProcessor);
                            }
                        }
                        if (pieceEnum == eXiangqiNotion.KING || pieceEnum == eXiangqiNotion.ROOK || pieceEnum == eXiangqiNotion.CANNON) {
                            for (int i = 0; i < 4; i++) {
                                toX = x + Rook.dx[i];
                                toY = y + Rook.dy[i];

                                while (pieceProcessor.isOnChessBoard(toX, toY)) {
                                    checkToPushMove(moveList, x, y, toX, toY, pieceProcessor);
                                    toX = toX + Rook.dx[i];
                                    toY = toY + Rook.dy[i];
                                    if (pieceEnum == eXiangqiNotion.KING && !pieceProcessor.isOnChessBoard(toX, toY)) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return (ArrayList<Integer>) moveList.clone();
    }

    public int Perft(int depth) {
        int nodes = 0;
        if (depth == 0) return 1;
        ArrayList<Integer> moves = generateMovesV2();
        for (int i = 0; i < moves.size(); i++) {
            boolean isValid = makeMove(moves.get(i));
            if (isValid) {
                nodes += Perft(depth - 1);
                takeBack();
            }
        }

        return nodes;
    }
    public void initBoard() {
        currentPosition = INITIAL_POSITION;
    }
}
