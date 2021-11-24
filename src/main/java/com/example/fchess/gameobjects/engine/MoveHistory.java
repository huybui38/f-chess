package com.example.fchess.gameobjects.engine;

public class MoveHistory {
    private Move value;
    private int side;

    public MoveHistory(Move move, int side) {
        this.value = move;
        this.side = side;
    }

    public Move getValue() {
        return value;
    }

    public void getValue(Move move) {
        this.value = move;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}
