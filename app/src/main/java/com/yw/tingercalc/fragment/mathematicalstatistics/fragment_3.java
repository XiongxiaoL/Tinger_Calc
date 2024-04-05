package com.yw.tingercalc.fragment.mathematicalstatistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
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

//数理统计--线性回归
public class fragment_3 extends Fragment implements View.OnClickListener {
    EditText et1, et2,et3,et4;
    TextView clear1, clear2, tv1, tv2, tv3, tv4, tv5, tv6,
            tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16,tv17;
    Button submit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_3, null);
        initView(view);
        setOnClickListener();
        if (myData.mathDataX != null){
            et1.setText(myData.mathDataX);
        }
        if (myData.mathDataY != null){
            et2.setText(myData.mathDataY);
        }
        return view;
    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
        clear1.setOnClickListener(this);
        clear2.setOnClickListener(this);
    }

    private void initView(View view) {
        submit = view.findViewById(R.id.submit);
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        clear1 = view.findViewById(R.id.clear1);
        clear2 = view.findViewById(R.id.clear2);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);
        tv4 = view.findViewById(R.id.tv_4);
        tv5 = view.findViewById(R.id.tv_5);
        tv6 = view.findViewById(R.id.tv_6);
        tv7 = view.findViewById(R.id.tv_7);
        tv8 = view.findViewById(R.id.tv_8);
        tv9 = view.findViewById(R.id.tv_9);
        tv10 = view.findViewById(R.id.tv_10);
        tv11 = view.findViewById(R.id.tv_11);
        tv12 = view.findViewById(R.id.tv_12);
        tv13 = view.findViewById(R.id.tv_13);
        tv14 = view.findViewById(R.id.tv_14);
        tv15 = view.findViewById(R.id.tv_15);
        tv16 = view.findViewById(R.id.tv_16);
        tv17=view.findViewById(R.id.tv_17);
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
            case R.id.submit:
                count();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void count() {
        //check();
        myData.mathDataX = et1.getText().toString();
        myData.mathDataY = et2.getText().toString();
        if (TextUtils.isEmpty(et1.getText())||TextUtils.isEmpty(et2.getText())){
            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_SHORT).show();
            return;
        }
        String ss1 = et1.getText().toString();
        myData.mathDataX = et1.getText().toString();
        String ss2 = et2.getText().toString();
        myData.mathDataY = et2.getText().toString();
        double[] a1 = MathCal.StringToDouble(ss1);
        double[] a2 = MathCal.StringToDouble(ss2);
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
        String[][] S = NASA.unaryLinearRegression(a1, a2);
        //for (int i = 0; i < S.length; i++) {
        //    for (int j = 0; j < S[0].length; j++) {
        //        System.out.print(S[i][j] + "      ");
        //    }
        //    System.out.println();
        //}
        //System.out.println();
        tv1.setText(S[0][0]);
        tv2.setText("n="+S[0][1]);
        tv3.setText(S[0][2]);
        tv4.setText(S[1][0]);
        tv5.setText(S[1][2]);
        tv6.setText(S[2][0]);
        tv7.setText(S[2][1]);
        tv8.setText(S[2][2]);
        tv9.setText(S[3][0]);
        tv10.setText(S[3][1]);
        tv11.setText(S[3][2]);
        tv12.setText(S[4][0]);
        tv13.setText(S[4][1]);
        tv14.setText(S[4][2]);
        tv15.setText(S[5][0]);
        tv16.setText(S[5][1]);
        tv17.setText(S[5][2]);
    }
    /*private void check() {
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "输入的数据不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }*/
}
