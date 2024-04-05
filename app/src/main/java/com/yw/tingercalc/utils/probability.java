package com.yw.tingercalc.utils;
//概率论
public class probability {

    int n;/**输入数组的长度(数的个数)*/
    double [] num = new double [n];/**输入的数组（小数形式）*/
    double [] P = new double [n];/**输入的概率数组（小数形式）*/
    double[] num_x = new double [n];
    double[] num_y = new double [n];

    public probability(double[] num){
        this.num = num;
        this.n = num.length;
        double[] P = new double[n];
        for (int i = 0;i<n;i++){
            P[i] = 1.0/n;
        }
        this.P = P;
    }

    public probability(double [] num, double[] P){
        this.num = num;
        this.P = P;
        this.n = num.length;
    }

    public probability(double [] num_x,double [] num_y,double [] P){
        this.num_x = num_x;
        this.num_y = num_y;
        this.P = P;
        this.n = num_x.length;
    }

    /**得到本对象的均值*/
    public double getAverage(){
        return getAverage(this.num, this.P);
    }

    /**得到本对象的方差*/
    public double getVar(){
        return getVar(this.num,this.P);
    }

    /**得到本对象的标准差*/
    public double getStandard(){
        return getStandard(this.num,this.P);
    }

    /**得到本对象的K阶原点矩*/
    public double getVk(int k){
        return getVk(this.num,this.P,k);
    }

    /**得到本对象的K阶中心矩*/
    public double getCk(int k){
        return getCk(this.num,this.P,k);
    }

    /**得到本对象的变异系数*/
    public double getCV(){
        return getCV(this.num,this.P);
    }

    /**得到本对象的偏度系数*/
    public double getBetas(){
        return getBetas(this.num,this.P);
    }

    /**得到本对象的峰度系数*/
    public double getBetak(){
        return getBetak(this.num,this.P);
    }

    /**得到本样本的协方差*/
    public double getCovariance(){
        return getCovariance(this.num_x,this.num_y,this.P);
    }

    /**得到本样本的相关系数*/
    public double getCorr(){
        return getCorr(this.num_x,this.num_y,this.P);
    }

    /**此方法用作求输入数组的均值（期望）*/
    public static double getAverage(double[] num,double[] P){
        int i;
        double sum = 0.0;
        int n = num.length;
        for(i=0;i<n;i++){
            sum += (P[i]*num[i]);
        }
        return sum;
    }

    /**此方法用作求方差*/
    public static double getVar(double[] num,double[] P){
        int i;
        double  average=0.0, var=0.0;
        int n = num.length;
        average=getAverage(num,P);
        for(i=0;i<n;i++){
            var += P[i]*(num[i]-average)*(num[i]-average);
        }

        return var;
    }

    /**此方法求标准差*/
    public static double getStandard(double[] num,double[] P){
        return java.lang.Math.sqrt(getVar(num,P));
    }

    /**此方法求k阶原点矩,k为阶数(k次方)*/
    public static double getVk(double[] num,double[] P,int k){
        int i;
        int n = num.length;
        double[] numk = new double[n];
        double Vk = 0;
        //初始化k次方数组
        for(i=0;i<n;i++){
            numk[i]=P[i]*myPow(num[i],k);
            Vk += numk[i];
        }
        return Vk;
    }

    /**求二项式系数C(n,m) , C(m，n)=C(m，n-1)+C(m-1，n-1)*/
    public static int getBinoCoeff(int n,int m) {
        int[][] arr = new int[n + 1][n + 1];
        boolean yn=true;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (i == j || j == 0) {
                    arr[i][j] = 1;
                } else {
                    arr[i][j] = arr[i - 1][j - 1] + arr[i - 1][j];
                }
                if(i==n&&j==m){
                    yn=false;
                    break;
                }
            }
            if(yn==false){
                break;
            }
        }
        return arr[n][m];
    }

    /**此方法求k阶中心矩*/
    public static double getCk(double[] num,double[] P,int k){
        int i;
        double Ck=0.0;
        for(i=0;i<=k;i++){
            Ck += (getBinoCoeff(k,i)*getVk(num,P,i)*myPow(-getVk(num,P,1),k-i));
        }
        return Ck;
    }

    /**此方法求变异系数*/
    public static double getCV(double[] num,double[] P){
        double Cv=0.0;
        Cv = getStandard(num,P)/getAverage(num,P);
        return Cv;
    }

    /**此方法求偏度系数*/
    public static double getBetas(double[] num,double[] P){
        return getVk(num,P,3)/myPow(getStandard(num,P),3);
    }

    /**此方法求峰度系数*/
    public static double getBetak(double[] num,double[] P){
        return getVk(num,P,4)/myPow(getVk(num,P,2),2)-3;
    }

    /**此方法求样本协方差*/
    public static double getCovariance(double[] num_x, double[] num_y ,double[] P){
        int n = num_x.length;
        double [] x_add_y=new double[n];
        double Covariance=0.0, x_Var, y_Var, x_add_y_Var;
        int i;
        for(i=0;i<n;i++){
            x_add_y[i] = num_x[i]+num_y[i];
        }
        x_Var = getVar(num_x,P);
        y_Var = getVar(num_y,P);
        x_add_y_Var = getVar(x_add_y,P);
        Covariance = 0.5 * (x_add_y_Var - x_Var - y_Var);
        return Covariance;
    }

    /**此方法求样本相关系数*/
    public static double getCorr(double[] num_x, double[] num_y,double[] P){
        double Corr=0.0;
        Corr = getCovariance(num_x,num_y,P)/(getStandard(num_x,P)*getStandard(num_y,P));
        return Corr;
    }

    /**求x的n次方,复杂度降到O(logn)*/
    public static double myPow(double x, int n) {
        if(n==0)  return 1;
        if(n==1)  return x;
        if(n==-1) return 1/x;
        double half=myPow(x,n/2);
        double rest=myPow(x,n%2);
        return half*half*rest;
    }
}