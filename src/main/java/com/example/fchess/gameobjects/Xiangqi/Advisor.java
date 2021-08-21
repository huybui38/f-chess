package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Advisor extends XiangqiPiece {
    public static final int[] dx = {-1, -1, 1, 1};
    public static final int[] dy = {-1, 1, -1, 1};

    public Advisor() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;

        eTeam team = getTeam(chessBoard[fromRow][fromColumn]);
        if (isOnPalace(fromRow, fromColumn, team) && isOnPalace(toRow, toColumn, team)) {
            return getStepDiagonal(fromRow, fromColumn, toRow, toColumn) == 1;
        }

        return false;
    }
}
