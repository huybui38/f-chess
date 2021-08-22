package com.example.fchess.gameobjects.engine.ai;

import com.example.fchess.gameobjects.engine.Move;
import com.example.fchess.gameobjects.engine.XiangqiEngineV1;

public interface MoveStrategy {
    Move execute(final XiangqiEngineV1 engine);
    int getTotalNodes();
}
