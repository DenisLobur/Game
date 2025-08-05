package com.example.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint = new Paint();
    private SurfaceHolder holder;
    private ArrayList<RndSquare> squares = new ArrayList<>();
    private Random rand = new Random();
    private GameLoop gameLoop;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.BLUE);

        gameLoop = new GameLoop(this);
    }

    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        synchronized (squares) {
            for (RndSquare square : squares)
                square.draw(c);
        }

        holder.unlockCanvasAndPost(c);
    }

    public void update(double delta) {
        synchronized (squares) {
            for (RndSquare square : squares) {
                square.move(delta);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            PointF pos = new PointF(event.getX(), event.getY());
            int color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            int size = 25 + rand.nextInt(100);

            synchronized (squares) {
                squares.add(new RndSquare(pos, color, size));
            }
        }

        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startGameLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    private class RndSquare {
        private PointF pos;
        private int size;
        private Paint paint;
        private int xDir = 1, yDir = 1;

        public RndSquare(PointF pos, int color, int size) {
            this.pos = pos;
            this.size = size;
            paint = new Paint();
            paint.setColor(color);
        }

        public void move(double delta) {
            pos.x += xDir * delta * 300; // *300 frames per second
            if (pos.x < 0 || pos.x + size > getWidth()) {
                xDir *= -1; // Reverse direction on X-axis
            }

            pos.y += yDir * delta * 300; // *300 frames per second
            if (pos.y < 0 || pos.y + size > getHeight()) {
                yDir *= -1; // Reverse direction on Y-axis
            }
        }

        public void draw(Canvas c) {
            c.drawRect(pos.x, pos.y, pos.x + size, pos.y + size, paint);
        }
    }
}
