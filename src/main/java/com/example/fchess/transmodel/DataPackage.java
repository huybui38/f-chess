package com.example.fchess.transmodel;

public class DataPackage {
    private int type;
    private Object data;

    @Override
    public String toString() {
        return "GameRoomPackage{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }

    public DataPackage(){

    }
    public DataPackage(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
