package com.example.fchess.gameserver.xiangqiroom;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.transmodel.GameDataPackage;

import java.util.List;
import java.util.Vector;

public abstract class BaseGameRoom {

    protected boolean isPlaying;
    protected String roomID;
    protected List<GameClient> players;
    protected int ready;

    public int getReady() {
        return ready;
    }

    protected GameClient[] slots;
    public GameClient[] getSlots() {
        return slots;
    }
    public String getRoomID() {
        return roomID;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public abstract void onPlayerReconnect(GameClient client);
    public abstract void onPlayerClosed(GameClient client);
    public abstract void onRemovePlayerFromSlot(GameClient client, int slot);
    public abstract void onAddPlayerToSlot(GameClient client, int slot);
    public abstract void onGameData(GameClient client, GameDataPackage data);
    public abstract void startGame();



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
