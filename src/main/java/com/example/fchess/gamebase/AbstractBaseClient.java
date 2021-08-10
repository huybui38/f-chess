package com.example.fchess.gamebase;

import com.corundumstudio.socketio.SocketIOClient;
import org.joda.time.DateTime;

public abstract class AbstractBaseClient {
    protected abstract void onDisconnected();
    protected abstract void onConnected();
    protected abstract void onReconnect();
    protected abstract void onClosed();
    protected SocketIOClient socket;
    protected DateTime disconnectedAt;
    protected String userID;

    public AbstractBaseClient(SocketIOClient socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }
}
