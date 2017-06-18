package com.example.home.halva;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Home on 05.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {

    // имя базы данных
    private static final String DATABASE_NAME = "mydatabase.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;


    // имя таблицы
    private static final String DATABASE_TABLE = "zatraty";
    // названия столбцов
    public static final String SLimita = "SLimita";
//    public static final String Ostatok_na_karte = "Ostatok_na_karte";
    public static final String date_ = "Date";
    public static final String Chto_Kupil = "Cto_Kupil";
    public static final String rassrochka = "Rassrochka_mesecev";
    public static final String summa_Pokup = "Summ_pokupki";
    public static final String S_v_mes = "S_v_mes";
    public static final String S_v_mes2 = "Platit_do";
    public static final String rassrochka_ostalos = "rassrochka_ostalos";


    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + SLimita + " real not null, "
//            + Ostatok_na_karte + " real not null, "
            + S_v_mes + " real not null, "
            + date_ + " text not null, "
            + Chto_Kupil + " text not null, "
            + rassrochka + " integer not null,"
            + summa_Pokup + " real not null,"
            + S_v_mes2 + " text not null,"
            + rassrochka_ostalos + " integer not null);";

    private static final String DATABASE2_TABLE = "datesumm";
    public static final String date_2ID = "Date2ID";
    public static final String date_2 = "Date2";
    public static final String Summ_date = "Summ_date";
    private static final String DATABASE_CREATE_SCRIPT2 = "create table "
            + DATABASE2_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + date_2ID + " integer not null, "
            + date_2 + " text not null, "
            + Summ_date + " real not null);";

    private static final String DATABASE3_TABLE = "datespis";
    public static final String dateid_ID = "Date2ID";
    public static final String date_pokup = "Date_pokup";
    public static final String Chto_Kupil2 = "Cto_Kupil";
    public static final String Summ_mes = "Summ_mes";
    private static final String DATABASE_CREATE_SCRIPT3 = "create table "
            + DATABASE3_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + dateid_ID + " integer not null, "
            + date_pokup + " text not null, "
            + Chto_Kupil2 + " text not null, "
            + Summ_mes + " real not null);";

    private static final String DATABASE4_TABLE = "ostatok";
    public static final String ost = "ost";
      private static final String DATABASE_CREATE_SCRIPT4 = "create table "
            + DATABASE4_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + ost + " real not null);";

    private DBHelper mDBHelper;
    private SQLiteDatabase db;

    DBHelper(String s, Context context, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    // данные по сумме в месяц
//    public Cursor getDateSummData() {
//        return db.query(DATABASE2_TABLE, null, null, null, null, null, null);
//    }

//    public Cursor getDateSpisData(long dateID) {
//        return db.query(DATABASE3_TABLE, null, dateid_ID + " = "
//                + dateID, null, null, null, null);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE_SCRIPT);
        db.execSQL(DATABASE_CREATE_SCRIPT2);
        db.execSQL(DATABASE_CREATE_SCRIPT3);
        db.execSQL(DATABASE_CREATE_SCRIPT4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }


}



