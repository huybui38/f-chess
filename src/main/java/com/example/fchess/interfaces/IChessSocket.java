package com.example.fchess.interfaces;

import com.example.fchess.gamebase.GamePacket;

public interface IChessSocket {
    void sendToAllInRoom(GamePacket gamePacket, String roomID);
    void sendToAll(GamePacket gamePacket);
    void send(GamePacket gamePacket);
    void sendMessage(String msg);
}
