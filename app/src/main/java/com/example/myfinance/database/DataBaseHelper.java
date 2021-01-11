package com.example.myfinance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "financeAppDB";
    public static final String TABLE_MAIN = "money";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EARNED = "earned";
    public static final String COLUMN_SPENT = "spent";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MAIN + " (" + COLUMN_ID + " integer primary key, " + COLUMN_EARNED +
                " integer, " + COLUMN_SPENT +" integer, " + COLUMN_DAY + " integer, " + COLUMN_MONTH + " integer, " + COLUMN_YEAR + " integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Данный метод вызывается, когда необходимо обновление схемы базы данных
         * Можно прописать логику для перехода от старой БД к новой
         *
         * В данном проекте не актуален
         */

    }
}
