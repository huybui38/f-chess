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
