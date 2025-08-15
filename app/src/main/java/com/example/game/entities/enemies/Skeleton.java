package com.example.game.entities.enemies;

import static com.example.game.main.MainActivity.GAME_HEIGHT;
import static com.example.game.main.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

import com.example.game.entities.Character;
import com.example.game.entities.GameCharacters;
import com.example.game.helpers.GameConstants;

import java.util.Random;

public class Skeleton extends Character {

    private long lastDirChange = System.currentTimeMillis();
    private Random rand = new Random();

    public Skeleton(PointF position) {
        super(position, GameCharacters.SKELETON);
    }

    public void update(double delta) {
        updateMove(delta);
        updateAnimation();
    }

    private void updateMove(double delta) {
        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            faceDir = rand.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }

        switch (faceDir) {
            case GameConstants.FaceDir.DOWN:
                hitbox.top += delta * 300;
                if (hitbox.top >= GAME_HEIGHT) {
                    faceDir = GameConstants.FaceDir.UP;
                }
                break;

            case GameConstants.FaceDir.UP:
                hitbox.top -= delta * 300;
                if (hitbox.top <= 0) {
                    faceDir = GameConstants.FaceDir.DOWN;
                }
                break;

            case GameConstants.FaceDir.RIGHT:
                hitbox.left += delta * 300;
                if (hitbox.left >= GAME_WIDTH) {
                    faceDir = GameConstants.FaceDir.LEFT;
                }
                break;

            case GameConstants.FaceDir.LEFT:
                hitbox.left -= delta * 300;
                if (hitbox.left <= 0) {
                    faceDir = GameConstants.FaceDir.RIGHT;
                }
                break;
        }
    }
}
