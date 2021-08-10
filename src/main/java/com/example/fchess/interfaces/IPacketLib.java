package com.example.fchess.interfaces;

import com.example.fchess.gameserver.GameClient;

public interface IPacketLib {
    void sendJoinRoom(String roomID);
    void sendJoinNotifyAllPlayersInRoom(String roomID);
    void sendPlayerChooseTeam(boolean isSuccess, String data, int team);
}
