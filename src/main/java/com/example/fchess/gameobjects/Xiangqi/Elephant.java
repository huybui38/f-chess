package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Elephant extends XiangqiPiece{
    public Elephant(eTeam team) {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
