package com.example.fchess.gameobjects.Xiangqi;

public class King extends  XiangqiPiece{
    public King() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
