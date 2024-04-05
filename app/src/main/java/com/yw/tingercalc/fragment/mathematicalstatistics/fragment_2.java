package com.yw.tingercalc.fragment.mathematicalstatistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;
import com.yw.tingercalc.adapter.MyAdapter;
import com.yw.tingercalc.bean.ANOVA;
import com.yw.tingercalc.bean.DesrcBean;
import com.yw.tingercalc.utils.NASA;
import com.yw.tingercalc.utils.myData;

import java.util.ArrayList;
import java.util.Arrays;

//数理统计--方差分析
public class fragment_2 extends Fragment implements View.OnClickListener {
    EditText et;
    Button clear,submit;
    ListView ls1,ls2;
    ArrayAdapter ad1;
    ArrayAdapter ad2;
    TextView desc,anova;
    TableRow tr1,tr2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_2,null);
        initView(view);
        setOnClickListener();
        if (myData.ANOVASample!=null){
            et.setText(myData.ANOVASample);
        }
        return view;
    }

    private void setOnClickListener() {
        clear.setOnClickListener(this);
        submit.setOnClickListener(this);
        desc.setOnClickListener(this);
        anova.setOnClickListener(this);

    }

    private void initView(View view) {
        et=view.findViewById(R.id.et_1);
        clear=view.findViewById(R.id.clear);
        submit=view.findViewById(R.id.submit);
        ls1=view.findViewById(R.id.ls1);
        ls2=view.findViewById(R.id.ls2);
        desc=view.findViewById(R.id.desc);
        anova=view.findViewById(R.id.anova);
        tr1=view.findViewById(R.id.tr1);
        tr2=view.findViewById(R.id.tr2);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                et.setText("");
                break;
            case R.id.submit:
                count();
                break;
            case R.id.desc:
                if(ls1.getVisibility()==View.VISIBLE){
                    ls1.setVisibility(View.GONE);
                    tr1.setVisibility(View.GONE);
                }else {
                    ls1.setVisibility(View.VISIBLE);
                    tr1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.anova:
                if(ls2.getVisibility()==View.VISIBLE){
                    ls2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                }else {
                    ls2.setVisibility(View.VISIBLE);
                    tr2.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void count() {
        if(et.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),R.string.noData,Toast.LENGTH_SHORT).show();
            return;
        }
        myData.ANOVASample = et.getText().toString();
        double[][] doubles = NASA.StringToDouble_ANOVA(et.getText().toString());
        ArrayList<DesrcBean>data1=new ArrayList<>();
        ArrayList<ANOVA>data2=new ArrayList<>();
        if(doubles.length==0){
            Toast.makeText(getActivity(),R.string.wrongData,Toast.LENGTH_SHORT).show();
            return;
        }else {
            String[][][] D = NASA.ANOVA(doubles);
            //先打印第一张表：
            DesrcBean desrcBean;
            ANOVA anova;
            //System.out.println("\n描述统计：");
            for (int i = 0;i<D[0].length;i++){
                desrcBean=new DesrcBean();
                try{
                    desrcBean.setD1(D[0][i][0]);
                    desrcBean.setD2(D[0][i][1]);
                    desrcBean.setD3(D[0][i][2]);
                    desrcBean.setD4(D[0][i][3]);
                    desrcBean.setD5(D[0][i][4]);
                    desrcBean.setD6(D[0][i][5]);
                }catch (Exception e){

                }finally {
                    data1.add(desrcBean);
                }
                //for (int j = 0;j<D[0][i].length;j++){
                //    System.out.printf("%s\t",D[0][i][j]);
                //}
                //System.out.println();
            }
            //System.out.println();
            //System.out.println("\nANOVA表：");
            for (int i = 0;i<D[1].length;i++){
                anova=new ANOVA();
                try{
                    if (i == 0){
                        anova.setD1(getResources().getString(R.string.FactorA));
                    }
                    else if (i == 1){
                        anova.setD1(getResources().getString(R.string.errorE));
                    }
                    else if (i == 2){
                        anova.setD1(getResources().getString(R.string.sumT));
                    }
                    //anova.setD1(D[1][i][0]);
                    anova.setD2(D[1][i][1]);
                    anova.setD3(D[1][i][2]);
                    anova.setD4(D[1][i][3]);
                    anova.setD5(D[1][i][4]);
                }catch (Exception e){
                }finally {
                    data2.add(anova);
                }
                //for (int j = 0;j<D[1][i].length;j++){
                //    System.out.printf("%s\t",D[1][i][j]);
                //}
                //System.out.println();
            }
        }

        //显示数据
        ad1=new MyAdapter(getActivity(),R.layout.item_desc,data1,MyAdapter.DESC_TYPE);
        ls1.setAdapter(ad1);
        ad2=new MyAdapter(getActivity(),R.layout.item_anova,data2,MyAdapter.ANOVA_TYPE);
        ls2.setAdapter(ad2);
    }
}
