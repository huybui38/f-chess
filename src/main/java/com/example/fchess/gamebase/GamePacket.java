package com.example.fchess.gamebase;

import com.example.fchess.enums.eChessPackage;
import com.example.fchess.interfaces.IGamePacket;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class GamePacket implements IGamePacket {
    String eventName;
    boolean isSuccess;
    Object data;
    private HashMap<String, Object> map;
    private final ObjectMapper mapper = new ObjectMapper();
    public GamePacket(String eventName){
        this.eventName = eventName;
        this.map = new HashMap<>();
    }
    public GamePacket(String eventName, Object data) {
        this(eventName);
        this.data = data;

    }
    public GamePacket(eChessPackage chessPackage){
        this(chessPackage.getValue());
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean writeData(String key, Object data){
        if (map.containsKey(key))
            return false;
        map.put(key, data);
        return true;
    }

    public boolean writeType(Object type){
        return this.writeData("type", type);
    }

    @Override
    public void serialize() {
        this.data = mapper.convertValue(map, Object.class);
    }

}
