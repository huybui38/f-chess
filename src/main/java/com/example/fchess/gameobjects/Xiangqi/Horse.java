package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Horse extends XiangqiPiece {
    public static final int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
    public static final int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

    public Horse() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        ePieceNotation pieceEnum = team == eTeam.RED ? ePieceNotation.BLACK_HORSE : ePieceNotation.RED_HORSE;

        for (int i = 0; i < 8; i++) {
            int fromRow = toRow + dx[i];
            int fromColumn = toColumn + dy[i];
            if (isOnChessBoard(fromRow, fromColumn) && chessBoard[fromRow][fromColumn] == pieceEnum.getNotation()) {
                if (this.validateMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return true;
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
