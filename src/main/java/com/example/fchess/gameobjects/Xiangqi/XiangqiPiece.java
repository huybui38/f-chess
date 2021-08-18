package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;
import com.example.fchess.gameobjects.AbstractPiece;

public abstract class XiangqiPiece extends AbstractPiece {
    public eTeam getTeam(char notation) {
        if (notation >= 'a' && notation <= 'z') {
            return eTeam.BLACK;
        } else if (notation >= 'A' && notation <= 'Z') {
            return eTeam.RED;
        }

        return eTeam.NULL;
    }

    public boolean isOnPalace(int x, int y, eTeam team) {
        if (team == eTeam.RED) {
            if (x < 0 || x > 2 || y < 3 || y > 5) return false;
        } else if (x < 7 || x > 9 || y < 3 || y > 5) return false;
        return true;
    }

    public boolean isSelfSide(int x, int y, eTeam team) {
        if (team == eTeam.RED) {
            if (x > 4 ) return false;
        } else if (x < 5) return false;
        return true;
    }

    public boolean isStraight(int fromRow, int fromColumn, int toRow, int toColumn) {
        return fromRow == toRow || fromColumn == toColumn;
    }

    public int getStepStraight(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (fromRow == toRow) return Math.abs(fromColumn - toColumn);
        if (fromColumn == toColumn) return Math.abs(fromRow - toRow);
        return 0;
    }

    public int getNumberPiecesBetween(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (getStepStraight(fromRow, fromColumn, toRow, toColumn) < 2) return 0;
        int count = 0, start, end;

        if (fromRow == toRow) {
            start = Math.min(fromColumn, toColumn);
            end = Math.max(fromColumn, toColumn);
            for (int col = start + 1 ; col < end; col++) {
                if (chessBoard[fromRow][col] != '.') count++;
            }
        } else {
            start = Math.min(fromRow, toRow);
            end = Math.max(fromRow, toRow);
            for (int row = start + 1 ; row < end; row++) {
                if (chessBoard[row][fromColumn] != '.') count++;
            }
        }

        return count;
    }

    public boolean isMoveDiagonal(int fromRow, int fromColumn, int toRow, int toColumn) {
        return (Math.abs((fromRow - toRow)) == Math.abs(fromColumn - toColumn));
    }

    public int getStepDiagonal(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (isMoveDiagonal(fromRow, fromColumn, toRow, toColumn)) {
            return Math.abs(fromRow - toRow);
        }
        return 0;
    }

    public boolean isOnChessBoard(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 8) return false;
        return true;
    }

    public boolean isSameTeam(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        return  (getTeam(chessBoard[fromRow][fromColumn]) == getTeam(chessBoard[toRow][toColumn]));
    }

    public boolean isLegalMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        if (!isOnChessBoard(fromRow, fromColumn) || !isOnChessBoard(toRow, toColumn)) return false;
        if (isSameTeam(fromRow, fromColumn, toRow, toColumn, chessBoard)) return false;
        return true;
    }

    abstract public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard);

    @Override
    protected void onMoved() {

    }

    @Override
    protected void onRemoved() {

    }
}
