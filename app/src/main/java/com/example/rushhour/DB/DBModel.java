package com.example.rushhour.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBModel extends SQLiteOpenHelper {
    public DBModel(Context context) {
        super(context, "DBModel.db", null, 1);
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE player (id INTEGER PRIMARY KEY, "+
        "level INTEGER, soundEffect INTEGER, bgm INTEGER, color INTEGER)";
        String query2 = "CREATE TABLE records (level INTEGER PRIMARY KEY, "+
                "time INTEGER)";
//        String query3 = "CREATE TABLE map (id INTEGER PRIMARY KEY, level INTEGER, "+
//                "x INTEGER, y INTEGER, width INTEGER, height INTEGER, color INTEGER, direction INTEGER)";

        db.execSQL(query1);
        db.execSQL(query2);
//        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS player";
        String query2 = "DROP TABLE IF EXISTS records";
//        String query3 = "DROP TABLE IF EXISTS map";
        db.execSQL(query1);
        db.execSQL(query2);
//        db.execSQL(query3);
        onCreate(db);
    }
}
