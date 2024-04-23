package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yw.tingercalc.R;

public class ProfessionalActivity extends AppCompatActivity implements View.OnClickListener {
    //这是主MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional);
        findViewById(R.id.returnImg).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void choose(View view) {
        switch (view.getId()){
            case R.id.math:
                start(MathActivity.class);
                break;
            case R.id.physics:
                start(PhysicsActivity.class);
                break;
            case R.id.biology:
                start(BiologyActivity.class);
                break;
            case R.id.chemistry:
                start(ChemistryActivity.class);
                break;
        }
    }

    private void start(Class cls) {
        Intent intent=new Intent(ProfessionalActivity.this,cls);
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnImg:
                finish();
                break;
        }
    }
}