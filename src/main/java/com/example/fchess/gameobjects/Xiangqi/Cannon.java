package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Cannon extends XiangqiPiece {
    private static final int[] dx = {0, 0, -1, 1};
    private static final int[] dy = {-1, 1, 0, 0};

    public Cannon() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        ePieceNotation pieceEnum = team == eTeam.RED ? ePieceNotation.BLACK_CANNON : ePieceNotation.RED_CANNON;

        for (int i = 0; i < 4; i++) {
            int fromRow = toRow + dx[i];
            int fromColumn = toColumn + dy[i];
            int count = 0;
            while (isOnChessBoard(fromRow, fromColumn)) {
                if (chessBoard[fromRow][fromColumn] == pieceEnum.getNotation()) {
                    if (count == 1) return true;
                        else break;
                };

                if (chessBoard[fromRow][fromColumn] != '.') count++;
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

        if (chessBoard[toRow][toColumn] == '.') {
            return getNumberPiecesBetween(fromRow, fromColumn, toRow, toColumn, chessBoard) == 0;
        } else {
            return getNumberPiecesBetween(fromRow, fromColumn, toRow, toColumn, chessBoard) == 1;
        }
    }
}
