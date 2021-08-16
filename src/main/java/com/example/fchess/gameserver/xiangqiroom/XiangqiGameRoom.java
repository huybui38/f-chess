package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.gameobjects.Xiangqi.XiangqiBoard;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.transmodel.GameDataPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XiangqiGameRoom extends BaseGameRoom {
    private final Logger log = LoggerFactory.getLogger(XiangqiGameRoom.class);

    public XiangqiBoard getGame() {
        return game;
    }

    private XiangqiBoard game;
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
        if (this.isPlayerReady(client) && this.isPlaying == false){
            this.removePlayerByClient(client);
        }
    }
    @Override
    public void onRemovePlayerFromSlot(GameClient client, int slot) {
        client.Out().sendPlayerSlots( this);
        client.gamePlayer.setViewer(true);
        client.gamePlayer.setTeam(-1);
    }
    @Override
    public void onAddPlayerToSlot(GameClient client, int slot) {
        client.Out().sendPlayerSlots( this);
        client.gamePlayer.setViewer(false);
        client.gamePlayer.setTeam(slot);
    }

    @Override
    public void onGameData(GameClient client, GameDataPackage data) {
        if (game.getCurrentTurn() != client.gamePlayer.getTeam())
            return;
        boolean result = game.onReceiveGameData(data.getSource(), data.getTarget());
        if (result){
            client.Out().sendGameDataBoard(game.getCurrentPosition(), game.getCurrentTurn());
        }
    }

    @Override
    public void startGame() {
        if (this.ready == 2){
            game = new XiangqiBoard(slots[0], slots[1]);
            game.startGame();
            this.isPlaying = true;
            return;
        }
        log.error("startGame: not enough players");
    }
}
