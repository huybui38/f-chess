package com.example.fchess.gameobjects.Xiangqi;

public class Pawn extends  XiangqiPiece{
    public Pawn() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
