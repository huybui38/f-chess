package com.example.fchess.gameserver;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.example.fchess.gamebase.BaseClient;
import com.example.fchess.gamebase.BaseSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServer extends BaseSocketServer {
    @Autowired
    public GameServer(SocketIOServer server) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        super(server);
    }

    @Override
    protected DataListener packetDispatcher(String packet) {
        return (client, data, ackRequest) -> {
                String token  = client.getHandshakeData().getSingleUrlParam("token");
                if (clients.containsKey(token)){
                    handlers.get(packet).handle((GameClient) clients.get(token), data);
                }
        };
    }
    @Override
    protected BaseClient getNewClient(SocketIOClient client, String token) {
        return new GameClient(client, token);
    }
}
