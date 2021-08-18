package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Horse extends XiangqiPiece {
    private static final int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

    public Horse() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        char pieceNotation = team == eTeam.RED ? ePieceNotation.RED_HORSE.getNotation() : ePieceNotation.BLACK_HORSE.getNotation();

        for (int i = 0; i < 8; i++) {
            int fromX = toRow + dx[i];
            int fromY = toColumn + dy[i];
            if (this.isOnChessBoard(fromX, fromY) && chessBoard[fromX][fromY] == pieceNotation) {
                if (validateMove(fromX, fromY, toRow, toColumn, chessBoard)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;

        if (Math.abs(fromRow - toRow) == 2 && Math.abs(fromColumn - toColumn) == 1) {
            return (chessBoard[(fromRow + toRow) / 2][fromColumn] == '.');
        } else if (Math.abs(fromRow - toRow) == 1 && Math.abs((fromColumn - toColumn)) == 2) {
            return (chessBoard[fromRow][(fromColumn + toColumn) / 2] == '.');
        }

        return false;
    }
}
