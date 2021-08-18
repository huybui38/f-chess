package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;

public class Cannon extends XiangqiPiece{
    public Cannon() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;
        if (!isStraight(fromRow, fromColumn, toRow, toColumn)) return false;

        if (chessBoard[toRow][toColumn] == '.') {
            return getNumberPiecesBetween(fromRow, fromColumn, toRow, toColumn, chessBoard) == 0;
        } else {
            return getNumberPiecesBetween(fromRow, fromColumn, toRow, toColumn, chessBoard) == 1;
        }
    }
}
