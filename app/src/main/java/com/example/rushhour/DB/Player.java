package com.example.rushhour.DB;


public class Player {
    private static int level, color;
    private static boolean soundEffect, bgm;

    public Player(int level, int soundEffect, int bgm, int color) {
        this.level = level;
        this.soundEffect = soundEffect==1;
        this.bgm = bgm==1;
        this.color = color;
    }

    public static int getLevel() {
        return level;
    }

    public static int getColor() {
        return color;
    }

    public static boolean isSoundEffect() {
        return soundEffect;
    }

    public static boolean isBgm() {
        return bgm;
    }

    public static void setLevel(int level) {
        Player.level = level;
    }

    public static void setColor(int color) {
        Player.color = color;
    }

    public static void setSoundEffect(boolean soundEffect) {
        Player.soundEffect = soundEffect;
    }

    public static void setBgm(boolean bgm) {
        Player.bgm = bgm;
    }
}
