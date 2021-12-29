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
import com.example.fchess.transmodel.ClientGameRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        pkg.writeData("data", this.client.playerInfo.getNickName());
        pkg.serialize();
        this.sendToAllInRoom(pkg, roomID);
    }

    @Override
    public void sendPlayerSlots(BaseGameRoom room) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.SELECT_TEAM.getValue());
        GameClient[] slots = room.getSlots();
        pkg.writeData("black", slots[0] == null ? "Empty" : slots[0].playerInfo.getNickName());
        pkg.writeData("red", slots[1] == null ? "Empty" :  slots[1].playerInfo.getNickName());
        pkg.writeData("ready",room.getReady());
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
    }

    @Override
    public GamePacket sendInfoChessRoom(XiangqiGameRoom room) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.ROOM_INFO.getValue());
        GameClient[] slots = room.getSlots();
        pkg.writeData("black", slots[0] == null ? "Empty" : slots[0].playerInfo.getNickName());
        pkg.writeData("red", slots[1] == null ? "Empty" :  slots[1].playerInfo.getNickName());
        pkg.writeData("ready",room.getReady());
        pkg.writeData("isPlaying",room.isPlaying());
        pkg.writeData("host",room.getHost().playerInfo.getNickName());
        pkg.writeData("isHost",room.getHost() == this.client);

        if (room.isPlaying()){
            pkg.writeData("currentPosition", room.getGame().getCurrentPosition());
            pkg.writeData("turn", room.getGame().getCurrentTurn());
            pkg.writeData("isViewer", this.client.gamePlayer.isViewer());
            if (!this.client.gamePlayer.isViewer()){
                pkg.writeData("team", this.client.gamePlayer.getTeam());
            }
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
    public GamePacket sendEndGame(String winnerName) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_DATA);
        pkg.writeType(eGameData.GAME_END.getValue());
        pkg.writeData("winnerName", winnerName);
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
        return pkg;
    }

    @Override
    public GamePacket sendExitRoom(String name) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.EXIT_ROOM.getValue());
        pkg.writeData("name", name);
        pkg.serialize();
        this.sendToAllInRoom(pkg, this.client.currentBaseGameRoom.getRoomID());
        return pkg;
    }

    @Override
    public GamePacket sendChat(String message, String data, boolean isSystem){
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        pkg.writeType(eGameRoom.CHAT.getValue());
        pkg.writeData("message", message);
        pkg.writeData("isSystem", isSystem);
        pkg.writeData("data", data);
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

    @Override
    public GamePacket syncTime() {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_DATA);
        BaseGameRoom gameRoom = this.client.currentBaseGameRoom;
        pkg.writeType(eGameData.GAME_SYNC.getValue());
        pkg.writeData("time", gameRoom.getTimeLeft());
        pkg.serialize();
//        this.sendToAllInRoom(pkg, gameRoom.getRoomID());
        this.send(pkg);
        return pkg;
    }

    @Override
    public GamePacket sendListRoom(ConcurrentHashMap<String, BaseGameRoom> rooms) {
        GamePacket pkg = new GamePacket(eChessPackage.GAME_ROOM);
        BaseGameRoom gameRoom = this.client.currentBaseGameRoom;
        pkg.writeType(eGameRoom.LIST_ROOM.getValue());
        pkg.writeData("length", rooms.size());
        List<ClientGameRoom> roomList = new ArrayList<>();
        for (Map.Entry<String, BaseGameRoom> obj :
                rooms.entrySet()) {
            BaseGameRoom baseGameRoom = obj.getValue();
            ClientGameRoom clientGameRoom = new ClientGameRoom();
            clientGameRoom.setRoomID(baseGameRoom.getRoomID());
            clientGameRoom.setPlaying(baseGameRoom.isPlaying());
            clientGameRoom.setFirst(baseGameRoom.getSlots()[0] == null ? "Empty" :  baseGameRoom.getSlots()[0].playerInfo.getNickName());
            clientGameRoom.setSecond(baseGameRoom.getSlots()[1] == null ? "Empty" :  baseGameRoom.getSlots()[1].playerInfo.getNickName());
            roomList.add(clientGameRoom);
        }
        pkg.writeData("list", roomList);
        pkg.serialize();
        this.send(pkg);
        return pkg;
    }
}
