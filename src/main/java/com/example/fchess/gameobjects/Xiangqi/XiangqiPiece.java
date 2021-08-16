package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;
import com.example.fchess.gameobjects.AbstractPiece;

public abstract class XiangqiPiece extends AbstractPiece {
    protected char[][] chessBoard;

    public char[][] getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(char[][] chessBoard) {
        this.chessBoard = chessBoard;
    }

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

    public int getDiffX(String source, String destination) {
        return getCoordinateX(source) - getCoordinateX(destination);
    }


    public int getDiffY(String source, String destination) {
        return getCoordinateY(source) - getCoordinateY(destination);
    }

    public boolean isOnChessBoard(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 8) return false;
        return true;
    }

    abstract public boolean isCapture(String source, String destination);

    @Override
    protected void onMoved() {

    }

    @Override
    protected void onRemoved() {

    }
}
