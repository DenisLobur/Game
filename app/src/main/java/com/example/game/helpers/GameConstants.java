package com.example.game.helpers;

public final class GameConstants {
    public static final class FaceDir {
        public static final int DOWN = 0;
        public static final int UP = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
    }

    public static final class Sprite {
        public static final int DEFAULT_SIZE = 16;
        public static final int SCALED_MULTIPLIER = 6;
        public static final int SIZE = DEFAULT_SIZE * SCALED_MULTIPLIER;
    }

    public static final class Animation {
        public static final int SPEED = 10;
        public static final int AMOUNT = 4;
    }
}
