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
}
