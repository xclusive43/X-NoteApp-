package com.xclusive.x_note.SQlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public static final String DBNAME = "NOTEDB";
    public static final String TABLENAME = "NOTES";
    public static final String COL1 = "TITLE";
    public static final String COL2 = "SUBTITLE";
    public static final String COL3 = "DESCRIPTION";
    public static final String COL4 = "DATE";
    public static final String COL5 = "DATA";
    public static final String COL6 = "COLOR";
    public DataBase(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLENAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + COL1 + " TEXT,"
                + COL2 + "  TEXT,"
                + COL3 + " TEXT,"
                + COL4 + " TEXT,"
                + COL5 + " TEXT,"
                + COL6 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public Long insertdata(String ID, String TITLE, String SUBTITILE, String DEC, String DATE, String DATA, String COLOR, int token) {
        Long res1 = null;
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        if (token == 1) {
            contentValues.put(COL1, TITLE);
            contentValues.put(COL2, SUBTITILE);
            contentValues.put(COL3, DEC);
            contentValues.put(COL4, DATE);
            contentValues.put(COL5, DATA);
            contentValues.put(COL6, COLOR);
            res1 = db.insert(TABLENAME, null, contentValues);

        } else if (token == 0) {
            contentValues.put("ID", ID);
            contentValues.put(COL1, TITLE);
            contentValues.put(COL2, SUBTITILE);
            contentValues.put(COL3, DEC);
            contentValues.put(COL4, DATE);
            contentValues.put(COL5, DATA);
            contentValues.put(COL6, COLOR);
            res1 = db.insert(TABLENAME, null, contentValues);
        }

        return res1;
    }

    public void deletedata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLENAME, "ID = ?", new String[]{id});

    }

    public Cursor getAlldata() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLENAME + ";", null);
    }

    public Integer update(String id, String TITLE, String SUBTITILE, String DEC, String DATE, String DATA, String COLOR) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put(COL1, TITLE);
        contentValues.put(COL2, SUBTITILE);
        contentValues.put(COL3, DEC);
        contentValues.put(COL4, DATE);
        contentValues.put(COL5, DATA);
        contentValues.put(COL6, COLOR);
        return db.update(TABLENAME, contentValues, "ID = ?", new String[]{id});
    }

}
