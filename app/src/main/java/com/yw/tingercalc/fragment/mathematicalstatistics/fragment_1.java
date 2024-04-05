package com.yw.tingercalc.fragment.mathematicalstatistics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.MathCal;
import com.yw.tingercalc.utils.NASA;
import com.yw.tingercalc.utils.myData;
//数理统计

public class fragment_1 extends Fragment implements View.OnClickListener {
    EditText et1,et2,et3;
    Button submit,clear;
    TextView  tv1, tv2, tv3, tv4, tv5, tv6,
            tv7, tv8;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.layout_1,null);

        initView(view);
        setOnClickListener();
        if (myData.mathFeature!=null){
            et1.setText(myData.mathFeature);
        }
        if (myData.K!=null){
            et2.setText(myData.K);
        }
        if (myData.P!=null){
            et3.setText(myData.P);
        }
        return view;
    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    private void initView(View view) {
        clear=view.findViewById(R.id.clear);
        submit=view.findViewById(R.id.submit);
        et1=view.findViewById(R.id.et_1);
        et2=view.findViewById(R.id.et_2);
        et3=view.findViewById(R.id.et_3);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);
        tv4 = view.findViewById(R.id.tv_4);
        tv5 = view.findViewById(R.id.tv_5);
        tv6 = view.findViewById(R.id.tv_6);
        tv7 = view.findViewById(R.id.tv_7);
        tv8 = view.findViewById(R.id.tv_8);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                et1.setText("");
                break;
            case R.id.submit:
                count();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void count() {
        myData.mathFeature = et1.getText().toString();
        myData.K = et2.getText().toString();
        myData.P = et3.getText().toString();
        if(et1.getText().toString().isEmpty()){
            setText();
            Toast.makeText(getActivity(),R.string.noData,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!et3.getText().toString().isEmpty()){
            double a3=NASA.StringToDouble(et3.getText().toString());
            myData.P = et3.getText().toString();
            if(a3<0||a3>1){
                setText();
                Toast.makeText(getActivity(),R.string.wrongP,Toast.LENGTH_SHORT).show();
                return;
            }
            myData.P = et3.getText().toString();
        }
        double[] a1 = MathCal.StringToDouble(et1.getText().toString());
        myData.mathFeature = et1.getText().toString();
        if(a1.length==0){
            setText();
            Toast.makeText(getActivity(),R.string.wrongData,Toast.LENGTH_SHORT).show();
            return;
        }
        //NASA nasa=new NASA(a1);
        String s1 = MathCal.abandonZero(NASA.average(a1));
        if(!et2.getText().toString().isEmpty()){
            //myData.proData2 = et2.getText().toString();
            myData.K = et2.getText().toString();
            int k=Integer.parseInt(et2.getText().toString());
            String s2 =MathCal.abandonZero(NASA.K_centralMoment(a1,k));
            String s4= MathCal.abandonZero(NASA.K_originMoment(a1,k));

            tv2.setText(getResources().getString(R.string.K_center)+s2);
            tv4.setText(getResources().getString(R.string.k_origin)+s4);
        }else {
            tv2.setText(getResources().getString(R.string.K_center));
            tv4.setText(getResources().getString(R.string.k_origin));
        }

        String s3 =MathCal.abandonZero(NASA.median(a1));

        String s5=MathCal.abandonZero(NASA.getVar(a1));
        if(!et3.getText().toString().isEmpty()){
            //myData.proData3 = et3.getText().toString();
            double p=NASA.StringToDouble(et3.getText().toString());
            String s6=MathCal.abandonZero(NASA.sampleQuantile(p,a1));
            tv6.setText(getResources().getString(R.string.pfenweishu)+s6);
        }else {
            tv6.setText(getResources().getString(R.string.pfenweishu));
        }

        String s7=MathCal.abandonZero(NASA.kurtosis(a1) );
        String s8=MathCal.abandonZero(NASA.skewness(a1) );

        tv1.setText(getResources().getString(R.string.Expectation)+s1);

        tv3.setText(getResources().getString(R.string.median)+s3);

        tv5.setText(getResources().getString(R.string.variance)+s5);

        tv7.setText(getResources().getString(R.string.KurtosisCoefficient)+s7);
        tv8.setText(getResources().getString(R.string.coefficientOfSkewness)+s8);
    }
    public void setText( ){
        tv1.setText(getResources().getString(R.string.Expectation));
        tv2.setText(getResources().getString(R.string.K_center));

        tv3.setText(getResources().getString(R.string.median));
        tv4.setText(getResources().getString(R.string.k_origin));
        tv5.setText(getResources().getString(R.string.variance));
        tv6.setText(getResources().getString(R.string.pfenweishu));
        tv7.setText(getResources().getString(R.string.KurtosisCoefficient));
        tv8.setText(getResources().getString(R.string.coefficientOfSkewness));
    }
}
