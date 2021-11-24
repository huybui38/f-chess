package com.example.fchess;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.fchess.gameserver.managers.GameRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameServerRunner implements CommandLineRunner {

    private final SocketIOServer socketIOServer;

    @Autowired
    public GameServerRunner(SocketIOServer server) {
        this.socketIOServer = server;
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        GameRoomManager.start();
    }
}
