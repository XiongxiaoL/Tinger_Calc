package com.yw.tingercalc.fragment.numericalcalculation;

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


public class fragmentT extends Fragment implements View.OnClickListener {
    EditText et1, et2,et3,et4;
    Button change;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_t,null);
        initView(view);
        setOnClickListener();
        if (myData.valuebefore!=null){
            et1.setText(myData.valuebefore);
        }
        if (myData.originalBase!=null){
            et2.setText(myData.originalBase);
        }
        if (myData.goalBase!=null){
            et4.setText(myData.goalBase);
        }
        return view;
    }

    private void setOnClickListener() {
        change.setOnClickListener(this);
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_3);
        et4 = view.findViewById(R.id.et_4);
        change=view.findViewById(R.id.change);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change:
                count();
                break;
        }
    }

    private void count() {
        myData.valuebefore = et1.getText().toString();
        myData.originalBase = et2.getText().toString();
        myData.goalBase = et4.getText().toString();
        myData.valuebefore = et1.getText().toString();
        myData.originalBase = et2.getText().toString();
        myData.goalBase = et4.getText().toString();
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()
                || et4.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_SHORT).show();
            return;
        }
        myData.valuebefore = et1.getText().toString();
        myData.originalBase = et2.getText().toString();
        myData.goalBase = et4.getText().toString();
        int k=Integer.valueOf(et2.getText().toString());//原进制
        int j=Integer.valueOf(et4.getText().toString());//目标进制
        String str=et1.getText().toString();
        String s = MathCal.binaryConversion(k, str, j);
        et3.setText(s);
    }
}
