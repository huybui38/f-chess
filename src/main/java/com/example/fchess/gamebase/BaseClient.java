package com.example.fchess.gamebase;

import com.corundumstudio.socketio.SocketIOClient;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.*;

public class BaseClient extends AbstractBaseClient{
    private final int DEFAULT_TIMEOUT = 30;
    private static final Logger log = LoggerFactory.getLogger(BaseClient.class);

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
        if (disconnectedAt.isBefore(now)) {
            Seconds diff = Seconds.secondsBetween(disconnectedAt, now);
            log.debug("Compare {} {}",disconnectedAt.toString(), now.toString());
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
