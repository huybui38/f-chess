package com.example.fchess.gameserver.xiangqiroom;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.transmodel.GameDataPackage;

import java.util.List;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public abstract class BaseGameRoom {

    protected boolean isPlaying;

    public boolean isBotRoom() {
        return isBotRoom;
    }

    protected boolean isBotRoom;
    protected String roomID;
    protected List<GameClient> players;

    public GameClient getHost() {
        return host;
    }

    protected GameClient host;
    protected int ready;

    public int getReady() {
        return ready;
    }

    protected GameClient[] slots;
    protected Future[] turnTimer;
    protected ScheduledExecutorService turnService;
    public GameClient[] getSlots() {
        return slots;
    }
    public String getRoomID() {
        return roomID;
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    public int getTotalPlayers(){
        return players.size();
    }
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public abstract void onPlayerReconnect(GameClient client);
    public abstract void onHostChanged(GameClient client);
    public abstract void onPlayerClosed(GameClient client);
    public abstract void onAfterPlayerMoved(GameClient client);
    public abstract void onRemovePlayerFromSlot(GameClient client, int slot);
    public abstract void onAddPlayerToSlot(GameClient client, int slot);
    public abstract void onGameData(GameClient client, GameDataPackage data);
    public abstract void startGame(int turnTime);
    public abstract void endGame(int teamWin);
    public abstract boolean canAddSlotBotRoom(GameClient player, int slot);
    public void resetRoom(){
        this.isPlaying = false;
    }

    public void changeHost(GameClient player){
        host = player;
        onHostChanged(player);
    }
    public boolean isHost(GameClient player){
        return host == player;
    }

    public BaseGameRoom(String roomID) {
        this.ready = 0;
        this.isPlaying = false;
        this.roomID = roomID;
        this.players = new Vector<>();
    }

    public boolean findSlot(int slot){
        if (slot > slots.length)
            return false;
        return slots[slot] == null;
    }
    public boolean isPlayerReady(GameClient client){
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i].equals(client)){
                return true;
            }
        }
        return false;
    }
    public abstract boolean canStartGame();
    public boolean addPlayerToSlot(int slot, GameClient player){
        if (slots[1-slot] != null && slots[1-slot] == player){
            this.removePlayerBySlot(1-slot);
        }
        if (slots[slot] == null){
            ready++;
            slots[slot] = player;
            onAddPlayerToSlot(player, slot);
            return true;
        }
        return false;
    }
    public boolean removePlayerByClient(GameClient player){
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i] == player){
                slots[i] = null;
                ready--;
                onRemovePlayerFromSlot(player, i);
                return true;
            }
        }
        return false;
    }
    public boolean removePlayerBySlot(int slot){
        if (slots[slot] != null){
            ready--;
            GameClient temp = slots[slot];
            slots[slot] = null;
            onRemovePlayerFromSlot(temp, slot);
            return true;
        }
        return false;
    }
    public void addPlayer(GameClient player){
        players.add(player);
        player.currentBaseGameRoom = this;
        player.getSocket().joinRoom(this.roomID);
    }
    public void removePlayer(GameClient player){
        players.remove(player);
        player.getSocket().leaveRoom(this.roomID);
        player.currentBaseGameRoom = null;
    }
    public GameClient findPlayerByID(String id){
        for (GameClient player:
             players) {
            if (player.playerInfo.getUserID().equals(id)){
                return player;
            }
        }
        return null;
    }

}
