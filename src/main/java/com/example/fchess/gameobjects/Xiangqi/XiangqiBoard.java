package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.gameobjects.AbstractBoard;
import com.example.fchess.gameobjects.engineV2.XiangqiEngineV2;
import com.example.fchess.gameserver.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XiangqiBoard extends AbstractBoard {
    private final String INITIAL_POSITION = "rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR";
    private final Logger log = LoggerFactory.getLogger(XiangqiBoard.class);
    public String getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }
    protected String currentPosition;//FEN
    private XiangqiEngineV2 engineV2;

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
    public boolean onReceiveGameData(String source, String destination) {
        /*
        if this moving is valid then toggle turn
         */
        engineV2.setTurn(turn);
        if (source.length() != 2) {
            return false;
        }


//        if (turn == 1 && board[source].matches("^r")){
//
//        }
        log.debug(source, destination);
        boolean validateMove = engineV2.makeMove(source, destination);

        if (!validateMove) {
            return false;
        }

        setCurrentPosition(engineV2.convertBoardToFen(engineV2.getChessBoard()));

        turn = 1 - turn;
        return true;
    }

    public XiangqiBoard(String fen, GameClient red, GameClient black) {
        this(red, black);
        setCurrentPosition(fen);
        engineV2 = new XiangqiEngineV2(currentPosition);
    }

    public XiangqiBoard(GameClient red, GameClient black) {
        this.red = red;
        this.black = black;
        initBoard();
        engineV2 = new XiangqiEngineV2(currentPosition);
    }

    @Override
    public boolean canEndGame() {
        return false;
    }

    @Override
    public void initBoard() {
        currentPosition = INITIAL_POSITION;
    }
}

