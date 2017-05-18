package com.example.home.halva;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.home.halva.DBHelper.rassrochka_ostalos;

/**
 * Created by Home on 18.05.2017.
 */

public class SvMes{
    final Calendar newCalendar=Calendar.getInstance();
    final Calendar now=Calendar.getInstance();
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;

    public double Summa_v_Mes(Calendar c ) throws ParseException {

        double bp1=0; // ближайший платеж
        int m1, m11, m12, m2;
        Calendar  DatePokupClone, c1Clone;
        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        //клонируем календари чтобы не испортить даты
        //  nowClone =(Calendar)c1.clone();
       // DatePokupClone=(Calendar)newCalendar.clone();
   //     mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                        mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, rassrochka_ostalos},
                null, null,
                null, null, null);
        cursor.moveToLast();
        int i=cursor.getPosition();
        while (cursor.getPosition()>=0){
            c1Clone =(Calendar)c.clone();
            DatePokupClone=(Calendar)now.clone();
            i=cursor.getPosition();
            DatePokupClone.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
            DatePokupClone.set(Calendar.DAY_OF_MONTH,1);
            c1Clone.set(Calendar.DAY_OF_MONTH,1);
            if (DatePokupClone.compareTo(c1Clone)<0) {
                DatePokupClone.add(Calendar.MONTH, (cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka)))); //прибавляем рассрочку к дате покупки
                DatePokupClone.set(Calendar.DAY_OF_MONTH,1);
                c1Clone.set(Calendar.DAY_OF_MONTH,1);
                if (DatePokupClone.compareTo(c1Clone) >= 0)//сравниваем дату платежа плюс рассрочка с текущей датой
                    bp1 = bp1 + cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            }
            cursor.moveToPrevious();
        }
        cursor.close();


        return bp1;
    }
}
