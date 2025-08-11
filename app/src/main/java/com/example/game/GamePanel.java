package com.example.game;

import static com.example.game.MainActivity.GAME_HEIGHT;
import static com.example.game.MainActivity.GAME_WIDTH;

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
import com.example.game.environments.GameMap;
import com.example.game.helpers.GameConstants;
import com.example.game.inputs.TouchEvents;

import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private final Paint paint = new Paint();
    private final SurfaceHolder holder;
    private float x, y;
    private boolean movePlayer;
    private PointF lastTouchDiff;
    private final Random rand = new Random();
    private final GameLoop gameLoop;
    private final TouchEvents touchEvents;
    private final PointF skeletonPos;
    private int skeletonDir = GameConstants.FaceDir.DOWN;
    private long lastDirChange = System.currentTimeMillis();

    private int playerAnimationIndexY, playerFaceDir = GameConstants.FaceDir.RIGHT;
    private int animationTick;
    private final int animationSpeed = 10;
    private final GameMap testMap;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.BLUE);
        touchEvents = new TouchEvents(this);
        gameLoop = new GameLoop(this);


        skeletonPos = new PointF(rand.nextInt(GAME_WIDTH), rand.nextInt(GAME_HEIGHT));

        int[][] spriteIds = {
                {454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132},
                {454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132},
                {454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
                {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132},
                {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
                {454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132},
                {454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132}
        };

        testMap = new GameMap(spriteIds);

    }

    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        testMap.draw(c);

        touchEvents.draw(c);

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
                if (skeletonPos.y >= GAME_HEIGHT) {
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
                if (skeletonPos.x >= GAME_WIDTH) {
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

        updatePlayerMove(delta);

        updateAnimation();
    }

    private void updatePlayerMove(double delta) {
        if (!movePlayer) {
            return;
        }

        float baseSpeed = (float) delta * 300f;
        float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        float xSpeed = (float) Math.cos(angle);
        float ySpeed = (float) Math.sin(angle);

        if (xSpeed > ySpeed) {
            if (lastTouchDiff.x > 0) {
                playerFaceDir = GameConstants.FaceDir.RIGHT;
            } else {
                playerFaceDir = GameConstants.FaceDir.LEFT;
            }
        } else {
            if (lastTouchDiff.y > 0) {
                playerFaceDir = GameConstants.FaceDir.DOWN;
            } else {
                playerFaceDir = GameConstants.FaceDir.UP;
            }
        }

        if (lastTouchDiff.x < 0) {
            xSpeed *= -1;
        }
        if (lastTouchDiff.y < 0) {
            ySpeed *= -1;
        }

        x += xSpeed * baseSpeed;
        y += ySpeed * baseSpeed;

    }

    private void updateAnimation() {
        if (!movePlayer) {
            return;
        }

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
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            float newX = event.getX();
//            float newY = event.getY();
//
//            float xDiff = Math.abs(newX - x);
//            float yDiff = Math.abs(newY - y);
//
//            if (xDiff > yDiff) {
//                if (newX < x) {
//                    playerFaceDir = GameConstants.FaceDir.LEFT;
//                } else {
//                    playerFaceDir = GameConstants.FaceDir.RIGHT;
//                }
//            } else {
//                if (newY < y) {
//                    playerFaceDir = GameConstants.FaceDir.UP;
//                } else {
//                    playerFaceDir = GameConstants.FaceDir.DOWN;
//                }
//            }
//
//            x = newX;
//            y = newY;
//        }

        return touchEvents.touchEvent(event);
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

    public void setPlayerMoveTrue(PointF lastTouchDiff) {
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }

    public void setPlayerMoveFalse() {
        movePlayer = false;
        resetAnimation();
    }

    private void resetAnimation() {
        animationTick = 0;
        playerAnimationIndexY = 0;
    }
}
