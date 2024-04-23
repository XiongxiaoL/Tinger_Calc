package com.yw.tingercalc.fragment.biology;

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


public class B_F1 extends Fragment implements View.OnClickListener{

    EditText et1, et2, et3, et4;
    Button calculate;

    private String z, n, t, f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.b_f1,null);
        initView(view);
        setOnClickListener();


        if (z != null){
            et1.setText(z);
        }
        if (n != null){
            et2.setText(n);
        }
        if (t != null){
            et3.setText(t);
        }
        if (f != null){
            et4.setText(f);
        }
        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_3);
        et4 = view.findViewById(R.id.et_4);

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
        String res = "z";
        int numData = 0;
        double dz = 1, dn = 1, dt = 1, df = 1;


        z = et1.getText().toString();
        n = et2.getText().toString();
        t = et3.getText().toString();
        f = et4.getText().toString();



        if(!z.isEmpty()) {
            numData++;
            dz = isNumeric(z);
        }
        else res = "z";

        if(!n.isEmpty()) {
            numData++;
            dn = isNumeric(n);
        }
        else res = "n";

        if(!t.isEmpty()) {
            numData++;
            dt = isNumeric(t);
        }
        else res = "t";

        if(!f.isEmpty()) {
            numData++;
            df = isNumeric(f);
        }
        else res = "f";



        if (dz <= 0 || dn <= 0 || dt <= 0 || df <= 0) {
            Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
        }
        if(numData == 3) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            switch (res) {
                case "z":

                    z = decimalFormat.format(dt * df / dn);
                    break;

                case "n":
                    n = decimalFormat.format(df * dt / dz);
                    break;

                case "t":
                    t = decimalFormat.format(dz * dn / df);
                    break;

                case "f":
                    f = decimalFormat.format(dz * dn / dt);
                    break;

            }

        } else {
            Toast.makeText(getActivity(), "数据数量错误", Toast.LENGTH_SHORT).show();
        }

        et1.setText(z);
        et2.setText(n);
        et3.setText(t);
        et4.setText(f);
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

