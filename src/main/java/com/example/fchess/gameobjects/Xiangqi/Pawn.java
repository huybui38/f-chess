package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Pawn extends XiangqiPiece {
    public static final int[][] dx = {{0, 0, -1}, {0, 0, 1}};
    public static final int[][] dy = {{-1, 1, 0},{-1, 1, 0}};

    public Pawn() {
    }

    @Override
    public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard) {
        ePieceNotation pieceEnum = team == eTeam.RED ? ePieceNotation.BLACK_PAWN : ePieceNotation.RED_PAWN;

        for (int i = 0; i < 3; i++) {
            int fromRow = toRow + dx[team.getLabel()][i];
            int fromColumn = toColumn + dy[team.getLabel()][i];
            if (isOnChessBoard(fromRow, fromColumn) && chessBoard[fromRow][fromColumn] == pieceEnum.getNotation()) {
                if (this.validateMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isLegalMove(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;

        eTeam team = getTeam(chessBoard[fromRow][fromColumn]);
        if (isStraight(fromRow, fromColumn, toRow, toColumn)) {
            if (getStepStraight(fromRow, fromColumn, toRow, toColumn) != 1) return false;
            if (isSelfSide(fromRow, fromColumn, team) && (fromColumn != toColumn)) return false;

            if (team == eTeam.RED && fromRow <= toRow) return true;
            if (team == eTeam.BLACK && fromRow >= toRow) return true;
        }

        return false;
    }
}
