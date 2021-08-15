package com.example.fchess.gameobjects;

import com.example.fchess.gameobjects.Xiangqi.XiangqiBoard;
import com.example.fchess.interfaces.IPiece;

public abstract class AbstractPiece implements IPiece {
    protected XiangqiBoard chessBoard;
    protected int size;
    protected int coordinatesX;
    protected int coordinatesY;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCoordinatesX() {
        return coordinatesX;
    }

    public void setCoordinatesX(int coordinatesX) {
        this.coordinatesX = coordinatesX;
    }

    public int getCoordinatesY() {
        return coordinatesY;
    }

    public void setCoordinatesY(int coordinatesY) {
        this.coordinatesY = coordinatesY;
    }

    public XiangqiBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(XiangqiBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    protected abstract void onMoved();
    protected abstract void onRemoved();
}
