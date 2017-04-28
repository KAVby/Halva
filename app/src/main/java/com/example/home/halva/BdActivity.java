package com.example.home.halva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by kureichyk on 28.04.2017.
 */
public class BdActivity extends Activity {
    Button nazad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd);
        nazad=(Button) findViewById(R.id.nazad);
    }

    public void onClickNazad(View v){
        Intent intent = new Intent(BdActivity.this, MainActivity.class);
        BdActivity.this.finish();
        startActivity(intent);
    }
}
