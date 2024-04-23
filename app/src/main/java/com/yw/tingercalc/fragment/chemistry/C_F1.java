package com.yw.tingercalc.fragment.chemistry;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;


public class C_F1 extends Fragment implements View.OnClickListener{

    EditText et1;
    TextView et2;
    Button calculate;

    private String s, z;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.c_f1,null);
        initView(view);
        setOnClickListener();

        if (s != null){
            et1.setText(s);
        }

        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        et2 = view.findViewById(R.id.et_2);
        calculate = view.findViewById(R.id.calculate);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calculate:
                count();
                break;
        }
    }

    private void setOnClickListener() {
        calculate.setOnClickListener(this);
    }

    private void count() {

        Map<String, Double> atomicWeights = new HashMap<>();
        atomicWeights.put("H", 1.007);
        atomicWeights.put("He", 4.0026);
        atomicWeights.put("Li", 6.94);
        atomicWeights.put("Be", 9.0122);
        atomicWeights.put("B", 10.81);
        atomicWeights.put("C", 12.011);
        atomicWeights.put("N", 14.007);
        atomicWeights.put("O", 15.999);
        atomicWeights.put("F", 18.998);
        atomicWeights.put("Ne", 20.180);
        atomicWeights.put("Na", 22.990);
        atomicWeights.put("Mg", 24.305);
        atomicWeights.put("Al", 26.982);
        atomicWeights.put("Si", 28.085);
        atomicWeights.put("P", 30.974);
        atomicWeights.put("S", 32.06);
        atomicWeights.put("Cl", 35.45);
        atomicWeights.put("K", 39.098);
        atomicWeights.put("Ar", 39.948);
        atomicWeights.put("Ca", 40.078);
        atomicWeights.put("Sc", 44.956);
        atomicWeights.put("Ti", 47.867);
        atomicWeights.put("V", 50.942);
        atomicWeights.put("Cr", 51.996);
        atomicWeights.put("Mn", 54.938);
        atomicWeights.put("Fe", 55.845);
        atomicWeights.put("Ni", 58.693);
        atomicWeights.put("Co", 58.933);
        atomicWeights.put("Cu", 63.546);
        atomicWeights.put("Zn", 65.38);
        atomicWeights.put("Ga", 69.723);
        atomicWeights.put("Ge", 72.630);
        atomicWeights.put("As", 74.922);
        atomicWeights.put("Se", 78.971);
        atomicWeights.put("Br", 79.904);
        atomicWeights.put("W", 183.84);
        atomicWeights.put("Au", 196.97);
        atomicWeights.put("Ag", 107.87);
        atomicWeights.put("Hg", 200.59);
        atomicWeights.put("Pb", 207.2);



        s = et1.getText().toString();
        z = et2.getText().toString();
        z = calculateMolecularWeight(s, atomicWeights) + "";

        if (Double.parseDouble(z) != -1) {
            et2.setText(z);
        } else {
            et2.setText("总分子量(g/mol)");
        }
    }


    //计算分子量
    private double calculateMolecularWeight(String formula, Map<String, Double> atomicWeights) {
        double totalWeight = 0.0;
        int i = 0;
        while (i < formula.length()) {
            char currentChar = formula.charAt(i);
            if (Character.isLetter(currentChar)) {
                // 获取元素符号
                StringBuilder elementSymbol = new StringBuilder();
                elementSymbol.append(currentChar);
                i++;
                while (i < formula.length() && Character.isLowerCase(formula.charAt(i))) {
                    elementSymbol.append(formula.charAt(i));
                    i++;
                }

                // 获取元素个数
                StringBuilder elementCountStr = new StringBuilder();
                while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                    elementCountStr.append(formula.charAt(i));
                    i++;
                }
                int elementCount = elementCountStr.length() > 0 ? Integer.parseInt(elementCountStr.toString()) : 1;

                // 计算元素质量
                Double elementWeight = atomicWeights.get(elementSymbol.toString());
                if (elementWeight == null) {
                    Toast.makeText(getActivity(), "请输入正确的分子式", Toast.LENGTH_SHORT).show();
                    return -1;
                } else {
                    totalWeight += elementWeight * elementCount;
                }

            } else {
                i++;
            }
        }
        return totalWeight;
    }

}
