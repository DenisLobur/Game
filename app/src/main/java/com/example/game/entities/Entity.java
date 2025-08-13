package com.example.game.entities;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {
    protected RectF hitbox;

    public Entity(PointF position, float width, float height) {
        this.hitbox = new RectF(position.x, position.y, width, height);
    }

    public RectF getHitbox() {
        return hitbox;
    }
}
