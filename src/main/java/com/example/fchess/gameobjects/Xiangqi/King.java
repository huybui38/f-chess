package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class King extends  XiangqiPiece{
    public King(eTeam team) {
        if (team == eTeam.RED) setLabel('K');
            else setLabel('k');

        setTeam(team);
    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
