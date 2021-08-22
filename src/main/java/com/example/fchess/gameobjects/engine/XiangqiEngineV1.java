package com.example.fchess.gameobjects.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static com.example.fchess.gameobjects.engine.EngineConstant.*;
import static com.example.fchess.gameobjects.engine.EngineUtils.*;

public class XiangqiEngineV1 {
    private int mailbox[] = new int[]{
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,-1,
            -1, 9,10,11,12,13,14,15,16,17,-1,
            -1,18,19,20,21,22,23,24,25,26,-1,
            -1,27,28,29,30,31,32,33,34,35,-1,
            -1,36,37,38,39,40,41,42,43,44,-1,
            -1,45,46,47,48,49,50,51,52,53,-1,
            -1,54,55,56,57,58,59,60,61,62,-1,
            -1,63,64,65,66,67,68,69,70,71,-1,
            -1,72,73,74,75,76,77,78,79,80,-1,
            -1,81,82,83,84,85,86,87,88,89,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
            -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
    };
    private int mailBox90[] = new int[]{
             23, 24, 25, 26, 27, 28, 29, 30, 31,
             34, 35, 36, 37, 38, 39, 40, 41, 42,
             45, 46, 47, 48, 49, 50, 51, 52, 53,
             56, 57, 58, 59, 60, 61, 62, 63, 64,
             67, 68, 69, 70, 71, 72, 73, 74, 75,
             78, 79, 80, 81, 82, 83, 84, 85, 86,
             89, 90, 91, 92, 93, 94, 95, 96, 97,
             100, 101, 102, 103, 104, 105, 106, 107, 108,
             111, 112, 113, 114, 115, 116, 117, 118, 119,
             122, 123, 124, 125, 126, 127, 128, 129, 130
    };
    public int board[] = new int[11 * 14];
    private char[] MAP_PIECE_TO_CHAR = new char[]{
            '.', 'P', 'A', 'B', 'N', 'C', 'R', 'K', 'p', 'a', 'b', 'n', 'c', 'r', 'k'
    };
    private HashMap<Integer, Move> hashMoves;
    private ArrayList<Move> moves;
    private ArrayList<MoveHistory> movesHistory;
    /*
        11 * 14 (9 * 10)
      x x x x x x x x x x x
      x x x x x x x x x x x
      x r h e a k a e h r x
      x . . . . . . . . . x
      x . c . . . . . c . x
      x p . p . p . p . p x
      x . . . . . . . . . x
      x . . . . . . . . . x
      x P . P . P . P . P x
      x . C . . . . . C . x
      x . . . . . . . . . x
      x R H E A K A E H R x
      x x x x x x x x x x x
      x x x x x x x x x x x
     */


    public int getSide() {
        return side;
    }

    private int side;

    private boolean checkmated;
    public boolean getCheckmate(){
        return checkmated;
    }

    private int[] kingPositionsCache = new int[2];

    public XiangqiEngineV1(){
        this.side = RED;
        this.hashMoves = new HashMap<>();
        this.moves =  new ArrayList<>();
        this.movesHistory =  new ArrayList<>();
        this.init();
    }
    public void init(){
        resetBoard();
    }
    private char getAsciiPiece(int piece){
        return MAP_PIECE_TO_CHAR[piece];
    }

    public void resetBoard(){
        this.checkmated = false;
        for (int rank = 0; rank < 14; rank++) {
            for (int file = 0; file < 11; file++) {
                int square = rank * 11 + file;
                if (rank < 2 || rank >= 12){
                    board[square] = OUT;
                }else if (file == 0 || file == 10){
                    board[square] = OUT;
                }else {
                    board[square] = EMPTY;
                }
            }
        }
    }
    public void printBoard(){
        for (int rank = 0; rank < 14; rank++) {
            for (int file = 0; file < 11; file++) {
                int square = rank * 11 + file;
                if (board[square] != OUT){
                    System.out.print(MAP_PIECE_TO_CHAR[board[square]] + " ");
                }
                if (file == 10){
                    System.out.println();
                }
            }
        }
    }
    public void setBoard(String fen){
        int index = 0;
        for (int rank = 0; rank < 14; rank++) {
            for (int file = 0; file < 11; file++) {
                int square = rank * 11 + file;
                if (board[square] != OUT){
                    char charPiece = fen.charAt(index);
                    if ((charPiece >= 'a' && charPiece <= 'z') || (charPiece >= 'A' && charPiece <= 'Z')) {
                        board[square] =  EngineUtils.getPieceByChar(charPiece);
                        if (EngineUtils.getPieceType(board[square]) == EngineConstant.KING){
                            kingPositionsCache[EngineUtils.getPieceSide(board[square])] = square;
                        }
                        index++;
                    }
                    if (charPiece >= '1' && charPiece <= '9'){
                        int numEmpty = (int) charPiece - 48;
                        file += numEmpty - 1;
                        index++;
                    }
                    if (index < fen.length() && fen.charAt(index) == '/') index++;
                }
                if (index >= fen.length()) break;
            }
        }
    }
    public boolean isAttack(int square, int side){
        /*
            NO NEED FOR ELEPHANTS & ADVISORS BECAUSE THEY CANNOT THREATEN ENEMY KING
         */
        //pawn check
        for (int i = 0; i < PAWN_ATTACK_OFFSETS[side].length ; i++) {
            int target = square + PAWN_ATTACK_OFFSETS[side][i];
            if (board[target] == (side == BLACK ? BLACK_PAWN : RED_PAWN))
                return true;
        }
        //horse check
        for (int i = 0; i < DIAGONALS.length; i++) {
            int target = square + DIAGONALS[i];
            if (board[target] != EMPTY) continue;
            for (int j = 0; j < 2; j++) {
                target = square + HORSE_ATTACK_OFFSETS[i][j];
                if (board[target] == (side == BLACK ? BLACK_HORSE : RED_HORSE))
                    return true;
            }
        }
        //canon & rook & king
        for (int i = 0; i < ORTHOGONALS.length; i++) {
            int target = square + ORTHOGONALS[i];
            int jump = 0;
            while (board[target] != OUT){
                if (jump == 0){
                    if (board[target] == (side == BLACK ? BLACK_KING : RED_KING) ||
                            board[target] == (side == BLACK ? BLACK_ROOK : RED_ROOK))
                        return true;
                }
                if (board[target] != EMPTY) jump++;
                if (board[target] == (side == BLACK ? BLACK_CANNON : RED_CANNON) && jump == 2)
                    return true;
                target +=  ORTHOGONALS[i];
            }
        }
        return false;
    }
    public int createUniqueID(int targetSquare, int sourceSquare){
        return targetSquare |
                (sourceSquare << 8);
    }
    public void pushMove( int targetSquare,int sourceSquare,  int targetPiece, int sourcePiece){
        if (targetPiece == EMPTY || EngineUtils.getPieceSide(targetPiece) == (side ^ 1)){
            int value;
            if (targetPiece == EMPTY){
                value = encode(sourceSquare, targetSquare, sourcePiece, targetPiece, 0);
            }else {
                value = encode(sourceSquare, targetSquare, sourcePiece, targetPiece, 1);
            }
            Move m = new Move(value);
            this.hashMoves.put(createUniqueID(targetSquare, sourceSquare), m);
            this.moves.add(m);
        }
    }

    public String getFEN(){
        String result = "";
        int file = 0;
        int empty = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != OUT){
                file++;
                if (board[i] != EMPTY){
                    if (empty != 0){
                        result += empty;
                        empty = 0;
                    }
                    result += getAsciiPiece(board[i]);
                }else empty++;
                if (file >= 9){
                    if (empty != 0){
                        result += empty;
                        empty = 0;
                    }
                    if (i < 122)
                        result += "/";
                    file = 0;
                }
            }
        }
        return result;
    }
    public boolean move(String source, String target){
        int sourceSquare = EngineUtils.convertPosition(source);
        int targetSquare = EngineUtils.convertPosition(target);
        if (sourceSquare == -1 || targetSquare == -1)
            return false;
        int id = createUniqueID(targetSquare, sourceSquare);
        if (!hashMoves.containsKey(id))
            return false;
        Move m = hashMoves.get(id);
        return move(m);
    }
    public boolean move(Move m){
        movesHistory.add(new MoveHistory(m, side));
        int sourceSquare = getSourceSquare(m.getMove());
        int targetSquare = getTargetSquare(m.getMove());
        int sourcePiece = getSourcePiece(m.getMove());
        int targetPiece = getTargetPiece(m.getMove());
        int capture = getCaptureFlag(m.getMove());
        board[targetSquare] = sourcePiece;
        board[sourceSquare] = EMPTY;

        if (EngineUtils.getPieceType(board[targetSquare]) == KING){
            kingPositionsCache[side] = targetSquare;
        }
        if (isInCheck()){
            undo();
            return false;
        }
        side ^= 1;
        generateMovesV1();
        checkmated = this.isCheckmated();
        return true;
    }
    private boolean move(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece, int capture){
        Move m = new Move(encode(sourceSquare, targetSquare, sourcePiece, targetPiece, capture));
        return move(m);
    }
    private boolean isInCheck(){
        return isAttack(kingPositionsCache[side], side ^1);
    }
    public boolean isCheckmated(){
        if (!isInCheck()) return false;
        int source = kingPositionsCache[side];
        boolean canMove = false;
        for (int i = 0; i < ORTHOGONALS.length; i++) {
            int target = source + ORTHOGONALS[i];
            if (board[target] == OUT) continue;
            if (board[target] == EMPTY){
                canMove = move(source, target, board[source], board[target], 0);
            }else if (EngineUtils.getPieceSide(board[target]) != side){
                canMove = move(source, target, board[source], board[target], 1);
            }
            if (canMove){
                undo();
                return false;
            }
        }
        for (int i = 0; i < moves.size(); i++) {
            Move element = moves.get(i);
            canMove = move(element);
            if (canMove){
                undo();
                return false;
            }
        }
        return canMove == false;
    }
    public boolean undo(){
        if (movesHistory.size() == 0)
            return false;
        int lastestIndex = movesHistory.size() -1;
        MoveHistory moveHistory = movesHistory.get(lastestIndex);
        int sourceSquare = getSourceSquare(moveHistory.getValue().getMove());
        int targetSquare = getTargetSquare(moveHistory.getValue().getMove());
        int sourcePiece = getSourcePiece(moveHistory.getValue().getMove());
        int targetPiece = getTargetPiece(moveHistory.getValue().getMove());
        int capture = getCaptureFlag(moveHistory.getValue().getMove());

        board[sourceSquare] = sourcePiece;
        board[targetSquare] = EMPTY;
        if (capture == 1){
            board[targetSquare] = targetPiece;
        }
        if (EngineUtils.getPieceType(board[sourceSquare]) == KING){
//            System.out.println("King position changed: "+ kingPositionsCache[EngineUtils.getPieceSide(board[sourceSquare])] + "=> " + sourceSquare);
            kingPositionsCache[EngineUtils.getPieceSide(board[sourceSquare])] = sourceSquare;
        }
        side = moveHistory.getSide();
        movesHistory.remove(lastestIndex);
        generateMovesV1();
        return true;
    }
    public ArrayList<Move> generateMovesV1(){
        this.moves.clear();
        this.hashMoves.clear();
        for (int source = 0; source < board.length; source++) {
            if (board[source] != OUT && board[source] != EMPTY){
                int pieceType = EngineUtils.getPieceType(board[source]);
                int pieceSide = EngineUtils.getPieceSide(board[source]);
                if (pieceSide == side){
                    if (pieceType == PAWN){
                        for (int i = 0; i < PAWN_MOVE_OFFSETS.length; i++) {
                            int target = source + PAWN_MOVE_OFFSETS[pieceSide][i];
                            if (board[target] != OUT) pushMove(target, source, board[target], board[source]);
//                            if (board[target] != EMPTY){
//                                if (EngineUtils.getPieceSide(board[target]) != pieceSide)
//                                    pushMove(source, target, true);
//                            }else pushMove(source, target, false);
                            if (EngineUtils.isOverBank(source, side) == false) break;
                        }
                    }

                    if (pieceType == HORSE){
                        for (int i = 0; i < ORTHOGONALS.length; i++) {
                            int target = source + ORTHOGONALS[i];
                            if (board[target] == EMPTY){
                                for (int j = 0; j < 2; j++) {
                                    target = source + HORSE_MOVE_OFFSETS[i][j];
                                    if (board[target] != OUT) pushMove(target, source, board[target], board[source]);
                                }
                            }
                        }
                    }
                    ///elephant
                    if (pieceType == ELEPHANT){
                        for (int i = 0; i < DIAGONALS.length; i++) {
                            int target = source + ELEPHANT_MOVE_OFFSETS[i];
                            int jumpOver = source + DIAGONALS[i];
                            if ( board[jumpOver] == EMPTY && EngineUtils.isOverBank(target, side) == false) pushMove(target, source, board[target], board[source]);
                        }
                    }

                    //
                    if (pieceType == KING || pieceType == ADVISOR){
                            int[] directions = (pieceType == KING) ? ORTHOGONALS : DIAGONALS;
                            for (int j = 0; j < directions.length; j++) {
                                int target = source + directions[j];
                                if (EngineUtils.isInPlace(target, side)){
                                    pushMove(target, source, board[target], board[source]);
                                }
                            }
                    }
                    if (pieceType == CANNON || pieceType == ROOK){
                        for (int i = 0; i < ORTHOGONALS.length; i++) {
                            int target = source + ORTHOGONALS[i];
                            int jumpOver = 0;
                            while (board[target] != OUT){
                                if (jumpOver == 0){
                                    if (pieceType == ROOK)
                                        pushMove(target, source, board[target], board[source]);
                                    else if (pieceType == CANNON && board[target] == EMPTY){
                                        pushMove(target, source, board[target], board[source]);
                                    }
                                }
                                if (board[target] != EMPTY) jumpOver++;
                                if (pieceType == CANNON && jumpOver == 2){
                                    pushMove(target, source, board[target], board[source]);
                                    break;
                                }
                                target += ORTHOGONALS[i];
                            }
                        }
                    }
                }
            }
        }
        return (ArrayList<Move>) moves.clone();
    }
    public int Perft(int depth){
        int nodes = 0;
        if (depth == 0) return 1;
        ArrayList<Move> moves = generateMovesV1();
        for (int i = 0; i < moves.size(); i++) {
            boolean isValid = move(moves.get(i));
            if (isValid){
                nodes += Perft(depth -1);
                undo();
            }
        }
        return nodes;
    }
}
