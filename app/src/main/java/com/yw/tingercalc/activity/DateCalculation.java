package com.yw.tingercalc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCalculation extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        findViewById(R.id.returnImg).setOnClickListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        EditText autoDate = findViewById(R.id.date_input1);
        autoDate.setText(simpleDateFormat.format(new Date()));

        findViewById(R.id.btn_calculation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText date1 = findViewById(R.id.date_input1);
                EditText date2 = findViewById(R.id.date_input2);
                String date1Str = date1.getText().toString();
                String date2Str = date2.getText().toString();


                Date newerDate = null;
                Date olderDate = null;
                try {
                    newerDate = simpleDateFormat.parse(date1Str);
                    olderDate = simpleDateFormat.parse(date2Str);
                    if(newerDate==null || olderDate==null){
                        tip("日期输入错误，必须是“2008-08-08”类型");
                        return;
                    }
                } catch (ParseException e) {
                    tip("日期输入错误，必须是“2008-08-08”类型");
                    return;
                }
                if(newerDate.after(olderDate)){
                    Date tempDate = new Date();
                    tempDate = newerDate;
                    newerDate = olderDate;
                    olderDate = tempDate;
                }
                int year = (int) ((olderDate.getTime() - newerDate.getTime()) / (1000 * 60 * 60 * 24)) / 365;
                int day = (int) ((olderDate.getTime() - newerDate.getTime()) / (1000 * 60 * 60 * 24));
                String resultStr = "\t"+simpleDateFormat.format(olderDate)+"跟"+simpleDateFormat.format(newerDate)+"相距:\n" +
                        "\t"+day + "天\n"+
                        "\t"+year + "年又"+(int) ((olderDate.getTime() - newerDate.getTime()) / (1000 * 60 * 60 * 24)) % 365+"天\n";

                TextView resultView = findViewById(R.id.result_text);
                resultView.setText(resultStr);
            }
        });

    }

    private void tip(String msg){
        Toast.makeText(DateCalculation.this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnImg:
                finish();
                break;
        }
    }
}