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

    private TextView radix_firstText;

    private TextView radix_secondText;

    private static int textFlag = 1;

    private static int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radix_conversion);
        findViewById(R.id.returnImg).setOnClickListener(this);

        radix_firstText = findViewById(R.id.radix_firstText);
        radix_secondText = findViewById(R.id.radix_secondText);


        radix_firstText.setOnClickListener(this);
        radix_secondText.setOnClickListener(this);

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
//      findViewById(R.id.double_zero).setOnClickListener(this::numberClick);

        findViewById(R.id.b_clean).setOnClickListener(this::functionClick);
    }

    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.b_clean:
                radix_firstText.setText("0");
                radix_secondText.setText("0");
                break;
        }
    }

    private void numberClick(View v) {
        if (!(v instanceof Button)){
            return;
        }
        number = Integer.parseInt(((Button) v).getText().toString());
        //输入的动态string
        StringBuilder first_value = new StringBuilder(radix_firstText.getText().toString());
        StringBuilder second_value = new StringBuilder(radix_secondText.getText().toString());


//        int decimalNumber = 42;
//        String binaryNumber = Integer.toBinaryString(decimalNumber);
//        System.out.println("二进制: " + binaryNumber);
//        String octalNumber = Integer.toOctalString(decimalNumber);
//        System.out.println("八进制: " + octalNumber);
//        String hexadecimalNumber = Integer.toHexString(decimalNumber);
//        System.out.println("十六进制: " + hexadecimalNumber);

//        String binaryNumber = "101010";
//        int decimalNumber = Integer.parseInt(binaryNumber, 2);
//        System.out.println("十进制: " + decimalNumber);

//        String octalNumber = "52";
//        decimalNumber = Integer.parseInt(octalNumber, 8);
//        System.out.println("十进制: " + decimalNumber);

//        String hexadecimalNumber = "2A";
//        decimalNumber = Integer.parseInt(hexadecimalNumber, 16);
//        System.out.println("十进制: " + decimalNumber);

        switch (textFlag){
            //10 -> 2
            case 1:
                //0需要清除再显示
                if (first_value.toString().equals("0")){
                    first_value = first_value.replace(0,1,number+"");
                }else{
                    first_value.append(number);
                }
                radix_firstText.setText(first_value);
                radix_secondText.setText(Integer.toBinaryString(Integer.parseInt(first_value.toString())));
                break;
            //2 -> 10
            case 2:

                if (second_value.toString().equals("0")){
                    second_value = second_value.replace(0,1,number+"");
                }else{
                    second_value.append(number);
                }
                radix_secondText.setText(second_value);
                if (second_value.toString().matches("[01]+")) {
                    radix_firstText.setText(Integer.toString(Integer.parseInt(second_value.toString(), 2)));
                } else {
                    showToast("请输入正确的二进制", Toast.LENGTH_SHORT);
                    radix_firstText.setText("0");
                }

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
                textFlag = 1;
                break;
            case R.id.radix_secondText:
                textFlag = 2;
                break;
        }
        changeTextColor();
    }

    private void showToast(String message, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();

        // 延迟2秒后取消弹窗
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.M)
    //只在API级别23及更高版本上执行的代码
    private void changeTextColor() {
        if (textFlag == 1) {
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
            radix_secondText.setTextColor(getColor(R.color.MIUI_yellow));
        } else {
            radix_secondText.setTextColor(getColor(R.color.black));
        }
    }
}