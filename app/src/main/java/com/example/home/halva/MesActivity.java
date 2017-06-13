package com.example.home.halva;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.home.halva.DBHelper.dateid_ID;

/**
 * Created by Home on 18.05.2017.
 */

public class MesActivity extends Activity {
    ExpandableListView elvMain;
    Calendar D;
    DBHelper mDatabaseHelper;
    SQLiteDatabase mSqLiteDatabase;
    final SimpleDateFormat dateFormat=new SimpleDateFormat("MM.yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes);
        TextView textSvm;
        Double dd;
        textSvm=(TextView) findViewById(R.id.textSvm);
        SvMes2 Summa_v_M=new SvMes2();
        D=Calendar.getInstance();
        mDatabaseHelper = new DBHelper(this, "mydatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        // здесь надо удалить таблицы для новой генерации (это роще чем анализировать появление новых записей
        //mSqLiteDatabase.db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        mSqLiteDatabase.delete("datesumm",null,null); // очищаем таблицы
        mSqLiteDatabase.delete("datespis",null,null);
// формируем новые таблицы
        int i;dd=null;
        for (i=1; i<10; i=i+1){

            try {
                dd=Summa_v_M.Summa_v_Mes(D,i,mDatabaseHelper,mSqLiteDatabase);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            D.add(Calendar.MONTH,1);

        }


        // готовим данные по группам для адаптера
        Cursor cursor = mSqLiteDatabase.query("datesumm", null, null, null, null, null, null);
        startManagingCursor(cursor);
        // сопоставление данных и View для групп
        String[] groupFrom = { mDatabaseHelper.date_2, mDatabaseHelper.Summ_date };
        int[] groupTo = { R.id.ll0, R.id.ll2 };
        // сопоставление данных и View для элементов
        String[] childFrom = {mDatabaseHelper.date_pokup, mDatabaseHelper.Chto_Kupil2, mDatabaseHelper.Summ_mes };
        int[] childTo = { R.id.ll0, R.id.ll1, R.id.ll2 };

        // создаем адаптер и настраиваем список
        SimpleCursorTreeAdapter sctAdapter = new MyAdapter(this, cursor,
                R.layout.list_txt2, groupFrom,
                groupTo, R.layout.list_txt2, childFrom,
                childTo);
        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        elvMain.setAdapter(sctAdapter);
    }

//    protected void onDestroy() {
//        super.onDestroy();
//        db.close();
//    }

    class MyAdapter extends SimpleCursorTreeAdapter {

        public MyAdapter(Context context, Cursor cursor, int groupLayout,
                         String[] groupFrom, int[] groupTo, int childLayout,
                         String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);
        }

        protected Cursor getChildrenCursor(Cursor groupCursor) { // метод срабатывает при раскрытии вкладки
            // получаем курсор по элементам для конкретной группы
            int idColumn = groupCursor.getColumnIndex(mDatabaseHelper.date_2ID);
            return mSqLiteDatabase.query("datespis", null, dateid_ID + " = "
                    + groupCursor.getInt(idColumn), null, null, null, null);
        }
    }




//пробуем заполнить БД за 10 месяцев
//        int i;dd=null;
//        for (i=1; i<10; i=i+1){
//
//            try {
//                dd=Summa_v_M.Summa_v_Mes(D,i,mDatabaseHelper,mSqLiteDatabase);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            D.add(Calendar.MONTH,1);
//
//        }
//
//
//        dd=null;
//        try {
//             dd=Summa_v_M.Summa_v_Mes(D,i,mDatabaseHelper,mSqLiteDatabase);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        textSvm.setText(dateFormat.format(D.getTime())+"         "+ dd);
//
//    }
    public void onClickMain (View view){
        Intent intent;
        intent = new Intent(MesActivity.this, MainActivity.class);
        MesActivity.this.finish();
        startActivity(intent);

    }

}
