package com.example.fchess.gameobjects;

import com.example.fchess.interfaces.IPiece;

public abstract class AbstractPiece implements IPiece {
    protected abstract void onMoved();
    protected abstract void onRemoved();
}
