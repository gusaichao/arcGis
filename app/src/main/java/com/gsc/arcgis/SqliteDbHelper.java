package com.gsc.arcgis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDbHelper extends SQLiteOpenHelper {
    public SqliteDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table black_num(id integer primary key autoincrement, name varchar(220),type integer,image varchar(200),creat_date varchar(40),point varchar(400),remark varchar(400),update_date varchar(40)); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
