package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yw.tingercalc.R;

public class MainActivity extends AppCompatActivity {
    //这是主MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("NonConstantResourceId")
    public void choose(View view) {
        switch (view.getId()){
            case R.id.daily:
                start(DailyActivity.class);
                break;
            case R.id.professional:
                start(ProfessionalActivity.class);
                break;
        }
    }

    private void start(Class cls) {
        Intent intent=new Intent(MainActivity.this,cls);
        startActivity(intent);
    }

    public void help(View view) {
        Intent intent=new Intent(MainActivity.this, DocumentActivity.class);
        intent.putExtra("type",5);
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}