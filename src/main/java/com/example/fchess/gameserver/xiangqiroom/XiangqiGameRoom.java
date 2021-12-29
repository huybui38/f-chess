package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.gameobjects.Xiangqi.XiangqiBoard;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.transmodel.GameDataPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class XiangqiGameRoom extends BaseGameRoom {
    public static final int RED = 1;
    public static final int BLACK = 0;
    public static final int DEFAULT_GAME_TIME = 180;
    private final Logger log = LoggerFactory.getLogger(XiangqiGameRoom.class);
    private CountDownTurnAction[] turnActions = new CountDownTurnAction[2];
    public XiangqiBoard getGame() {
        return game;
    }
    private XiangqiBoard game;
    private int turnTime;
    private int botTeam;
    public XiangqiGameRoom(String roomID, GameClient host, boolean isBotRoom) {
        super(roomID);
        slots = new GameClient[2];
        turnTimer = new Future[2];
        this.host = host;
        turnTime = DEFAULT_GAME_TIME;
        turnService = Executors.newScheduledThreadPool(1);
        turnActions[XiangqiGameRoom.RED] = new CountDownTurnAction(DEFAULT_GAME_TIME, this, XiangqiGameRoom.RED);
        turnActions[XiangqiGameRoom.BLACK] = new CountDownTurnAction(DEFAULT_GAME_TIME, this, XiangqiGameRoom.BLACK);
        this.isBotRoom = isBotRoom;
        if (this.isBotRoom){

        }
    }

    @Override
    public long[] getTimeLeft() {
        long[] data = new long[2];
        data[XiangqiGameRoom.RED] =  turnActions[XiangqiGameRoom.RED].getTimeLeft();
        data[XiangqiGameRoom.BLACK] =  turnActions[XiangqiGameRoom.BLACK].getTimeLeft();
        return data;
    }

    @Override
    public boolean canStartGame() {
        return this.ready == 2 || (isBotRoom && this.ready == 1);
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
    public void onHostChanged(GameClient player) {
        player.Out().sendInfoChessRoom(this);
    }



    @Override
    public boolean canAddSlotBotRoom(GameClient player, int slot) {
        return ready == 0 || player == slots[slot ^ 1];
    }

    @Override
    public void onPlayerClosed(GameClient client) {
        if (this.isPlayerReady(client) && this.isPlaying == false){
            this.removePlayerByClient(client);
        }
        if (isHost(client)){
            for (int i = 0; i < players.size(); i++) {
                GameClient temp = players.get(i);
                if (temp != client && temp.isConnected()){
                    changeHost(temp);
                }
            }
        }
    }

    @Override
    public void onAfterPlayerMoved(GameClient client) {
        stopTurnTimer(game.getCurrentTurn() ^ 1);
        startTurnTimer(game.getCurrentTurn());
        GameClient temp = client == null ? slots[botTeam ^ 1] : client;
        temp.Out().sendGameDataBoard(game.getCurrentPosition(), game.getCurrentTurn());
        if (game.isCheckmated()){
            endGame(client == null ? botTeam : client.gamePlayer.getTeam());
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
        if (isBotRoom){
            botTeam = slot ^ 1;
        }
    }

    @Override
    public void onGameData(GameClient client, GameDataPackage data) {
        if (game.getCurrentTurn() != client.gamePlayer.getTeam())
            return;
        boolean result = game.onReceiveGameData(data.getSource(), data.getTarget());
        if (result){
            onAfterPlayerMoved(client);
            if (isBotRoom){
                game.onBotTurn();
                onAfterPlayerMoved(null);
            }
        }
    }
    @Override
    public void startGame(int turnTime) {
        if (canStartGame()){
            this.turnTime = turnTime;
            resetTurnTimer();
            game = new XiangqiBoard(slots[0], slots[1], isBotRoom);
            game.startGame();
            this.isPlaying = true;
            startTurnTimer(RED);
            if (isBotRoom && botTeam == RED){
                game.onBotTurn();
                onAfterPlayerMoved(null);
            }
//            startTurnTimer(BLACK);
            return;
        }
        log.error("startGame: not enough players");
    }
    private void startTurnTimer(int team){
        turnTimer[team] = turnService.scheduleAtFixedRate(turnActions[team], 0, 1000, TimeUnit.MILLISECONDS);
    }
    private void stopTurnTimer(int team){
        if (turnTimer[team] != null)
            turnTimer[team].cancel(true);
    }
    private void resetTurnTimer(){
        if (turnTimer[RED] != null && turnTimer[RED].isCancelled() == false){
            turnTimer[RED].cancel(true);
        }
        if (turnTimer[BLACK] != null &&  turnTimer[BLACK].isCancelled() == false){
            turnTimer[BLACK].cancel(true);
        }
        turnActions[XiangqiGameRoom.RED] = new CountDownTurnAction(turnTime, this, XiangqiGameRoom.RED);
        turnActions[XiangqiGameRoom.BLACK] = new CountDownTurnAction(turnTime, this, XiangqiGameRoom.BLACK);
    }

    @Override
    public void endGame(int teamWin) {
        if (this.isPlaying()){
            if (isBotRoom){
                int humanTeam = botTeam ^ 1;
                boolean isHumanWin = teamWin != botTeam;
                this.slots[humanTeam].gamePlayer.setWin(isHumanWin);
                if (isHumanWin){
                    this.slots[humanTeam].gamePlayer.onWinning();
                }
                this.slots[humanTeam].Out().sendEndGame(isHumanWin ? this.slots[humanTeam].playerInfo.getNickName() : "BOT");
            }else {
                this.slots[teamWin].gamePlayer.setWin(true);
                this.slots[teamWin].gamePlayer.onWinning();
                this.slots[1-teamWin].gamePlayer.setWin(false);
                this.slots[1-teamWin].gamePlayer.onLosing();
                this.slots[teamWin].Out().sendEndGame(this.slots[teamWin].playerInfo.getNickName());
            }
            resetRoom();
            resetTurnTimer();
        }
    }

    @Override
    public void resetRoom() {
        super.resetRoom();
        removePlayerBySlot(XiangqiGameRoom.RED);
        removePlayerBySlot(XiangqiGameRoom.BLACK);
        game = null;
    }
}
