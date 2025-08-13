package com.example.game.entities;

import static com.example.game.helpers.GameConstants.Animation.AMOUNT;
import static com.example.game.helpers.GameConstants.Animation.SPEED;

import android.graphics.PointF;

import com.example.game.helpers.GameConstants;

public abstract class Character extends Entity {
    protected int animationTick, animationIndex;
    protected int faceDir = GameConstants.FaceDir.DOWN;
    protected GameCharacters gameCharType;

    public Character(PointF position, GameCharacters gameCharType) {
        super(position, 1, 1);
        this.gameCharType = gameCharType;
    }

    public void resetAnimation() {
        animationTick = 0;
        animationIndex = 0;
    }

    protected void updateAnimation() {
        animationTick++;
        if (animationTick >= SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= AMOUNT) {
                animationIndex = 0;
            }
        }
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getFaceDir() {
        return faceDir;
    }

    public GameCharacters getGameCharType() {
        return gameCharType;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }
}
