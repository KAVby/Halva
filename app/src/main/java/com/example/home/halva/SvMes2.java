package com.example.home.halva;

import android.app.Activity;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.home.halva.DBHelper.rassrochka_ostalos;

/**
 * Created by Home on 18.05.2017.
 */

public class SvMes2 extends Activity{
    final Calendar newCalendar=Calendar.getInstance();
    final Calendar now=Calendar.getInstance();



    public double Summa_v_Mes(Calendar c, int i,DBHelper mDatabaseHelper2, SQLiteDatabase mSqLiteDatabase2) throws ParseException {

        double bp1=0; // ближайший платеж
//        TODO need to rename var bp1
        Calendar  DatePokupClone, c1Clone;
        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");



        Cursor cursor = mSqLiteDatabase2.query("zatraty", new String[]{mDatabaseHelper2._ID, mDatabaseHelper2.SLimita,
                        mDatabaseHelper2.Ostatok_na_karte, mDatabaseHelper2.S_v_mes, mDatabaseHelper2.date_, mDatabaseHelper2.Chto_Kupil,
                        mDatabaseHelper2.rassrochka, mDatabaseHelper2.summa_Pokup, mDatabaseHelper2.S_v_mes2, rassrochka_ostalos},
                null, null,
                null, null, null);
        cursor.moveToLast();
        ContentValues values2 = new ContentValues();// для таблицы покупок по месяцам
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
                if (DatePokupClone.compareTo(c1Clone) >= 0){//сравниваем дату платежа плюс рассрочка с текущей датой
                    Double m=cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper2.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper2.rassrochka));
                    bp1 = bp1 + m;
                    BigDecimal m1 = BigDecimal.valueOf(m).setScale(2, BigDecimal.ROUND_HALF_UP); // BigDecimal для округления
                    values2.put(DBHelper.dateid_ID, i);
                    values2.put(DBHelper.Chto_Kupil2, cursor.getString(cursor.getColumnIndex(mDatabaseHelper2.Chto_Kupil)));
                    values2.put(DBHelper.date_pokup, cursor.getString(cursor.getColumnIndex(mDatabaseHelper2.date_)));
                    values2.put(DBHelper.Summ_mes, m1.toString());
                    mSqLiteDatabase2.insert("datespis", null, values2);
                }}
            cursor.moveToPrevious();
        }
        cursor.close();
        BigDecimal m2 = BigDecimal.valueOf(bp1).setScale(2, BigDecimal.ROUND_HALF_UP); //округляем
        ContentValues values = new ContentValues();
        values.put(DBHelper.date_2ID, i);
        values.put(DBHelper.date_2, dateFormat.format(c.getTime()));
        values.put(DBHelper.Summ_date, m2.toString());
        mSqLiteDatabase2.insert("datesumm", null, values);

        return bp1;
    }
}
