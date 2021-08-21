package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Elephant extends XiangqiPiece {
    public static final int[] dx = {-2, -2, 2, 2};
    public static final int[] dy = {-2, 2, -2, 2};

    public Elephant() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;

        eTeam team = getTeam(chessBoard[fromRow][fromColumn]);
        if (isSelfSide(toRow, toColumn, team) && getStepDiagonal(fromRow, fromColumn, toRow, toColumn) == 2) {
            return (chessBoard[(fromRow + toRow) / 2][(fromColumn + toColumn) / 2] == '.');
        }

        return false;
    }
}
