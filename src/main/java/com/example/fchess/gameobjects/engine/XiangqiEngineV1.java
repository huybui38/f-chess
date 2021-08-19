package com.example.fchess.gameobjects.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
    private final String INITIAL_FEN = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR";
    private final int OUT = -1;
    private final int EMPTY = 0;
    private final int RED_PAWN = 1;
    private final int RED_ADVISOR = 2;
    private final int RED_ELEPHANT  = 3;
    private final int RED_HORSE  = 4;
    private final int RED_CANNON  = 5;
    private final int RED_ROOK = 6;
    private final int RED_KING = 7;
    private final int BLACK_PAWN = 8;
    private final int BLACK_ADVISOR = 9;
    private final int BLACK_ELEPHANT  = 10;
    private final int BLACK_HORSE  = 11;
    private final int BLACK_CANNON  = 12;
    private final int BLACK_ROOK = 13;
    private final int BLACK_KING = 14;

    private final int PAWN = 15;
    private final int ADVISOR = 16;
    private final int ELEPHANT = 17;
    private final int HORSE = 18;
    private final int CANNON = 19;
    private final int ROOK = 20;
    private final int KING = 21;


    private final int BLACK = 0;
    private final int RED = 1;

    /*
        DIRECTION
     */
    private final int UP = 11;
    private final int DOWN = -11;
    private final int LEFT = -1;
    private final int RIGHT = 1;
    private final int[] ORTHOGONALS = new int[]{
            UP, DOWN, LEFT, RIGHT
    };
    private final int[] DIAGONALS = new int[]{
        UP + LEFT, UP + RIGHT, DOWN + LEFT, DOWN + RIGHT
    };

    /*

     */
    private final int[][] PAWN_ATTACK_OFFSETS = new int[][] {
            { DOWN, LEFT, RIGHT},
            { UP, LEFT, RIGHT }

    };

    private final int[][] HORSE_ATTACK_OFFSETS = new int[][] {
            { UP + UP + LEFT,  LEFT + LEFT + UP},
            { UP + UP + RIGHT, RIGHT + RIGHT + UP},
            { LEFT + LEFT + DOWN,  DOWN + DOWN + LEFT},
            { RIGHT + RIGHT + DOWN, DOWN + DOWN + RIGHT}
    };
    /*

     */
    private final int[][] PAWN_MOVE_OFFSETS = new int[][] {
            { UP, LEFT, RIGHT },
            { DOWN, LEFT, RIGHT}
    };
    private final int[] ELEPHANT_MOVE_OFFSETS = new int[] {
            (UP + LEFT) * 2, (UP + RIGHT) *2, (DOWN + LEFT) *2, (DOWN + RIGHT) *2
    };
    private final int[][] HORSE_MOVE_OFFSETS = new int[][]{
            { UP * 2 + LEFT, UP *2 + RIGHT},
            { DOWN * 2 + LEFT, DOWN *2 + RIGHT },
            { LEFT *2 + UP, LEFT*2 + DOWN},
            { RIGHT *2 + UP, RIGHT*2 + DOWN},
    };

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
//        char result = 'k';
//        int type = getPieceType(piece);
//        int side = getPieceSide(piece);
//        switch (type){
//            case PAWN:
//                result = 'p';
//                break;
//            case ROOK:
//                result = 'r';
//                break;
//            case CANNON:
//                result = 'c';
//                break;
//            case ADVISOR:
//                result = 'a';
//                break;
//            case HORSE:
//                result = 'n';
//                break;
//            case ELEPHANT:
//                result = 'e';
//                break;
//        }
//        return side == RED ? Character.toUpperCase(result) : result;
    }
    private int getPieceType(int piece){
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
    public int getPieceSide(int piece){
        if (piece >= RED_PAWN && piece <= RED_KING)
            return RED;
        if (piece >= BLACK_PAWN && piece <= BLACK_KING)
            return BLACK;
        return EMPTY;
    }
    private int getPieceByChar(char c){
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
//                System.out.print(board[square] + "            ");
//                if (file == 10) System.out.println();
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
                        board[square] =  getPieceByChar(charPiece);
                        if (getPieceType(board[square]) == KING){
                            kingPositionsCache[getPieceSide(board[square])] = square;
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
        if (targetPiece == EMPTY || getPieceSide(targetPiece) == (side ^ 1)){
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
    private int encode(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece, int capture){
        return (sourceSquare) |
                (targetSquare << 8) |
                (sourcePiece << 16) |
                (targetPiece << 20) |
                (capture << 24);
    }
    private int getSourceSquare(int move){
        return move & 0xFF;
    }
    private int getTargetSquare(int move){
        return (move >> 8) & 0xFF;
    }
    private int getSourcePiece(int move){
        return (move >> 16) & 0xF;
    }
    private int getTargetPiece(int move){
        return (move >> 20) & 0xF;
    }
    private int getCaptureFlag(int move){
        return (move >> 24) & 0x1;
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
    private boolean isInPlace(int square){
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
    public int convertPosition(String position){
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
    public boolean move(String source, String target){
        int sourceSquare = convertPosition(source);
        int targetSquare = convertPosition(target);
        if (sourceSquare == -1 || targetSquare == -1)
            return false;
        int id = createUniqueID(targetSquare, sourceSquare);
        if (!hashMoves.containsKey(id))
            return false;
        Move m = hashMoves.get(id);
        return move(m);
    }
    private boolean move(Move m){
        movesHistory.add(new MoveHistory(m, side));
        int sourceSquare = getSourceSquare(m.getMove());
        int targetSquare = getTargetSquare(m.getMove());
        int sourcePiece = getSourcePiece(m.getMove());
        int targetPiece = getTargetPiece(m.getMove());
        int capture = getCaptureFlag(m.getMove());
        board[targetSquare] = sourcePiece;
        board[sourceSquare] = EMPTY;

        if (getPieceType(board[targetSquare]) == KING){
            kingPositionsCache[side] = targetSquare;
        }
        if (isInCheck()){
            undo();
            return false;
        }
        side ^= 1;
        generateMovesV1();
        checkmated = this.isCheckmated();
        System.out.println("isCheckmate:"+ checkmated);
        return true;
    }
    private boolean move(int sourceSquare, int targetSquare, int sourcePiece, int targetPiece, int capture){
        Move m = new Move(encode(sourceSquare, targetSquare, sourcePiece, targetPiece, capture));
        return move(m);
    }
    private boolean isInCheck(){
        return isAttack(kingPositionsCache[side], side ^1);
    }
    private boolean isCheckmated(){
        if (!isInCheck()) return false;
        int source = kingPositionsCache[side];
        boolean canMove = false;
        for (int i = 0; i < ORTHOGONALS.length; i++) {
            int target = source + ORTHOGONALS[i];
            if (board[target] == OUT) continue;
            if (board[target] == EMPTY){
                canMove = move(source, target, board[source], board[target], 0);
            }else if (getPieceSide(board[target]) != side){
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
        if (getPieceType(board[sourceSquare]) == KING){
            System.out.println("King position changed: "+ kingPositionsCache[getPieceSide(board[sourceSquare])] + "=> " + sourceSquare);
            kingPositionsCache[getPieceSide(board[sourceSquare])] = sourceSquare;
        }
        side = moveHistory.getSide();
        movesHistory.remove(lastestIndex);
        generateMovesV1();
        return true;
    }
    private boolean isOverBank(int square){
//        int pieceSide = getPieceSide(board[square]);
        if (side == RED){
            if ((square >= 23 && square <=31) || (square >= 34 && square <= 42) || (square >= 45 && square <=53) || (square >= 56 && square <= 64) || (square >= 67 && square <= 75) ) return true;
        }else {
            if ((square >= 78 && square <= 86) || (square >= 89 && square <= 97) || (square >= 100 && square <= 108) || (square >= 111 && square <= 119) || (square >= 122 && square <= 130) ) return true;
        }
        return false;
    }
    public ArrayList<Move> generateMovesV1(){
        this.moves.clear();
        this.hashMoves.clear();
        for (int source = 0; source < board.length; source++) {
            if (board[source] != OUT && board[source] != EMPTY){
                int pieceType = getPieceType(board[source]);
                int pieceSide = getPieceSide(board[source]);
                if (pieceSide == side){
                    if (pieceType == PAWN){
                        for (int i = 0; i < PAWN_MOVE_OFFSETS.length; i++) {
                            int target = source + PAWN_MOVE_OFFSETS[pieceSide][i];
                            if (board[target] != OUT) pushMove(target, source, board[target], board[source]);
//                            if (board[target] != EMPTY){
//                                if (getPieceSide(board[target]) != pieceSide)
//                                    pushMove(source, target, true);
//                            }else pushMove(source, target, false);
                            if (isOverBank(source) == false) break;
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
                            if ( board[jumpOver] == EMPTY && isOverBank(target) == false) pushMove(target, source, board[target], board[source]);
                        }
                    }

                    //
                    if (pieceType == KING || pieceType == ADVISOR){
//                        for (int i = 0; i < DIAGONALS.length; i++) {
                            int[] directions = (pieceType == KING) ? ORTHOGONALS : DIAGONALS;
                            for (int j = 0; j < directions.length; j++) {
                                int target = source + directions[j];
                                if (isInPlace(target)){
                                    pushMove(target, source, board[target], board[source]);
                                }
                            }
//                        }
                    }
                    //
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
