package com.example.fchess.gameserver.xiangqiroom;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class CountDownTurnAction implements Runnable {
    private final Logger log = LoggerFactory.getLogger(CountDownTurnAction.class);

    public long getTimeLeft() {
        return timeLeft;
    }

    private long timeLeft;
    private BaseGameRoom room;
    private int team;
    public CountDownTurnAction(long timeLeft, BaseGameRoom room, int team){
        this.timeLeft = timeLeft;
        this.room = room;
        this.team = team;
    }
    @Override
    public void run() {
        this.timeLeft--;
        room.syncTime();
        log.debug("Count down turn team side: {} - value: {}", team, timeLeft);
        if (this.timeLeft <=0){
            room.endGame(1 - team);
        }
    }
}
