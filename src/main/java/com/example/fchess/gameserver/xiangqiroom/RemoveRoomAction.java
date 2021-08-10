package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.gameserver.managers.GameRoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveRoomAction implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(CreateRoomAction.class);
    private BaseGameRoom game;
    public RemoveRoomAction(BaseGameRoom game){
        this.game = game;
    }
    @Override
    public void run() {
        if (!GameRoomManager.rooms.containsKey(game.getRoomID())){
            log.error("Remove Room: not found roomID "+ game.getRoomID());
        }
        int index = Integer.parseInt(game.getRoomID().split("_")[1]);
        GameRoomManager.rooms.remove(game.getRoomID());
        log.debug("Removed gameroomID: {}", game.getRoomID());
    }
}
