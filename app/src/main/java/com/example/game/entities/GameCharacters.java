package com.example.game.entities;

import static com.example.game.helpers.GameConstants.Sprite.DEFAULT_SIZE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.game.MainActivity;
import com.example.game.R;
import com.example.game.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {
    PLAYER(R.drawable.player_spritesheet),
    SKELETON(R.drawable.skeleton_spritesheet);

    private final Bitmap spriteSheet;
    private final Bitmap[][] sprites = new Bitmap[7][4];


    GameCharacters(int resID) {
        options.inScaled = false; // Disable scaling to avoid issues with sprite size
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for (int j = 0; j < sprites.length; j++) {
            for (int i = 0; i < sprites[j].length; i++) {
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, DEFAULT_SIZE * i, DEFAULT_SIZE * j, DEFAULT_SIZE, DEFAULT_SIZE));
            }
        }
    }

    @SuppressWarnings("unused")
    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }

    public Bitmap getSprite(int row, int col) {
        if (row < 0 || row >= sprites.length || col < 0 || col >= sprites[row].length) {
            throw new IndexOutOfBoundsException("Invalid sprite index");
        }
        return sprites[row][col];
    }


}
