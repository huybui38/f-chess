package com.example.fchess.packethandler;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;
import com.example.fchess.gameserver.managers.GameRoomManager;
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
                handleJoinGameRoom(client, dataPackage.getData().toString());
                break;
            case CREATE_ROOM:
                if (client.currentBaseGameRoom != null){
                    client.Out().sendMessage("GAME_ROOM.CREATE_ROOM.ALREADY_IN_ROOM");
                    return;
                }
                GameRoomManager.addRoom(client);
                break;
            case SELECT_TEAM:
                int team =  Integer.parseInt(dataPackage.getData().toString());
                if (client.currentBaseGameRoom == null){
                    client.Out().sendMessage("GAME_ROOM.SELECT_TEAM.ROOM_NOT_FOUND");
                    return;
                }
                if (team > 1 || team < 0){
                    client.Out().sendMessage("GAME_ROOM.SELECT_TEAM.INVALID_TEAM");
                    return;
                }
                if (!client.currentBaseGameRoom.findSlot(team)){
                    client.Out().sendMessage("GAME_ROOM.SELECT_TEAM.POSITION_BUSY");
                    return;
                }
                client.currentBaseGameRoom.addPlayerToSlot(team, client);
//                client.Out().sendPlayerSlots(client.currentBaseGameRoom);
                break;
            case START_GAME:
                if (client.currentBaseGameRoom == null){
                    client.Out().sendMessage("GAME_ROOM.START_GAME.INVALID_ROOM");
                    return;
                }
                if (client.gamePlayer.isViewer()){
                    client.Out().sendMessage("GAME_ROOM.START_GAME.INVALID_CLIENT");
                    return;
                }
                if (!client.currentBaseGameRoom.canStartGame()){
                    client.Out().sendMessage("GAME_ROOM.START_GAME.NOT_ENOUGH_PLAYER");
                    return;
                }
                client.currentBaseGameRoom.startGame();
                GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
                pkg.writeType(eGameRoom.START_GAME.getValue());
                pkg.serialize();
                client.Out().sendToAllInRoom(pkg, client.currentBaseGameRoom.getRoomID());
                break;
            default:
                log.error("GameRoomPackage Not Found: {}", dataPackage.getType());
                break;
        }
    }
    private void handleJoinGameRoom(GameClient client, String roomID) {
        if (client.currentBaseGameRoom != null){
            client.Out().sendMessage("GAME_ROOM.JOIN_ROOM.ALREADY_IN_ROOM");
            return;
        }
        BaseGameRoom room = GameRoomManager.findRoomByID(roomID);
        if (room == null){
            client.Out().sendMessage("GAME_ROOM.JOIN_ROOM.NOT_FOUND");
            return;
        }
        if (room.findPlayerByID(client.playerInfo.getUserID()) != null){
            client.Out().sendMessage("GAME_ROOM.JOIN_ROOM.EXISTED");
            return;
        }
        room.addPlayer(client);
        client.Out().sendJoinRoom(roomID);
    }
}
