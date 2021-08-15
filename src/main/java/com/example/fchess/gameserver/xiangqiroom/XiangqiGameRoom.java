package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.gameserver.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XiangqiGameRoom extends BaseGameRoom {
    private final Logger log = LoggerFactory.getLogger(XiangqiGameRoom.class);


    public XiangqiGameRoom(String roomID) {
        super(roomID);
        slots = new GameClient[2];
    }

    @Override
    public boolean canStartGame() {
        return this.ready == 2;
    }

    @Override
    public void addPlayer(GameClient player) {
        super.addPlayer(player);

        player.Out().sendJoinNotifyAllPlayersInRoom(this.getRoomID());
        player.Out().sendInfoChessRoom(this);
    }

    @Override
    public void onPlayerReconnect(GameClient player) {
        player.Out().sendJoinNotifyAllPlayersInRoom(this.getRoomID());
        player.Out().sendInfoChessRoom(this);
        log.debug("onPlayerReconnect");
    }

    @Override
    public void onPlayerClosed(GameClient client) {
        if (this.isPlayerReady(client)){
            this.removePlayerByClient(client);
        }
    }
    @Override
    public void onRemovePlayerFromSlot(GameClient client, int slot) {
        client.Out().sendPlayerSlots( this);
        client.setViewer(true);
    }
    @Override
    public void onAddPlayerToSlot(GameClient client) {
        client.Out().sendPlayerSlots( this);
        client.setViewer(false);
    }
}
