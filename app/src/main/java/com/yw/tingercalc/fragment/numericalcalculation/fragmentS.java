package com.yw.tingercalc.fragment.numericalcalculation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.MathCal;
import com.yw.tingercalc.utils.myData;

//数值计算--拟合
public class fragmentS extends Fragment implements View.OnClickListener {
    EditText et1, et2,et3,et4;
    Button submit;
    TextView clear1, clear2,clear3,tv1, tv2;
    Switch aSwitch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_s,null);

        initView(view);
        setOnClickListener();
        if (myData.FitDataX!=null){
            et1.setText(myData.FitDataX);
        }
        if (myData.FitDataY!=null){
            et2.setText(myData.FitDataY);
        }
        if (myData.FitDegree!=null){
            et4.setText(myData.FitDegree);
        }
        return view;
    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
        clear1.setOnClickListener(this);
        clear2.setOnClickListener(this);
        clear3.setOnClickListener(this);
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_3);
        et4 = view.findViewById(R.id.et_insert1);
        clear1 = view.findViewById(R.id.clear1);
        clear2 = view.findViewById(R.id.clear2);
        clear3 = view.findViewById(R.id.clear3);
        submit = view.findViewById(R.id.submit);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        aSwitch=view.findViewById(R.id.switch_);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear1:
                et1.setText("");
                break;
            case R.id.clear2:
                et2.setText("");
                break;
            case R.id.clear3:
                et3.setText("");
                break;    
            case R.id.submit:
                count();
                break;
        }
    }

    private void count() {
        myData.FitDataX = et1.getText().toString();
        myData.FitDataY = et2.getText().toString();
        myData.FitDegree = et4.getText().toString();
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_SHORT).show();
            return;
        }
        myData.FitDataX = et1.getText().toString();
        myData.FitDataY = et2.getText().toString();
        double[] a1 = MathCal.StringToDouble(et1.getText().toString());
        double[] a2 = MathCal.StringToDouble(et2.getText().toString());
        double[] a3 = null;
        if (a1.length == 0) {
            Toast.makeText(getActivity(), R.string.wrongX, Toast.LENGTH_SHORT).show();
            return;
        }
        if (a2.length == 0) {
            Toast.makeText(getActivity(), R.string.wrongY, Toast.LENGTH_SHORT).show();
            return;
        }
        if(a1.length!=a2.length){
            Toast.makeText(getActivity(), R.string.differentLength, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!aSwitch.isChecked()){
            if (et3.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), R.string.noquanzhong, Toast.LENGTH_SHORT).show();
                return;
            }else {
                a3 = MathCal.StringToDouble(et3.getText().toString());
                if (a3.length == 0) {
                    Toast.makeText(getActivity(), R.string.wrongquanzhong, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(a1.length!=a3.length){
                        Toast.makeText(getActivity(), R.string.differentLength, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
        myData.FitDataX = et1.getText().toString();
        myData.FitDataY = et2.getText().toString();
        if(et4.getText().toString().isEmpty()){
            tv1.setText(R.string.nocishu);
        }else {
            int a4=Integer.valueOf(et4.getText().toString());
            myData.FitDegree = et4.getText().toString();
            if(aSwitch.isChecked()){
                String polyfit = MathCal.polyfit(a1, a2, a4);
                tv1.setText(polyfit);
            }else {
                String polyfit = MathCal.polyfit(a1, a2, a3, a4);
                tv1.setText(polyfit);
            }

        }
        if(aSwitch.isChecked()){//等权重
            String expFit = MathCal.expFit(a1,a2);
            tv2.setText(expFit);
        }else {//非等权重
            String expFit = MathCal.expFit(a1, a2, a3);
            tv2.setText(expFit);
        }
    }
}
