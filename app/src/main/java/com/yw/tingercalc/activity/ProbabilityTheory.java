package com.yw.tingercalc.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.MathCal;
import com.yw.tingercalc.utils.myData;
import com.yw.tingercalc.utils.probability;

import java.util.Locale;

//概率论
public class ProbabilityTheory extends AppCompatActivity {
    EditText et1,et2,et3,k;
    CheckBox checkBox1,checkBox2;
    Switch aSwitch;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probability_theory);
        initView();
        if (myData.proData1!=null){
            et1.setText(myData.proData1);
        }
        if (myData.proData2!=null){
            et2.setText(myData.proData2);
        }
        if (myData.proData3!=null){
            et3.setText(myData.proData3);
        }
        if (myData.proK!=null){
            k.setText(myData.proK);
        }
    }

    private void initView() {
        et1=findViewById(R.id.et_1);
        et2=findViewById(R.id.et_2);
        et3=findViewById(R.id.et_3);
        k=findViewById(R.id.ed_k);
        checkBox1=findViewById(R.id.check1);
        checkBox2=findViewById(R.id.check2);
        aSwitch=findViewById(R.id.switch_);
        tv1=findViewById(R.id.tv_1);
        tv2=findViewById(R.id.tv_2);
        tv3=findViewById(R.id.tv_3);
        tv4=findViewById(R.id.tv_4);
        tv5=findViewById(R.id.tv_5);
        tv6=findViewById(R.id.tv_6);
        tv7=findViewById(R.id.tv_7);
        tv8=findViewById(R.id.tv_8);
        tv9=findViewById(R.id.tv_9);
    }
    //计算
    public void submit(View view) {
        myData.proData1 = et1.getText().toString();
        myData.proData2 = et2.getText().toString();
        myData.proData3 = et3.getText().toString();
        myData.proK = k.getText().toString();
        double[] a1=null,a2=null,a3=null;
        if(!aSwitch.isChecked()&&et3.getText().toString().isEmpty()){
            Toast.makeText(ProbabilityTheory.this,R.string.PleaseEntergailv,Toast.LENGTH_LONG).show();
        return;
        }
        if(checkBox1.isChecked()) {
            if (et1.getText().toString().isEmpty()) {
                Toast.makeText(ProbabilityTheory.this, R.string.noData, Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(checkBox2.isChecked()) {
            if(et2.getText().toString().isEmpty()){
                Toast.makeText(ProbabilityTheory.this,R.string.noData,Toast.LENGTH_LONG).show();
                return;
            }
            myData.proData2 = et2.getText().toString();
            a2 = MathCal.StringToDouble(et2.getText().toString());
           // Log.d("12345","a2长度"+a2.length);
        }

        if(!checkBox1.isChecked()&&!checkBox2.isChecked()){
            Toast.makeText(ProbabilityTheory.this,R.string.noChoice,Toast.LENGTH_LONG).show();
            return;
        }
        if(checkBox1.isChecked()){
             a1 = MathCal.StringToDouble(et1.getText().toString());
             myData.proData1 = et1.getText().toString();
             //Log.d("12345","a1长度"+a1.length);
        }
        if(checkBox2.isChecked()){
            a2 = MathCal.StringToDouble(et2.getText().toString());
            myData.proData2 = et2.getText().toString();
           // Log.d("12345","a1长度"+a2.length);
        }
        if(!aSwitch.isChecked()){
             a3 = MathCal.StringToDouble(et3.getText().toString());
            myData.proData3 = et3.getText().toString();
            //Log.d("12345","a1长度"+a3.length);
        }
        if(a1!=null){
            if(a1.length==0){
                Toast.makeText(ProbabilityTheory.this,R.string.wrongAtFirst,Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(a2!=null){
            if(a2.length==0){
                Toast.makeText(ProbabilityTheory.this,R.string.wrongAtSecond,Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(a1!=null&&a2!=null){
            if(a1.length!=a2.length){
                Toast.makeText(ProbabilityTheory.this,R.string.differentLength,Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(aSwitch.isChecked()&&checkBox1.isChecked()){
            a3=new double[a1.length];
            for(int i=0;i<a1.length;i++){
                a3[i]=1.0/a1.length;
                //Log.d("12345","a3第"+i+"-->"+a3[i]);
            }
        }
        if(aSwitch.isChecked()&&checkBox2.isChecked()){
            a3=new double[a2.length];
            for(int i=0;i<a2.length;i++){
                a3[i]=1.0/a2.length;
                //Log.d("12345","a3第"+i+"-->"+a3[i]);
            }
        }

        //只输入第一组数据  输入在第一个输入框中
        probability p;
        if(a2==null){
            //Log.d("12345","只输入了一组数据");
            if(!aSwitch.isChecked()){//第三组数据有用
                if(a1.length!=a3.length){
                    Toast.makeText(ProbabilityTheory.this,R.string.differentLength,Toast.LENGTH_LONG).show();
                    return;
                }
                p=new probability(a1,a3);

            }else {
                //Log.d("12345","只输入了一组数据--等概率"+   Arrays.toString(a1));
                p=new probability(a1);
            }
            String s5="";
            String s7="";
            if(!k.getText().toString().isEmpty()){
                //Log.d("12345","K不为空");
                myData.proK = k.getText().toString();
                s5=MathCal.abandonZero(p.getVk(Integer.valueOf(k.getText().toString())));
                s7=MathCal.abandonZero(p.getCk(Integer.valueOf(k.getText().toString())));

            }

            setTest(MathCal.abandonZero(p.getAverage()),
                    MathCal.abandonZero(p.getBetak()),
                            MathCal.abandonZero(p.getVar()),
                                    MathCal.abandonZero(p.getBetas()),
                    s5, MathCal.abandonZero(p.getCV()),

                    s7,"",""
                    );
            return;
        }

        //只输入一组数据  输入在第二个框中
        if(a1==null){
            //Log.d("12345","只输入了一组数据");
            if(!aSwitch.isChecked()){//第三组数据有用
                if(a2.length!=a3.length){
                    Toast.makeText(ProbabilityTheory.this,R.string.differentLength,Toast.LENGTH_LONG).show();
                    return;
                }
                p=new probability(a2,a3);

            }else {
                //Log.d("12345","只输入了一组数据--等概率"+   Arrays.toString(a2));
                p=new probability(a2);
            }
            String s5="";
            String s7="";
            if(!k.getText().toString().isEmpty()){
                //Log.d("12345","K不为空");
                s5=MathCal.abandonZero(p.getVk(Integer.valueOf(k.getText().toString())));
                s7=MathCal.abandonZero(p.getCk(Integer.valueOf(k.getText().toString())));

            }

            setTest(MathCal.abandonZero(p.getAverage()),
                    MathCal.abandonZero(p.getBetak()),
                    MathCal.abandonZero(p.getVar()),
                    MathCal.abandonZero(p.getBetas()),
                    s5, MathCal.abandonZero(p.getCV()),
                    s7,"",""
            );
            return;
        }


        //输入两组数据
        //协方差
        //Log.d("12345","输入了两组数据");
        p=new probability(a1,a2,a3);
        String s8 = MathCal.abandonZero(probability.getCovariance(a1, a2, a3));
        String s9 = MathCal.abandonZero(probability.getCorr(a1,a2,a3));
        String s5="";
        String s7="";
        if(!k.getText().toString().isEmpty()){
            //Log.d("12345","k不为空");
            s5=MathCal.abandonZero(p.getVk(Integer.valueOf(k.getText().toString())));
            s7=MathCal.abandonZero(p.getCk(Integer.valueOf(k.getText().toString())));

        }
        setTest(MathCal.abandonZero(p.getAverage()),
                MathCal.abandonZero(p.getBetak()),
                MathCal.abandonZero(p.getVar()),
                MathCal.abandonZero(p.getBetas()),
                s5, MathCal.abandonZero(p.getCV()),
                s7,s8,s9
        );
        setTest("","","","","","","",s8,s9
        );
    }

    @SuppressLint("SetTextI18n")
    private void setTest(String s1, String s2,
                         String s3, String s4,
                         String s5, String s6,
                         String s7, String s8,
                         String s9) {
        tv1.setText(getResources().getString(R.string.Expectation)+s1);
        tv2.setText(getResources().getString(R.string.KurtosisCoefficient)+s2);
        tv3.setText(getResources().getString(R.string.variance)+s3);
        tv4.setText(getResources().getString(R.string.coefficientOfSkewness)+s4);
        tv5.setText(getResources().getString(R.string.k_origin)+s5);
        tv6.setText(getResources().getString(R.string.CoefficientOfVariation)+s6);
        tv7.setText(getResources().getString(R.string.K_center)+s7);
        tv8.setText(getResources().getString(R.string.covariance)+s8);
        tv9.setText(getResources().getString(R.string.coefficientOfSkewness)+s9);
    }

    @SuppressLint("NonConstantResourceId")
    public void clear(View view) {
        switch (view.getId()){
            case R.id.clear1:
                et1.setText("");
                break;
            case R.id.clear2:
                et2.setText("");
                break;
            case R.id.clear3:
                et3.setText("");
                break;
        }
    }

    public void back(View view) {
        myData.proData1 = et1.getText().toString();
        myData.proData2 = et2.getText().toString();
        myData.proData3 = et3.getText().toString();
        myData.proK = k.getText().toString();
        finish();
    }

    public static String getSystemLanguage() {
        //抓取系统语言，如果是中文，返回zh，英文，返回en
        return Locale.getDefault().getLanguage();
    }

    public void help(View view) {
        String a = getSystemLanguage();
        Intent intent=new Intent(this, DocumentActivity.class);
        if (a.equals("en")) {//系统语言为英文
            intent.putExtra("type", 11);
            startActivity(intent);
        } else {
            intent.putExtra("type", 1);
            startActivity(intent);
        }
    }
}