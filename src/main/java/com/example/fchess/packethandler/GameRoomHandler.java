package com.example.fchess.packethandler;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gamebase.GameClient;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.interfaces.IPacketHandler;
import com.example.fchess.interfaces.PacketHandler;
import com.example.fchess.transmodel.DataPackage;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

@PacketHandler(packetName = eChessPackage.GAME_ROOM, handler = GameRoomHandler.class, dataType = DataPackage.class)
public class GameRoomHandler  implements IPacketHandler{
    private static final Logger log = LoggerFactory.getLogger(GameRoomHandler.class);
    @Override
    public void handle(GameClient client, Object data) {
        DataPackage dataPackage = (DataPackage)data;
        eGameRoom packageType = eGameRoom.fromId(dataPackage.getType());
        switch (packageType){
            case JOIN_ROOM:
                handleJoinGame(client, dataPackage.getData().toString());
                break;
            default:
                log.error("GameRoomPackage Not Found: {}", dataPackage.getType());
                break;
        }
    }
    private void handleJoinGame(GameClient client, String roomID) {
        if (client.getCurrentRoomID() != null && client.getCurrentRoomID().equals(roomID)){
            return;
        }
        client.setCurrentRoomID(roomID);
        GamePacket response = new GamePacket(eChessPackage.GAME_ROOM);
        response.writeData("type", eGameRoom.JOIN_ROOM.getValue());
        response.writeData("roomID", roomID);
        response.serialize();
        client.getChessSocket().send(response);
    }
}
