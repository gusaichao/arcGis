package com.gsc.arcgis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.gisinfo.android.core.base.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaoUtils {

    private SqliteDbHelper sqliteDbHelper;

    private String table_name = "black_num";

    private DaoUtils(Context context) {
        sqliteDbHelper = new SqliteDbHelper(context, "db_map.db", null, 1);
    }

    private static DaoUtils daoUtils;

    public static synchronized DaoUtils getInstance(Context context) {
        if (daoUtils == null) {
            daoUtils = new DaoUtils(context);
        }
        return daoUtils;
    }

    public void add(String name, int type, String image, String point, String remark) {
        SQLiteDatabase db = sqliteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type", type);
        values.put("image", image);
        values.put("creat_date", DateUtils.getStringByFormat(DateUtils.dateFormatYMDHMS));
        values.put("update_date", "");
        values.put("point", point);
        values.put("remark", remark);
        db.insert(table_name, null, values);
    }

    public List<DataBean> select() {
        List<DataBean> list = new ArrayList<>();
        SQLiteDatabase db = sqliteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from black_num order by id desc ", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String creat_date = cursor.getString(cursor.getColumnIndex("creat_date"));
            String update_date = cursor.getString(cursor.getColumnIndex("update_date"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String point = cursor.getString(cursor.getColumnIndex("point"));
            DataBean dataBean = new DataBean(id, name, type, image, creat_date, update_date, point, remark);
            list.add(dataBean);
        }
        cursor.close();
        SystemClock.sleep(1000);
        return list;
    }

    public void delete(String id) {
        SQLiteDatabase db = sqliteDbHelper.getWritableDatabase();
        db.delete(table_name, "id = ?", new String[]{id});
    }

    public void update(String id, String name, int type, String image, String point, String remark) {
        SQLiteDatabase db = sqliteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type", type);
        values.put("image", image);
        values.put("update_date", DateUtils.getStringByFormat(DateUtils.dateFormatYMDHMS));
        values.put("point", point);
        values.put("remark", remark);
        db.update(table_name, values, "id = ?", new String[]{id});
    }
}
