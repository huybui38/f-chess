package com.example.fchess;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.fchess.gameobjects.Xiangqi.XiangqiBoard;
import com.example.fchess.gameobjects.engine.Move;
import com.example.fchess.gameobjects.engine.XiangqiEngineV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        SpringApplication.run(FChessApplication.class, args);
        //TEST HERE
        XiangqiEngineV1 engineV1 = new XiangqiEngineV1();
        engineV1.resetBoard();
//        engineV1.setBoard("5k3/9/9/9/9/9/P8/9/9/4K4");
//        engineV1.setBoard("CRH1k1e2/3ca4/4ea3/9/2hr5/9/9/4E4/4A4/4KA3");
        engineV1.setBoard("rCeakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/7C1/9/RHEAKAEHR");
        engineV1.generateMovesV1();
        boolean result = engineV1.move("A0", "A4");
        engineV1.printBoard();
        System.out.println(result);
//        for (int i = 0; i < list.size(); i++) {
//            Move m = list.get(i);
//            boolean result = engineV1.actualMove(list.get(i));
//            engineV1.printBoard();
//            System.out.println(i+1);
//            System.out.println(engineV1.getSourceSquare(m.getMove()) + "->" +engineV1.getTargetSquare(m.getMove()) + ":" + result);
//            engineV1.undo();
//        }
//        System.out.println(engineV1.Perft(5));
//        engineV1.printBoard();

    }
}
