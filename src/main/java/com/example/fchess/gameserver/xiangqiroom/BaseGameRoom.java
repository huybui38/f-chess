package com.example.fchess.gameserver.xiangqiroom;

import com.example.fchess.gameserver.GameClient;

import java.util.List;
import java.util.Vector;

public abstract class BaseGameRoom {
    private boolean isPLaying;
    private String roomID;
    public String getRoomID() {
        return roomID;
    }
    private List<GameClient> players;
    protected int ready;
    protected GameClient[] slots;
    public abstract void onPlayerReconnect(GameClient client);
    public abstract void onPlayerClosed(GameClient client);
    public abstract void onRemovePlayerFromSlot(GameClient client, int slot);
    public abstract void onAddPlayerToSlot(GameClient client);

    public BaseGameRoom(String roomID) {
        ready = 0;
        isPLaying = false;
        this.roomID = roomID;
        players = new Vector<>();
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
        if (slots[slot] == null){
            ready++;
            slots[slot] = player;
            onAddPlayerToSlot(player);
            return true;
        }
        return false;
    }
    public boolean removePlayerByClient(GameClient player){
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null && slots[i] == player){
                onRemovePlayerFromSlot(slots[i], i);
                slots[i] = null;
                return true;
            }
        }
        return false;
    }
    public boolean removePlayerBySlot(int slot){
        if (slots[slot] != null){
            ready--;
            onRemovePlayerFromSlot(slots[slot], slot);
            slots[slot] = null;
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
