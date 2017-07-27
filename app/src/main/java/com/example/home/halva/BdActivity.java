package com.example.home.halva;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.provider.BaseColumns._ID;
import static com.example.home.halva.DBHelper.ost;

/**
 * Created by kureichyk on 28.04.2017.
 */
public class BdActivity extends Activity {
    Button nazad, mes;
    ListView listVBD;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    SimpleCursorAdapter scAdapter;
    Cursor cursor, cursor2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd);
        nazad=(Button) findViewById(R.id.nazad);
        mes=(Button) findViewById(R.id.mes);
        listVBD=(ListView) findViewById(R.id.listVBD);
        registerForContextMenu(listVBD); // зарегистрировать контекстное меню для ListView

        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        cursor = mSqLiteDatabase.query("zatraty", null,
                null, null,
                null, null, "_ID DESC");
      //  cursor.moveToFirst();
        String[] from = new String[] {DBHelper._ID, DBHelper.date_, DBHelper.Chto_Kupil, DBHelper.summa_Pokup, DBHelper.rassrochka, DBHelper.rassrochka_ostalos};//берем этот набор данных
        int[] to = new int[] { R.id.l0, R.id.l1, R.id.l2, R.id.l3, R.id.l4, R.id.l5};// и вставляем их сюда
        scAdapter = new SimpleCursorAdapter(this, R.layout.list_txt, cursor, from, to);
        listVBD.setAdapter(scAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
        menu.add(0, 2, 0, "Редактировать");
        menu.add(0, 3, 0, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Double S;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();  // получаем инфу о пункте списка
        if (item.getItemId() == 1) {  //если выбрано удалить

//делаем запрос по ид
            cursor2 = mSqLiteDatabase.query("zatraty", new String[]{mDatabaseHelper.SLimita,
                            mDatabaseHelper.S_v_mes, mDatabaseHelper.date_, mDatabaseHelper.Chto_Kupil,
                            mDatabaseHelper.rassrochka, mDatabaseHelper.summa_Pokup, mDatabaseHelper.S_v_mes2, mDatabaseHelper.rassrochka_ostalos}, "_ID = ?" ,  new String[]{Long.toString(acmi.id)},
                    null, null, null);

            cursor2.moveToLast();

            S=  cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.summa_Pokup))-(cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.summa_Pokup)))*cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.rassrochka_ostalos))/cursor2.getInt(cursor2.getColumnIndex(mDatabaseHelper.rassrochka));
            cursor2 = mSqLiteDatabase.query("ostatok", null,
                    null, null,
                    null, null, null);
            cursor2.moveToLast();
            S=cursor2.getDouble(cursor2.getColumnIndex(mDatabaseHelper.ost))+S;
            ContentValues newrassr = new ContentValues();
            newrassr.put(ost, S);
            mSqLiteDatabase.update("ostatok", newrassr,"_ID=?",new String[] {"1"});
            // удаляем строку по _ID из таблицы, используя позицию пункта в списке
            mSqLiteDatabase.delete("zatraty", "_ID = " + acmi.id, null);
            // уведомляем адаптер, что данные изменились
            cursor.requery();
            return true;
        }
        if (item.getItemId() == 2){         //если выбрано изменить
            Intent intent = new Intent(BdActivity.this, RedActivity.class);
            intent.putExtra("id", Long.toString(acmi.id));   //передаю данные об ид списка
            BdActivity.this.finish();
            startActivity(intent);                               // тут надо реализовать переход в новое активити с передачей значения о пункте списка acmi

        }
        return super.onContextItemSelected(item);
    }

    public void onClickNazad(View v){
        Intent intent = new Intent(BdActivity.this, MainActivity.class);
        BdActivity.this.finish();
        startActivity(intent);
    }
    public void onClickSMes (View view){
        Intent intent;
        intent = new Intent(BdActivity.this, MesActivity.class);
        BdActivity.this.finish();
        startActivity(intent);
    }
}
