package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Horse extends  XiangqiPiece{
    public Horse(eTeam team) {
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
