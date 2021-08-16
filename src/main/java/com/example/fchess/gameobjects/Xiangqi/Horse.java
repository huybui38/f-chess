package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.ePieceNotation;
import com.example.fchess.enums.eTeam;

public class Horse extends  XiangqiPiece{
    private static final int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

    @Override
    public boolean isCapture(String source, String destination) {
//        int xBeCapture = this.getCoordinateX(destination);
//        int yBeCapture = this.getCoordinateY(destination);
//        int team = this.getTeam(chessBoard[xBeCapture][yBeCapture]);
//        char notationPiece;
//
//        if (eTeam.RED.getLabel() == team) {
//            notationPiece = ePieceNotation.BLACK_HORSE.getNotation();
//        } else notationPiece = ePieceNotation.RED_HORSE.getNotation();
//
//        for (int i = 0; i < 8; i++) {
//            int xSource = xBeCapture + dx[i];
//            int ySource = yBeCapture + dy[i];
//            if (this.isOnChessBoard(xSource, ySource) && chessBoard[xSource][ySource] == notationPiece) {
//
//            }
//        }
//        return true;
        return false;
    }

    @Override
    public boolean validateMove(String source, String destination) {
        int xSource = this.getCoordinateX(source);
        int ySource = this.getCoordinateY(source);
        int xTarget = this.getCoordinateX(destination);
        int yTarget = this.getCoordinateY(destination);

        for (int i = 0; i < 8; i++) {
            int xGo = xSource + dx[i];
            int yGo = ySource + dy[i];
            if (this.isOnChessBoard(xGo, yGo) && xGo == xTarget && yGo == yTarget) {
                // Check clean path
                if (this.getDiffX(source, destination) == -2) {
                    if (chessBoard[xSource + 1][ySource] == '.')
                        return true;
                }
                if (this.getDiffX(source, destination) == 2) {
                    if (chessBoard[xSource - 1][ySource - 1] == '.')
                        return true;
                }
                if (this.getDiffY(source, destination) == -2) {
                    if (chessBoard[xSource][ySource + 1] == '.')
                        return true;
                }
                if (this.getDiffY(source, destination) == 2) {
                    if (chessBoard[xSource][ySource - 1] == '.')
                        return true;
                }
            }
        }
        return false;
    }
}
