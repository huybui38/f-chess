package com.example.fchess.interfaces;

public interface IPiece {
    boolean validateMove(int fromRow, int fromColumn, int toRow, int toColumn, char[][] chessBoard);
}
