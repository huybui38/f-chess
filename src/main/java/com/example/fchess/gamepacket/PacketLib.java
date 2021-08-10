package com.example.fchess.gamepacket;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IChessSocket;
import com.example.fchess.interfaces.IPacketLib;

public class PacketLib implements IPacketLib, IChessSocket {
    private GameClient client;

    public PacketLib(GameClient client){
        this.client = client;
    }

    @Override
    public void sendJoinRoom(String roomID) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.JOIN_ROOM.getValue());
        pkg.writeData("roomID", roomID);
        pkg.writeData("msg", "GAME_ROOM.JOIN_ROOM.SUCCESS");
        pkg.serialize();
        this.send(pkg);
    }

    @Override
    public void sendJoinNotifyAllPlayersInRoom(String roomID) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.CHAT.getValue());
        pkg.writeData("isSystem", true);
        pkg.writeData("message", "GAME_ROOM.CHAT.PLAYER_JOINED");
        pkg.writeData("data", this.client.playerInfo.getUserID());
        pkg.serialize();
        this.sendToAllInRoom(pkg, roomID);
    }

    @Override
    public void sendPlayerChooseTeam(boolean isAdded, String data, int team) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.SELECT_TEAM.getValue());
        pkg.writeData("isAdded", isAdded);
        pkg.writeData("data", data);
        pkg.writeData("team", team);
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
    }

    @Override
    public void sendToAllInRoom(GamePacket gamePacket, String roomID) {
        client.getSocket().getNamespace().getRoomOperations(roomID).sendEvent(gamePacket.getEventName(), gamePacket.getData());
    }

    @Override
    public void sendToAll(GamePacket gamePacket) {
        client.getSocket().getNamespace().getBroadcastOperations().sendEvent(gamePacket.getEventName(), gamePacket.getData());
    }

    @Override
    public void send(GamePacket gamePacket) {
        client.getSocket().sendEvent(gamePacket.getEventName(), gamePacket.getData());
    }
    @Override
    public void sendMessage(String msg){
        GamePacket pkg = new GamePacket(eChessPackage.NOTIFY);
        pkg.writeData("msg", msg);
        pkg.serialize();
        this.send(pkg);
    }
}
