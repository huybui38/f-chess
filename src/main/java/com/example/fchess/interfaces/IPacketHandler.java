package com.example.fchess.interfaces;

import com.example.fchess.gamebase.GameClient;

public interface IPacketHandler {
    void handle(GameClient client, Object data);
}
