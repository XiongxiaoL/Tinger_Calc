package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yw.tingercalc.R;
import com.yw.tingercalc.fragment.numericalcalculation.fragmentF;
import com.yw.tingercalc.fragment.numericalcalculation.fragmentS;
import com.yw.tingercalc.fragment.numericalcalculation.fragmentT;

import java.util.Locale;

//数值计算
public class NumericalCalculation extends AppCompatActivity {
    private FragmentManager fManager;//管理Fragment界面
    Fragment f1;
    Fragment f2;
    Fragment f3;
    TextView title;
    /*定义手势检测实例*/
    //public static GestureDetector detector;
    /*做标签，记录当前是哪个fragment*/
    //public int MARK=0;
    /*定义手势两点之间的最小距离*/
    //final int DISTANT=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerical_calculation);
        title=findViewById(R.id.title);
        if(f1==null){
            f1=new fragmentF();
        }
        fManager=getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.fragment,f1).commit();
        //创建手势检测器
        //detector=new GestureDetector((GestureDetector.OnGestureListener) this);
    }
    public void back(View view) {
        finish();
    }

    /**滑动切换效果的实现*/
    /*public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
        resetlaybg();
        //当是Fragment0的时候
        if (MARK == 0) {
            if (arg1.getX() > arg0.getX() + DISTANT) {
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
                        .show(fragments[1]).commit();
                linearLayouts[1].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[1].setTextColor(getResources().getColor(R.color.lightseagreen));
                MARK = 1;
            } else {
                linearLayouts[0].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[0].setTextColor(getResources().getColor(R.color.black));
            }

        }
        //当是Fragment1的时候
        else if (MARK == 1) {
            if (arg1.getX() > arg0.getX() + DISTANT) {
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
                        .show(fragments[2]).commit();
                linearLayouts[2].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[2].setTextColor(getResources().getColor(R.color.lightseagreen));
                MARK = 2;
            } else if (arg0.getX() > arg1.getX() + DISTANT) {
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
                        .show(fragments[0]).commit();
                linearLayouts[0].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[0].setTextColor(getResources().getColor(R.color.lightseagreen));
                MARK = 0;
            } else {
                linearLayouts[1].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[1].setTextColor(getResources().getColor(R.color.black));
            }
        }
        //当是Fragment2的时候
        else if (MARK == 2) {
            if (arg0.getX() > arg1.getX() + DISTANT) {
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
                        .show(fragments[1]).commit();
                linearLayouts[1].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[1].setTextColor(getResources().getColor(R.color.lightseagreen));
                MARK = 1;
            } else {
                linearLayouts[2].setBackgroundResource(R.drawable.lay_select_bg);
                textViews[2].setTextColor(getResources().getColor(R.color.black));
            }
        }
        return false;
    }*/

    public void chooseFragment(View view) {
        fManager=getSupportFragmentManager();
        switch (view.getId()){
            case R.id.c1:
                title.setText(R.string.interpolationT);
                if(f1==null){
                    f1=new fragmentF();
                }
                fManager.beginTransaction().replace(R.id.fragment,f1).commit();
                break;
            case R.id.c2:
                title.setText(R.string.FittingT);
                if(f2==null){
                    f2=new fragmentS();
                }
                fManager.beginTransaction().replace(R.id.fragment,f2).commit();
                break;
            case R.id.c3:
                title.setText(R.string.HexadecimalConversionT);
                if(f3==null){
                    f3=new fragmentT();
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
            intent.putExtra("type", 44);
            startActivity(intent);
        } else {
            intent.putExtra("type", 4);
            startActivity(intent);
        }
    }
}