package com.example.fchess.gamebase;

public class GamePacket {
    String eventName;
    Object data;

    public GamePacket(String eventName){
        this.eventName = eventName;
    }

    public GamePacket(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
