package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Advisor extends XiangqiPiece {
    public Advisor(eTeam team) {
        if (team == eTeam.RED) setLabel('A');
        else setLabel('a');

        setTeam(team);
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
