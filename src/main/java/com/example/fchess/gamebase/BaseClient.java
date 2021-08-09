package com.example.fchess.gamebase;

import com.corundumstudio.socketio.SocketIOClient;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.*;

public class BaseClient extends AbstractBaseClient{
    private final int DEFAULT_TIMEOUT = 30;


    public BaseClient(SocketIOClient socket, String userID) {
        super(socket, userID);
    }
    public void setSocket(SocketIOClient socket) {
        this.socket = socket;
    }
    public SocketIOClient getSocket() {
       return this.socket;
    }
    public void setDisconnectedAt(DateTime disconnectedAt) {
        this.disconnectedAt = disconnectedAt;
    }
    public boolean canRemove() {
        DateTime now = DateTime.now();
        if (disconnectedAt != null && disconnectedAt.isBefore(now)) {
            Seconds diff = Seconds.secondsBetween(disconnectedAt, now);
            return diff.getSeconds() >= DEFAULT_TIMEOUT;
        }
        return false;
    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {
        socket.sendEvent("connected", socket.getHandshakeData().getSingleUrlParam("token"));
    }
    @Override
    protected void onReconnect() {
        disconnectedAt = DateTime.now().plusDays(1);
    }

}
