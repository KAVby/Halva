package com.example.home.halva;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import static com.example.home.halva.DBHelper.Ostatok_na_karte;
import static com.example.home.halva.DBHelper.rassrochka_ostalos;
import static com.example.home.halva.R.id.button2;

public class MainActivity extends AppCompatActivity {
double SLZ, OZ;
    EditText editSumLimZ,editOstatokZ, editBlizPlatez, editChto, editRassrMes, editSummPok , editBlizPlatez2;
    TextView textVivod1, textBliz1, textBliz2;
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
        Zapisat=(Button) findViewById(R.id.Zapisat);
        button2=(Button) findViewById(R.id.button2);
        Posmotret=(Button) findViewById(R.id.Posmotret);
        txtRegWinBD=(EditText)findViewById(R.id.txtRegWindowBD);
        textVivod1=(TextView) findViewById(R.id.textVivod1);
        editBlizPlatez2=(EditText) findViewById(R.id.editBlizPlatez2);
        textBliz1=(TextView) findViewById(R.id.textBliz1);
        textBliz2=(TextView) findViewById(R.id.textBliz2);


// выводим даты платежей на этот и след. месяцы, до 15 числа месяца, потом не забыть сделать проверку оплаты задолжности и алгоритм смены дат
        c1=(Calendar) now.clone();

        if (c1.get(Calendar.DAY_OF_MONTH)<15){
        textBliz1.setText("до 15." + (c1.get(Calendar.MONTH)+1) + "." + c1.get(Calendar.YEAR)); // не забыть проверить на переходе года корректность
        textBliz2.setText("до 15." + (c1.get(Calendar.MONTH)+2) + "." + c1.get(Calendar.YEAR));}
        else{
            textBliz1.setText("до 15." + (c1.get(Calendar.MONTH)+2) + "." + c1.get(Calendar.YEAR)); // не забыть проверить на переходе года корректность
        textBliz2.setText("до 15." + (c1.get(Calendar.MONTH)+3) + "." + c1.get(Calendar.YEAR));}


        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initDateBuyDatePicker();

        editSummPok.setSelectAllOnFocus(true); //выделить весь текст при получении фокуса
        editRassrMes.setSelectAllOnFocus(true);
        editSumLimZ.setSelectAllOnFocus(true);
        editChto.setSelectAllOnFocus(true);
        editChto.requestFocus(); //тыкаем фокус что бы не на последнем - т.к. текст выделяется при получении фокуса.

   if (estDannie()){
       c1=(Calendar) now.clone();
       c2 =(Calendar) now.clone();
//реализуем до 15 числа месяца или после
       if (now.get(Calendar.DAY_OF_MONTH)<15) {
           c1.add(Calendar.MONTH,-1);
           try {
               editBlizPlatez.setText(""+Summa_v_Mes(c1));
               editBlizPlatez2.setText(""+Summa_v_Mes(c2));
           } catch (ParseException e) {
               e.printStackTrace();
           }
           }
       else{
           try {
               c2.add(Calendar.MONTH,1);
           editBlizPlatez.setText(""+Summa_v_Mes(c1));
           editBlizPlatez2.setText(""+Summa_v_Mes(c2));
       } catch (ParseException e) {
           e.printStackTrace();
       }

       }
       vivodText();
   }
   else{ editSumLimZ.setText("Заполить");
        button2.setEnabled(false);}
    }

public void onClickPogasit(View v) throws ParseException {

            double bp1=0, bp2=0; // ближайший платеж
            int m1, m11, m12, m2;
            Calendar DatePokupClone2, c1Clone2;
            //клонируем календари чтобы не испортить даты
            //  nowClone =(Calendar)c1.clone();
            //DatePokupClone2=(Calendar)newCalendar.clone();

            Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                            mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                            mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, mDatabaseHelper.rassrochka_ostalos},
                    null, null,
                    null, null, null);

     //   int rassr_ost=Integer.parseInt(zaprosPola(11));
            cursor.moveToLast();
            int i=cursor.getCount(); //число записей чисто для себя
            while (cursor.getPosition()>=0){

                c1Clone2 =(Calendar)now.clone();
                DatePokupClone2=(Calendar)now.clone();
                i=cursor.getPosition();
                DatePokupClone2.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
                int j,h;
                h=cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka))-cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka_ostalos)); //получаем рассрочку
              //  DatePokupClone2.add(Calendar.MONTH,h);
                DatePokupClone2.clear(Calendar.DAY_OF_MONTH);
                c1Clone2.clear(Calendar.DAY_OF_MONTH);
                int test=DatePokupClone2.compareTo(c1Clone2);
                for (j=1;j<=h&DatePokupClone2.compareTo(c1Clone2)<0;j=j+1) {

                    DatePokupClone2.clear(Calendar.DAY_OF_MONTH);
                    c1Clone2.clear(Calendar.DAY_OF_MONTH);
                    DatePokupClone2.add(Calendar.MONTH,1);
                        bp1 = bp1 + cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));

                }
                if (bp1>0){ //если есть данные в bp1 значит погасили за некоторое количество месяцев и запишем это в бд
                String id =zaprosPola(1);
                ContentValues newrassr = new ContentValues();
                newrassr.put(rassrochka_ostalos, (j-1));
                mSqLiteDatabase.update("zatraty", newrassr,"_ID=?",new String[] {id});}
            cursor.moveToPrevious();
            }

        bp2=Double.parseDouble(editOstatokZ.getText().toString())+bp1;
        editOstatokZ.setText(""+bp2);// надо это положить в бд, не забыть сделать


//       int cu= cursor.getPosition(); //не забывать что отсчет с 0
//        String id =zaprosPola(1);
//        ContentValues newrassr = new ContentValues();
//         newcursorposition.put(rassrochka_ostalos, cu);
//        newcursorposition.put(Ostatok_na_karte, bp2);
//        mSqLiteDatabase.update("zatraty", newcursorposition,"_ID=?",new String[] {id});
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
            c1=(Calendar) now.clone();
            c2 =(Calendar) now.clone();
            if (now.get(Calendar.DAY_OF_MONTH)<15) {
                c1.add(Calendar.MONTH,-1);
                double test2=Summa_v_Mes(c1);double test3=Summa_v_Mes(c2);
                editBlizPlatez.setText(""+Summa_v_Mes(c1));
                editBlizPlatez2.setText(""+Summa_v_Mes(c2));
            }
            else{
                c2.add(Calendar.MONTH,1);
                editBlizPlatez.setText(""+Summa_v_Mes(c1));
                editBlizPlatez2.setText(""+Summa_v_Mes(c2));
            }
        c1=(Calendar) now.clone();//обновляем календари
            c2=(Calendar) now.clone();
        c2.add(Calendar.MONTH,1);
         double BP1=  Summa_v_Mes(c1);
         double BP2=  Summa_v_Mes(c2);
            String rasr_ostalos="0";// разобраться, тут я менял алгоритм
//            if (estDannie()==false){
//                rasr_ostalos;
//                rasr_ostalos=(zaprosPola(11));}
            zapis(BP1,BP2,rasr_ostalos);
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
public void zapis(double BP1, double BP2, String rasr_ostalos ) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.SLimita, Double.parseDouble(editSumLimZ.getText().toString()));		//записываем в базу Сумма лимита
        values.put(DBHelper.Ostatok_na_karte, Double.parseDouble(editOstatokZ.getText().toString()));		//записываем в базу Сумма остаток на карте
        values.put(DBHelper.S_v_mes, BP1);		//записываем в базу Сумма платежа в месяц
        values.put(DBHelper.date_, dateFormat.format(newCalendar.getTime()));          //записываем в базу дата
        values.put(DBHelper.Chto_Kupil, editChto.getText().toString());          //записываем в базу Что купил
        values.put(DBHelper.rassrochka, Integer.parseInt(editRassrMes.getText().toString()));      //записываем в базу Рассрочка, месяцев
        values.put(DBHelper.summa_Pokup, Double.parseDouble(editSummPok.getText().toString()));			 //записываем в базу Накопившийся долг
  //      values.put(DBHelper.Bliz_Platez2, Double.parseDouble(editBlizPlatez2.getText().toString()));
        values.put(DBHelper.S_v_mes2, BP2); //сумма в след месяц
        values.put(DBHelper.rassrochka_ostalos, rasr_ostalos);
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
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.S_v_mes));
            case 6:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_));
            case 7:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.Chto_Kupil));
            case 8:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            case 9:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup));
            case 10:
                return cursor.getString(cursor.getColumnIndex(mDatabaseHelper.S_v_mes2));
            case 11:
                return cursor.getString(cursor.getColumnIndex(rassrochka_ostalos));

        }
        cursor.close();

        return "error";
    }

public boolean estDannie() {     //проверяем есть ли данные в таблице
        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                        mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2,rassrochka_ostalos},
                null, null,
                null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() > 0)
        {cursor.close();
            return true;}
        else {cursor.close();
        return false;}


    }

public double Summa_v_Mes(Calendar c ) throws ParseException {
    double bp1=0, bp2=0; // ближайший платеж
        int m1, m11, m12, m2;
        Calendar DatePokupClone, c1Clone;
        //клонируем календари чтобы не испортить даты
      //  nowClone =(Calendar)c1.clone();
        DatePokupClone=(Calendar)newCalendar.clone();

        Cursor cursor = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper._ID, mDatabaseHelper.SLimita,
                        mDatabaseHelper.Ostatok_na_karte, mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                        mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, rassrochka_ostalos},
                null, null,
                null, null, null);
        cursor.moveToLast();
        int i=cursor.getCount();
        while (cursor.getPosition()>0){
            c1Clone =(Calendar)c.clone();
            DatePokupClone=(Calendar)now.clone();
            i=cursor.getPosition();
            DatePokupClone.setTime(dateFormat.parse(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.date_))));
            DatePokupClone.clear(Calendar.DAY_OF_MONTH);
            c1Clone.clear(Calendar.DAY_OF_MONTH);
            if (DatePokupClone.compareTo(c1Clone)<0) {
                DatePokupClone.add(Calendar.MONTH, (cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka)))); //прибавляем рассрочку к дате покупки
                DatePokupClone.clear(Calendar.DAY_OF_MONTH);
                c1Clone.clear(Calendar.DAY_OF_MONTH);
                if (DatePokupClone.compareTo(c1Clone) >= 0)//сравниваем дату платежа плюс рассрочка с текущей датой
                    bp1 = bp1 + cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup)) / cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            }
//            nowClone.add(Calendar.MONTH,1);
//            if (newCalendarClone.compareTo(nowClone)>0)
//                bp2=bp2+cursor.getDouble(cursor.getColumnIndex(mDatabaseHelper.summa_Pokup))/cursor.getInt(cursor.getColumnIndex(mDatabaseHelper.rassrochka));
            cursor.moveToPrevious();
        }
        cursor.close();

        //мы проверили все что в бд. и получили суммы платежа на текущий и след. месяц. (bp1,bp2)
        // теперь проанализируем запись, которую набрали, но в бд она еще не внесена
        //т.е. данные берем из editText ов
        c1Clone =(Calendar)c.clone();//обновляем календари
        DatePokupClone=(Calendar)newCalendar.clone();
        DatePokupClone.setTime(dateFormat.parse(txtRegWinBD.getText().toString()));
        if (DatePokupClone.compareTo(c1Clone)<0) {
            DatePokupClone.add(Calendar.MONTH, Integer.parseInt(editRassrMes.getText().toString())); // прибавляем рассрочку, например 3 мес
            DatePokupClone.clear(Calendar.DAY_OF_MONTH);//убираем из даты числа месяца что бы не мешали при сравнении
            c1Clone.clear(Calendar.DAY_OF_MONTH);
            if (DatePokupClone.compareTo(c1Clone) > 0)//сравниваем дату платежа плюс рассрочка с текущей датой
                bp1 = bp1 + Double.parseDouble(editSummPok.getText().toString()) / Integer.parseInt(editRassrMes.getText().toString()); //и включаем платеж в текущий месяц
        }
        return bp1;
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
