package com.example.game;

import static com.example.game.helpers.GameConstants.Sprite.SIZE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.game.entities.Character;
import com.example.game.entities.Player;
import com.example.game.entities.enemies.Skeleton;
import com.example.game.environments.MapManager;
import com.example.game.helpers.GameConstants;
import com.example.game.inputs.TouchEvents;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private final Paint paint = new Paint();
    private final SurfaceHolder holder;
    private float cameraX, cameraY;
    private boolean movePlayer;
    private PointF lastTouchDiff;
    private final Random rand = new Random();
    private final GameLoop gameLoop;
    private final TouchEvents touchEvents;
    private final MapManager mapManager;

    private Player player;
    private ArrayList<Skeleton> skeletons;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.BLUE);
        touchEvents = new TouchEvents(this);
        gameLoop = new GameLoop(this);
        mapManager = new MapManager();

        player = new Player();
        skeletons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            skeletons.add(new Skeleton(new PointF(100, 100)));
        }
    }

    public void render() {
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
        mapManager.draw(c);
        touchEvents.draw(c);

        drawPlayer(c);
        for (Skeleton skeleton : skeletons) {
            drawCharacter(c, skeleton);
        }

        holder.unlockCanvasAndPost(c);
    }

    private void drawPlayer(Canvas c) {
        c.drawBitmap(
                player.getGameCharType().getSprite(player.getAnimationIndex(), player.getFaceDir()),
                player.getHitbox().left,
                player.getHitbox().top,
                null
        );
    }

    public void drawCharacter(Canvas canvas, Character c) {
        canvas.drawBitmap(
                c.getGameCharType().getSprite(c.getAnimationIndex(), c.getFaceDir()),
                c.getHitbox().left + cameraX,
                c.getHitbox().top + cameraY,
                null
        );
    }

    public void update(double delta) {
        updatePlayerMove(delta);
        player.update(delta, movePlayer);
        for (Skeleton skeleton : skeletons) {
            skeleton.update(delta);
        }
        mapManager.setCameraValues(cameraX, cameraY);
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
                player.setFaceDir(GameConstants.FaceDir.RIGHT);
            } else {
                player.setFaceDir(GameConstants.FaceDir.LEFT);
            }
        } else {
            if (lastTouchDiff.y > 0) {
                player.setFaceDir(GameConstants.FaceDir.DOWN);
            } else {
                player.setFaceDir(GameConstants.FaceDir.UP);
            }
        }

        if (lastTouchDiff.x < 0) {
            xSpeed *= -1;
        }
        if (lastTouchDiff.y < 0) {
            ySpeed *= -1;
        }

        int playerWidth = SIZE;
        int playerHeight = SIZE;

        if (xSpeed <= 0) {
            playerWidth = 0;
        }
        if (ySpeed <= 0) {
            playerHeight = 0;
        }

        float deltaX = xSpeed * baseSpeed * -1;
        float deltaY = ySpeed * baseSpeed * -1;

        if (mapManager.canMoveHere(player.getHitbox().left + cameraX * -1 + deltaX * -1 + playerWidth, player.getHitbox().top + cameraY * -1 + deltaY * -1 + playerHeight)) {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        player.resetAnimation();
    }
}
