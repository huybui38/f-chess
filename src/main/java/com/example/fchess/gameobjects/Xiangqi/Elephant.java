package com.example.fchess.gameobjects.Xiangqi;

public class Elephant extends XiangqiPiece{
    public Elephant() {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
