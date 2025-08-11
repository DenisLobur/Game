package com.example.game.environments;

import static com.example.game.helpers.GameConstants.Sprite.DEFAULT_SIZE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.game.MainActivity;
import com.example.game.R;
import com.example.game.helpers.interfaces.BitmapMethods;

public enum Floor implements BitmapMethods {


    OUTSIDE(R.drawable.tileset_floor, 22, 26);

    private final Bitmap[] sprites;

    Floor(int resID, int tilesInWidth, int tilesInHeight) {
        options.inScaled = false;
        sprites = new Bitmap[tilesInWidth * tilesInHeight];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
        for (int j = 0; j < tilesInHeight; j++) {
            for (int i = 0; i < tilesInWidth; i++) {
                int index = j * tilesInWidth + i;
                sprites[index] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, DEFAULT_SIZE * i, DEFAULT_SIZE * j, DEFAULT_SIZE, DEFAULT_SIZE));
            }
        }
    }

    public Bitmap getSprite(int id) {
        return sprites[id];
    }
}
