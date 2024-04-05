package com.yw.tingercalc.fragment.numericalcalculation;

import android.annotation.SuppressLint;
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
import com.yw.tingercalc.utils.myData;

import java.util.Arrays;


public class fragmentF extends Fragment implements View.OnClickListener {
    EditText et1, et2,et3,et4;
    TextView clear1, clear2,tv1, tv2, tv3;
    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_f,null);
        initView(view);
        setOnClickListener();
        if (myData.interDataX!=null) {
            et1.setText(myData.interDataX);
        }
        if (myData.interDataY!=null){
            et2.setText(myData.interDataY);
        }
        if (myData.interPoint!=null){
            et3.setText(myData.interPoint);
        }
        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        et3 = view.findViewById(R.id.et_insert1);
        et4 = view.findViewById(R.id.et_insert2);
        clear1 = view.findViewById(R.id.clear1);
        clear2 = view.findViewById(R.id.clear2);
        submit = view.findViewById(R.id.submit);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);

    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
        clear1.setOnClickListener(this);
        clear2.setOnClickListener(this);
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
        myData.interDataX = et1.getText().toString();
        myData.interDataY = et2.getText().toString();
        myData.interPoint = et3.getText().toString();
        if (et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_SHORT).show();
            return;
        }
        if (et3.getText().toString().isEmpty() ) {
            Toast.makeText(getActivity(), R.string.nochazhidian, Toast.LENGTH_SHORT).show();
            return;
        }
        myData.interDataX = et1.getText().toString();
        myData.interDataY = et2.getText().toString();
        double[] a1 = MathCal.StringToDouble(et1.getText().toString());
        double[] a2 = MathCal.StringToDouble(et2.getText().toString());
        double a3= Double.parseDouble(et3.getText().toString());
        myData.interPoint = et3.getText().toString();
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
        myData.interDataX = et1.getText().toString();
        myData.interDataY = et2.getText().toString();
        myData.interPoint = et3.getText().toString();
//        if (!et4.getText().toString().isEmpty() ) {
//            int a4=Integer.valueOf(et4.getText().toString());
//            if(a4>a1.length-1||a4<0){
//                Toast.makeText(getActivity(), "牛顿插值的次数不可以大于等于数据点的数目！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String[][] S = MathCal.newton(a1, a2, a3, a4);
//            tv1.setText(Arrays.toString(S));
//            for (int i = 0;i<S.length;i++){
//                for (int j = 0;j<S[i].length;j++){
//                    System.out.print(S[i][j]+"      ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//            System.out.println("待插值点上的函数值 = " + S[S.length-1][1]);
//            System.out.println("误差 = " + S[S.length-1][S[S.length-1].length-1]);
//
//        }else {
//            tv1.setText("未输入插值次数！");
//        }
        String[] strings = MathCal.polynomialVal(a1, a2, a3);
        tv2.setText(getResources().getString(R.string.chazhi) +
                strings[0]+"\n"+getResources().getString(R.string.duoxiangshiwei)+ strings[1]);
        String[] strings1 = MathCal.LagrangeInterpolationPolynomial(a1, a2, a3);
        tv3.setText(getResources().getString(R.string.chazhi) + strings1[0]+"\n"+getResources().getString(R.string.chazhidedao) + strings1[1]+"\n"+getResources().getString(R.string.huajianhoude) + strings1[2]);

    }
}
