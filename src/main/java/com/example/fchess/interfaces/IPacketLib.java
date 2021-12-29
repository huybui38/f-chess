package com.example.fchess.interfaces;

import com.example.fchess.gamebase.GamePacket;
import com.example.fchess.gameserver.xiangqiroom.XiangqiGameRoom;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;

import java.util.concurrent.ConcurrentHashMap;

public interface IPacketLib {
    void sendJoinRoom(String roomID);
    void sendJoinNotifyAllPlayersInRoom(String roomID);
    void sendPlayerSlots(BaseGameRoom room);
    GamePacket sendInfoChessRoom(XiangqiGameRoom room);
    GamePacket sendGameDataBoard(String position, int turn);
    GamePacket sendEndGame(String winnerName);
    GamePacket sendExitRoom(String name);
    GamePacket sendChat(String message, String data, boolean isSystem);
    GamePacket syncTime();
    GamePacket sendListRoom(ConcurrentHashMap<String, BaseGameRoom> rooms);
}
