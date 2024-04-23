package com.yw.tingercalc.fragment.physics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;

import java.text.DecimalFormat;

public class P_F3 extends Fragment implements View.OnClickListener{


    EditText et1, et2, et3;
    Button calculate;

    private String x, w, r;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.p_f3,null);
        initView(view);
        setOnClickListener();


        if (x != null){
            et1.setText(x);
        }
        if (w != null){
            et2.setText(w);
        }
        if (r != null){
            et3.setText(r);
        }
        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_3);

        calculate = view.findViewById(R.id.calculate);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calculate:
                count();
                break;
        }
    }

    private void setOnClickListener() {
        calculate.setOnClickListener(this);
    }

    private void count() {
        String res = "r";
        int numData = 0;
        double dw = 1, dx = 1, dr = 1;


        x = et1.getText().toString();
        w = et2.getText().toString();
        r = et3.getText().toString();



        if(!x.isEmpty()) {
            numData++;
            dx = isNumeric(x);
        }
        else res = "x";

        if(!w.isEmpty()) {
            numData++;
            dw = isNumeric(w);
        }
        else res = "w";

        if(!r.isEmpty()) {
            numData++;
            dr = isNumeric(r);
        }
        else res = "r";

        if (dw <= 0 || dx <= 0 || dr <= 0) {
            Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
        }
        if(numData == 2) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            switch (res) {
                case "x":
                    x = decimalFormat.format(dw * dw / dr);
                    break;
                case "w":
                    w = decimalFormat.format(Math.sqrt(dx * dr));
                    break;
                case "r":
                    r = decimalFormat.format(dw * dw / dx);
                    break;

            }

        } else {
            Toast.makeText(getActivity(), "数据数量错误", Toast.LENGTH_SHORT).show();
        }

        et1.setText(x);
        et2.setText(w);
        et3.setText(r);
    }


    //检测正数
    private static double isNumeric(String input) {
        try {
            double d = Double.parseDouble(input);
            if (d >= 0) return d;
            else return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}