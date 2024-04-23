package com.yw.tingercalc.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;
import com.yw.tingercalc.activity.HouseResult;
import com.yw.tingercalc.utils.ConvertTable;
import com.yw.tingercalc.utils.DoubleArrayParcelable;

import java.text.DecimalFormat;
import java.util.Calendar;

public class House extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "House";

    private TextView total_Text, choose_way_Text, choose_time_Text, rate_Text;

    private static int textFlag = 1;

    private static String number = "0";

    private boolean way = true; //还款方式true为等额本息

    private double loanMoney = 0;  //贷款金额

    private int years = 30; //贷款年限

    private double yIns = 0; //年利率

    private double totalMoney;  //还款金额（本金+利息）

    private double totalInterests; //利息总额

    private double detail[][];  //每月还款详情


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textFlag = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        findViewById(R.id.returnImg).setOnClickListener(this);

        total_Text = findViewById(R.id.total);
        choose_way_Text = findViewById(R.id.choose_way);
        choose_time_Text = findViewById(R.id.choose_time);
        rate_Text = findViewById(R.id.rate);

        total_Text.setOnClickListener(this);
        rate_Text.setOnClickListener(this);


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

        findViewById(R.id.point).setEnabled(false);

        findViewById(R.id.house_calculate).setOnClickListener(this::calculateClick);

        findViewById(R.id.clean).setOnClickListener(this::functionClick);

        findViewById(R.id.choose_way).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
                //builder.setTitle("请选择一个选项");
                final String[] items = {"等额本息", "等额本金"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理选项点击事件，i 表示选项的索引
                        way = i == 0;
                        choose_way_Text.setText(items[i]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        findViewById(R.id.choose_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
                //builder.setTitle("请选择一个选项");
                final String[] items = {"10", "20", "30"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理选项点击事件，i 表示选项的索引
                        years = Integer.parseInt(items[i]);
                        choose_time_Text.setText(items[i]);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.clean:
                total_Text.setText("0");
                rate_Text.setText("0");
                findViewById(R.id.point).setEnabled(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnImg:
                finish();
                break;
            case R.id.total:
                textFlag = 1;
                findViewById(R.id.point).setEnabled(false);
                break;
            case R.id.rate:
                textFlag = 2;
                findViewById(R.id.point).setEnabled(!rate_Text.getText().toString().contains("."));
                break;
        }
        changeTextColor();
    }


    private void numberClick(View v) {
        if (!(v instanceof Button)){
            return;
        }
        number = ((Button) v).getText().toString();
        //输入的动态string
        StringBuilder first_value = new StringBuilder(total_Text.getText().toString());
        StringBuilder second_value = new StringBuilder(rate_Text.getText().toString());
        switch (textFlag) {
            case 1:
                if (first_value.toString().equals("0") && (number.toCharArray()[0] != '.')) {
                    first_value = first_value.replace(0, 1, number);
                } else {
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

                total_Text.setText(first_value);

                //只允许一个小数点
                if (first_value.charAt(first_value.length() - 1) == '.') {
                    findViewById(R.id.point).setEnabled(false);
                    loanMoney = Double.parseDouble(first_value.substring(0, first_value.length() - 1));
                } else {
                    loanMoney = Double.parseDouble(first_value.toString());
                }
                break;

            case 2:
                if (second_value.toString().equals("0") && (number.toCharArray()[0] != '.')) {
                    second_value = second_value.replace(0, 1, number);
                } else {
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

                rate_Text.setText(second_value);

                //只允许一个小数点
                if (second_value.charAt(second_value.length() - 1) == '.') {
                    findViewById(R.id.point).setEnabled(false);
                    yIns = Double.parseDouble(second_value.substring(0, second_value.length() - 1));
                } else {
                    yIns = Double.parseDouble(second_value.toString());
                }
                break;
        }
    }

    private void calculateClick(View v) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        if (yIns == 0 || loanMoney == 0) {
            showToast("请设置总额或年利率", Toast.LENGTH_SHORT);
        } else {
            if (way) {

                //等额本息
                EqualPrincipalandInterestMethod();
                Intent intent=new Intent(House.this, HouseResult.class);
                intent.putExtra("totalMoney", decimalFormat.format(totalMoney));
                intent.putExtra("totalInterests", decimalFormat.format(totalInterests));

                // 创建 DoubleArrayParcelable 对象并传入数据
                DoubleArrayParcelable pDetail = new DoubleArrayParcelable(detail);

                intent.putExtra("detail", pDetail);

                startActivity(intent);

            } else {
                //等额本金
                EqualPrincipalMethod();
                Intent intent=new Intent(House.this, HouseResult.class);
                intent.putExtra("totalMoney", decimalFormat.format(totalMoney));
                intent.putExtra("totalInterests", decimalFormat.format(totalInterests));

                DoubleArrayParcelable pDetail = new DoubleArrayParcelable(detail);
                intent.putExtra("detail", pDetail);

                startActivity(intent);
            }
        }
    }

    //等额本息
    private void EqualPrincipalandInterestMethod() {

        double mIns = yIns / 100 / 12; //月利率
        int months = (years * 12); //还款所需月份
        double pow = Math.pow(1 + mIns,months);
        double remains = loanMoney;
        totalMoney = (months * loanMoney * mIns * pow) / (pow - 1);  //总还款金额
        totalMoney = Math.floor(totalMoney * 100 + 0.5) / 100;  //floor函数 保留两位小数
        totalInterests = totalMoney - loanMoney;
        totalInterests = Math.floor(totalInterests * 100 + 0.5) / 100;
        double temp[][] = new double[months][3];
        for (int i = 0; i < months; i++) {
            if(i == months - 1) {
                temp[i][1] = remains * mIns;
                temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100;
                temp[i][0] = remains;
                temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100;
                temp[i][2] = temp[i][0] + temp[i][1];
                temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100;
                break;
            }
            //由于精度问题 最后一个月实际的本金会有差别 需要单独计算
            temp[i][1] = remains * mIns;
            temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100;
            temp[i][2] = totalMoney / months;
            temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100;
            temp[i][0] = temp[i][2] - temp[i][1];
            temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100;
            remains -= temp[i][0];
        }
        //temp[][0]为每月还款本金 temp[][1]为每月还款利息 temp[][2]为每月还款总额
        detail = temp;
    }

    //等额本金
    private void EqualPrincipalMethod() {

        double mIns = yIns / 100 / 12; //月利率
        int months = (years * 12);
        double remains = loanMoney;
        double sum = 0; // 总计还款金额
        double temp[][] = new double[months][3];
        for (int i = 0; i < months; i++)
        {
            temp[i][0] = loanMoney / months;
            temp[i][0] = Math.floor(temp[i][0] * 100 + 0.5) / 100;
            temp[i][1] = remains * mIns;
            temp[i][1] = Math.floor(temp[i][1] * 100 + 0.5) / 100;
            remains -= temp[i][0];
            temp[i][2] = temp[i][0] + temp[i][1];
            temp[i][2] = Math.floor(temp[i][2] * 100 + 0.5) / 100;
            sum += temp[i][2];
        }
        //temp[][0]为每月还款本金 temp[][1]为每月还款利息 temp[][2]为每月还款总额
        detail = temp;
        totalMoney = sum;
        totalMoney = Math.floor(totalMoney * 100 + 0.5) / 100;
        totalInterests = totalMoney - loanMoney;
        totalInterests = Math.floor(totalInterests * 100 + 0.5) / 100;
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
        total_Text.setTextColor(textFlag == 1 ? getColor(R.color.MIUI_yellow) : getColor(R.color.black));
        rate_Text.setTextColor(textFlag == 2 ? getColor(R.color.MIUI_yellow) : getColor(R.color.black));
    }


}