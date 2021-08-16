package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eTeam;
import com.example.fchess.gameobjects.AbstractPiece;

public class XiangqiPiece extends AbstractPiece {
    protected XiangqiBoard chessBoard;
    protected char label;
    protected eTeam team;
    protected int coordinatesX;
    protected int coordinatesY;

    public char getLabel() { return label; }

    public void setLabel(char label) { this.label = label; }

    public eTeam getTeam() { return team; }

    public void setTeam(eTeam team) { this.team = team; }

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

    public XiangqiPiece getPieceByCharFen(char c) {
        switch (c) {
            case 'r': return new Rook(eTeam.BLACK);
            case 'n': return new Horse(eTeam.BLACK);
            case 'b': return new Elephant(eTeam.BLACK);
            case 'a': return new Advisor(eTeam.BLACK);
            case 'k': return new King(eTeam.BLACK);
            case 'c': return new Cannon(eTeam.BLACK);
            case 'p': return new Pawn(eTeam.BLACK);
            case 'R': return new Rook(eTeam.RED);
            case 'N': return new Horse(eTeam.RED);
            case 'B': return new Elephant(eTeam.RED);
            case 'A': return new Advisor(eTeam.RED);
            case 'K': return new King(eTeam.RED);
            case 'C': return new Cannon(eTeam.RED);
            case 'P': return new Pawn(eTeam.RED);
        }
        return null;
    }

    @Override
    protected void onMoved() {

    }
    @Override
    protected void onRemoved() {

    }

    @Override
    public boolean validateMove(String team, String newPosition) {
        return false;
    }
}
