package com.example.home.halva;

import android.content.Context;
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
    public static final String date_ = "Date";
    //public static final String kurs_USD = "kurs_usd";
    public static final String summa_ = "Summ_pokupki";
    public static final String rassrochka = "Rassrochka_mesecev";
    public static final String prihod = "Polozil_na_kartu";
    public static final String dolg = "Nakopil_dolg";
    public static final String summa_na_karte = "Summa_na_karte";
//    public static final String cena_USD = "Cena_zapravki_USD";
//    public static final String cena_BYN = "Cena_zapravki_BYN";
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID + " integer primary key autoincrement, "
            + date_ + " text not null, "
            + summa_ + " real not null, "
            + rassrochka + " integer not null,"
            + prihod + " real not null,"
            + dolg + " real not null,"
            + summa_na_karte + " real not null);";



    DBHelper(Context context) {
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }


}



