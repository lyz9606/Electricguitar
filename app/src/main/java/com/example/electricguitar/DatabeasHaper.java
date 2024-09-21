package com.example.electricguitar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabeasHaper extends SQLiteOpenHelper {
    public static final String TB_NAME = "user";
    public static final String tb_name="information";
    public DatabeasHaper(Context context, String name, SQLiteDatabase.CursorFactory factory, int verson){
        super(context,name,factory,verson);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT,account VARCHAR(50),password VARCHAR(100))");
        db.execSQL("CREATE TABLE information(id INTEGER PRIMARY KEY AUTOINCREMENT,account VARCHAR(50),title VARCHAR(50),inform VARCHAR(100),flag int(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion){
            db.execSQL("ALTER TABLE user ADD notes VARCHAR(200)");
        }

    }

    static void drop(SQLiteDatabase db){
        db.execSQL("DROP TABLE user");
    }

}
