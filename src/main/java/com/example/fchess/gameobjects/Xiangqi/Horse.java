package com.example.fchess.gameobjects.Xiangqi;

public class Horse extends  XiangqiPiece{
    public Horse() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
