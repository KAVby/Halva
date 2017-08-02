package com.example.home.halva;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.home.halva.DBHelper.rassrochka_viplatil_mes;

/**
 * Created by Home on 18.05.2017.
 */

public class SvMes{
    final Calendar newCalendar=Calendar.getInstance();
    final Calendar now=Calendar.getInstance();



    public double Summa_v_Mes(Calendar c, DBHelper mDatabaseHelper2, SQLiteDatabase mSqLiteDatabase2) throws ParseException {

        double bp1=0; // ближайший платеж
//        TODO need to rename var bp1
         Calendar  DatePokupClone, c1Clone;
        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");

        Cursor cursor = mSqLiteDatabase2.query("zatraty", new String[]{mDatabaseHelper2._ID, mDatabaseHelper2.SLimita,
                         mDatabaseHelper2.date_, mDatabaseHelper2.Chto_Kupil,
                        mDatabaseHelper2.rassrochka, mDatabaseHelper2.summa_Pokup, mDatabaseHelper2.rassrochka_viplatil_mes},
                null, null,
                null, null, null);
        cursor.moveToLast();
        while (cursor.getPosition()>=0){
            c1Clone =(Calendar)c.clone();
            DatePokupClone=(Calendar)now.clone();
            DatePokupClone.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper2.date_))));
            DatePokupClone.set(Calendar.DAY_OF_MONTH,1);
            c1Clone.set(Calendar.DAY_OF_MONTH,1);
            if (DatePokupClone.compareTo(c1Clone)<0) {
                DatePokupClone.add(Calendar.MONTH, (cursor.getInt(cursor.getColumnIndex(mDatabaseHelper2.rassrochka)))); //прибавляем рассрочку к дате покупки
                DatePokupClone.set(Calendar.DAY_OF_MONTH,1);
                c1Clone.set(Calendar.DAY_OF_MONTH,1);
                if (DatePokupClone.compareTo(c1Clone) >= 0)//сравниваем дату платежа плюс рассрочка с текущей датой
                    bp1 = bp1 + cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper2.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper2.rassrochka));
            }
            cursor.moveToPrevious();
        }
        cursor.close();


        return bp1;
    }
}
