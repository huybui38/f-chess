package com.example.fchess.gameobjects.engine.ai;

import com.example.fchess.gameobjects.engine.Move;
import com.example.fchess.gameobjects.engine.XiangqiEngineV1;

import java.util.List;

public class NegaMax implements  MoveStrategy{
    private BoardEvaluator evaluator;
    private int depth;
    private int nodes;
    public NegaMax(int depth){
        this.depth = depth;
        this.nodes = 0;
        evaluator = new TestEvaluator();
    }
    @Override
    public Move execute(final XiangqiEngineV1 engine) {
        Move bestMove = null;
        List<Move> moveList = engine.generateMovesV1();
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < moveList.size(); i++) {
            boolean isValid = engine.move(moveList.get(i));
            if (isValid){
                int value = negamax(engine, this.depth);
                engine.undo();
                if (value > best){
                    bestMove = moveList.get(i);
                    best = value;
                }
            }
        }
        return bestMove;
    }

    @Override
    public int getTotalNodes() {
        return this.nodes;
    }

    private int negamax(XiangqiEngineV1 engine, final int depth){
        if (depth == 0 || engine.isCheckmated()){
            this.nodes++;
            return this.evaluator.evaluate(engine);
        }
        int best = Integer.MIN_VALUE;
        List<Move> moveList = engine.generateMovesV1();
        for (int i = 0; i < moveList.size(); i++) {
            boolean isValid = engine.move(moveList.get(i));
            if (isValid){
                int value = -negamax(engine, depth - 1);
                engine.undo();
                if (value > best) best = value;
            }
        }
        return best;
    }
}
