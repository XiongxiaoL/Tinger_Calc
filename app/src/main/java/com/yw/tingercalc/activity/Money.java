package com.yw.tingercalc.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.ConvertTable;

public class Money extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Money";

    private ImageButton returnImg;

    private TextView money_firstText;

    private TextView money_secondText;

    private TextView money_threeText;

    private static int textFlag = 1;

    private static String number = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        returnImg = findViewById(R.id.returnImg);
        money_firstText = findViewById(R.id.money_firstText);
        money_secondText = findViewById(R.id.money_secondText);
        money_threeText = findViewById(R.id.money_threeText);


        money_firstText.setOnClickListener(this);
        money_secondText.setOnClickListener(this);
        money_threeText.setOnClickListener(this);
        returnImg.setOnClickListener(this);

        findViewById(R.id.one).setOnClickListener(this::numberClick);
        findViewById(R.id.two).setOnClickListener(this::numberClick);
        findViewById(R.id.three).setOnClickListener(this::numberClick);
        findViewById(R.id.four).setOnClickListener(this::numberClick);
        findViewById(R.id.five).setOnClickListener(this::numberClick);
        findViewById(R.id.six).setOnClickListener(this::numberClick);
        findViewById(R.id.seven).setOnClickListener(this::numberClick);
        findViewById(R.id.eight).setOnClickListener(this::numberClick);
        findViewById(R.id.nine).setOnClickListener(this::numberClick);
        findViewById(R.id.zero).setOnClickListener(this::numberClick);
        findViewById(R.id.point).setOnClickListener(this::numberClick);

        findViewById(R.id.clean).setOnClickListener(this::functionClick);

    }

    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.clean:
                money_firstText.setText("0");
                money_secondText.setText("0");
                money_threeText.setText("0");
                findViewById(R.id.point).setEnabled(true);
                break;
        }
    }

    private void numberClick(View v) {
        if (!(v instanceof Button)){
            return;
        }
        number = ((Button) v).getText().toString();
        //输入的动态string
        StringBuilder first_value = new StringBuilder(money_firstText.getText().toString());
        StringBuilder second_value = new StringBuilder(money_secondText.getText().toString());
        StringBuilder three_value = new StringBuilder(money_threeText.getText().toString());
        //转换率
        double firstConvert = 0;
        double secondConvert = 0;
        double threeConvert = 0;
        switch (textFlag){
            //后期增加长度限制
            case 1:
                if (first_value.toString().equals("0") && (number.toCharArray()[0] != '.')){
                    first_value = first_value.replace(0,1,number);
                }else{
                    int small = 0;
                    char targetChar = '.';
                    int index = first_value.toString().indexOf(targetChar);
                    if (index >= 0) {
                        small = first_value.length() - index - 1; // 小数数量
                    }
                    if (first_value.length() < 12 && small < 3) {
                        first_value.append(number);
                    }
                }
                secondConvert = ConvertTable.findRateValue("人民币","美元");
                threeConvert = ConvertTable.findRateValue("人民币","欧元");
                money_firstText.setText(first_value);

                //只允许一个小数点和三位小数
                if (first_value.charAt(first_value.length() - 1) == '.') {
                    findViewById(R.id.point).setEnabled(false);
                    String first_tep = first_value.substring(0, first_value.length() - 1);
                    money_secondText.setText(String.format("%.2f", (Double.parseDouble(first_tep) * secondConvert)));
                    money_threeText.setText(String.format("%.2f", (Double.parseDouble(first_tep) * threeConvert)));
                } else {
                    money_secondText.setText(String.format("%.2f", (Double.parseDouble(first_value.toString()) * secondConvert)));
                    money_threeText.setText(String.format("%.2f", (Double.parseDouble(first_value.toString()) * threeConvert)));
                }
                break;

            case 2:
                if (second_value.toString().equals("0") && (number.toCharArray()[0] != '.')){
                    second_value = second_value.replace(0,1,number);
                }else{
                    int small = 0;
                    char targetChar = '.';
                    int index = second_value.toString().indexOf(targetChar);
                    if (index >= 0) {
                        small = second_value.length() - index - 1; // 小数数量
                    }
                    if (second_value.length() < 12 && small < 3) {
                        second_value.append(number);
                    }
                }
                firstConvert = ConvertTable.findRateValue("美元","人民币");
                threeConvert = ConvertTable.findRateValue("美元","欧元");
                money_secondText.setText(second_value);

                //只允许一个小数点和三位小数
                if (first_value.charAt(first_value.length() - 1) == '.') {
                    findViewById(R.id.point).setEnabled(false);
                    String second_tep = second_value.substring(0, second_value.length() - 1);
                    money_firstText.setText(String.format("%.2f", (Double.parseDouble(second_tep) * firstConvert)));
                    money_threeText.setText(String.format("%.2f", (Double.parseDouble(second_tep) * threeConvert)));
                } else {
                    money_firstText.setText(String.format("%.2f", (Double.parseDouble(second_value.toString()) * firstConvert)));
                    money_threeText.setText(String.format("%.2f", (Double.parseDouble(second_value.toString()) * threeConvert)));
                }
                break;

            case 3:
                if (three_value.toString().equals("0") && (number.toCharArray()[0] != '.')){
                    three_value = three_value.replace(0,1,number);
                }else{
                    int small = 0;
                    char targetChar = '.';
                    int index = three_value.toString().indexOf(targetChar);
                    if (index >= 0) {
                        small = three_value.length() - index - 1; // 小数数量
                    }
                    if (three_value.length() < 12 && small < 3) {
                        three_value.append(number);
                    }
                }
                firstConvert = ConvertTable.findRateValue("欧元","人民币");
                secondConvert = ConvertTable.findRateValue("欧元","美元");
                money_threeText.setText(three_value);

                //只允许一个小数点和三位小数
                if (first_value.charAt(first_value.length() - 1) == '.') {
                    findViewById(R.id.point).setEnabled(false);
                    String three_tep = three_value.substring(0, three_value.length() - 1);
                    money_firstText.setText(String.format("%.2f", (Double.parseDouble(three_tep) * firstConvert)));
                    money_secondText.setText(String.format("%.2f", (Double.parseDouble(three_tep) * secondConvert)));
                } else {
                    money_firstText.setText(String.format("%.2f", (Double.parseDouble(three_value.toString()) * firstConvert)));
                    money_secondText.setText(String.format("%.2f", (Double.parseDouble(three_value.toString()) * secondConvert)));
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
            case R.id.money_firstText:
                textFlag = 1;
                break;
            case R.id.money_secondText:
                textFlag = 2;
                break;
            case R.id.money_threeText:
                textFlag = 3;
                break;
        }
        changeTextColor();
    }

    @TargetApi(Build.VERSION_CODES.M)
    //只在API级别23及更高版本上执行的代码
    private void changeTextColor() {
        money_firstText.setTextColor(textFlag == 1 ? getColor(R.color.MIUI_yellow) : getColor(R.color.black));
        money_secondText.setTextColor(textFlag == 2 ? getColor(R.color.MIUI_yellow) : getColor(R.color.black));
        money_threeText.setTextColor(textFlag == 3 ? getColor(R.color.MIUI_yellow) : getColor(R.color.black));
    }
}
