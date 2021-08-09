package com.example.fchess.gameobjects;

import com.example.fchess.gameobjects.Xiangqi.XiangqiPiece;
import com.example.fchess.interfaces.IBoard;

import java.util.List;
import java.util.Vector;

public abstract class AbstractBoard implements IBoard{
    protected abstract void onStartGame();
    protected abstract void onEndGame();
    protected abstract void onPauseGame();
    protected abstract void onReceiveGameData(String orientation, String newPosition);
}
