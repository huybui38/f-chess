package com.example.fchess.gameobjects.engine;

public class Move {
    public Move(int move) {
        this.move = move;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    private int move;

    @Override
    public String toString() {
        int target  = EngineUtils.getTargetSquare(this.move);
        int source  = EngineUtils.getSourceSquare(this.move);
        return "source: "+ source +";target"+target;
    }
}
