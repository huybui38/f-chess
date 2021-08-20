package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Rook extends XiangqiPiece {
    private static final int[] dx = {0, 0, -1, 1};
    private static final int[] dy = {-1, 1, 0, 0};

    public Rook() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        ePieceNotation pieceEnum = team == eTeam.RED ? ePieceNotation.BLACK_ROOK : ePieceNotation.RED_ROOK;

        for (int i = 0; i < 4; i++) {
            int fromRow = toRow + dx[i];
            int fromColumn = toColumn + dy[i];

            while (isOnChessBoard(fromRow, fromColumn)) {
                if (chessBoard[fromRow][fromColumn] == pieceEnum.getNotation()) return true;
                if (chessBoard[fromRow][fromColumn] != '.') break;

                fromRow = fromRow + dx[i];
                fromColumn = fromColumn + dy[i];
            }
        }
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;
        if (!isStraight(fromRow, fromColumn, toRow, toColumn)) return false;

        return getNumberPiecesBetween(fromRow, fromColumn, toRow, toColumn, chessBoard) == 0;
    }
}
