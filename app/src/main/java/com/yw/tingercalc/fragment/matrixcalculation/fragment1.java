package com.yw.tingercalc.fragment.matrixcalculation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.util.AttributeSet;
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


public class fragment1 extends Fragment implements View.OnClickListener {
    EditText et1;
    Button submit,clear;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    TextView c1,c2,c3,c4,c5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_1,null);
        initView(view);
        setOnClickListener();
        if (myData.matrixSign!=null){
            et1.setText(myData.matrixSign);
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
        c1=view.findViewById(R.id.c1);
        c2=view.findViewById(R.id.c2);
        c3=view.findViewById(R.id.c3);
        c4=view.findViewById(R.id.c4);
        c5=view.findViewById(R.id.c5);
        et1=view.findViewById(R.id.et_1);
        tv1 = view.findViewById(R.id.tv_1);
        tv2 = view.findViewById(R.id.tv_2);
        tv3 = view.findViewById(R.id.tv_3);
        tv4 = view.findViewById(R.id.tv_4);
        tv5 = view.findViewById(R.id.tv_5);
        tv6 = view.findViewById(R.id.tv_6);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int index;
        Editable edit;
        switch (v.getId()){
            case R.id.clear:
                et1.setText("");
                break;
            case R.id.submit:
                count();
                break;
            case R.id.c1:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
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
                break;
            case R.id.c5:
                index = et1.getSelectionStart(); //获取光标所在位置
                edit = et1.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(";");
                } else {
                    edit.insert(index, ";"); //光标所在位置插入文字
                }
                break;
        }
    }

    public static class NoteContentEditText extends androidx.appcompat.widget.AppCompatEditText {

        public NoteContentEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override
        protected void onSelectionChanged(int selStart, int selEnd) {
            // TODO Auto-generated method stub
            super.onSelectionChanged(selStart, selEnd);
            //Logg.D("onSelectionChanged selStart "+selStart+" selEnd "+selEnd);
        }
    }

    private void count() {
        if(et1.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.noData,Toast.LENGTH_SHORT).show();
            return;
        }
        myData.matrixSign = et1.getText().toString();
        String[][] strings = Matrix.StringToArray(et1.getText().toString());
        int row=strings.length;
        boolean judge = Matrix.judge(strings);
        if(!judge){
            Toast.makeText(getActivity(),R.string.wrongMatrixData,Toast.LENGTH_SHORT).show();
            return;
        }
        Matrix m = new Matrix(strings);
        Matrix m1 = m.det();
        if (m1.getBasicType() == 0){
            tv1.setText(getFormatString(getString(m1.getErrorType())));
        }
        else {
            tv1.setText(getFormatString(m1.getString()));
        }

        m1 = m.inv();
        if (m1.getBasicType() == 0){
            tv2.setText(getFormatString(getString(m1.getErrorType())));
        }
        else {
            tv2.setText(getFormatString(m1.getString()));
        }
        //tv2.setText(getFormatString(m.inv().getString()));

        m1 = m.rowEchelon();
        if (m1.getBasicType() == 0){
            tv3.setText(getFormatString(getString(m1.getErrorType())));
        }
        else {
            tv3.setText(getFormatString(m1.getString()));
        }
        //tv3.setText(getFormatString(m.rowEchelon().getString()));

        m1 = m.rank();
        if (m1.getBasicType() == 0){
            tv4.setText(getFormatString(getString(m1.getErrorType())));
        }
        else {
            tv4.setText(getFormatString(m1.getString()));
        }
        //tv4.setText(getFormatString(m.rank().getString()));
        String[] S = m.positive();
        if (S[1].equals("")){
            //说明是错误
            if (S[1].equals("行列式求值发生错误")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.wrongDet)}}));
            }
            else if (S[1].equals("请输入方阵")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.enterFangzhen)}}));
            }
            else if (S[1].equals("数据错误")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.wrongDta1)}}));
            }
            else {
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.error6)}}));
            }
        }
        else {
            if (S[0].equals("0")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.zhengdinde)}}));
            }
            else if (S[0].equals("1")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.fudinde)}}));
            }
            else if (S[0].equals("2")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.all0)}}));
            }
            else if (S[0].equals("3")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.banzhengdin)}}));
            }
            else if (S[0].equals("4")){
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.banfudin)}}));
            }
            else {//if (S[0].equals("5"))
                tv5.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.zhengdinpandin) + "\n" + getResources().getString(R.string.shunxuzhuzishi) + m.getLine() + getResources().getString(R.string.jieyiciwei) + S[1] + "\n" + getResources().getString(R.string.suoyi) + getResources().getString(R.string.budin)}}));
            }
        }
        //tv5.setText(m.positive());

        S = m.orthogonal();
        if (S[0].equals("该矩阵与其转置的乘积为1，属于第一种正交变换，也称为旋转")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.firstZhengjiaobianhuan)}}));
        }
        else if (S[0].equals("该矩阵与其转置的乘积为-1，属于第二种正交变换，也称为镜面反射")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.SecondZhengjiaobianhuan)}}));
        }
        else if (S[0].equals("这不是正交变换，其与其转置的乘积的行列式为")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.noZhengjiaobianhuan) + S[1]}}));
        }
        else if (S[0].equals("出现错误，不能判断正交变换的类型")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.nozhengjiaobianhuan)}}));
        }
        else if (S[0].equals("发生错误")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.fashengcuowu)}}));
        }
        else if (S[0].equals("数据错误")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.wrongDta1)}}));
        }
        else if (S[0].equals("暂不支持的运算")){
            tv6.setText(getFormatString(new String[][]{new String[]{getResources().getString(R.string.error6)}}));
        }
        //tv6.setText(m.orthogonal());

    }

    public String[][] getString(byte errorType){
        String s = "";
        switch (errorType){
            case 0:{
                s = getResources().getString(R.string.error0);
                break;
            }
            case 1:{
                s = getResources().getString(R.string.error1);
                break;
            }
            case 2:{
                s = getResources().getString(R.string.error2);
                break;
            }
            case 21:{
                s = getResources().getString(R.string.error21);
                break;
            }
            case 22:{
                s = getResources().getString(R.string.error22);
                break;
            }
            case 23:{
                s = getResources().getString(R.string.error23);
                break;
            }
            case 24:{
                s = getResources().getString(R.string.error24);
                break;
            }
            case 3:{
                s = getResources().getString(R.string.error3);
                break;
            }
            case 31:{
                s = getResources().getString(R.string.error31);
                break;
            }
            case 32:{
                s = getResources().getString(R.string.error32);
                break;
            }
            case 33:{
                s = getResources().getString(R.string.error33);
                break;
            }
            case 4:{
                s = getResources().getString(R.string.error4);
                break;
            }
            case 41:{
                s = getResources().getString(R.string.error41);
                break;
            }
            case 42:{
                s = getResources().getString(R.string.error42);
                break;
            }
            case 43:{
                s = getResources().getString(R.string.error43);
                break;
            }
            case 5:{
                s = getResources().getString(R.string.error5);
                break;
            }
            case 6:{
                s = getResources().getString(R.string.error6);
                break;
            }
            case 7:{
                s = getResources().getString(R.string.error7);
                break;
            }
            case 71:{
                s = getResources().getString(R.string.error71);
                break;
            }
            case 72:{
                s = getResources().getString(R.string.error72);
                break;
            }
            default:{
                s = getResources().getString(R.string.errorDefault);
                break;
            }
        }
        return new String[][]{new String[]{s}};
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
                    s[0][j]=String.format("%10s", s[0][j]+"  ,  ");
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
}
