package com.example.fchess.gameobjects.Xiangqi;

import com.example.fchess.enums.eXiangqiNotion;

import java.util.HashMap;

public class PiecesProcessor {
    protected HashMap<eXiangqiNotion, XiangqiPiece> processor;

    public PiecesProcessor() {
        this.processor = new HashMap<>();
        this.processor.put(eXiangqiNotion.HORSE, new Horse());
        this.processor.put(eXiangqiNotion.ELEPHANT, new Elephant());
        this.processor.put(eXiangqiNotion.ADVISOR, new Advisor());
        this.processor.put(eXiangqiNotion.PAwN, new Pawn());
        this.processor.put(eXiangqiNotion.KING, new King());
        this.processor.put(eXiangqiNotion.CANNON, new Cannon());
        this.processor.put(eXiangqiNotion.ROOK, new Rook());
    }

    public HashMap<eXiangqiNotion, XiangqiPiece> getProcessor() {
        return processor;
    }

    public void setProcessor(HashMap<eXiangqiNotion, XiangqiPiece> processor) {
        this.processor = processor;
    }
}
