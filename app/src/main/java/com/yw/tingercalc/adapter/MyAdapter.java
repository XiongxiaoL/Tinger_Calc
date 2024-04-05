package com.yw.tingercalc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yw.tingercalc.R;
import com.yw.tingercalc.bean.ANOVA;
import com.yw.tingercalc.bean.DesrcBean;

import java.util.ArrayList;

public class MyAdapter  extends ArrayAdapter {
    private final int resourceId;
    public static int DESC_TYPE=1;
    public static int ANOVA_TYPE=2;
    private  int type;
    public  MyAdapter (@NonNull Context context, int resource, ArrayList<?> list,int TYPE) {
        super(context, resource,list);
        resourceId = resource;
        type=TYPE;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object bean =  getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        if(type==DESC_TYPE){
            initView1(view,bean);
        }else {
            initView2(view,bean);
        }



        return view;
    }
    private void initView1(View view,Object o){
        DesrcBean bean=(DesrcBean)o;
        TextView d1 =  view.findViewById(R.id.d1);//获取该布局内的图片视图
        TextView d2 =  view.findViewById(R.id.d2);
        TextView d3=  view.findViewById(R.id.d3);
        TextView d4 =  view.findViewById(R.id.d4);//获取该布局内的图片视图
        TextView d5 =  view.findViewById(R.id.d5);
        TextView d6=  view.findViewById(R.id.d6);
        d1.setText(bean.getD1());
        d2.setText(bean.getD2());
        d3.setText(bean.getD3());
        d4.setText(bean.getD4());
        d5.setText(bean.getD5());
        d6.setText(bean.getD6());
    }
    private void initView2(View view,Object o){
        ANOVA bean=(ANOVA) o;
        TextView d1 =  view.findViewById(R.id.d1);//获取该布局内的图片视图
        TextView d2 =  view.findViewById(R.id.d2);
        TextView d3=  view.findViewById(R.id.d3);
        TextView d4 =  view.findViewById(R.id.d4);//获取该布局内的图片视图
        TextView d5 =  view.findViewById(R.id.d5);
        d1.setText(bean.getD1());
        d2.setText(bean.getD2());
        d3.setText(bean.getD3());
        d4.setText(bean.getD4());
        d5.setText(bean.getD5());
    }
}
