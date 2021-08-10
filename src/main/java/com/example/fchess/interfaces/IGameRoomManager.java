package com.example.fchess.interfaces;

import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;

public interface IGameRoomManager {
     BaseGameRoom findRoomByID(String roomID);
     boolean createRoom();
     boolean removeRoom(BaseGameRoom room);
}
