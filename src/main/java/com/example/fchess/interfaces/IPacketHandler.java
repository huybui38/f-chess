package com.example.fchess.interfaces;

import com.example.fchess.gameserver.GameClient;

public interface IPacketHandler {
    void handle(GameClient client, Object data);
}
