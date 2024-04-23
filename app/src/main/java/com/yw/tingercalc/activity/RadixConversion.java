package com.yw.tingercalc.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.tingercalc.R;

import android.content.Context;
import android.widget.Toast;

public class RadixConversion extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RadixConversion";

    private TextView radix_firstText, radix_secondText, radix_thirdText, radix_fourthText;


    private static int textFlag = 10;

    private static String number = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radix_conversion);
        findViewById(R.id.returnImg).setOnClickListener(this);

        radix_firstText = findViewById(R.id.radix_firstText);
        radix_secondText = findViewById(R.id.radix_secondText);
        radix_thirdText = findViewById(R.id.radix_thirdText);
        radix_fourthText = findViewById(R.id.radix_fourthText);


        radix_firstText.setOnClickListener(this);
        radix_secondText.setOnClickListener(this);
        radix_thirdText.setOnClickListener(this);
        radix_fourthText.setOnClickListener(this);

        findViewById(R.id.b_0).setOnClickListener(this::numberClick);
        findViewById(R.id.b_1).setOnClickListener(this::numberClick);
        findViewById(R.id.b_2).setOnClickListener(this::numberClick);
        findViewById(R.id.b_3).setOnClickListener(this::numberClick);
        findViewById(R.id.b_4).setOnClickListener(this::numberClick);
        findViewById(R.id.b_5).setOnClickListener(this::numberClick);
        findViewById(R.id.b_6).setOnClickListener(this::numberClick);
        findViewById(R.id.b_7).setOnClickListener(this::numberClick);
        findViewById(R.id.b_8).setOnClickListener(this::numberClick);
        findViewById(R.id.b_9).setOnClickListener(this::numberClick);

        findViewById(R.id.b_a).setOnClickListener(this::numberClick);
        findViewById(R.id.b_b).setOnClickListener(this::numberClick);
        findViewById(R.id.b_c).setOnClickListener(this::numberClick);
        findViewById(R.id.b_d).setOnClickListener(this::numberClick);
        findViewById(R.id.b_e).setOnClickListener(this::numberClick);
        findViewById(R.id.b_f).setOnClickListener(this::numberClick);

        findViewById(R.id.b_a).setEnabled(false);
        findViewById(R.id.b_b).setEnabled(false);
        findViewById(R.id.b_c).setEnabled(false);
        findViewById(R.id.b_d).setEnabled(false);
        findViewById(R.id.b_e).setEnabled(false);
        findViewById(R.id.b_f).setEnabled(false);

        findViewById(R.id.b_mul).setEnabled(false);
        findViewById(R.id.b_00).setEnabled(false);

        findViewById(R.id.b_clean).setOnClickListener(this::functionClick);
    }

    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.b_clean:
                radix_firstText.setText("0");
                radix_secondText.setText("0");
                radix_thirdText.setText("0");
                radix_fourthText.setText("0");
                break;
        }
    }

    private void numberClick(View v) {
        if (!(v instanceof Button)){
            return;
        }
        number = ((Button) v).getText().toString();
        //输入的动态string
        StringBuilder first_value = new StringBuilder(radix_firstText.getText().toString());
        StringBuilder second_value = new StringBuilder(radix_secondText.getText().toString());
        StringBuilder third_value = new StringBuilder(radix_thirdText.getText().toString());
        StringBuilder fourth_value = new StringBuilder(radix_fourthText.getText().toString());

        String four;
        switch (textFlag){
            case 10:
                //0需要清除再显示
                if (first_value.toString().equals("0")){
                    first_value = first_value.replace(0,1,number+"");
                }else{
                    first_value.append(number);
                }
                //限制十六进制长度不超过7
                four = Integer.toHexString(Integer.parseInt(first_value.toString()));
                if (four.length() > 7) {
                    Toast.makeText(getApplicationContext(), "数据过长", Toast.LENGTH_SHORT).show();
                    break;
                }

                radix_firstText.setText(first_value);
                radix_secondText.setText(Integer.toBinaryString(Integer.parseInt(first_value.toString())));
                radix_thirdText.setText(Integer.toOctalString(Integer.parseInt(first_value.toString())));
                radix_fourthText.setText(four);
                break;

            case 2:
                if (second_value.toString().equals("0")){
                    second_value = second_value.replace(0,1,number+"");
                }else{
                    second_value.append(number);
                }

                four = Integer.toHexString(Integer.parseInt(second_value.toString(), 2));
                if (four.length() > 7) {
                    Toast.makeText(getApplicationContext(), "数据过长", Toast.LENGTH_SHORT).show();
                    break;
                }

                radix_secondText.setText(second_value);
                radix_firstText.setText(Integer.toString(Integer.parseInt(second_value.toString(), 2)));
                radix_thirdText.setText(Integer.toOctalString(Integer.parseInt(second_value.toString(), 2)));
                radix_fourthText.setText(four);
                break;

            case 8:
                //0需要清除再显示
                if (third_value.toString().equals("0")){
                    third_value = third_value.replace(0,1,number+"");
                }else{
                    third_value.append(number);
                }

                four = Integer.toHexString(Integer.parseInt(third_value.toString(), 8));
                if (four.length() > 7) {
                    Toast.makeText(getApplicationContext(), "数据过长", Toast.LENGTH_SHORT).show();
                    break;
                }

                radix_thirdText.setText(third_value);
                radix_firstText.setText(Integer.toString(Integer.parseInt(third_value.toString(), 8)));
                radix_secondText.setText(Integer.toBinaryString(Integer.parseInt(third_value.toString(), 8)));
                radix_fourthText.setText(four);
                break;

            case 16:
                //0需要清除再显示
                if (fourth_value.toString().equals("0")){
                    fourth_value = fourth_value.replace(0,1,number+"");
                }else{
                    fourth_value.append(number);
                }

                four = fourth_value.toString();
                if (four.length() > 7) {
                    Toast.makeText(getApplicationContext(), "数据过长", Toast.LENGTH_SHORT).show();
                    break;
                }

                radix_fourthText.setText(four);
                radix_firstText.setText(Integer.toString(Integer.parseInt(fourth_value.toString(), 16)));
                radix_secondText.setText(Integer.toBinaryString(Integer.parseInt(fourth_value.toString(), 16)));
                radix_thirdText.setText(Integer.toOctalString(Integer.parseInt(fourth_value.toString(), 16)));
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnImg:
                finish();
                break;
            case R.id.radix_firstText:
                textFlag = 10; //十进制
                break;
            case R.id.radix_secondText:
                textFlag = 2; //二进制
                break;
            case R.id.radix_thirdText:
                textFlag = 8; //八进制
                break;
            case R.id.radix_fourthText:
                textFlag = 16; //十六进制
                break;
        }
        changeTextColor();
    }


    @TargetApi(Build.VERSION_CODES.M)
    //只在API级别23及更高版本上执行的代码
    private void changeTextColor() {
        if (textFlag == 10) {
            findViewById(R.id.b_2).setEnabled(true);
            findViewById(R.id.b_3).setEnabled(true);
            findViewById(R.id.b_4).setEnabled(true);
            findViewById(R.id.b_5).setEnabled(true);
            findViewById(R.id.b_6).setEnabled(true);
            findViewById(R.id.b_7).setEnabled(true);
            findViewById(R.id.b_8).setEnabled(true);
            findViewById(R.id.b_9).setEnabled(true);

            findViewById(R.id.b_a).setEnabled(false);
            findViewById(R.id.b_b).setEnabled(false);
            findViewById(R.id.b_c).setEnabled(false);
            findViewById(R.id.b_d).setEnabled(false);
            findViewById(R.id.b_e).setEnabled(false);
            findViewById(R.id.b_f).setEnabled(false);
            radix_firstText.setTextColor(getColor(R.color.MIUI_yellow));
        } else {
            radix_firstText.setTextColor(getColor(R.color.black));
        }

        if (textFlag == 2) {
            //禁止部分按钮，后期可设置变色
            findViewById(R.id.b_2).setEnabled(false);
            findViewById(R.id.b_3).setEnabled(false);
            findViewById(R.id.b_4).setEnabled(false);
            findViewById(R.id.b_5).setEnabled(false);
            findViewById(R.id.b_6).setEnabled(false);
            findViewById(R.id.b_7).setEnabled(false);
            findViewById(R.id.b_8).setEnabled(false);
            findViewById(R.id.b_9).setEnabled(false);

            findViewById(R.id.b_a).setEnabled(false);
            findViewById(R.id.b_b).setEnabled(false);
            findViewById(R.id.b_c).setEnabled(false);
            findViewById(R.id.b_d).setEnabled(false);
            findViewById(R.id.b_e).setEnabled(false);
            findViewById(R.id.b_f).setEnabled(false);

            radix_secondText.setTextColor(getColor(R.color.MIUI_yellow));
        } else {
            radix_secondText.setTextColor(getColor(R.color.black));
        }

        if (textFlag == 8) {
            findViewById(R.id.b_2).setEnabled(true);
            findViewById(R.id.b_3).setEnabled(true);
            findViewById(R.id.b_4).setEnabled(true);
            findViewById(R.id.b_5).setEnabled(true);
            findViewById(R.id.b_6).setEnabled(true);
            findViewById(R.id.b_7).setEnabled(true);

            findViewById(R.id.b_8).setEnabled(false);
            findViewById(R.id.b_9).setEnabled(false);

            findViewById(R.id.b_a).setEnabled(false);
            findViewById(R.id.b_b).setEnabled(false);
            findViewById(R.id.b_c).setEnabled(false);
            findViewById(R.id.b_d).setEnabled(false);
            findViewById(R.id.b_e).setEnabled(false);
            findViewById(R.id.b_f).setEnabled(false);
            radix_thirdText.setTextColor(getColor(R.color.MIUI_yellow));
        } else {
            radix_thirdText.setTextColor(getColor(R.color.black));
        }

        if (textFlag == 16) {
            findViewById(R.id.b_2).setEnabled(true);
            findViewById(R.id.b_3).setEnabled(true);
            findViewById(R.id.b_4).setEnabled(true);
            findViewById(R.id.b_5).setEnabled(true);
            findViewById(R.id.b_6).setEnabled(true);
            findViewById(R.id.b_7).setEnabled(true);
            findViewById(R.id.b_8).setEnabled(true);
            findViewById(R.id.b_9).setEnabled(true);

            findViewById(R.id.b_a).setEnabled(true);
            findViewById(R.id.b_b).setEnabled(true);
            findViewById(R.id.b_c).setEnabled(true);
            findViewById(R.id.b_d).setEnabled(true);
            findViewById(R.id.b_e).setEnabled(true);
            findViewById(R.id.b_f).setEnabled(true);
            radix_fourthText.setTextColor(getColor(R.color.MIUI_yellow));
        } else {
            radix_fourthText.setTextColor(getColor(R.color.black));
        }
    }
}