package com.example.home.halva;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.provider.BaseColumns._ID;

/**
 * Created by kureichyk on 28.04.2017.
 */
public class BdActivity extends Activity {
    Button nazad;
    ListView listVBD;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd);
        nazad=(Button) findViewById(R.id.nazad);
        listVBD=(ListView) findViewById(R.id.listVBD);

        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        Cursor cursor = mSqLiteDatabase.query("zatraty", null,
                null, null,
                null, null, "_ID DESC");
      //  cursor.moveToFirst();

        String[] from = new String[] {DBHelper.date_, DBHelper.Chto_Kupil, DBHelper.summa_Pokup, DBHelper.rassrochka, DBHelper.Cursor_poition};//берем этот набор данных
        int[] to = new int[] { R.id.l1, R.id.l2, R.id.l3, R.id.l4, R.id.l5};// и вставляем их сюда

        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(this, R.layout.list_txt, cursor, from, to);
listVBD.setAdapter(scAdapter);


    }

    public void onClickNazad(View v){
        Intent intent = new Intent(BdActivity.this, MainActivity.class);
        BdActivity.this.finish();
        startActivity(intent);
    }
}
