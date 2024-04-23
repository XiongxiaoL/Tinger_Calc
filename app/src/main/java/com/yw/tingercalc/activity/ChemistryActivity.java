package com.yw.tingercalc.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.yw.tingercalc.R;
import com.yw.tingercalc.fragment.chemistry.C_F2;
import com.yw.tingercalc.fragment.chemistry.C_F1;


public class ChemistryActivity extends AppCompatActivity{

    private FragmentManager fManager;//管理Fragment界面
    Fragment f1;
    Fragment f2;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistry);
        title = findViewById(R.id.title);
        if(f1 == null){
            f1 = new C_F1();
        }
        fManager = getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.fragment, f1).commit();
    }


    public void back(View view) {
        finish();
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    public void chooseFragment(View view) {
        fManager=getSupportFragmentManager();
        switch (view.getId()){
            case R.id.c1:
                title.setText("分子量");
                if(f1==null){
                    f1=new C_F1();
                }
                fManager.beginTransaction().replace(R.id.fragment,f1).commit();
                break;
            case R.id.c2:
                title.setText("摩尔浓度");
                if(f2==null){
                    f2=new C_F2();
                }
                fManager.beginTransaction().replace(R.id.fragment,f2).commit();
                break;

        }
    }
}


