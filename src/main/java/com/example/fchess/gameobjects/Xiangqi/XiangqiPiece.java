package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;
import com.example.fchess.gameobjects.AbstractPiece;

public abstract class XiangqiPiece extends AbstractPiece {
    public int getTeam(char notation) {
        if (notation >= 'a' && notation <= 'z') {
            return eTeam.BLACK.getLabel();
        } else if (notation >= 'A' && notation <= 'Z') {
            return eTeam.RED.getLabel();
        }

        return -1;
    }

    public int getCoordinateX(String coordinate) {
        return coordinate.charAt(1) - 48;
    }

    public int getCoordinateY(String coordinate) {
        return coordinate.charAt(0) - 97;
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

    public boolean isOnChessBoard(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 8) return false;
        return true;
    }

    public boolean isSameTeam(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard) {
        return  (getTeam(chessBoard[fromRow][fromColumn]) == getTeam(chessBoard[toRow][toColumn]));
    }

    abstract public boolean isCapture(int toRow, int toColumn, eTeam team, char[][] chessBoard);

    @Override
    protected void onMoved() {

    }

    @Override
    protected void onRemoved() {

    }
}
