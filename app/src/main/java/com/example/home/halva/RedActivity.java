package com.example.home.halva;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.home.halva.DBHelper.ost;

/**
 * Created by kureichyk on 15.07.2017.
 */

public class RedActivity extends Activity {
    EditText editChto, editRassrMes, editSummPok;
    private EditText txtRegWinBD;
    private DatePickerDialog dateBirdayDatePicker;
    Calendar dd;
    final Calendar newCalendar=Calendar.getInstance();
    final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
    String id;
    Cursor cursor, cursor2;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //todo поприсваивать редактируемые значения в текстовые поля
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);
        txtRegWinBD=(EditText)findViewById(R.id.txtRegWindowBD);
        editChto=(EditText) findViewById(R.id.editChto);
        editRassrMes=(EditText) findViewById(R.id.editRassrMes);
        editSummPok=(EditText) findViewById(R.id.editSummPok);
        initDateBuyDatePicker();
        id=getIntent().getStringExtra("id");
        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
       //заполняем по id поля формы редактирования
        cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper.SLimita,
                        mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, mDatabaseHelper.rassrochka_ostalos}, "_ID = ?" ,  new String[]{id},
                null, null, null);
        cursor.moveToLast();
        dd=(Calendar)newCalendar.clone();
        try {
            dd.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtRegWinBD.setText(dateFormat.format(dd.getTime()));
        editChto.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Chto_Kupil)));
        editRassrMes.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka)));
        editSummPok.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup)));
    }


    public  void onClickSave(View v){
        Double S,S2, Ost, Sopl, Sopl2; // S сумма покупки до редактирования, S2 сумма покупки после редактирования, Ost новый остаток на карте после редактирования, Sopl сумма уже оплаченная по месяцам
        cursor2 = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper.SLimita,
                        mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, mDatabaseHelper.rassrochka_ostalos}, "_ID = ?" ,  new String[]{id},
                null, null, null);

        cursor2.moveToLast();

        S=  cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.summa_Pokup));//-(cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.summa_Pokup)));
        S2= Double.parseDouble(editSummPok.getText().toString());




        cursor = mSqLiteDatabase.query("ostatok", null,
                null, null,
                null, null, null);
        cursor.moveToLast();
        //  todo реализовать проверку чтобы новая рассрочка не оказалась меньше уже оплаченных месяцев
        //расчитываю остаток на карте : вначале удалю старую потом добавлю новую покупку с учетом сделанных платежей
        Ost =cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.ost))+S-cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.rassrochka_ostalos))*S/cursor2.getInt(cursor2.getColumnIndex(mDatabaseHelper.rassrochka));
        Ost= Ost-S2+cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.rassrochka_ostalos))*S2/Double.parseDouble(editRassrMes.getText().toString());
        ContentValues newrassr = new ContentValues();
        newrassr.put(ost, Ost);
        mSqLiteDatabase.update("ostatok", newrassr,"_ID=?",new String[] {"1"});
        // изменяем строку по _ID из таблицы, используя позицию пункта в списке
        ContentValues values = new ContentValues();
        values.put(DBHelper.date_, dateFormat.format(dd.getTime()));
        values.put(DBHelper.Chto_Kupil, editChto.getText().toString());
        values.put(DBHelper.rassrochka, Integer.parseInt(editRassrMes.getText().toString()));      //записываем в базу Рассрочка, месяцев
        values.put(DBHelper.summa_Pokup, Double.parseDouble(editSummPok.getText().toString()));


 mSqLiteDatabase.update("zatraty", values, "_ID = ?", new String[] { id });






        Intent intent;
        intent = new Intent(RedActivity.this, BdActivity.class);
        RedActivity.this.finish();
        startActivity(intent);

    }
    public  void onClickCancel(View v){
        Intent intent;
        intent = new Intent(RedActivity.this, BdActivity.class);
        RedActivity.this.finish();
        startActivity(intent);

    }




    public  void onClickDate(View w){
        switch (w.getId()){
            case R.id.txtRegWindowBD:
                // функцией show() мы говорим, что календарь нужно отобразить
                dateBirdayDatePicker.show();
                break;
        }
    }
    private void initDateBuyDatePicker(){
        txtRegWinBD.setText(dateFormat.format(newCalendar.getTime()));
        dateBirdayDatePicker=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            // функция onDateSet  отображает выбранные нами данные в элементе EditText
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year,monthOfYear,dayOfMonth);
                txtRegWinBD.setText(dateFormat.format(newCalendar.getTime()));
            }
        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
}
