package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yw.tingercalc.R;
import com.yw.tingercalc.fragment.matrixcalculation.fragment1;
import com.yw.tingercalc.fragment.matrixcalculation.fragment2;
import com.yw.tingercalc.fragment.matrixcalculation.fragment3;

import java.util.Locale;

//矩阵计算
public class MatrixCalculation extends AppCompatActivity {
    private FragmentManager fManager;//管理Fragment界面
    Fragment f1;
    Fragment f2;
    Fragment f3;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_calculation);
        title=findViewById(R.id.title);
        if(f1==null){
            f1=new fragment1();
        }
        fManager=getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.fragment,f1).commit();
    }
    public void back(View view) {
        start(MainActivity.class);
    }

    private void start(Class cls) {
        Intent intent=new Intent(MatrixCalculation.this,cls);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    public void chooseFragment(View view) {
        fManager=getSupportFragmentManager();
        switch (view.getId()){

            case R.id.c1:
                title.setText(R.string.SignMatrixT);
                if(f1==null){
                    f1=new fragment1();
                }
                fManager.beginTransaction().replace(R.id.fragment,f1).commit();
                break;
            case R.id.c2:
                title.setText(R.string.MoreMatrixT);
                if(f2==null){
                    f2=new fragment2();
                }
                fManager.beginTransaction().replace(R.id.fragment,f2).commit();
                break;
            case R.id.c3:
                title.setText(R.string.equationSetT);
                if(f3==null){
                    f3=new fragment3();
                }
                fManager.beginTransaction().replace(R.id.fragment,f3).commit();
                break;
        }
    }

    public static String getSystemLanguage() {
        //抓取系统语言，如果是中文，返回zh，英文，返回en
        return Locale.getDefault().getLanguage();
    }

    public void help(View view) {
        String a = getSystemLanguage();
        Intent intent=new Intent(this, DocumentActivity.class);
        if (a.equals("en")) {//系统语言为英文
            intent.putExtra("type", 22);
            startActivity(intent);
        } else {
            intent.putExtra("type", 2);
            startActivity(intent);
        }
    }
}