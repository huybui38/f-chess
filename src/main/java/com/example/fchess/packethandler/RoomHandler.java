package com.example.fchess.packethandler;

import com.example.fchess.gamebase.GameClient;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.interfaces.PacketHandler;
import com.example.fchess.interfaces.IPacketHandler;

@PacketHandler(packetName = "enterRoom", handler = RoomHandler.class, dataType = String.class)
public class RoomHandler implements IPacketHandler {

    @Override
    public void handle(GameClient client, Object data) {
        String roomID = (String) data;
        client.setCurrentRoomID(roomID);
        client.getSocket().joinRoom(roomID);
        GamePacket packet = new GamePacket("enterRoom");
        packet.setData(roomID);
        client.getChessSocket().send(packet);
    }
}
