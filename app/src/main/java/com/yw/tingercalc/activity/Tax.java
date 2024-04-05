package com.yw.tingercalc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

public class Tax extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Tax";

    private String data = "-1";

    private TextView tax_Text1, tax_Text2, tax_Text3, tax_Text4, tax_Text5, tax_Text6, tax_Text7, tax_Text8;

    private static String number = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax);
        findViewById(R.id.returnImg).setOnClickListener(this);

        tax_Text1 = findViewById(R.id.tax_Text1);
        tax_Text2 = findViewById(R.id.tax_Text2);
        tax_Text3 = findViewById(R.id.tax_Text3);
        tax_Text4 = findViewById(R.id.tax_Text4);
        tax_Text5 = findViewById(R.id.tax_Text5);
        tax_Text6 = findViewById(R.id.tax_Text6);
        tax_Text7 = findViewById(R.id.tax_Text7);
        tax_Text8 = findViewById(R.id.tax_Text8);

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

        findViewById(R.id.tax_calculate).setOnClickListener(this::calculateClick);

        findViewById(R.id.clean).setOnClickListener(this::functionClick);

    }


    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.clean:
                tax_Text1.setText("0");
                tax_Text2.setText("0");
                tax_Text3.setText("0");
                tax_Text4.setText("0");
                tax_Text5.setText("0");
                tax_Text6.setText("0");
                tax_Text7.setText("0");
                tax_Text8.setText("0");
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
        StringBuilder first_value = new StringBuilder(tax_Text1.getText().toString());

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

        tax_Text1.setText(first_value);

        //只允许一个小数点
        if (first_value.charAt(first_value.length() - 1) == '.') {
            findViewById(R.id.point).setEnabled(false);
            data = first_value.substring(0, first_value.length() - 1);
        } else {
            data = first_value.toString();
        }

    }

    private void calculateClick(View v) {
        float money , old = 0, medicare = 0, outWork = 0, house = 0, total = 0;
        if (data.equals("-1")) {
            money = 0;
        } else {
            money = Float.parseFloat(data);
        }

        old = (float) (money * 0.08);// 计算养老保险,税率为8%
        medicare = (float) (money * 0.02);// 计算医保保险，税率为2%
        outWork = (float) (money * 0.002);// 计算失业保险，税率为0.2%
        house = money * 0.12f;// 计算住房公积金，税率为12%
        total = old + medicare + outWork + house;
        money = money - total;
        float gerenTax = calculateTax(money);
        float income = money - gerenTax;

        tax_Text2.setText(old + "");
        tax_Text3.setText(medicare + "");
        tax_Text4.setText(outWork + "");
        tax_Text5.setText(house + "");
        tax_Text6.setText(total + "");
        tax_Text7.setText(gerenTax + "");
        tax_Text8.setText(income + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnImg:
                finish();
                break;
        }
    }

    //计算个人所得税，按照2022年最新个人所得税税率表计算
    private static float calculateTax(float money) {
        float one = 0, two = 0, three = 0, four = 0, five = 0, six = 0;
        one = (8000 - 5000) * 0.03f;//第一档满额缴费
        two = (17000 - 8000) * 0.1f;//第二档满额缴费
        three = (30000 - 17000) * 0.2f;//第三档满额缴费
        four = (40000 - 30000) * 0.25f;//第四档满额缴费
        five = (60000 - 40000) * 0.3f;//第五档满额缴费
        six = (85000 - 60000) * 0.35f;//第六档满额缴费
        float tax = 0;
        if (money <= 5000)
            tax = 0;//级别1税率
        else if (money <= 8000)
            tax = (money - 5000) * 0.03f;//级别2税率
        else if (money <= 17000)
            tax = (money - 8000) * 0.1f + one;//级别3税率
        else if (money <= 30000)
            tax = (money - 17000) * 0.2f + two + one;//级别4税率
        else if (money <= 40000)
            tax = (money - 30000) * 0.25f + three + two + one;//级别5税率
        else if (money <= 60000)
            tax = (money - 40000) * 0.3f + four + three + two + one;//级别6税率
        else if (money <= 85000)
            tax = (money - 60000) * 0.35f + five + four + three + two + one;//级别7税率
        else
            tax = (money - 85000) * 0.45f + six + five + four + three + two + one;//级别8税率
        return tax;
    }
}