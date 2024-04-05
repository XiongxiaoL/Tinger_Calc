package com.yw.tingercalc.fragment.mathematicalstatistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.MathCal;
import com.yw.tingercalc.utils.NASA;
import com.yw.tingercalc.utils.myData;

//数理统计--假设检验
public class fragment_4 extends Fragment implements View.OnClickListener {
    private static final Double[] m={0.05,0.01,0.1};
    private Spinner spinner;
    private ArrayAdapter<Double> adapter;
    private double choose;
    Button submit,clear;
    EditText et1,et2,et3;
    TextView  tv1, tv2, tv3, tv4, tv5, tv6,
            tv7, tv8;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.layout_4,null);
        initSpinner(view);
        initView(view);
        setOnClickListener();
        if (myData.HySample!=null){
            et1.setText(myData.HySample);
        }
        if (myData.average!=null){
            et2.setText(myData.average);
        }
        if (myData.variance!=null){
            et3.setText(myData.variance);
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

    private void initSpinner(View view) {
        spinner=view.findViewById(R.id.spinner);

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<Double>(getActivity(),android.R.layout.simple_spinner_item,m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
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

    private void count() {
        myData.HySample = et1.getText().toString();
        myData.average = et2.getText().toString();
        myData.variance = et3.getText().toString();
        if(et2.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.noAverage,Toast.LENGTH_SHORT).show();
            return;
        }
        if(et1.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.noData,Toast.LENGTH_SHORT).show();
            return;
        }
        double[] a1 = MathCal.StringToDouble(et1.getText().toString());
        myData.HySample = et1.getText().toString();
        double a2=Double.valueOf(et2.getText().toString());
        myData.average = et2.getText().toString();
        if(et3.getText().toString().isEmpty()){
            String s = NASA.TestT(a1, a2);
            tv2.setText("t="+s);//显示t
            String[] strings = NASA.criticalRegionT(choose,a1.length);
            tv4.setText(strings[0]);
            tv6.setText(strings[1]);
            tv8.setText(strings[2]);
            tv1.setText("u=");//显示u
            tv3.setText("");
            tv5.setText("");
            tv7.setText("");
        }else {
            double a3=Double.valueOf(et3.getText().toString());
            myData.variance = et3.getText().toString();
            String s = NASA.TestU(a1, a2, a3);
            tv1.setText("u="+s);//显示u
            String[] strings = NASA.criticalRegionU(choose);
            tv3.setText(strings[0]);
            tv5.setText(strings[1]);
            tv7.setText(strings[2]);
            tv2.setText("t=");//显示t
           tv4.setText("");
            tv6.setText("");
            tv8.setText("");
        }
//        tv1.setText(S[0][0]);
//        tv2.setText("n="+S[0][1]);
//        tv3.setText(S[0][2]);
//        tv4.setText(S[1][0]);
//        tv5.setText(S[1][2]);
//        tv6.setText(S[2][0]);
//        tv7.setText(S[2][1]);
//        tv8.setText(S[2][2]);

    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
//            view.setText("你的血型是："+m[arg2]);
            choose=m[arg2];
            //Log.d("12345","下拉框选中:"+m[arg2]);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
