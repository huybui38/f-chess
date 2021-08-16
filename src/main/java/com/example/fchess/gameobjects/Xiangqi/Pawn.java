package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Pawn extends  XiangqiPiece{
    public Pawn(eTeam team) {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
