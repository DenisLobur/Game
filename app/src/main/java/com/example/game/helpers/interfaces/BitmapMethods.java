package com.example.game.helpers.interfaces;

import static com.example.game.helpers.GameConstants.Sprite.SCALED_MULTIPLIER;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public interface BitmapMethods {
    BitmapFactory.Options options = new BitmapFactory.Options();

    default Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * SCALED_MULTIPLIER, bitmap.getHeight() * SCALED_MULTIPLIER, false);
    }
}
