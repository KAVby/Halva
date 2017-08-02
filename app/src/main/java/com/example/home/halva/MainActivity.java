// todo сделать наконец нормальную разметку и интерфейс


package com.example.home.halva;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//import static com.example.home.halva.DBHelper.Cursor_poition;
//import static com.example.home.halva.DBHelper.Ostatok_na_karte;
import static com.example.home.halva.DBHelper.ost;
import static com.example.home.halva.DBHelper.rassrochka_viplatil_mes;

public class MainActivity extends AppCompatActivity {


    EditText editSumLimZ,editOstatokZ, editBlizPlatez, editChto, editRassrMes, editSummPok , editBlizPlatez2;
    TextView textVivod1, textBliz1, textBliz2, textDolg;
    Button Zapisat, Posmotret, button2;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    private EditText txtRegWinBD;
    private DatePickerDialog dateBirdayDatePicker;

    final Calendar newCalendar=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
    final Calendar now=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
    Calendar c1;
   Calendar c2;
    final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");

//внимательно с датами - из бд и editText мы получаем реальный месяц, а так он идет с 0.

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
        Zapisat=(Button) findViewById(R.id.Save);
        button2=(Button) findViewById(R.id.button2);
        Posmotret=(Button) findViewById(R.id.Cancel);
        txtRegWinBD=(EditText)findViewById(R.id.txtRegWindowBD);
        textVivod1=(TextView) findViewById(R.id.textVivod1);
        editBlizPlatez2=(EditText) findViewById(R.id.editBlizPlatez2);
        textBliz1=(TextView) findViewById(R.id.textBliz1);
        textBliz2=(TextView) findViewById(R.id.textBliz2);
        textDolg=(TextView) findViewById(R.id.textDolg);

//убираем время из даты, что бы при сравнении не мешало. now клонируем для получения сегодняшней даты
        now.set(Calendar.HOUR,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        now.set(Calendar.HOUR_OF_DAY,0);


// выводим даты платежей на этот и след. месяцы, до 15 числа месяца, потом не забыть сделать проверку оплаты задолжности и алгоритм смены дат
        c1=(Calendar) now.clone();

        if (c1.get(Calendar.DAY_OF_MONTH)<15){
        textBliz1.setText("с 1 до 15." + (c1.get(Calendar.MONTH)+1) + "." + c1.get(Calendar.YEAR)); // не забыть проверить на переходе года корректность
        textBliz2.setText("с 1 до 15." + (c1.get(Calendar.MONTH)+2) + "." + c1.get(Calendar.YEAR));}
        else{
            textBliz1.setText("с 1 до 15." + (c1.get(Calendar.MONTH)+2) + "." + c1.get(Calendar.YEAR)); // не забыть проверить на переходе года корректность
        textBliz2.setText("с 1 до 15." + (c1.get(Calendar.MONTH)+3) + "." + c1.get(Calendar.YEAR));}


        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
       mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initDateBuyDatePicker();

        editSummPok.setSelectAllOnFocus(true); //выделить весь текст при получении фокуса
        editRassrMes.setSelectAllOnFocus(true);
        editSumLimZ.setSelectAllOnFocus(true);
        editChto.setSelectAllOnFocus(true);
        editChto.requestFocus(); //тыкаем фокус что бы не на последнем - т.к. текст выделяется при получении фокуса.
SvMes Summa_v_M=new SvMes();
   if (estDannie()){
       c1=(Calendar) now.clone();
       c2 =(Calendar) now.clone();
//реализуем до 15 числа месяца или после
       if (now.get(Calendar.DAY_OF_MONTH)<15) {
           c1.add(Calendar.MONTH,0);
           c2.add(Calendar.MONTH,1);
           try {
               editBlizPlatez.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_M.Summa_v_Mes(c1,mDatabaseHelper,mSqLiteDatabase)));
               editBlizPlatez2.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_M.Summa_v_Mes(c2,mDatabaseHelper,mSqLiteDatabase)));
           } catch (ParseException e) {
               e.printStackTrace();
           }
           }
       else{
           try {
               c1.add(Calendar.MONTH,1);
               c2.add(Calendar.MONTH,2);
           editBlizPlatez.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_M.Summa_v_Mes(c1,mDatabaseHelper,mSqLiteDatabase)));
           editBlizPlatez2.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_M.Summa_v_Mes(c2,mDatabaseHelper,mSqLiteDatabase)));
       } catch (ParseException e) {
           e.printStackTrace();
       }

       }
       vivodText();
   }
   else{
       editSumLimZ.setText("Заполнить");
       editSumLimZ.setOnKeyListener(new View.OnKeyListener()
                               {

                                   @Override
                                   public boolean onKey(View v, int keyCode, KeyEvent event) {
                                       // TODO Auto-generated method stub
                                       //butOk1.setEnabled(true);
                                       if(event.getAction() == KeyEvent.ACTION_UP)
                                       {
//                                           double k=Double.parseDouble((LitrN2.getText().toString()))*(Double.parseDouble(LitrN4.getText().toString()));
//                                           k2=String.format(Locale.ENGLISH,"%.2f", k);//Double.toString(k);
//                                           NaSummu.setText(k2);
//                                           k=k/Double.parseDouble((LitrN.getText().toString()));
//                                           k2=String.format(Locale.ENGLISH,"%.2f",k);//Double.toString(k);
//                                           NaSummuUSD.setText(k2);
                                           editOstatokZ.setText(editSumLimZ.getText());
                                           return true;
                                       }
                                       return false;
                                   }

                               }
       );




       button2.setEnabled(false);}
    }

public void onClickPogasit(View v) throws ParseException {
            double bp1=0, bp2=0; // ближайший платеж
            Calendar DatePokupClone2, DatePokupClone3, c1Clone2;
            Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                            mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                            mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.rassrochka_viplatil_mes},
                    null, null,
                    null, null, null);
            cursor.moveToLast();
            int i=cursor.getCount(); //число записей чисто для себя
            while (cursor.getPosition()>=0) {
                c1Clone2 = (Calendar) now.clone();
                DatePokupClone2 = (Calendar) now.clone();
                i = cursor.getPosition();
                DatePokupClone2.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
                int j, h, r;
                r = cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka_viplatil_mes));
                h = cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka)) - r; //получаем рассрочку
                //  DatePokupClone2.add(Calendar.MONTH,h);
                DatePokupClone2.set(Calendar.DAY_OF_MONTH, 1);
                c1Clone2.set(Calendar.DAY_OF_MONTH, 1);
                int test = DatePokupClone2.compareTo(c1Clone2);
                DatePokupClone3 = (Calendar) DatePokupClone2.clone();
                DatePokupClone3.add(Calendar.MONTH, r);
                // if (DatePokupClone3.compareTo(c1Clone2)<0){
                for (j = 1; j <= h & DatePokupClone3.compareTo(c1Clone2) < 0; j = j + 1) {

                    DatePokupClone3.set(Calendar.DAY_OF_MONTH, 1);
                    c1Clone2.set(Calendar.DAY_OF_MONTH, 1);
                    DatePokupClone3.add(Calendar.MONTH, 1);
                    //     test=DatePokupClone2.compareTo(c1Clone2);
                    bp1 = bp1 + cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));

                }

                if (j - 1 > 0) { //если есть данные в bp1 значит погасили за некоторое количество месяцев и запишем это в бд
                    // String id =zaprosPola(1);
                    String id = cursor.getString(cursor.getColumnIndex(mDatabaseHelper._ID));
                    i = cursor.getPosition();
                    ContentValues newrassr = new ContentValues();
                    j = j - 1 + Integer.parseInt(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka_viplatil_mes)));
                    newrassr.put(mDatabaseHelper.rassrochka_viplatil_mes, j);
                    mSqLiteDatabase.update("zatraty", newrassr, "_ID=?", new String[]{id});
                }

                cursor.moveToPrevious();
            }

        bp2=Double.parseDouble(editOstatokZ.getText().toString())+bp1;
        editOstatokZ.setText(String.format(Locale.ENGLISH,"%.2f", bp2));// надо это положить в бд в новую табл, не забыть сделать - сделал

    ContentValues newrassr = new ContentValues();
    newrassr.put(ost, bp2);
    mSqLiteDatabase.update("ostatok", newrassr,"_ID=?",new String[] {"1"});


        cursor.close();
          }

//план: погасить пересчитывает и гасит повторно. чтобы этого не было введем в базе еще одно поле и скинем туда позицию курсора с которой делать пересчет
    // и тягаем его за собой.
public void onClickZapisat(View v) throws ParseException {
        double ostatok;
        ostatok=Double.parseDouble(editOstatokZ.getText().toString())-Double.parseDouble(editSummPok.getText().toString());
        editOstatokZ.setText(String.format(Locale.ENGLISH,"%.2f", ostatok));
        Double s=Double.parseDouble(editSummPok.getText().toString());
        if (s==0)
        {textVivod1.setText("сумма покупки не должна = 0");}
        else{
            SvMes Summa_v_Mes=new SvMes();
        c1=(Calendar) now.clone();//обновляем календари
            c2=(Calendar) now.clone();
        c2.add(Calendar.MONTH,1);
   //      double BP1=  Summa_v_Mes.Summa_v_Mes(c1,mDatabaseHelper,mSqLiteDatabase);
   //      double BP2=  Summa_v_Mes.Summa_v_Mes(c2,mDatabaseHelper,mSqLiteDatabase);
            String rasr_ostalos="0";// разобраться все ли верно, тут я менял алгоритм
            zapis(rasr_ostalos);
            c1=(Calendar) now.clone();
            c2 =(Calendar) now.clone();
            if (now.get(Calendar.DAY_OF_MONTH)<15) {
                c1.add(Calendar.MONTH,0);
                c2.add(Calendar.MONTH,1);
                editBlizPlatez.setText(String.format(Locale.ENGLISH,"%.2f", Summa_v_Mes.Summa_v_Mes(c1,mDatabaseHelper,mSqLiteDatabase)));
                editBlizPlatez2.setText(String.format(Locale.ENGLISH,"%.2f", Summa_v_Mes.Summa_v_Mes(c2,mDatabaseHelper,mSqLiteDatabase)));
            }
            else{
                c1.add(Calendar.MONTH,1);
                c2.add(Calendar.MONTH,2);
                editBlizPlatez.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_Mes.Summa_v_Mes(c1,mDatabaseHelper,mSqLiteDatabase)));
                editBlizPlatez2.setText(String.format(Locale.ENGLISH,"%.2f",Summa_v_Mes.Summa_v_Mes(c2,mDatabaseHelper,mSqLiteDatabase)));
            }
            vivodText();
        textVivod1.setText(zaprosPola(6)+" затарился "+zaprosPola(7)+" на сумму "+zaprosPola(9));
        editSummPok.setText("0");

    }}
public void vivodText(){
    editSumLimZ.setText(zaprosPola(2));
    editOstatokZ.setText(zaprosPola(4));
//    editBlizPlatez.setText(zaprosPola(5));
//    editBlizPlatez2.setText(zaprosPola(10));
}
public void zapis( String rasr_ostalos ) {

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        values.put(DBHelper.SLimita, Double.parseDouble(editSumLimZ.getText().toString()));		//записываем в базу Сумма лимита
        values2.put(DBHelper.ost, Double.parseDouble(editOstatokZ.getText().toString()));		//записываем в базу Сумма остаток на карте
  //      values.put(DBHelper.S_v_mes, BP1);		//записываем в базу Сумма платежа в месяц суммарно
        values.put(DBHelper.date_, dateFormat.format(newCalendar.getTime()));          //записываем в базу дата
        values.put(DBHelper.Chto_Kupil, editChto.getText().toString());          //записываем в базу Что купил
        values.put(DBHelper.rassrochka, Integer.parseInt(editRassrMes.getText().toString()));      //записываем в базу Рассрочка, месяцев
        values.put(DBHelper.summa_Pokup, Double.parseDouble(editSummPok.getText().toString()));			 //записываем в базу Накопившийся долг
  //      values.put(DBHelper.Bliz_Platez2, Double.parseDouble(editBlizPlatez2.getText().toString()));
  //      values.put(DBHelper.S_v_mes2, BP2); //сумма в след месяц суммарно
        values.put(DBHelper.rassrochka_viplatil_mes, rasr_ostalos);
        mSqLiteDatabase.insert("zatraty", null, values);
    if (estDannie()) mSqLiteDatabase.update("ostatok", values2, "_ID=?",new String[] {"1"} );
        else mSqLiteDatabase.insert("ostatok", null, values2);
   //
    }

public String zaprosPola(int i) {     //запрос поля

        Cursor cursor = mSqLiteDatabase.query("zatraty", null,
                null, null,
                null, null, null);
        cursor.moveToLast();
    Cursor cursor2 = mSqLiteDatabase.query("ostatok", null,
            null, null,
            null, null, null);
    cursor2.moveToLast();



        switch (i){
            case 1:
             return cursor.getString(cursor.getColumnIndex(mDatabaseHelper._ID));
            case 2:
             return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.SLimita));
            case 4:
             return cursor2.getString(cursor2.getColumnIndex(mDatabaseHelper.ost));
//            case 5:
//                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.S_v_mes));
            case 6:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_));
            case 7:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Chto_Kupil));
            case 8:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            case 9:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup));
//            case 10:
//                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.S_v_mes2));
            case 11:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka_viplatil_mes));

        }
        cursor.close();
        cursor2.close();

        return "error";
    }

public boolean estDannie() {     //проверяем есть ли данные в таблице
        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                    mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.rassrochka_viplatil_mes},
                null, null,
                null, null, null);
        cursor.moveToLast();
    Cursor cursor2 = mSqLiteDatabase.query("ostatok", new String[]{mDatabaseHelper._ID, mDatabaseHelper.ost},
            null, null,
            null, null, null);
    cursor2.moveToLast();

        if ((cursor.getCount() > 0)&(cursor2.getCount() > 0))
        {cursor.close();
        cursor2.close();
            return true;}
        else {cursor.close();cursor2.close();
        return false;}


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

    public void onClickPosmotret(View v){
        Intent intent = new Intent(MainActivity.this, BdActivity.class);
        MainActivity.this.finish();
        startActivity(intent);
    }


}
