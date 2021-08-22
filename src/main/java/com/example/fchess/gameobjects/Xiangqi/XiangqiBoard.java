package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eXiangqiNotion;
import com.example.fchess.gameobjects.AbstractBoard;
import com.example.fchess.gameobjects.AbstractPiece;
import com.example.fchess.gameobjects.engine.Move;
import com.example.fchess.gameobjects.engine.XiangqiEngineV1;
import com.example.fchess.gameobjects.engine.ai.MoveStrategy;
import com.example.fchess.gameobjects.engine.ai.NegaMax;
import com.example.fchess.gameserver.GameClient;
import com.example.fchess.gameserver.xiangqiroom.BaseGameRoom;
import com.example.fchess.gameserver.xiangqiroom.XiangqiGameRoom;
import com.example.fchess.interfaces.IBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class XiangqiBoard extends AbstractBoard {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    private final  Logger log = LoggerFactory.getLogger(XiangqiBoard.class);
    private XiangqiEngineV1 engine;
    private boolean isFightWithBot;
    private MoveStrategy aiPlayer;
    public String getCurrentPosition() {
        return engine.getFEN();
    }
    @Override
    protected void onStartGame() {

    }

    @Override
    protected void onEndGame() {

    }

    @Override
    protected void onPauseGame() {

    }

    @Override
    public void onBotTurn() {
        Move nextBotMove = aiPlayer.execute(engine);
        log.debug("total nodes:{}", aiPlayer.getTotalNodes());
        engine.move(nextBotMove);

    }

    @Override
    public boolean onReceiveGameData(String source, String destination) {
        if (source.length() != 2){
            return false;
        }

        boolean result = engine.move(source, destination);
        log.debug("{} {} : {}", source, destination, result);
        return result;
    }

    @Override
    public boolean isCheckmated() {
        return engine.getCheckmate();
    }

    @Override
    public int getCurrentTurn() {
        return engine.getSide();
    }

    @Override
    public boolean canEndGame() {
        return false;
    }

    @Override
    public void initBoard() {
        engine = new XiangqiEngineV1();
        engine.setBoard(INITIAL_POSITION);
        engine.generateMovesV1();
    }

    public XiangqiBoard(String fen, GameClient red, GameClient black) {
        this(red, black, false);
    }
    public XiangqiBoard(GameClient red, GameClient black, boolean isBotRoom){
        this.isFightWithBot = isBotRoom;
        this.red = red;
        this.black =  black;
        initBoard();
        if (isBotRoom){
            aiPlayer = new NegaMax(3);
        }
    }
}
