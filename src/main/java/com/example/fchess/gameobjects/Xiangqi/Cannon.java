package com.example.fchess.gameobjects.Xiangqi;

public class Cannon extends XiangqiPiece{
    public Cannon() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
