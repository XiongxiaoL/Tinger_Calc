package com.yw.tingercalc.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.yw.tingercalc.R;
import com.yw.tingercalc.fragment.mathematicalstatistics.fragment_1;
import com.yw.tingercalc.fragment.mathematicalstatistics.fragment_2;
import com.yw.tingercalc.fragment.mathematicalstatistics.fragment_3;
import com.yw.tingercalc.fragment.mathematicalstatistics.fragment_4;

import java.util.Locale;


//数理统计
public class MathematicalStatistics extends AppCompatActivity {
    private FragmentManager fManager;//管理Fragment界面
    Fragment f1;
    Fragment f2;
    Fragment f3;
    Fragment f4;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathematical_statistics);
        title=findViewById(R.id.title);
        if(f1==null){
            f1=new fragment_1();
        }
        fManager=getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.fragment,f1).commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void back(View view) {
        finish();
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    public void chooseFragment(View view) {
        fManager=getSupportFragmentManager();
        switch (view.getId()){
            case R.id.c1:
                title.setText(R.string.tztjT);
                if(f1==null){
                    f1=new fragment_1();
                }
                fManager.beginTransaction().replace(R.id.fragment,f1).commit();
                break;
            case R.id.c2:
                title.setText(R.string.fcfxT);
                if(f2==null){
                    f2=new fragment_2();
                }
                fManager.beginTransaction().replace(R.id.fragment,f2).commit();
                break;
            case R.id.c3:
                title.setText(R.string.xxhgT);
                if(f3==null){
                    f3=new fragment_3();
                }
                fManager.beginTransaction().replace(R.id.fragment,f3).commit();
                break;
            case R.id.c4:
                title.setText(R.string.jsjyT);
                if(f4==null){
                    f4=new fragment_4();
                }
                fManager.beginTransaction().replace(R.id.fragment,f4).commit();
                break;
        }
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public void help(View view) {
        Intent intent=new Intent(MathematicalStatistics.this, DocumentActivity.class);
        String a = getSystemLanguage();
        if (a.equals("en")) {
            intent.putExtra("type", 33);
            startActivity(intent);
        } else {
            intent.putExtra("type", 3);
            startActivity(intent);
        }
    }
}