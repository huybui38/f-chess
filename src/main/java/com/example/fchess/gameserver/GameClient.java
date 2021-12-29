package com.example.fchess.gameserver;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.fchess.gamebase.BaseClient;
import com.example.fchess.gamepacket.PacketClientLib;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;
import com.example.fchess.web.model.User;
import com.example.fchess.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GameClient extends BaseClient {

    public PlayerInfo playerInfo;
    public GamePlayer gamePlayer;
    public BaseGameRoom currentBaseGameRoom;
    private PacketClientLib out;
    public PacketClientLib Out(){
        return out;
    }
    public GameClient(SocketIOClient socket, String userID) {
        super(socket, userID);
        LoadFromDatabase();
        out = new PacketClientLib(this);
        gamePlayer = new GamePlayer(userID);
    }
    private void LoadFromDatabase(){
//        playerInfo = new PlayerInfo(nickname, userID);
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();
        currentBaseGameRoom.removePlayer(this);
        currentBaseGameRoom = null;
    }

    @Override
    protected void onClosed() {
        super.onClosed();
        if (currentBaseGameRoom != null){
            currentBaseGameRoom.onPlayerClosed(this);
        }
    }

    @Override
    protected void onConnected() {
        super.onConnected();
    }

    @Override
    protected void onReconnect() {
        super.onReconnect();
        if (currentBaseGameRoom != null){
            this.Out().sendJoinRoom(currentBaseGameRoom.getRoomID());
            this.getSocket().joinRoom(currentBaseGameRoom.getRoomID());
            currentBaseGameRoom.onPlayerReconnect(this);
//            if (currentBaseGameRoom.isPlaying() && currentBaseGameRoom.isPlayerReady(this))
        }
    }



}
