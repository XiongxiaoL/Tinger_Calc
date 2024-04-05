package com.yw.tingercalc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

public class DailyActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        findViewById(R.id.returnImg).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void choose(View view) {
        switch (view.getId()){
            case R.id.radix_conversion://进制转换
                start(RadixConversion.class);
                break;
            case R.id.money://汇率转换
                start(Money.class);
                break;
            case R.id.tax://个税计算
                start(Tax.class);
                break;
            case R.id.house://房贷计算
                start(House.class);
                break;
            case R.id.date://日期转换
                start(DateCalculation.class);
                break;
            case R.id.bigWrite://大写金额
                start(BigWrite.class);
                break;
        }
    }

    private void start(Class cls) {
        Intent intent=new Intent(DailyActivity.this,cls);
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
