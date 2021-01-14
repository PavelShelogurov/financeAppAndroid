package com.example.myfinance.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class DataBaseSQLQuery {
    private static DataBaseSQLQuery instance;

    private DataBaseSQLQuery(){

    }

    public static DataBaseSQLQuery getInstance(){
        if (instance == null){
            instance = new DataBaseSQLQuery();
        }
        return instance;
    }

    //метод который ищет максимальный или минимальный элемент в определённом столбце
    public static int searchMinOrMaxIntValueOfColumnInDB (String minOrMax, String tableName, String columnName, String where, SQLiteDatabase database){
        Cursor cursor = database.query(tableName, new String[] {minOrMax + " (" + columnName + ")"}, where, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(minOrMax + " (" + columnName + ")"));
    }


}
