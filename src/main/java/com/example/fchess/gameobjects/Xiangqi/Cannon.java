package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Cannon extends XiangqiPiece{
    public Cannon(eTeam team) {
        if (team == eTeam.RED) setLabel('C');
            else setLabel('c');

        setTeam(team);
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
