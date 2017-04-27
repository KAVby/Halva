package com.example.home.halva;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.icu.util.Calendar.MONTH;

public class MainActivity extends AppCompatActivity {
double SLZ, OZ;
    EditText editSumLimZ,editOstatokZ, editBlizPlatez, editChto, editRassrMes, editSummPok , editBlizPlatez2;
    TextView textVivod1, textBliz1, textBliz2;
    Button Zapisat;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    private EditText txtRegWinBD;
    private DatePickerDialog dateBirdayDatePicker;

    final Calendar newCalendar=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
    final Calendar now=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
    final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSumLimZ=(EditText) findViewById(R.id.editSumLimZ);
        editOstatokZ=(EditText) findViewById(R.id.editOstatokZ);
        editBlizPlatez=(EditText) findViewById(R.id.editBlizPlatez);
        editChto=(EditText) findViewById(R.id.editChto);
        editRassrMes=(EditText) findViewById(R.id.editRassrMes);
        editSummPok=(EditText) findViewById(R.id.editSummPok);
        Zapisat=(Button) findViewById(R.id.Zapisat);
        txtRegWinBD=(EditText)findViewById(R.id.txtRegWindowBD);
        textVivod1=(TextView) findViewById(R.id.textVivod1);
        editBlizPlatez2=(EditText) findViewById(R.id.editBlizPlatez2);
        textBliz1=(TextView) findViewById(R.id.textBliz1);
        textBliz2=(TextView) findViewById(R.id.textBliz2);

        textBliz1.setText("до 15." + (now.get(Calendar.MONTH)+2) + "." + now.get(Calendar.YEAR));
        textBliz2.setText("до 15." + (now.get(Calendar.MONTH)+3) + "." + now.get(Calendar.YEAR));

        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initDateBuyDatePicker();

        editSummPok.setSelectAllOnFocus(true); //выделить весь текст при получении фокуса
        editRassrMes.setSelectAllOnFocus(true);
        editSummPok.requestFocus(); //тыкаем фокус что бы не на последнем - т.к. текст выделяется при получении фокуса.

   if (estDannie()){
      vivodText();
   }
   else editSumLimZ.setText("Заполить");
    }

    public void onClickZapisat(View v) throws ParseException {
        double ostatok;
        ostatok=Double.parseDouble(editOstatokZ.getText().toString())-Double.parseDouble(editSummPok.getText().toString());
        editOstatokZ.setText(String.format(Locale.ENGLISH,"%.2f", ostatok));
        Double s=Double.parseDouble(editSummPok.getText().toString());
        if (s==0)
        {textVivod1.setText("сумма покупки не должна = 0");}
        else{
            blizPlatez();
            zapis();
        vivodText();
        textVivod1.setText(zaprosPola(6)+" затарился "+zaprosPola(7)+" на сумму "+zaprosPola(9));
        editSummPok.setText("0");

    }}
public void vivodText(){
    editSumLimZ.setText(zaprosPola(2));
    editOstatokZ.setText(zaprosPola(4));
    editBlizPlatez.setText(zaprosPola(5));
    editBlizPlatez2.setText(zaprosPola(10));
}
    public void zapis(){

        ContentValues values = new ContentValues();
        values.put(DBHelper.SLimita, Double.parseDouble(editSumLimZ.getText().toString()));		//записываем в базу Сумма лимита
        values.put(DBHelper.Ostatok_na_karte, Double.parseDouble(editOstatokZ.getText().toString()));		//записываем в базу Сумма остаток на карте
        values.put(DBHelper.Bliz_Platez, Double.parseDouble(editBlizPlatez.getText().toString()));		//записываем в базу Сумма сумма ближайшего платежа
        values.put(DBHelper.date_, dateFormat.format(newCalendar.getTime()));          //записываем в базу дата
        values.put(DBHelper.Chto_Kupil, editChto.getText().toString());          //записываем в базу Что купил
        values.put(DBHelper.rassrochka, Integer.parseInt(editRassrMes.getText().toString()));      //записываем в базу Рассрочка, месяцев
        values.put(DBHelper.summa_Pokup, Double.parseDouble(editSummPok.getText().toString()));			 //записываем в базу Накопившийся долг
        values.put(DBHelper.Bliz_Platez2, Double.parseDouble(editBlizPlatez2.getText().toString()));
        mSqLiteDatabase.insert("zatraty", null, values);
    }

    public String zaprosPola(int i) {     //запрос поля
        Cursor cursor = mSqLiteDatabase.query("zatraty", null,
                null, null,
                null, null, null);
        cursor.moveToLast();
        switch (i){
            case 1:
             return cursor.getString(cursor.getColumnIndex(mDatabaseHelper._ID));
            case 2:
             return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.SLimita));
            case 4:
             return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Ostatok_na_karte));
            case 5:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Bliz_Platez));
            case 6:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_));
            case 7:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Chto_Kupil));
            case 8:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            case 9:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup));
            case 10:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Bliz_Platez2));

        }
        cursor.close();
        return "error";}

    public boolean estDannie() {     //проверяем есть ли данные в таблице
        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                        mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.Bliz_Platez, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.Bliz_Platez2},
                null, null,
                null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0)
        {cursor.close();
            return true;}
        else {cursor.close();
        return false;}


    }

    public void blizPlatez() throws ParseException {
    double bp1=0, bp2=0;
        int m1, m11, m12, m2;
        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                        mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.Bliz_Platez, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.Bliz_Platez2},
                null, null,
                null, null, null);
        cursor.moveToLast();
        int i=cursor.getCount();
        while (cursor.getPosition()>=0){
            i=cursor.getPosition();
            newCalendar.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
            m11=newCalendar.get(Calendar.MONTH);//месяц оплаты
            m12 =cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));//месяцев рассрочки
            m1=m11+m12;
            m2=now.get(Calendar.MONTH);
            if (m1>m2)
                bp1=bp1+cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup))/cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            if (m1>(m2+1))
                bp2=bp2+cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup))/cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            cursor.moveToPrevious();
        }
        cursor.close();
        //мы проверили все что в бд. и получили суммы платежа на текущий и след. месяц. (bp1,bp2)
        // теперь проанализируем запись, которую набрали, но в бд она еще не внесена
        //т.е. данные берем из editText ов


        newCalendar.setTime(dateFormat.parse(txtRegWinBD.getText().toString()));
        m11=newCalendar.get(Calendar.MONTH);//месяц оплаты
        m12 =Integer.parseInt(editRassrMes.getText().toString());//месяцев рассрочки

        //надо делать подсчет месяцев как я делал в календаре смен - перебором, иначе проблема переходов через год (с 12 на 1)

//        newCalendar.set(Calendar.MONTH,newCalendar.get(Calendar.MONTH)+(Integer.parseInt(editRassrMes.getText().toString()))); // прибавляем рассрочку, например 3 мес
//        newCalendar.clear(Calendar.DAY_OF_MONTH);
//        now.clear(Calendar.DAY_OF_MONTH);
        m1=m11+m12; //месяц плюс месяцев рассрочки
        m2=now.get(Calendar.MONTH);

        if (m1>m2)
            bp1=bp1+Double.parseDouble(editSummPok.getText().toString())/Integer.parseInt(editRassrMes.getText().toString());
        if (m1>(m2+1))
            bp2=bp2+Double.parseDouble(editSummPok.getText().toString())/Integer.parseInt(editRassrMes.getText().toString());


        editBlizPlatez.setText(String.format(Locale.ENGLISH,"%.2f", bp1));
        editBlizPlatez2.setText(String.format(Locale.ENGLISH,"%.2f", bp2));
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
