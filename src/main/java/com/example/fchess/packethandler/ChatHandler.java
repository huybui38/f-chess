package com.example.fchess.packethandler;

import com.example.fchess.gamebase.GameClient;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.interfaces.PacketHandler;
import com.example.fchess.interfaces.IPacketHandler;
import com.example.fchess.transmodel.ChatMessage;

@PacketHandler(packetName = "chat", handler = ChatHandler.class, dataType = ChatMessage.class)
public class ChatHandler  implements IPacketHandler {

    @Override
    public void handle(GameClient client, Object data) {
        String roomID = client.getCurrentRoomID();
        GamePacket packet = new GamePacket("chat", data);
        client.getChessSocket().sendToAllInRoom(packet,roomID);
    }
}
