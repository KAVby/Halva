package com.example.home.halva;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
double SLZ, OZ;
    EditText SumLimZ,OstatokZ, editPlatez, editChto, editRassrMes, editSummPok ;
    Button Zapisat;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    private EditText txtRegWinBD;
    private DatePickerDialog dateBirdayDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SumLimZ=(EditText) findViewById(R.id.SumLimZ);
        OstatokZ=(EditText) findViewById(R.id.OstatokZ);
        editPlatez=(EditText) findViewById(R.id.editPlatez);
        editChto=(EditText) findViewById(R.id.editChto);
        editRassrMes=(EditText) findViewById(R.id.editRassrMes);
        editSummPok=(EditText) findViewById(R.id.editSummPok);

        Zapisat=(Button) findViewById(R.id.Zapisat);
        txtRegWinBD=(EditText)findViewById(R.id.txtRegWindowBD);


        SLZ=Double.parseDouble(SumLimZ.getText().toString()); //считываем и присваеваем значения
        OZ=Double.parseDouble(OstatokZ.getText().toString());

        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initDateBuyDatePicker();

        editSummPok.requestFocus(); //тыкаем фокус что бы не на последнем - т.к. текст выделяется при получении фокуса.
        editSummPok.setSelectAllOnFocus(true); //выделить весь текст при получении фокуса
        editRassrMes.setSelectAllOnFocus(true);
    }

    public void onClickZapisat(View v) {
        zapis();
    }

    public void zapis(){
        double d=25.1;
        ContentValues values = new ContentValues();
        values.put(DBHelper.date_, "05.04.2017");                                  //записываем в базу дата
        values.put(DBHelper.summa_, d);          //записываем в базу Сумма покупки
        values.put(DBHelper.rassrochka, "3");      //записываем в базу Рассрочка, месяцев
        values.put(DBHelper.prihod, "0");		//записываем в базу Положил не карту
        values.put(DBHelper.dolg, OstatokZ.getText().toString());			 //записываем в базу Накопившийся долг
        values.put(DBHelper.summa_na_karte, SumLimZ.getText().toString());		//записываем в базу Сумма на карте
     //   values.put(DBHelper.cena_BYN, NaSummu.getText().toString());		//записываем в базу стоимость заправки в BYN

        mSqLiteDatabase.insert("zatraty", null, values);
    }
    public  void onClick(View w){
        switch (w.getId()){
            case R.id.txtRegWindowBD:
                // это шаг 3, функцией show() мы говорим, что календарь нужно отобразить
                dateBirdayDatePicker.show();
                break;
        }
    }

    private void initDateBuyDatePicker(){
        final Calendar newCalendar=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy"); // это строка нужна для дальнейшего преобразования даты в строку
        //создаем объект типа DatePickerDialog и инициализируем его конструктор обработчиком события выбора даты и данными для даты по умолчанию
        txtRegWinBD.setText(dateFormat.format(newCalendar.getTime()));
        dateBirdayDatePicker=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            // функция onDateSet обрабатывает шаг 2: отображает выбранные нами данные в элементе EditText
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Calendar newCal=Calendar.getInstance();
                newCalendar.set(year,monthOfYear,dayOfMonth);
                //txtRegWinBD.setText(dateFormat.format(newCal.getTime()));
                txtRegWinBD.setText(dateFormat.format(newCalendar.getTime()));
            }
        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

}
