package com.yw.tingercalc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

public class MathActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        findViewById(R.id.returnImg).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void choose(View view) {
        switch (view.getId()){
            case R.id.probability_theory://概率论
                start(ProbabilityTheory.class);
                break;
            case R.id.matrix://矩阵计算
                start(MatrixCalculation.class);
                break;
            case R.id.mathematical_statistics://数理统计
                start(MathematicalStatistics.class);
                break;
            case R.id.numerical_calculation://数值计算
                start(NumericalCalculation.class);
                break;
        }
    }

    private void start(Class cls) {
        Intent intent=new Intent(MathActivity.this,cls);
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
