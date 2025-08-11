package com.example.game.environments;

import static com.example.game.helpers.GameConstants.Sprite.SIZE;

import android.graphics.Canvas;

public class GameMap {
    private final int[][] spriteIds;

    public GameMap(int[][] spriteIds) {
        this.spriteIds = spriteIds;
    }

    public void draw(Canvas c) {
        for (int j = 0; j < spriteIds.length; j++) {
            for (int i = 0; i < spriteIds[j].length; i++) {
                c.drawBitmap(Floor.OUTSIDE.getSprite(spriteIds[j][i]), i * SIZE, j * SIZE, null); // Adjust coordinates as needed
            }
        }
    }
}
