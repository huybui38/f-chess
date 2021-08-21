package com.example.fchess;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gameobjects.Xiangqi.XiangqiBoard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

@SpringBootApplication
public class FChessApplication {
    @Value("${rt-server.host}")
    private String host;

    @Value("${rt-server.port}")
    private Integer port;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        return new SocketIOServer(config);
    }

    public static void main(String[] args) {
        ArrayList<Integer> moveList = new ArrayList<>();

        SpringApplication.run(FChessApplication.class, args);
        //TEST HERE
        XiangqiBoard chessBoard = new XiangqiBoard("5a3/3k5/3aR4/9/5r3/5n3/9/3A1A3/5K3/2BC2B2", null, null);
        moveList = chessBoard.generateMovesV2();
        chessBoard.showChessBoard();
        int countTest = chessBoard.Perft(3);
        System.out.println("test: " + countTest);

//        String s1 = chessBoard.getSquareFromCoordinate(2, 1);
//        String t1 = chessBoard.getSquareFromCoordinate(1, 1);
//        String s2 = chessBoard.getSquareFromCoordinate(9, 6);
//        String t2 = chessBoard.getSquareFromCoordinate(7, 8);
//        String s3 = chessBoard.getSquareFromCoordinate(9, 1);
//        String t3 = chessBoard.getSquareFromCoordinate(9, 3);

//        chessBoard.makeMove(s1, t1);
//        chessBoard.makeMove(s2, t2);
//        chessBoard.makeMove(s3, t3);

        System.out.println(chessBoard.countMove);
//        for (int i = 0; i < moveList.size(); i++) {
//            int move = moveList.get(i);
//            System.out.println(move + " => " + chessBoard.getSourceSquare(move) + chessBoard.getTargetSquare(move));
//            System.out.println(chessBoard.getSourcePiece(move) + " => " + chessBoard.getTargetPiece(move) + " " + chessBoard.getCaptureFlag(move));
//            System.out.println("----------------------------------------");
//        }
//

//        chessBoard.showChessBoard();
//        chessBoard.takeBack();
//        for (int i = 0; i < chessBoard.moveList.size(); i++) {
//            int move = chessBoard.moveList.get(i);
//            System.out.println(move + " => " + chessBoard.getSourceSquare(move) + chessBoard.getTargetSquare(move));
//            System.out.println(chessBoard.getSourcePiece(move) + " => " + chessBoard.getTargetPiece(move) + " " + chessBoard.getCaptureFlag(move));
//        }
//        chessBoard.showChessBoard();
//
//        String s4 = chessBoard.getSquareFromCoordinate(2, 7);
//        String t4 = chessBoard.getSquareFromCoordinate(2, 4);
//        chessBoard.makeMove(s4, t4);
//        chessBoard.showChessBoard();
    //
}
}
