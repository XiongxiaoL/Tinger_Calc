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
import com.yw.tingercalc.utils.MathCal;
import com.yw.tingercalc.utils.myData;

import java.text.DecimalFormat;

public class P_F1 extends Fragment implements View.OnClickListener{

    EditText et1, et2, et3, et4, et5;
    Button calculate;

    private String xf, wf, x, w, r;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.p_f1,null);
        initView(view);
        setOnClickListener();

        if (xf != null){
            et1.setText(xf);
        }
        if (wf != null){
            et2.setText(wf);
        }
        if (x != null){
            et3.setText(x);
        }
        if (w != null){
            et4.setText(w);
        }
        if (r != null){
            et5.setText(r);
        }
        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_3);
        et4 = view.findViewById(R.id.et_4);
        et5 = view.findViewById(R.id.et_5);
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
        double dxf = 1, dwf = 1, dw = 1, dx = 1, dr = 1;

        xf = et1.getText().toString();
        wf = et2.getText().toString();
        x = et3.getText().toString();
        w = et4.getText().toString();
        r = et5.getText().toString();


        if(!xf.isEmpty()) {
            dxf = isNumeric(xf);
            numData++;
        }
        else res = "xf";

        if(!wf.isEmpty()) {
            numData++;
            dwf = isNumeric(wf);
        }
        else res = "wf";

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

        if (dxf <= 0 || dwf <= 0 || dw <= 0 || dx <= 0 || dr <= 0) {
            Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
        }
        if(numData == 4) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            double divisor;
            switch (res) {
                case "xf":
                    divisor = dr - dx;
                    if (divisor != 0)  xf = ((dr - dw) * dx * dwf) / divisor * dw + "";
                    else {
                        Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
                        xf = null;
                    }
                    break;
                case "wf":
                    divisor = dr - dw;
                    if (divisor != 0)  wf = decimalFormat.format(((dr - dx) * dw * dxf) / divisor * dx);
                    else {
                        Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
                        wf = null;
                    }
                    break;
                case "w":
                    divisor = dr * dxf + dx * dwf - dx * dxf;
                    if (divisor != 0) w = decimalFormat.format(dx * dr * dwf / divisor);
                    else {
                        Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
                        w = null;
                    }
                    break;
                case "x":
                    divisor = dr * dwf + dw * dxf - dw * dwf;
                    if (divisor != 0) x = decimalFormat.format(dw * dr * dxf / divisor);
                    else {
                        Toast.makeText(getActivity(), "数据格式有误", Toast.LENGTH_SHORT).show();
                        x = null;
                    }
                    break;
                case "r":
                    r = decimalFormat.format((dxf - dwf) * (dxf / dx - dwf / dw));
                    break;

            }

        } else {
            Toast.makeText(getActivity(), "数据数量错误", Toast.LENGTH_SHORT).show();
        }

        et1.setText(xf);
        et2.setText(wf);
        et3.setText(x);
        et4.setText(w);
        et5.setText(r);
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
