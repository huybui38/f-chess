package com.example.fchess.gamebase;

import com.corundumstudio.socketio.SocketIOClient;

public class GameClient extends BaseClient{
    String currentRoomID;
    private  ChessSocket chessSocket;
    public GameClient(SocketIOClient socket, String userID) {
        super(socket, userID);
        chessSocket = new ChessSocket(socket);
    }

    public ChessSocket getChessSocket() {
        return chessSocket;
    }

    public String getCurrentRoomID() {
        return currentRoomID;
    }
    @Override
    public void setSocket(SocketIOClient socket) {
        super.setSocket(socket);
        chessSocket = new ChessSocket(socket);
    }
    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
    }
}
