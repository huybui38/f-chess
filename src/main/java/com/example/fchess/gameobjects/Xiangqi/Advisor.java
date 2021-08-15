package com.example.fchess.gameobjects.Xiangqi;

public class Advisor extends XiangqiPiece{
    public Advisor() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
