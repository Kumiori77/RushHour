package com.example.rushhour.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBController {
    static SQLiteDatabase db;
    DBModel dbModel;

    public DBController(Context content) {
        dbModel = new DBModel(content);
    }

    public void insert(int id, int level, int soundEffect, int bgm, int color) {
        try {
            db = dbModel.getWritableDatabase();
            String query = "INSERT INTO player(id, level, soundEffect, bgm, color) VALUES("+id+", "+level+","+soundEffect+","+bgm+","+color+")";
            db.execSQL(query);
            dbModel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 초기값 있는지 확인
    public boolean checkFirstValue() {
        try {
            boolean result = false;
            db = dbModel.getReadableDatabase();
            String query = "SELECT COUNT(*) FROM player";
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                if (cursor.getInt(0) < 1) {
                    result = false;
                }
                else {
                    result = true;
                }
            }
            cursor.close();
            dbModel.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 플래이어 테이블 select 메소드
    public int[] selectPlayer() {
        try {
            db = dbModel.getReadableDatabase();
            String query = "SELECT * FROM player";
            int[] result = new int[4];

            // 쿼리 실행
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                result[0] = cursor.getInt(1);
                result[1] = cursor.getInt(2);
                result[2] = cursor.getInt(3);
                result[3] = cursor.getInt(4);
            }
            cursor.close();
            dbModel.close();
            Log.d("level", ""+result[0]);
            Log.d("effect", ""+result[1]);
            Log.d("bgm", ""+result[2]);
            Log.d("color", ""+result[3]);
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 세팅값을 DB에 저장
    public void updateSetting(int effect, int bgm, int colorType) {
        try {
            db = dbModel.getWritableDatabase();
            String query = "UPDATE player SET soundEffect = " + effect + ", bgm = " + bgm + ", color = " + colorType +" WHERE id = 1";
            Log.d("LTE", query);
            db.execSQL(query);
            dbModel.close();
            Log.d("let", "?되는디..");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 레벨업 메소드
    public void levelUp(int level){
        try {
            db = dbModel.getWritableDatabase();
            String query = "UPDATE player SET level = " + level + " WHERE id = 1";
            db.execSQL(query);
            dbModel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            db = dbModel.getWritableDatabase();
            String query1 = "DELETE FROM player";
            String query2 = "DELETE FROM records";
            db.execSQL(query1);
            db.execSQL(query2);
            dbModel.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 기존 기록 확인 메소드
    public int getOldRecord(int level) {
        try {
            db = dbModel.getReadableDatabase();
//            String query = "SELECT COUNT(*) FROM records where level = "+level;
            String query = "SELECT time FROM records WHERE level = "+level;
            int result = 0;

            // 쿼리 실행
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                result = cursor.getInt(0);
            }
            cursor.close();
            dbModel.close();
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    // DB에 첫 기록 저장
    public void saveNewRecord(int level, int time){
        try {
            db = dbModel.getWritableDatabase();
            String query = "INSERT INTO records(level,time) VALUES("+level+", "+time+")";
            db.execSQL(query);
            dbModel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DB에 기록 갱신
    public void saveBestRecord(int level, int time) {
        try {
            db = dbModel.getWritableDatabase();
            String query = "UPDATE records SET time = " + time + " WHERE level = " + level;
            db.execSQL(query);
            dbModel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
