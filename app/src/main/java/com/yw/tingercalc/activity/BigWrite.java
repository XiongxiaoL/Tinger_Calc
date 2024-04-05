package com.yw.tingercalc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yw.tingercalc.R;
import com.yw.tingercalc.utils.ConvertTable;

import java.text.DecimalFormat;

public class BigWrite extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BigWrite";

    private TextView big_firstText;

    private TextView big_secondText;

    private static String number = "1";

    private final static String[] STR_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍",
            "陆", "柒", "捌", "玖" };
    private final static String[] STR_UNIT = { "", "拾", "佰", "仟", "万", "拾",
            "佰", "仟", "亿", "拾", "佰", "仟" };// 整数单位
    private final static String[] STR_UNIT2 = { "角", "分", "厘" };// 小数单位



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_write);
        findViewById(R.id.returnImg).setOnClickListener(this);

        big_firstText = findViewById(R.id.big_firstText);
        big_secondText = findViewById(R.id.big_secondText);

        big_firstText.setOnClickListener(this);
        big_secondText.setOnClickListener(this);

        findViewById(R.id.one).setOnClickListener(this::numberClick);
        findViewById(R.id.two).setOnClickListener(this::numberClick);
        findViewById(R.id.three).setOnClickListener(this::numberClick);
        findViewById(R.id.four).setOnClickListener(this::numberClick);
        findViewById(R.id.five).setOnClickListener(this::numberClick);
        findViewById(R.id.six).setOnClickListener(this::numberClick);
        findViewById(R.id.seven).setOnClickListener(this::numberClick);
        findViewById(R.id.eight).setOnClickListener(this::numberClick);
        findViewById(R.id.nine).setOnClickListener(this::numberClick);
        findViewById(R.id.zero).setOnClickListener(this::numberClick);
        findViewById(R.id.point).setOnClickListener(this::numberClick);

        findViewById(R.id.clean).setOnClickListener(this::functionClick);

    }

    private void functionClick(View v) {
        switch (v.getId()){
            case R.id.clean:
                big_firstText.setText("0");
                big_secondText.setText("零元整");
                findViewById(R.id.point).setEnabled(true);
                break;
        }
    }

    private void numberClick(View v) {
        if (!(v instanceof Button)){
            return;
        }

        number = ((Button) v).getText().toString();
        //输入的动态string
        StringBuilder first_value = new StringBuilder(big_firstText.getText().toString());

        if (first_value.toString().equals("0") && (number.toCharArray()[0] != '.')){
            first_value = first_value.replace(0,1,number);
        }else{
            int small = 0;
            char targetChar = '.';
            int index = first_value.toString().indexOf(targetChar);
            if (index >= 0) {
                small = first_value.length() - index - 1; // 小数数量
            }
            if (first_value.length() < 12 && small < 3) {
                first_value.append(number);
            }
        }

        big_firstText.setText(first_value);

        //只允许一个小数点和三位小数
        if (first_value.charAt(first_value.length() - 1) == '.') {
            findViewById(R.id.point).setEnabled(false);
            big_secondText.setText(convert(Double.parseDouble(first_value.substring(0, first_value.length() - 1))));
        } else {
            big_secondText.setText(convert(Double.parseDouble(first_value.toString())));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnImg:
                finish();
                break;
        }
    }

    /**
     * 获取可数部分
     *
     * @param num
     *            金额
     * @return 金额整数部分
     */
    public static String getInteger(String num) {
        if (num.indexOf(".") != -1) { // 判断是否包含小数点
            num = num.substring(0, num.indexOf("."));
        }
        num = new StringBuffer(num).reverse().toString(); // 反转字符串
        StringBuffer temp = new StringBuffer(); // 创建一个StringBuffer对象
        for (int i = 0; i < num.length(); i++) {// 加入单位
            temp.append(STR_UNIT[i]);
            temp.append(STR_NUMBER[num.charAt(i) - 48]);
        }
        num = temp.reverse().toString();// 反转字符串
        num = numReplace(num, "零拾", "零"); // 替换字符串的字符
        num = numReplace(num, "零佰", "零"); // 替换字符串的字符
        num = numReplace(num, "零仟", "零"); // 替换字符串的字符
        num = numReplace(num, "零万", "万"); // 替换字符串的字符
        num = numReplace(num, "零亿", "亿"); // 替换字符串的字符
        num = numReplace(num, "零零", "零"); // 替换字符串的字符
        num = numReplace(num, "亿万", "亿"); // 替换字符串的字符
        // 如果字符串以零结尾将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    /**
     * 获取小数部分
     *
     * @param num
     *            金额
     * @return 金额的小数部分
     */
    public static String getDecimal(String num) {
        // 判断是否包含小数点
        if (num.indexOf(".") == -1) {
            return "";
        }
        num = num.substring(num.indexOf(".") + 1);
        // 创建一个StringBuffer对象
        StringBuffer temp = new StringBuffer();
        // 加入单位
        for (int i = 0; i < num.length(); i++) {
            temp.append(STR_NUMBER[num.charAt(i) - 48]);
            temp.append(STR_UNIT2[i]);
        }
        num = temp.toString(); // 替换字符串的字符
        num = numReplace(num, "零角", "零"); // 替换字符串的字符
        num = numReplace(num, "零分", "零"); // 替换字符串的字符
        num = numReplace(num, "零厘", "零"); // 替换字符串的字符
        num = numReplace(num, "零零", "零"); // 替换字符串的字符
        // 如果字符串以零结尾将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    /**
     * 替换字符串中内容
     *
     * @param num
     *            字符串
     * @param oldStr
     *            被替换内容
     * @param newStr
     *            新内容
     * @return 替换后的字符串
     */
    public static String numReplace(String num, String oldStr, String newStr) {
        while (true) {
            // 判断字符串中是否包含指定字符
            if (num.indexOf(oldStr) == -1) {
                break;
            }
            // 替换字符串
            num = num.replaceAll(oldStr, newStr);
        }
        // 返回替换后的字符串
        return num;
    }

    /**
     * 金额转换
     *
     * @param d
     *            金额
     * @return 转换成大写的全额
     */
    public static String convert(double d) {
        if (d == 0) return "零元整";
        // 实例化DecimalFormat对象
        DecimalFormat df = new DecimalFormat("#0.###");
        // 格式化double数字
        String strNum = df.format(d);
        // 判断是否包含小数点
        if (strNum.indexOf(".") != -1) {
            String num = strNum.substring(0, strNum.indexOf("."));
            // 整数部分大于12不能转换
            if (num.length() > 12) {
                System.out.println("数字太大，不能完成转换！");
                return "";
            }
        }
        String point = "";// 小数点
        if (strNum.indexOf(".") != -1) {
            point = "元";
        } else {
            point = "元整";
        }
        // 转换结果
        String result = getInteger(strNum) + point + getDecimal(strNum);
        if (result.startsWith("元")) { // 判断是字符串是否已"元"结尾
            result = result.substring(1, result.length()); // 截取字符串
        }
        return result; // 返回新的字符串
    }
}