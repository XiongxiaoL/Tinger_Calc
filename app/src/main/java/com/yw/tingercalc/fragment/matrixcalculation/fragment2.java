package com.yw.tingercalc.fragment.matrixcalculation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;
import com.yw.tingercalc.activity.InputMatrixActivity;
import com.yw.tingercalc.utils.Data;
import com.yw.tingercalc.utils.Matrix;
import com.yw.tingercalc.utils.myData;

//矩阵计算-多矩阵
public class fragment2 extends Fragment implements View.OnClickListener {
    TextView matrixA,matrixB,tv;
    EditText et1,et2;
    CheckBox check1,check2;
    Button submit,exChange;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_2,null);
        initView(view);
        setOnClickListener();
        if (myData.MatrixApow!=null){
            et1.setText(myData.MatrixApow);
        }
        if (myData.MatrixBpow!=null){
            et2.setText(myData.MatrixBpow);
        }
        return view;
    }

    private void setOnClickListener() {
        matrixA.setOnClickListener(this);
        matrixB.setOnClickListener(this);
        submit.setOnClickListener(this);
        exChange.setOnClickListener(this);
    }

    private void initView(View view) {
        matrixA=view.findViewById(R.id.tv_1);
        matrixB=view.findViewById(R.id.tv_2);
        et1=view.findViewById(R.id.et_1);
        et2=view.findViewById(R.id.et_2);
        check1=view.findViewById(R.id.check1);
        check2=view.findViewById(R.id.check2);
        submit=view.findViewById(R.id.submit);
        exChange=view.findViewById(R.id.exchange);
        tv=view.findViewById(R.id.tv);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), InputMatrixActivity.class);

        switch (v.getId()){
            case R.id.tv_1:
                
                intent.putExtra("title",getResources().getString(R.string.MatrixA));
                intent.putExtra("tip",getResources().getString(R.string.enterMatrixA));
                startActivityForResult(intent, 1);//带请求码打开activity (请求码自定 这里设为1

                break;
            case R.id.tv_2:
                intent.putExtra("title",getResources().getString(R.string.MatrixB));
                intent.putExtra("tip",getResources().getString(R.string.enterMatrixB));
                startActivityForResult(intent, 2);//带请求码打开activity (请求码自定 这里设为2

                break;
            case R.id.submit:
                count();
                break;
            case R.id.exchange:
                String t=Data.A;
                Data.A=Data.B;
                Data.B=t;
                Toast.makeText(getActivity(),R.string.exchangeMatrixSuccess,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void count() {
        myData.MatrixApow = et1.getText().toString();
        myData.MatrixBpow = et2.getText().toString();
        if(!check1.isChecked()&&!check2.isChecked()){
            Toast.makeText(getActivity(),R.string.noMatrix,Toast.LENGTH_SHORT).show();
            return;
        }
        if(check1.isChecked()){
            if(Data.A.isEmpty()){
                Toast.makeText(getActivity(),R.string.noMatrixA,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(check2.isChecked()){
            if(Data.B.isEmpty()){
                Toast.makeText(getActivity(),R.string.noMatrixB,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        int A=1,B=1;
        if(!et1.getText().toString().isEmpty()){
            myData.MatrixApow = et1.getText().toString();
            if (isNumeric(et1.getText().toString())) {
                A = Integer.valueOf(et1.getText().toString());
                if (A > 21) {
                    Toast.makeText(getActivity(), R.string.maxPower, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {
                Toast.makeText(getActivity(), R.string.qingshuruzhengshu, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!et2.getText().toString().isEmpty()){
            myData.MatrixBpow = et2.getText().toString();
            B=Integer.valueOf(et2.getText().toString());
            if(B>21){
                Toast.makeText(getActivity(),R.string.maxPower,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Matrix a = null;
        Matrix b = null;
        if(check1.isChecked()){
            String[][] s = Matrix.StringToArray(Data.A);
            //int row=s.length;
            a=new Matrix(s);
        }
        if(check2.isChecked()){
            String[][] s = Matrix.StringToArray(Data.B);
            //int row=s.length;
            b=new Matrix(s);
        }
        String[][] s = null;
        if(a!=null&&b!=null){
            Matrix c = Matrix.multiply(a, Byte.valueOf(A + ""), b, Byte.valueOf(B + ""));
            if (c.getBasicType()==0){
                switch (c.getErrorType()){
                    case 0:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error0)}};
                        break;
                    }
                    case 1:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error1)}};
                        break;
                    }
                    case 2:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error2)}};
                        break;
                    }
                    case 21:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error21)}};
                        break;
                    }
                    case 22:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error22)}};
                        break;
                    }
                    case 23:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error23)}};
                        break;
                    }
                    case 24:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error24)}};
                        break;
                    }
                    case 3:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error3)}};
                        break;
                    }
                    case 31:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error31)}};
                        break;
                    }
                    case 32:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error32)}};
                        break;
                    }
                    case 33:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error33)}};
                        break;
                    }
                    case 4:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error4)}};
                        break;
                    }
                    case 41:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error41)}};
                        break;
                    }
                    case 42:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error42)}};
                        break;
                    }
                    case 43:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error43)}};
                        break;
                    }
                    case 5:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error5)}};
                        break;
                    }
                    case 6:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error6)}};
                        break;
                    }
                    case 7:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error7)}};
                        break;
                    }
                    case 71:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error71)}};
                        break;
                    }
                    case 72:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error72)}};
                        break;
                    }
                    default:{
                        s = new String[][]{new String[]{getResources().getString(R.string.errorDefault)}};
                        break;
                    }
                }
            }
            else {
                s = c.getString();
            }
        }
        if(a==null&&b!=null){
            Matrix c = b.power(Byte.valueOf(B+""));
            if (c.getBasicType()==0){
                switch (c.getErrorType()){
                    case 0:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error0)}};
                        break;
                    }
                    case 1:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error1)}};
                        break;
                    }
                    case 2:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error2)}};
                        break;
                    }
                    case 21:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error21)}};
                        break;
                    }
                    case 22:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error22)}};
                        break;
                    }
                    case 23:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error23)}};
                        break;
                    }
                    case 24:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error24)}};
                        break;
                    }
                    case 3:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error3)}};
                        break;
                    }
                    case 31:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error31)}};
                        break;
                    }
                    case 32:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error32)}};
                        break;
                    }
                    case 33:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error33)}};
                        break;
                    }
                    case 4:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error4)}};
                        break;
                    }
                    case 41:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error41)}};
                        break;
                    }
                    case 42:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error42)}};
                        break;
                    }
                    case 43:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error43)}};
                        break;
                    }
                    case 5:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error5)}};
                        break;
                    }
                    case 6:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error6)}};
                        break;
                    }
                    case 7:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error7)}};
                        break;
                    }
                    case 71:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error71)}};
                        break;
                    }
                    case 72:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error72)}};
                        break;
                    }
                    default:{
                        s = new String[][]{new String[]{getResources().getString(R.string.errorDefault)}};
                        break;
                    }
                }
            }
            else {
                s = c.getString();
            }
           //s= b.power(Byte.valueOf(B+"")).getString();
        }
        if(b==null&&a!=null){
            Matrix c = a.power(Byte.valueOf(A+""));
            if (c.getBasicType()==0){
                switch (c.getErrorType()){
                    case 0:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error0)}};
                        break;
                    }
                    case 1:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error1)}};
                        break;
                    }
                    case 2:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error2)}};
                        break;
                    }
                    case 21:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error21)}};
                        break;
                    }
                    case 22:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error22)}};
                        break;
                    }
                    case 23:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error23)}};
                        break;
                    }
                    case 24:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error24)}};
                        break;
                    }
                    case 3:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error3)}};
                        break;
                    }
                    case 31:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error31)}};
                        break;
                    }
                    case 32:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error32)}};
                        break;
                    }
                    case 33:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error33)}};
                        break;
                    }
                    case 4:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error4)}};
                        break;
                    }
                    case 41:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error41)}};
                        break;
                    }
                    case 42:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error42)}};
                        break;
                    }
                    case 43:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error43)}};
                        break;
                    }
                    case 5:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error5)}};
                        break;
                    }
                    case 6:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error6)}};
                        break;
                    }
                    case 7:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error7)}};
                        break;
                    }
                    case 71:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error71)}};
                        break;
                    }
                    case 72:{
                        s = new String[][]{new String[]{getResources().getString(R.string.error72)}};
                        break;
                    }
                    default:{
                        s = new String[][]{new String[]{getResources().getString(R.string.errorDefault)}};
                        break;
                    }
                }
            }
            else {
                s = c.getString();
            }
           //s= a.power(Byte.valueOf(A+"")).getString();
        }
        tv.setText(getFormatString(s));
    }
    public String getFormatString(String[][]s){
        String s1="";
        if (s.length==1){
            //只有一行？
            if (s[0].length==1){
                //只有一行一列？
                s1+=String.format("%10s", s[0][0]);
                return s1;
            }else{
                for(int j=0;j<s[0].length - 1;j++){
                    s[0][j]=String.format("%10s", s[0][j]+",");
                    s1+=s[0][j];
                }
                s1 += String.format("%10s", s[0][s[0].length - 1]);
                return s1;
            }
        }
        else {
            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s[i].length - 1; j++) {
                    s[i][j] = String.format("%10s", s[i][j] + "  ,  ");
                    s1 += s[i][j];
                }
                s1 += String.format("%10s", s[i][s[i].length - 1] + ";\n");
            }
            return s1;
        }
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
