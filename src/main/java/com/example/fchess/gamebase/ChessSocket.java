package com.example.fchess.gamebase;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.fchess.interfaces.IChessSocket;

public class ChessSocket implements IChessSocket {
    SocketIOClient socket;
    ChessSocket(SocketIOClient socket){
        this.socket = socket;
    }
    @Override
    public void sendToAllInRoom(GamePacket gamePacket, String roomID) {
        socket.getNamespace().getRoomOperations(roomID).sendEvent(gamePacket.eventName, gamePacket.data);
    }

    @Override
    public void sendToAll(GamePacket gamePacket) {
        socket.getNamespace().getBroadcastOperations().sendEvent(gamePacket.eventName, gamePacket.data);
    }

    @Override
    public void send(GamePacket gamePacket) {
        socket.sendEvent(gamePacket.eventName, gamePacket.data);
    }

}
