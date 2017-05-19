package com.example.home.halva;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Home on 18.05.2017.
 */

public class MesActivity extends Activity {
    Calendar D;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes);
        TextView textSvm;
        Double dd;
        textSvm=(TextView) findViewById(R.id.textSvm);
        SvMes Summa_v_M=new SvMes();
        D=Calendar.getInstance();
        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        dd=null;
        try {
             dd=Summa_v_M.Summa_v_Mes(D,mDatabaseHelper,mSqLiteDatabase);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textSvm.setText(""+ dd);

    }
    public void onClickMain (View view){
        Intent intent;
        intent = new Intent(MesActivity.this, MainActivity.class);
        MesActivity.this.finish();
        startActivity(intent);

    }

}
