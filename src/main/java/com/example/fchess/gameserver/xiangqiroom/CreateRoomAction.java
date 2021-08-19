package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.gameserver.managers.GameRoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateRoomAction implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(CreateRoomAction.class);
    private GameClient client;
    public CreateRoomAction(GameClient client){
        this.client = client;
    }
    @Override
    public void run() {
        String roomID = "chess_" + GameRoomManager.getFreeRoom();
        if (GameRoomManager.rooms.containsKey(roomID)){
            log.error("Existed roomID:"+roomID);
        }
        BaseGameRoom game = new XiangqiGameRoom(roomID);
        game.addPlayer(client);
        game.changeHost(client);
        GameRoomManager.rooms.put(roomID, game);
        GamePacket response = new GamePacket(eChessPackage.GAME_ROOM);
        response.writeData("type", eGameRoom.CREATE_ROOM.getValue());
        response.writeData("roomID", roomID);
        response.serialize();

        client.Out().send(response);
        log.debug("Created gameRoomID: {}", roomID);
    }
}
