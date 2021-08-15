package com.example.fchess.gamepacket;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.enums.eGameData;
import com.example.fchess.enums.eGameRoom;
import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.gameserver.xiangqiroom.XiangqiGameRoom;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;
import com.example.fchess.interfaces.IChessSocket;
import com.example.fchess.interfaces.IPacketLib;

public class PacketClientLib implements IPacketLib, IChessSocket {
    private GameClient client;

    public PacketClientLib(GameClient client){
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
    public void sendPlayerSlots(BaseGameRoom room) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.SELECT_TEAM.getValue());
        GameClient[] slots = room.getSlots();
        pkg.writeData("red", slots[0] == null ? "" : slots[0].playerInfo.getUserID());
        pkg.writeData("black", slots[1] == null ? "" :  slots[1].playerInfo.getUserID());
        pkg.writeData("ready",room.getReady());
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
    }

    @Override
    public GamePacket sendInfoChessRoom(XiangqiGameRoom room) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.ROOM_INFO.getValue());
        GameClient[] slots = room.getSlots();
        pkg.writeData("red", slots[0] == null ? "" : slots[0].playerInfo.getUserID());
        pkg.writeData("black", slots[1] == null ? "" :  slots[1].playerInfo.getUserID());
        pkg.writeData("ready",room.getReady());
        pkg.writeData("isPlaying",room.isPlaying());
        if (room.isPlaying()){
            pkg.writeData("currentPosition", room.getGame().getCurrentPosition());
            pkg.writeData("turn", room.getGame().getCurrentTurn());
        }
        pkg.serialize();
        this.send(pkg);
        return pkg;
    }

    @Override
    public GamePacket sendGameDataBoard(String position, int turn) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_DATA);
        pkg.writeType(eGameData.GAME_DATA.getValue());
        pkg.writeData("position", position);
        pkg.writeData("turn", turn);
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
        return pkg;
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
