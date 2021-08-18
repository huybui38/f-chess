package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Pawn extends XiangqiPiece{
    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) {
            eTeam team =  getTeam(chessBoard[fromRow][fromColumn]);
            if (isStraight(fromRow, fromColumn, toRow, toColumn)) {
                if (getStepStraight(fromRow, fromColumn, toRow, toColumn) != 1) return false;
                if (isSelfSide(fromRow, fromColumn, team) && (fromColumn != toColumn)) return false;

                if (team == eTeam.RED && fromRow <= toRow) return true;
                if (team == eTeam.BLACK && fromRow >= toRow) return true;
            }
        }
        return false;
    }
}
