package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Rook extends XiangqiPiece {
    public Rook(eTeam team) {
        if (team == eTeam.RED) setLabel('R');
            else setLabel('r');

        setTeam(team);
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
