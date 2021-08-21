package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class King extends XiangqiPiece {
    public static final int[][] dx = {{-1}, {1}};
    public static final int[][] dy = {{0}, {0}};

    public King() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        if (chessBoard[toRow][toColumn] != ePieceNotation.RED_KING.getNotation()
                && chessBoard[toRow][toColumn] != ePieceNotation.BLACK_KING.getNotation())
            return false;

        ePieceNotation pieceEnum = team == eTeam.RED ? ePieceNotation.BLACK_KING : ePieceNotation.RED_KING;

        for (int i = 0; i < 1; i++) {
            int fromRow = toRow + dx[team.getLabel()][i];
            int fromColumn = toColumn + dy[team.getLabel()][i];

            while (isOnChessBoard(fromRow, fromColumn)) {
                if (chessBoard[fromRow][fromColumn] == pieceEnum.getNotation()) return true;
                if (chessBoard[fromRow][fromColumn] != '.') break;

                fromRow = fromRow + dx[team.getLabel()][i];
                fromColumn = fromColumn + dy[team.getLabel()][i];
            }
        }
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;

        eTeam team = getTeam(chessBoard[fromRow][fromColumn]);
        if (isOnPalace(fromRow, fromColumn, team) && isOnPalace(toRow, toColumn, team)) {
            return getStepStraight(fromRow, fromColumn, toRow, toColumn) == 1;
        }

        return false;
    }
}
