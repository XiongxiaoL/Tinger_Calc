package com.yw.tingercalc.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

import java.util.Calendar;

public class House extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "House";

    private double loanMoney;  //贷款金额

    private int years; //贷款年限

    private double yIns; //年利率

    private double totalMoney;  //还款金额（本金+利息）

    private double totalInterests; //利息总额

    private double detail[][];  //每月还款详情


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        findViewById(R.id.returnImg).setOnClickListener(this);

        findViewById(R.id.choose_way).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(House.this);
                //builder.setTitle("请选择一个选项");
                final String[] items = {"等额本金", "等额本息"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理选项点击事件，i 表示选项的索引
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理确定按钮点击事件
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理取消按钮点击事件
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
                final String[] items = {"10年", "20年", "30年"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理选项点击事件，i 表示选项的索引
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理确定按钮点击事件
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 处理取消按钮点击事件
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnImg:
                finish();
                break;
        }
    }

    //等额本息
    public void EqualPrincipalandInterestMethod() {

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


    //时间选择器
    private void dayChoose() {
        Calendar mcalendar = Calendar.getInstance();
        int year = mcalendar.get(Calendar.YEAR);         //  得到当前年
        int month = mcalendar.get(Calendar.MONTH);       //  得到当前月
        final int day = mcalendar.get(Calendar.DAY_OF_MONTH);  //  得到当前日

        new DatePickerDialog(House.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //  这个方法是得到选择后的 年，月，日，分别对应着三个参数 — year、month、dayOfMonth

            }
        },year,month,day).show();   //  弹出日历对话框时，默认显示 年，月，日
    }


}