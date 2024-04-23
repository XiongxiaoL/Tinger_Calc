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
import com.yw.tingercalc.fragment.physics.P_F1;
import com.yw.tingercalc.fragment.physics.P_F2;
import com.yw.tingercalc.fragment.physics.P_F3;
import com.yw.tingercalc.fragment.physics.P_F4;


public class PhysicsActivity extends AppCompatActivity{

    private FragmentManager fManager;//管理Fragment界面
    Fragment f1;
    Fragment f2;
    Fragment f3;
    Fragment f4;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics);
        title = findViewById(R.id.title);
        if(f1 == null){
            f1 = new P_F1();
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
                title.setText("球面折射");
                if(f1==null){
                    f1=new P_F1();
                }
                fManager.beginTransaction().replace(R.id.fragment,f1).commit();
                break;
            case R.id.c2:
                title.setText("球面反射");
                if(f2==null){
                    f2=new P_F2();
                }
                fManager.beginTransaction().replace(R.id.fragment,f2).commit();
                break;
            case R.id.c3:
                title.setText("相干长度");
                if(f3==null){
                    f3=new P_F3();
                }
                fManager.beginTransaction().replace(R.id.fragment,f3).commit();
                break;
            case R.id.c4:
                title.setText("可见光色");
                if(f4==null){
                    f4=new P_F4();
                }
                fManager.beginTransaction().replace(R.id.fragment,f4).commit();
                break;
        }
    }
}


