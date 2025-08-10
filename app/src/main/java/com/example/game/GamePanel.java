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

import com.example.game.entities.GameCharacters;
import com.example.game.helpers.GameConstants;

import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint = new Paint();
    private SurfaceHolder holder;
    private float x, y;
    private Random rand = new Random();
    private GameLoop gameLoop;
    //    private ArrayList<PointF> skeletons = new ArrayList<>();
    private PointF skeletonPos;
    private int skeletonDir = GameConstants.FaceDir.DOWN;
    private long lastDirChange = System.currentTimeMillis();

    private int playerAnimationIndexY, playerFaceDir = GameConstants.FaceDir.RIGHT;
    private int animationTick;
    private int animationSpeed = 10;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.BLUE);

        skeletonPos = new PointF(rand.nextInt(1080), rand.nextInt(1920));

//        for (int i = 0; i < 50; i++) {
//            skeletons.add(new PointF(rand.nextInt(1080), rand.nextInt(1920)));
//        }

        gameLoop = new GameLoop(this);
    }

    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAnimationIndexY, playerFaceDir), x, y, null);

        c.drawBitmap(GameCharacters.SKELETON.getSprite(playerAnimationIndexY, skeletonDir), skeletonPos.x, skeletonPos.y, null);

//        for (PointF skeleton : skeletons) {
//            c.drawBitmap(GameCharacters.SKELETON.getSprite(0, 0), skeleton.x, skeleton.y, null);
//        }

        holder.unlockCanvasAndPost(c);
    }

    public void update(double delta) {
        if (System.currentTimeMillis() - lastDirChange >= 3000) {
            skeletonDir = rand.nextInt(4);
            lastDirChange = System.currentTimeMillis();
        }

        switch (skeletonDir) {
            case GameConstants.FaceDir.DOWN:
                skeletonPos.y += delta * 300;
                if (skeletonPos.y >= getHeight()) {
                    skeletonDir = GameConstants.FaceDir.UP;
                }
                break;

            case GameConstants.FaceDir.UP:
                skeletonPos.y -= delta * 300;
                if (skeletonPos.y <= 0) {
                    skeletonDir = GameConstants.FaceDir.DOWN;
                }
                break;

            case GameConstants.FaceDir.RIGHT:
                skeletonPos.x += delta * 300;
                if (skeletonPos.x >= getWidth()) {
                    skeletonDir = GameConstants.FaceDir.LEFT;
                }
                break;

            case GameConstants.FaceDir.LEFT:
                skeletonPos.x -= delta * 300;
                if (skeletonPos.x <= 0) {
                    skeletonDir = GameConstants.FaceDir.RIGHT;
                }
                break;
        }

        updateAnimation();
    }

    private void updateAnimation() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            playerAnimationIndexY++;
            if (playerAnimationIndexY >= 4) {
                playerAnimationIndexY = 0;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float newX = event.getX();
            float newY = event.getY();

            float xDiff = Math.abs(newX - x);
            float yDiff = Math.abs(newY - y);

            if (xDiff > yDiff) {
                if (newX < x) {
                    playerFaceDir = GameConstants.FaceDir.LEFT;
                } else {
                    playerFaceDir = GameConstants.FaceDir.RIGHT;
                }
            } else {
                if (newY < y) {
                    playerFaceDir = GameConstants.FaceDir.UP;
                } else {
                    playerFaceDir = GameConstants.FaceDir.DOWN;
                }
            }

            x = newX;
            y = newY;
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
}
