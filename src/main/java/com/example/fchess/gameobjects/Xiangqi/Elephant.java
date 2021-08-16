package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Elephant extends XiangqiPiece{
    public Elephant(eTeam team) {
        if (team == eTeam.RED) setLabel('B');
            else setLabel('b');

        setTeam(team);
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
