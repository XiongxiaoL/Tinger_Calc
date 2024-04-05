package com.yw.tingercalc.utils;
public class NASA {

    int length;
    double[] sample = new double[length];

    public NASA(double[] sample) {
        this.length = sample.length;
        this.sample = sample;
    }

    public NASA() {}

    /**得到本对象的平均数*/
    public double average() { return average(this.sample); }

    /**得到本对象的方差*/
    public double getVar(){ return getVar(this.sample); }

    /**得到本对象的样本分位数*/
    public double sampleQuantile(double p) { return sampleQuantile(p, this.sample); }

    /**得到本对象的K阶原点矩*/
    public double K_originMoment(int k) { return K_originMoment(this.sample, k); }

    /**得到本对象的K阶中心矩*/
    public double K_centralMoment(int k) { return K_centralMoment(this.sample, k); }

    /**得到本对象的偏度系数*/
    public double skewness() { return skewness(sample); }

    /**得到本对象的峰度系数*/
    public double kurtosis() { return kurtosis(sample); }

    /**得到本对象的中位数*/
    public double median() { return median(sample); }

    /**计算平均数*/
    public static double average(double[] num) {
        int n = num.length;
        double sum = 0, aver;
        int i;
        for (i = 0; i < n; i++) {
            sum = sum + num[i];
        }
        aver = sum / n;
        return aver;
    }

    /**此方法用作求方差*/
    public static double getVar(double[] num){
        int i;
        double  average, var=0.0;
        int n = num.length;
        average=average(num);
        for(i=0;i<n;i++){
            var += (num[i]-average)*(num[i]-average);
        }
        return var/n;
    }

    /**计算K阶原点矩*/
    public static double K_originMoment(double[] num, int k) {
        int n = num.length;
        int i, j;
        double t, h;
        double []num1=new double[n];
        for(i=0;i<n;i++){
            num1[i]=num[i];
        }
        for (i = 0; i < n; i++) {
            t = 1;
            for (j = 0; j < k; j++) {
                t = num1[i] * t;
            }
            num1[i] = t;
        }

        h = NASA.average(num1);
        return h;
    }

    /**计算K阶中心矩*/
    public static double K_centralMoment(double[] num, int k) {
        int n = num.length;
        int i;
        double h, m;
        h = NASA.average(num);
        double []num1=new double[n];
        for(i=0;i<n;i++){
            num1[i]=num[i];
        }
        for (i = 0; i < n; i++) {
            num1[i] = num1[i] - h;
        }
        m = NASA.K_originMoment(num1, k);
        return m;
    }

    /**计算偏度系数*/
    public static double skewness(double[] num) {
        int i;
        double h, t, k;
        double j = 1;
        h = NASA.K_centralMoment(num, 3);
        t = NASA.K_centralMoment(num, 2);
        for (i = 0; i < 3; i++) {
            j = j * t;
        }
        j = Math.sqrt(j);
        k = h / j;
        return k;
    }

    /**计算峰度系数*/
    public static double kurtosis(double[] num) {
        double h, g, t;
        h = NASA.K_centralMoment(num, 4);
        g = NASA.K_centralMoment(num, 2);
        g = g * g;
        t = h / g - 3;
        return t;
    }

    /**计算中位数*/
    public static double median(double[] num) {
        int n = num.length;
        int i, j;
        double max, t;
        for (i = 0; i < n; i++) {
            max = num[i];
            for (j = i + 1; j < n; j++) {
                if (max > num[j]) {
                    max = num[j];
                    num[j] = num[i];
                    num[i] = max;
                }
            }
        }
        if (n % 2 == 0)
            t = (num[n / 2] + num[n / 2 - 1]) / 2;
        else
            t = num[(n - 1) / 2];

        return t;
    }

    /**计算样本分位数*/
    public static double sampleQuantile(double k, double[] num) {
        int n = num.length;
        int i, j, p;
        double max, t, m;
        double []num1=new double[n];
        for(i=0;i<n;i++){
            num1[i]=num[i];
        }
        for (i = 0; i < n; i++) {
            max = num1[i];
            for (j = i + 1; j < n; j++) {
                if (max > num1[j]) {
                    max = num1[j];
                    num1[j] = num1[i];
                    num1[i] = max;
                }
            }
        }

        m = n * k;
        if (m == (int) m)
            t = (num1[(int) (m - 1)] + num1[(int) m]) / 2;
        else {
            p = (int) m + 1;
            t = num1[p - 1];
        }
        return t;
    }

    /**单因子方差分析，data中存储数据
     * 本函数用于返回两个二维数组，第一个数组和第二个数组分别对应描述统计表和ANOVA表，
     * 使用本函数之前，必须先检查data数组是不是一个长度宽度均为0的数组，如果是，不允许调用本函数而是跳出弹窗，弹窗内容为：输入错误！*/
    public static String[][][] ANOVA(double[][] data) {
        int i, j, n, ft, fa, fe;
        int r = data.length;
        int[] m = new int[r];
        double sum_of_all_data = 0.0, average_of_all_data = 0.0, ST = 0.0, SA = 0.0, SE = 0.0, F, MSA, MSE, sum_of_squares = 0.0, something_strange = 0.0;
        //本行代码的最后一个变量的意义是对各个水平的数据求和后除以该水平的数据个数的和，实在想不到该给它起什么名字了就随便打一个，其他变量名的意义参见注释
        double [] summation = new double[r];
        double [] average = new double[r];
        double [] Sum_of_squares = new double[r];

        for (i = 0;i<r;i++){
            m[i] = data[i].length;
        }
        //以下为计算部分
        //第一步，先计算每个水平下的和以及平均数，分别存放在summation和average中
        for (i = 0; i < r; i++) {
            for (j = 0; j < m[i]; j++) {
                summation[i] += data[i][j];
            }
            average[i] = summation[i] / m[i];
        }
        //第二步，计算所有数据的总和以及它们的平均值，分别存入sum_of_all_data和average_of_all_data中
        //为了尽可能的减小计算误差，在此还是对原始数据进行处理，不使用已经计算好的summation以及average里面的数据
        n = 0;
        for (i = 0; i < r; i++) {
            n += m[i];
        }//n用来记录数据总个数
        for (i = 0; i < r; i++) {
            for (j = 0; j < m[i]; j++) {
                sum_of_all_data += data[i][j];
            }
        }
        average_of_all_data = sum_of_all_data / n;
        //第三步，依次计算总偏差平方和，组间偏差平方和（因子A的偏差平方和），组内偏差平方和，分别存入ST，SA，SE中，以及它们的自由度，分别存入ft，fa，fe中
        //同第二步，仍使用原始数据,按定义式计算
        for (i = 0; i < r; i++) {
            for (j = 0; j < m[i]; j++) {
                ST += (data[i][j] - average_of_all_data)*(data[i][j] - average_of_all_data);
                SE += (data[i][j] - average[i])*(data[i][j] - average[i]);
                Sum_of_squares[i] += data[i][j] * data[i][j];//这个数组存储的是每一种水平的数据的平方的和
            }
            SA += m[i] * (average[i] - average_of_all_data) * (average[i] - average_of_all_data);
            sum_of_squares += Sum_of_squares[i];//这里计算的是各种水平的数据的平方的和
            something_strange += summation[i] * summation[i] / (double)m[i];//此变量的意义是对各个水平的数据求和后除以该水平的数据个数的和
        }
        ft = n - 1;
        fa = r - 1;
        fe = n - r;
        //第四步，计算均方MSA=SA/fa，MSE=SE/fe，并计算出统计量F的值
        MSA = SA / (double) fa;
        MSE = SE / (double) fe;
        F = MSA / MSE;
        //至此，计算完成
        //以下为输出部分
        String[][][] D = new String[2][][];
        String[][] d1 = new String[r+1][];
        for (i = 0;i<r;i++){
            d1[i] = new String[6];
            d1[i][0] = "A"+(i+1);
            d1[i][1] = MathCal.abandonZero(m[i]);
            d1[i][2] = MathCal.abandonZero(summation[i]);
            d1[i][3] = MathCal.abandonZero(summation[i] * summation[i] / (double) m[i]);
            d1[i][4] = MathCal.abandonZero(average[i]);
            d1[i][5] = MathCal.abandonZero(Sum_of_squares[i]);
        }
        d1[r] = new String[5];
        d1[r][0] = "sum";//这个地方不是数据！
        d1[r][1] = MathCal.abandonZero(n);
        d1[r][2] = MathCal.abandonZero(sum_of_all_data);
        d1[r][3] = MathCal.abandonZero(something_strange);
        d1[r][4] = MathCal.abandonZero(sum_of_squares);
        //printf("\n水平\t mi(个数)\t     Ti(和)\t\t Ti⒉/mi \t\t 均值 \t \t∑yij⒉\n");
        //for (i = 0; i < r; i++) {
        //    printf(" A%d\t   %d\t\t %f\t\t %f\t\t %f   %f\t\n", i + 1, m[i], summation[i], summation[i] * summation[i] / (float)m[i], average[i], Sum_of_squares[i]);
        //}
        //printf(" 和\t n=%d \t    T=%f\t ∑Ti⒉/mi=%f\t \t ∑∑yij⒉=%f\n\n", n, sum_of_all_data, something_strange, sum_of_squares);
        String[][] d2 = new String[3][];
        for (i = 0;i<3;i++){
            d2[i] = new String[5-i];
        }
        d2[0][0] = "因子A";
        d2[1][0] = "误差e";
        d2[2][0] = "总和T";
        d2[0][1] = MathCal.abandonZero(SA);d2[0][2] = MathCal.abandonZero(fa);d2[0][3] = MathCal.abandonZero(MSA);d2[0][4] = MathCal.abandonZero(F);
        d2[1][1] = MathCal.abandonZero(SE);d2[1][2] = MathCal.abandonZero(fe);d2[1][3] = MathCal.abandonZero(MSE);
        d2[2][1] = MathCal.abandonZero(ST);d2[2][2] = MathCal.abandonZero(ft);

        D[0] = d1;
        D[1] = d2;
        //printf("\n来  源\t 平方和\t     自由度\t   均方\t          F比\n");
        //printf("因子A   %f\t%d\t%f\t%f \n", SA, fa, MSA, F);
        //printf("误差e   %f\t%d\t%f  \n", SE, fe, MSE);
        //printf("总和T   %f\t%d \n", ST, ft);
        return D;
    }


    /**将整数，分数，小数的字符串转化为小数，
     * 注意：仅能识别形 5/2  -5/2 +5/2这样的分数，
     * 形如-5/-2，(-5)/(-2) 5/-2  5/(-2)这样的式子不在考虑之列
     * 另外，不允许字符串中含有除数字，.和+，-，/之外的符号--2021.3.1*/
    public static double StringToDouble(String S){
        //首先，去掉可能存在的+号，空格以及换行符
        S = S.replace(" ","");//去掉空格
        S = S.replace("+","");//去掉加号
        S = S.replace("\n","");//去掉换行
        // 先判断是正的还是负的，程序认为，输入的分数是合理的分数，即形如-5/2 5/2 这样的式子，
        // 形如-5/-2，(-5)/(-2) 5/-2  5/(-2)这样的式子不在考虑之列--2021.3.1
        char[] input = S.toCharArray();
        boolean first = false;
        if (input[0] == '-'){
            first = true;
            S = S.replace("-","");//去掉负号
        }
        int p;
        for (p=0;p<S.length();p++){
            if (S.charAt(p)=='/'){
                break;
            }
            else if (S.charAt(p)=='.'){
                if (first){
                    return -1*Double.parseDouble(S);
                }
                else {
                    return Double.parseDouble(S);
                }
            }
        }//此时，C[p] == '/'（字符串为分数）或者C[p]==字符串最后一个数字（说明此时是整数）
        if (p==S.length()){
            //说明是整数
            long a = Integer.parseInt(S);
            if (first){
                return -1*a;
            }
            else {
                return a;
            }
        }
        else {
            //分数
            //System.out.println(S.substring(0,p));
            long u = Integer.parseInt(S.substring(0,p));
            //System.out.printf("\n分子 = %d\n",u);
            long d = Integer.parseInt(S.substring(p+1,S.length()));
            //System.out.println(S.substring(p+1,n));
            //System.out.printf("\n分母 = %d\n",d);
            if (first){
                return -1*(double)u/(double)d;
            }
            else {
                return (double)u/(double)d;
            }
        }
    }

    /**本函数仅用于单因子方差分析部分的输入，将一个字符串转化为一个double矩阵，
     * 注意了，这个double二维数组可能并不是规则的！这是由于各个水平的数据的数目可能是不同的！
     * 如果字符串中含有非法字符，返回一个长度宽度均为0的数组*/
    public static double[][] StringToDouble_ANOVA(String S){
        //将字符串以中文分号或者英文分号分隔
        String[] s = S.split("[;；\n]");

        String[] S3 = new String[s.length];
        int num = 0;
        for (int i = 0;i<s.length;i++){
            if (s[i].equals(" ")||s[i].equals("")){
                //那么这一行应该被删掉
            }
            else {
                S3[num] = s[i];
                num++;
            }
        }
        String[] S4 = new String[num];
        for (int i = 0;i<num;i++){
            S4[i] = S3[i];
        }
        int n = S4.length;
        double[][] D = new double[n][];
        for (int i = 0;i<n;i++){
            D[i] = MathCal.StringToDouble(S4[i]);
            if (D[i].length==0){
                //说明有一组数据里面出现了非法字符，此时，返回一个长度和宽度均为0的double二维数组
                return new double[][]{};
            }
        }
        return D;
    }


    /**本函数用于作一元线性回归，与MathCla类中的多项式拟合不同的是，这个函数将返回一张计算表，此表中记录了计算过程*/
    public static String[][] unaryLinearRegression(double[] x,double[] y){
        double[][] d = new double[6][3];
        int n = x.length;
        d[0][1] = n;
        for (int i = 0;i<n;i++){
            d[0][0] = d[0][0] + x[i];//sum(x)
            d[0][2] = d[0][2] + y[i];//sum(y)
            d[2][0] = d[2][0] + x[i] * x[i];//sum(x^2)
            d[2][1] = d[2][1] + x[i] * y[i];//sum(xy)
            d[2][2] = d[2][2] + y[i] * y[i];//sum(y^2)
        }
        d[1][0] = d[0][0]/d[0][1];//avg(x)
        d[1][2] = d[0][2]/d[0][1];//avg(y)

        d[3][0] = d[0][1]*d[1][0]*d[1][0];//n*(avg(x))^2
        d[3][1] = d[0][1]*d[1][0]*d[1][2];//n*avg(x)*avg(y)
        d[3][2] = d[0][1]*d[1][2]*d[1][2];//n*(avg(y))^2

        d[4][0] = d[2][0] - d[0][0]*d[0][0]/d[0][1];//lxx
        d[4][1] = d[2][1] - d[0][0]*d[0][2]/d[0][1];//lxy
        d[4][2] = d[2][2] - d[0][2]*d[0][2]/d[0][1];//lyy

        d[5][0] = d[4][1]/d[4][0];//beta1
        d[5][1] = d[1][2] - d[1][0] * d[5][0];
        d[5][2] = 0;
        String[][] S = new String[6][3];
        for (int i = 0;i<6;i++){
            for (int j = 0;j<3;j++){
                S[i][j] = MathCal.abandonZero(d[i][j]);
            }
        }
        S[5][2] = "y = " + S[5][1];
        if (d[5][0]>=0){
            S[5][2] = S[5][2] + " + " + S[5][0];
        }
        else {
            S[5][2] = S[5][2] + " - " + MathCal.abandonZero(-1*d[5][0]);
        }
        S[5][2] = S[5][2] + "x";
        return S;
    }

    /**方差未知情况下的t检验,x为样本数据，avg是样本空间均值*/
    public static String TestT(double[] x,double avg){
        int n = x.length;
        double avgX = NASA.average(x);//样本均值，参数avg是样本空间均值
        double varX = 0;//样本方差
        for(int i=0;i<n;i++){
            varX += (x[i]-avgX)*(x[i]-avgX);
        }
        varX = varX/(n-1);
        varX = Math.sqrt(varX);
        double t = (Math.sqrt(n)*(avgX-avg))/varX;
        return MathCal.abandonZero(t);
    }

    /**方差已知情况下的u检验，x为样本数据，avg是样本空间均值，var是样本空间标准差，返回u*/
    public static String TestU(double[] x,double avg,double var){
        int n = x.length;
        double avgX = NASA.average(x);//样本均值，
        double u = (Math.sqrt(n)*(avgX-avg))/var;
        return MathCal.abandonZero(u);
    }

    /**返回一个长度为3的字符串数组,下标为0是ta(n-1),下标为1是t1-a(n-1) 下标为2是t1-a/2(n-1)*/
    public static String[] criticalRegionT(double a,int n){
        double[][] D = excel();
        double b = 1-a;
        double[] X = {-1,-1,-1};
        for (int i = 0;i<D.length;i++){
            if (D[i][0] == n-1){
                for (int j = 0;j<D[i].length;j++){
                    if (D[0][j] == b){
                        X[0] = 1-D[i][j];
                        X[1] = D[i][j];
                    }
                    if (D[0][j] == 1-a/2){
                        X[2] = D[i][j];
                    }
                }
            }
        }
        return new String[]{MathCal.abandonZero(X[0]),MathCal.abandonZero(X[1]),MathCal.abandonZero(X[2])};
    }

    /**返回一个长度为3的字符串数组,下标为0是ua,下标为1是u1-a 下标为2是u1-a/2*/
    public static String[] criticalRegionU(double a){
        double[][] D = {
                {1.645,1.645,1.96},
                {2.33,2.33,2.575},
                {1.283,1.283,1.645}
        };//第一行 a = 0.05
        //第二行   a = 0.01
        //第三行   a = 0.1
        double[] X = {-1,-1,-1};
        if (a == 0.05){
            return new String[]{MathCal.abandonZero(D[0][0]),MathCal.abandonZero(D[0][1]),MathCal.abandonZero(D[0][2])};
        }
        else if (a == 0.01){
            return new String[]{MathCal.abandonZero(D[1][0]),MathCal.abandonZero(D[1][1]),MathCal.abandonZero(D[1][2])};
        }
        else if (a == 0.1){
            return new String[]{MathCal.abandonZero(D[2][0]),MathCal.abandonZero(D[2][1]),MathCal.abandonZero(D[2][2])};
        }
        else {
            return new String[]{MathCal.abandonZero(X[0]), MathCal.abandonZero(X[1]), MathCal.abandonZero(X[2])};
        }
    }

    public static double[][] excel(){
        int i,j;
        int n=21;
        double [][] temp=new double [n][4];
        for(i=0;i<n;i++){
            temp[i][0]=i;
            switch (i){
                case 0 : temp[i][1]=0.90;temp[i][2]=0.95;temp[i][3]=0.975; break;
                case 1 : temp[i][1]=3.0777;temp[i][2]=6.3138;temp[i][3]=12.7062; break;
                case 2 : temp[i][1]=1.8856;temp[i][2]=2.9200;temp[i][3]=4.3027; break;
                case 3 : temp[i][1]=1.6377;temp[i][2]=2.3534;temp[i][3]=3.1824; break;
                case 4 : temp[i][1]=1.5332;temp[i][2]=2.1318;temp[i][3]=2.7764; break;
                case 5 : temp[i][1]=1.4579;temp[i][2]=2.0150;temp[i][3]=2.5706; break;
                case 6 : temp[i][1]=1.4398;temp[i][2]=1.9432;temp[i][3]=2.4469; break;
                case 7 : temp[i][1]=1.4149;temp[i][2]=1.8946;temp[i][3]=2.3646; break;
                case 8 : temp[i][1]=1.3968;temp[i][2]=1.8595;temp[i][3]=2.3060; break;
                case 9 : temp[i][1]=1.3830;temp[i][2]=1.8331;temp[i][3]=2.2622; break;
                case 10 :temp[i][1]=1.3722;temp[i][2]=1.8125;temp[i][3]=2.2281; break;
                case 11: temp[i][1]=1.3634;temp[i][2]=1.7959;temp[i][3]=2.2010; break;
                case 12 : temp[i][1]=1.3562;temp[i][2]=1.7823;temp[i][3]=2.1788; break;
                case 13 : temp[i][1]=1.3502;temp[i][2]=1.7709;temp[i][3]=2.1604; break;
                case 14 : temp[i][1]=1.3450;temp[i][2]=0.95;temp[i][3]=2.1448; break;
                case 15 : temp[i][1]=1.3406;temp[i][2]=1.7531;temp[i][3]=2.1314; break;
                case 16 : temp[i][1]=1.3368;temp[i][2]=1.7459;temp[i][3]=2.1199; break;
                case 17 : temp[i][1]=1.3334;temp[i][2]=1.7396;temp[i][3]=2.1098; break;
                case 18 : temp[i][1]=1.3304;temp[i][2]=1.7341;temp[i][3]=2.1009; break;
                case 19 : temp[i][1]=1.3277;temp[i][2]=1.7291;temp[i][3]=2.0930; break;
                case 20 : temp[i][1]=1.3253;temp[i][2]=1.7247;temp[i][3]=2.0860; break;

            }
        }
        return temp;
    }
}