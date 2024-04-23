package com.yw.tingercalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yw.tingercalc.utils.DoubleArrayParcelable;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;

import java.text.DecimalFormat;
import java.util.Objects;

public class HouseResult extends AppCompatActivity implements View.OnClickListener {

    ListView listView;

    private TextView zong, li;

    private double detail[][];  //每月还款详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_result);

        findViewById(R.id.returnImg).setOnClickListener(this);
        zong = findViewById(R.id.zong);
        li = findViewById(R.id.li);

        Intent intent=getIntent();


        zong.setText(intent.getStringExtra("totalMoney"));
        li.setText(intent.getStringExtra("totalInterests"));

        // 使用 getParcelableExtra() 方法获取 Parcelable 对象
        DoubleArrayParcelable parcelable = intent.getParcelableExtra("detail");
        // 从 Parcelable 对象中获取 double[][] 数据
        //detail[][0]为每月还款本金 detail[][1]为每月还款利息 detail[][2]为每月还款总额
        assert parcelable != null;
        detail = parcelable.getData();
        String [] details = new String[detail.length];
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        for (int i = 0; i < detail.length; i ++) {
            details[i] = "第" + i + "期\n本金:" + decimalFormat.format(detail[i][0]) + "\n利息:" + decimalFormat.format(detail[i][1]) + "\n总额:" + decimalFormat.format(detail[i][2]);
        }


        listView = findViewById(R.id.list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(HouseResult.this, android.R.layout.simple_list_item_1, details);
        //将适配器加载到控件中
        listView.setAdapter(adapter);


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