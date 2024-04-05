package com.yw.tingercalc.fragment.matrixcalculation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
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
import com.yw.tingercalc.utils.Matrix;
import com.yw.tingercalc.utils.myData;

import java.util.Arrays;


public class fragment3 extends Fragment implements View.OnClickListener {
    EditText et1;
    TextView tv1;
    Button clear,submit;
    TextView c1,c2,c3,c4,c5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_3,null);
        initView(view);
        setOnClickListener();
        if (myData.EquationSet!=null){
            et1.setText(myData.EquationSet);
        }
        return view;
    }

    private void setOnClickListener() {
        submit.setOnClickListener(this);
        clear.setOnClickListener(this);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
    }

    private void initView(View view) {
        clear=view.findViewById(R.id.clear);
        submit=view.findViewById(R.id.submit);
        et1=view.findViewById(R.id.et_1);
        tv1 = view.findViewById(R.id.tv_1);
        c1=view.findViewById(R.id.c1);
        c2=view.findViewById(R.id.c2);
        c3=view.findViewById(R.id.c3);
        c4=view.findViewById(R.id.c4);
        c5=view.findViewById(R.id.c5);

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
            case R.id.c1:
                int index = et1.getSelectionStart(); //获取光标所在位置
                Editable edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append("(");
                } else {
                    edit.insert(index, "("); //光标所在位置插入文字
                }
                //et1.getText().append("(");
                break;
            case R.id.c2:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(")");
                } else {
                    edit.insert(index, ")"); //光标所在位置插入文字
                }
                //et1.getText().append(")");
                break;
            case R.id.c3:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append("^");
                } else {
                    edit.insert(index, "^"); //光标所在位置插入文字
                }
                //et1.getText().append("^");
                break;
            case R.id.c4:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(",");
                } else {
                    edit.insert(index, ","); //光标所在位置插入文字
                }
                //et1.getText().append(",");
                break;
            case R.id.c5:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(";");
                } else {
                    edit.insert(index, ";"); //光标所在位置插入文字
                }
                //et1.getText().append(";");
                break;
        }

    }

    private void count() {
        if(et1.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.noData,Toast.LENGTH_SHORT).show();
            return;
        }
        myData.EquationSet = et1.getText().toString();
        String[][] strings = Matrix.StringToArray(et1.getText().toString());
        boolean judge = Matrix.judge(strings);
        if(!judge){
            Toast.makeText(getActivity(),R.string.wrongMatrixData,Toast.LENGTH_SHORT).show();
            return;
        }
        Matrix m = new Matrix(strings);
        String[] strings1 = m.linearSystemEquations();
        String s= "";
        if (strings1[0].equals("请保证你所输入的是增广矩阵(矩阵列数=矩阵行数+1)")){
            s = getResources().getString(R.string.baozhengshizengguangjuzhen);
        }
        else if (strings1[0].equals("该系数矩阵是奇异的(行列式为0),故原方程组有无穷多解")){
            s = getResources().getString(R.string.jiyide);
        }
        else if (strings1[0].equals("暂不支持的运算")){
            s = getResources().getString(R.string.error6);
        }
        else {
            for (int i = 0; i < strings1.length; i++) {
                s += strings1[i] + "\n";
            }
        }
        tv1.setText(s);
    }
}
