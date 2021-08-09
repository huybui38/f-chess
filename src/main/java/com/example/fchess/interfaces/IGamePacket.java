package com.example.fchess.interfaces;

public interface IGamePacket {
     boolean writeData(String key, Object data);
     void serialize();
}
