package com.example.game.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.game.helpers.interfaces.GameStateInterface;
import com.example.game.main.Game;

public class Menu extends BaseState implements GameStateInterface {

    private Paint paint;

    public Menu(Game game) {
        super(game);

        paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Canvas c) {
        c.drawText("MENU", 800, 200, paint);
    }

    @Override
    public void touchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            game.setGameState(Game.GameState.PLAYING);
        }
    }
}
