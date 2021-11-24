package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;
import com.example.fchess.gameobjects.AbstractPiece;

public class XiangqiPiece extends AbstractPiece {
    public String getPieceByCharFen(char c) {
        switch (c) {
            case 'r':
                return "br";
            case 'n':
                return "bn";
            case 'b':
                return "bb";
            case 'a':
                return "ba";
            case 'k':
                return "bk";
            case 'c':
                return "bc";
            case 'p':
                return "bp";
            case 'R':
                return"rr";
            case 'N':
                return "rn";
            case 'B':
                return "rb";
            case 'A':
                return "ra";
            case 'K':
                return "rk";
            case 'C':
                return "rc";
            case 'P':
                return "rp";
        }
        return null;
    }

    @Override
    protected void onMoved() {

    }
    @Override
    protected void onRemoved() {

    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
