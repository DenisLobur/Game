package com.example.game.entities;

import static com.example.game.main.MainActivity.GAME_HEIGHT;
import static com.example.game.main.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

public class Player extends Character {

    public Player() {
        super(new PointF(GAME_WIDTH / 2f, GAME_HEIGHT / 2f), GameCharacters.PLAYER);
    }

    public void update(double delta, boolean movePlayer) {
        if (movePlayer) {
            updateAnimation();
        }
    }
}
