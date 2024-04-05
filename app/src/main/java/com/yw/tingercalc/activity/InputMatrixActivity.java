package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.Data;
import com.yw.tingercalc.utils.Matrix;

//多矩阵页面的矩阵输入
public class InputMatrixActivity extends AppCompatActivity {
    EditText et;
    TextView title;
    String mtitle;
    TextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_matrix);
        et=findViewById(R.id.et_1);
        title=findViewById(R.id.title);
        submit=findViewById(R.id.submit);
        String tip=getIntent().getStringExtra("tip");
         mtitle=getIntent().getStringExtra("title");
        et.setHint(tip);
        this.title.setText(mtitle);
        if(!Data.A.isEmpty()&&(mtitle.equals(getResources().getString(R.string.MatrixA)))){
            et.setText(Data.A);
        }
        if(!Data.B.isEmpty()&&(mtitle.equals(getResources().getString(R.string.MatrixB)))){
            et.setText(Data.B);
        }
    }

    public void putData(View view) {
        if(et.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.noMatrix,Toast.LENGTH_SHORT).show();
            return;
        }
        String[][] strings = Matrix.StringToArray(et.getText().toString());
        boolean judge = Matrix.judge(strings);
        if(!judge){
            Toast.makeText(this,R.string.wrongMatrixData,Toast.LENGTH_SHORT).show();
            return;
        }
        if(mtitle.equals(getResources().getString(R.string.MatrixA))){
            Data.A=et.getText().toString();
        }else {
            Data.B=et.getText().toString();
        }

        Intent intent = new Intent();
        intent.putExtra("data",et.getText().toString());
        setResult(Activity.RESULT_OK,intent);
        finish(); //关闭活动
    }

    public void back(View view) {
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    public void choose(View view) {
        switch (view.getId()){
            case R.id.c1:
                int index = et.getSelectionStart(); //获取光标所在位置
                Editable edit = et.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append("(");
                } else {
                    edit.insert(index, "("); //光标所在位置插入文字
                }
                //et.getText().append("(");
                break;
            case R.id.c2:
                index = et.getSelectionStart(); //获取光标所在位置
                edit = et.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(")");
                } else {
                    edit.insert(index, ")"); //光标所在位置插入文字
                }
                //et.getText().append(")");
                break;
            case R.id.c3:
                index = et.getSelectionStart(); //获取光标所在位置
                edit = et.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append("^");
                } else {
                    edit.insert(index, "^"); //光标所在位置插入文字
                }
                //et.getText().append("^");
                break;
            case R.id.c4:
                index = et.getSelectionStart(); //获取光标所在位置
                edit = et.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(",");
                } else {
                    edit.insert(index, ","); //光标所在位置插入文字
                }
                //et.getText().append(",");
                break;
            case R.id.c5:
                index = et.getSelectionStart(); //获取光标所在位置
                edit = et.getEditableText(); //获取EditText的文字
                if (index < 0 || index >= edit.length() ) {
                    edit.append(";");
                } else {
                    edit.insert(index, ";"); //光标所在位置插入文字
                }
                //et.getText().append(";");
                break;
            case R.id.clear:
                et.setText("");
                break;
        }
    }
}