package com.example.fchess.gameobjects;

import com.example.fchess.gameobjects.Xiangqi.XiangqiPiece;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.interfaces.IBoard;

import java.util.List;
import java.util.Vector;

public abstract class AbstractBoard implements IBoard{
    protected abstract void onStartGame();
    protected abstract void onEndGame();
    protected abstract void onPauseGame();
    public abstract void onReceiveGameData( String newPosition);
    protected GameClient red;
    protected GameClient black;

    public int getCurrentTurn() {
        return turn;
    }

    protected int turn;
    public void startGame(){
        onStartGame();
    }
}
