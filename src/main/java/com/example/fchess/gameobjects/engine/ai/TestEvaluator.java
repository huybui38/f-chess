package com.example.fchess.gameobjects.engine.ai;

import com.example.fchess.gameobjects.engine.EngineConstant;
import com.example.fchess.gameobjects.engine.EngineUtils;
import com.example.fchess.gameobjects.engine.XiangqiEngineV1;

import static com.example.fchess.gameobjects.engine.EngineConstant.*;

public class TestEvaluator implements BoardEvaluator{
    private static int E_ROOK = 2000;
    private static int E_CANNON = 2000;
    private static int E_PAWN = 2000;
    private static int E_HORSE = 2000;
    private static int E_ADVISER = 2000;
    @Override
    public int evaluate(XiangqiEngineV1 engineV1) {
        int[] board = engineV1.board;
        int sum = 0;
        for (int i = 0; i < engineV1.board.length; i++) {
            if (board[i] != OUT && board[i] != EMPTY){
                if (EngineUtils.getPieceSide(board[i]) == RED){
                    sum += calcByType(board[i]);
                }else  sum -= calcByType(board[i]);
            }
        }
        return sum;
    }
    private int calcByType(int piece){
        int type = EngineUtils.getPieceType(piece);
        switch (type){
            case HORSE:
                return E_HORSE;
            case CANNON:
                return E_CANNON;
            case PAWN:
                return E_PAWN;
            case ROOK:
                return E_ROOK;
            case ADVISOR:
                return E_ADVISER;
        }
        return 0;
    }
}
