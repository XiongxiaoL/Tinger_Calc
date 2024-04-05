package com.yw.tingercalc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathCal {

    /**k为输入数字的进制数，string为输入的数据构成的字符串，j为要进行转换的进制*/
    public static String binaryConversion(int k, String str, int j) { return Integer.toString(Integer.parseInt(str, k), j); }

    /**多项式插值函数，x，y为数据点，x0为待求点，返回一个字符串数组，第一行是插值点的函数值的字符串形式
     * 第二行是多项式的字符串表达形式*/
    public static String[] polynomialVal(double[] x, double[] y, double x0) {
        double[] p = polynomial(x, y);
        int n = p.length;
        double[] p1 = new double[n];
        for (int i= 0;i<n;i++){
            p1[i] = format(p[n-1-i]);
        }//改成升幂排列，并保留四位数字，然后传入函数ATomToString中
        String[] S = new String[2];
        //S[0] = "在"+x0+"上的插值结果为:"+abandonZero(polyVal(polynomial(x, y), x0));
        //S[1] = "多项式为："+ATomToString(p1);
        S[0] = abandonZero(polyVal(polynomial(x, y), x0));
        S[1] = ATomToString(p1);
        return S;
    }

    /**拉格朗日插值函数,x,y,为数据点，x0为待插值点，
     * 返回的String数组: 第一个（下标为0）是元素插值点的函数值的字符串形式
     *                 第二个（下标为1）元素是化简前的多项式
     *                 第三个（下标为2）元素是化简后的多项式*/
    public static String[] LagrangeInterpolationPolynomial(double[] x, double[] y, double x0){
        return lagToString(LagrangePolynomial(x,y,x0));
    }

    /**拉格朗日插值函数，x，y为数据点，x0为待求点
     * 返回的数组中，第一行第一个元素为插值点的函数值
     * 第二行是系数向量，额，这个系数是用来显示化简前的多项式的，
     * 第三行是数据点x,作用同第二行的系数向量
     * 第四行是数据点y,作用同第二行的系数向量，
     * 因为要显示化简后的多项式，需要用到多项式插值函数来获取，这样方便，直接用拉格朗日插值不太好求*/
    private static double[][] LagrangePolynomial(double[] x, double[] y, double x0) {
        //如果给了四个点，求出的是三次多项式
        int n = x.length;
        int i, j;
        double[] temp = new double[n];
        double[][] p = new double[4][n];
        for (i = 0; i < n; i++) {
            temp[i] = y[i];
            p[0][i] = 1;
            for (j = 0; j < n; j++) {
                if (j != i) {
                    temp[i] = (temp[i] * (x0 - x[j])) / (x[i] - x[j]);
                    p[0][i] = p[0][i]*(x[i] - x[j]);
                }
            }
            p[1][i] = y[i]/p[0][i];
        }
        double sum = 0;
        for (i = 0; i < n; i++) {
            sum = sum + temp[i];
            p[1][i] = format(p[1][i]);
        }
        p[0][0] = sum;
        p[2] = x;
        p[3] = y;
        return p;
    }

    /**将拉格朗日插值函数返回的数组转化成字符串
     * 参数p的意义等同于函数LagrangePolynomial的返回值
     * 返回的String数组: 第一个是元素插值点的函数值的字符串形式
     *                 第二个元素是化简前的多项式
     *                 第三个元素是化简后的多项式*/
    private static String[] lagToString(double[][] p){
        String[] S = new String[3];
        int n = p[1].length;
        //S[0] = "在插值点上的插值结果为："+MathCal.abandonZero(p[0][0]);
        //S[1] = "插值得到的多项式：f(x) = ";
        S[0] = MathCal.abandonZero(p[0][0]);
        S[1] = "";
        //必须先找到系数不为0的系数，打印首项
        int first = 0;//记录首项的下标
        for (int i = 0;i<n;i++){
            if (p[1][i]!=0){
                first = i;
                break;
            }
        }//找到了第一个不为0的系数，那么以first为下标的便是首项
        if (p[1][first] != 1) {
            //1最好不要打印出来
            S[1] = S[1] + p[1][first];
            if (p[1][first]==-1){
                //-1只需要打印出-号就够了
                S[1] = S[1] + "-";
            }
        }
        for (int j = 0; j < n; j++) {
            if (j != first) {
                if (p[2][j] != 0) {
                    S[1] = S[1] + "(x";
                    if (p[2][j] > 0) {
                        S[1] = S[1] + "-" + p[2][j] + ")";
                    } else if (p[2][j] < 0) {
                        S[1] = S[1] + "+" + -1 * p[2][j] + ")";
                    }
                }
                else {
                    //p[2][j] == 0
                    S[1] = S[1] + "x";
                }
            }
        }
        for (int i = first + 1; i < n; i++) {
            if (p[1][i]!=0) {
                if (p[1][i] > 0) {
                    if (p[1][i] != 1) {
                        S[1] = S[1] + "+" + p[1][i];
                    } else {
                        S[1] = S[1] + "+";
                    }
                } else if (p[1][i] < 0) {
                    if (p[1][i] != -1) {
                        S[1] = S[1] + "-" + -1 * p[1][i];
                    } else {
                        S[1] = S[1] + "-";
                    }
                }
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        if (p[2][j] != 0) {
                            S[1] = S[1] + "(x";
                            if (p[2][j] > 0) {
                                S[1] = S[1] + "-" + p[2][j] + ")";
                            } else if (p[2][j] < 0) {
                                S[1] = S[1] + "+" + -1 * p[2][j] + ")";
                            }
                        } else {
                            //p[2][j] == 0
                            S[1] = S[1] + "x";
                        }
                    }
                }
            }
        }
        //S[2] = "化简后的"+polynomialVal(p[2], p[3], 1)[1];
        S[2] = polynomialVal(p[2], p[3], 1)[1];
        return S;
    }

    /**牛顿插值函数，输入数据点x，y，待插值的点x0后，还需输入插值次数p，注意：插值次数不可以超过所输入的数据点的个数*/
    public static String[][] newton(double[] x, double[] y, double x0,int p) {
        int i, j;
        double[] f = new double[p + 1];
        double[][] F = new double[x.length + 1][p + 1];
        String[][] S = new String[x.length + 2][];
        double t, z;
        for (i = p; i > 0; i--) {
            f[i] = (y[i] - y[i - 1]) / (x[i] - x[i - 1]);
            F[i][0] = f[i];
        }
        for (i = 1; i < p; i++) {
            for (j = p; j > i; j--) {
                f[j] = (f[j] - f[j - 1]) / (x[j] - x[j - i - 1]);
                F[j][i] = f[j];
            }
        }
        z = y[0];
        for (i = 1; i <= p; i++) {
            t = 1.0;
            for (j = 0; j < i; j++) {
                t = t * (x0 - x[j]);
            }
            z = z + f[i] * t;
        }//z是插值点x0对应的函数值
        //对最后一行做差商
        //先计算1阶差商
        F[x.length][0] = (z - y[y.length - 1]) / (x0 - x[x.length - 1]);
        for (j = 1; j < p + 1; j++) {
            F[x.length][j] = (F[x.length][j - 1] - F[x.length - 1][j - 1]) / (x0 - x[x.length - j - 1]);
        }
        S[0] = new String[p + 3];
        S[0][0] = "x";
        S[0][1] = "y";
        for (j = 1; j <= p + 1; j++) {
            S[0][j + 1] = j + "阶差商";
        }
        //用户自己输入的x和y不做处理，直接转String，但是其他的，额，还是保留四位小数然后去掉多余的0吧
        for (i = 1; i < x.length + 1; i++) {
            S[i] = new String[1 + i];
            S[i][0] = String.valueOf(x[i - 1]);
            S[i][1] = String.valueOf(y[i - 1]);
        }
        S[i] = new String[i + 1];
        S[i][0] = String.valueOf(x0);
        S[i][1] = abandonZero(z);
        for (i = 2; i < x.length + 2; i++) {
            for (j = 2; j < 1 + i&&j<p+3; j++) {
                System.out.println("i = "+i + "   j = "+j);
                S[i][j] = abandonZero(F[i - 1][j - 2]);
            }
        }
        return S;
    }

    /**高斯消去函数，m是拟合的次数，a * X = b，相当于输入的是一个增广矩阵，返回x*/
    public static double[] gauss(int m, double[][] a, double[] b) {
        int n = a.length;//实际上，m+1 = n；
        double[][] l = new double[m + 1][m + 1];
        double[] x = new double[m + 1];
        double sum;
        int i, j, k;
        for (k = 0; k <= m - 1; k++) {
            for (i = k + 1; i <= m; i++) {
                l[i][k] = a[i][k] / a[k][k];
                b[i] = b[i] - l[i][k] * b[k];
                for (j = k + 1; j <= m; j++) {
                    a[i][j] = a[i][j] - l[i][k] * a[k][j];
                }
            }
        }
        x[m] = b[m] / a[m][m];
        for (k = m - 1; k >= 0; k--) {
            sum = 0;
            for (j = k + 1; j <= m; j++) {
                sum = sum + a[k][j] * x[j];
            }
            x[k] = (b[k] - sum) / a[k][k];
        }
        return x;
    }

    /**多项式拟合函数，x，y是数据点，w是权重，m是拟合次数*/
    public static String polyfit(double[] x,double[] y,double[] w,int m){ return ATomToString(polyFit(x,y,w,m)); }

    /**多项式拟合函数，x，y是数据点，m是拟合次数*/
    public static String polyfit(double[] x,double[] y,int m){ return ATomToString(polyFit(x,y,m)); }

    /**指数拟合函数，返回一个指数函数的字符串表达形式*/
    public static String expFit(double[] x,double[] y,double[] w){
        //首先，对数据点y求个In
        int n = x.length;
        for (int i = 0;i<n;i++){
            y[i] = Math.log(y[i]);
        }
        //对InY和x进行一次拟合
        double[] p = polyFit(x,y,w,1);
        //p[0] = a'  p[1] = b';
        double[] p1 = {
                Math.exp(p[0]),Math.exp(p[1])
        };
        double a = format(p[0]);
        double b = format(p[1]);
        if (a==1){
            if (b == 1){
                return "y = 1";
            }
            else if (b == -1){
                return "y = (-1)^x";
            }
            else if (b < 0){
                return "y = (" + b + ")^x";
            }
            else {
                return "y = " + b + "^x";
            }
        }
        else if (a == -1){
            if (b == 1){
                return "y = -1";
            }
            else if (b == -1){
                return "y = -1*(-1)^x";
            }
            else if (b < 0){
                return "y = -(" + b + ")^x";
            }
            else {
                return "y = " + b + "^x";
            }
        }
        else {
            if (b==1){
                return "y = " + a;
            }
            else if (b<0){
                return "y = " + a + "*(" + b + ")^x";
            }
            else {
                return "y = " + a + "*" + b + "^" + "x";
            }
        }
    }

    /**指数拟合函数，返回一个指数函数的字符串表达形式*/
    public static String expFit(double[] x,double[] y){
        //首先，对数据点y求个In
        int n = x.length;
        double[] w = new double[n];
        for (int i = 0;i<n;i++){
            y[i] = Math.log(y[i]);
            w[i] = 1;
        }
        //对InY和x进行一次拟合
        double[] p = polyFit(x,y,w,1);
        //p[0] = a'  p[1] = b';
        double[] p1 = {
                Math.exp(p[0]),Math.exp(p[1])
        };
        double a = format(p[0]);
        double b = format(p[1]);
        if (a==1){
            if (b == 1){
                return "y = 1";
            }
            else if (b == -1){
                return "y = (-1)^x";
            }
            else if (b < 0){
                return "y = (" + b + ")^x";
            }
            else {
                return "y = " + b + "^x";
            }
        }
        else if (a == -1){
            if (b == 1){
                return "y = -1";
            }
            else if (b == -1){
                return "y = -1*(-1)^x";
            }
            else if (b < 0){
                return "y = -(" + b + ")^x";
            }
            else {
                return "y = " + b + "^x";
            }
        }
        else {
            if (b==1){
                return "y = " + a;
            }
            else if (b<0){
                return "y = " + a + "*(" + b + ")^x";
            }
            else {
                return "y = " + a + "*" + b + "^" + "x";
            }
        }
    }

    /**多项式拟合函数，x，y是数据点，w是权重，m是拟合次数,返回所求得的多项式系数向量（升幂排列）*/
    private static double[] polyFit(double[] x,double[] y,double[] w,int m){
        int n = x.length;
        int i,j,k;
        double[] X = new double[2*m+1];
        X[0] = n;
        double T = 0;
        double[][] A = new double[m+1][m+1];
        double[] b = new double[m+1];
        double[] YX = new double[n];
        //开始计算A和b
        for (i = 0; i < n; i++) {
            T = w[i] * x[i]; X[1] += T;
            for (k = 2; k <= 2 * m; k++) {
                T *= x[i]; X[k] += T;
            }
        }
        for (i = 0; i < n; i++) {
            T = w[i] * y[i]; YX[0] += T;
            for (k = 1; k <= m; k++) {
                T *= x[i]; YX[k] += T;
            }
        }
        for (i = 0; i < m + 1; i++) {
            for (j = 0; j < m + 1; j++) {
                A[i][j] = X[i + j];
            }
            b[i] = YX[i];
        }
        //A和b计算完成
        /*for (i = 0; i < 2 * m; i++) {
            printf("X的%d次方的和为（带权值）：%lf\n", i, X[i]);
        }
        for (i = 0; i < m + 1; i++) {
            printf("Y乘X的%d次方的和为（带权值）%lf\n", i, YX[i]);
        }
        printf("此问题的正则方程组的增广矩阵为：\n");
        for (i = 0; i < m + 1; i++) {
            for (j = 0; j < m + 1; j++) {
                printf("%lf\t", A[i][j]);
            }
            printf("%lf", b[i]);
            printf("\n");
        }
        printf("\n");*/
        return gauss(m, A, b);
    }

    /**多项式拟合函数，x，y是数据点，权重默认为全1，m是拟合次数,返回所求得的多项式系数向量（升幂排列）*/
    private static double[] polyFit(double[] x,double[] y,int m){
        double [] w = new double[x.length];
        for (int i = 0;i<x.length;i++){
            w[i] = 1;
        }
        return polyFit(x,y,w,m);
    }

    /**单纯形表的一次迭代新版
     * into 是入基变量，out 是出基变量，matrix是迭代前的单纯形表（注意：迭代前，最后一行的检验数不一定正确，但是这一行必须得有）
     * 函数不会修改参数 matrix*/
    public static UD[][] doAnIterationOfSimplexMethod(int into, int out, UD[][] matrix){
        int line = matrix.length;
        int column = matrix[0].length;
        int i, j, number_of_unKnows = column - 3, number_of_constraints = line - 1;
        UD [] checking = new UD[column];
        UD [][] matrixA = new UD[line][column];
        for (j = 0; j < number_of_unKnows; j++) {
            checking[j] = new UD(1);
        }
        into = into + 2;
        for (i = 0; i < line; i++) {
            for (j = 0; j < column; j++) {
                matrixA[i][j] = UD.copy(matrix[i][j]);
            }
        }
        //开始迭代
        for (i = 1; i < line; i++) {
            if (matrixA[i][1].u == out) break;
        }
        out = i;
        if ((matrixA[out][into].u != 1) || (matrixA[out][into].d != 1)) {
            for (j = 2; j < column; j++) {
                if (into != j){
                    //这里要表达的是将出基变量那一行除以被选中的那一个元素
                    matrixA[out][j] = UD.division(matrixA[out][j],matrixA[out][into]);
                }
            }
            //matrixA_up[out][1] = into + 2;////就是因为把这一步放在了这个if里面 导致了计算得到的表没有置换那个第一和第二列
        }
        matrixA[out][into] = new UD(1);
        matrixA[out][0] = UD.copy(matrix[0][into]);
        matrixA[out][1].u = into - 2;///////////////
        for (i = 1; i < line - 1; i++) {
            if (out == i);
            else if (matrixA[i][into].u == 0);
            else {
                for (j = 2; j < column; j++) {
                    if (into != j){
                        //这里要表达的是matrixA[i][j]=matrixA[i][j]-matrixA[i][into]
                        UD temp = UD.multiply(matrixA[out][j],matrixA[i][into]);
                        matrixA[i][j]=UD.less(matrixA[i][j],temp);
                    }
                }
                matrixA[i][into] = new UD(0);
            }
        }
        //迭代完成

        //下面开始计算检验数
        for (j = 3; j < column; j++) {
            //checking[j] = UD.multiply(matrixA[1][0], matrixA[1][j]);
            checking[j] = new UD(0);
            for (i = 1; i < line - 1; i++) {
                UD temp = UD.multiply(matrixA[i][0],matrixA[i][j]);
                checking[j] = UD.add(checking[j],temp);
            }
            checking[j] = UD.less(matrixA[0][j],checking[j]);
        }
        //检验数计算完成
        //将检验数放到矩阵的最后一行
        for(j = 0;j<column;j++){
            matrixA[line-1][j] = checking[j];
        }
        return matrixA;
    }

    /**本函数仅用于测试单纯形表的一次迭代函数的计算结果，用于打印单纯形表*/
    public static void printS(UD[][] A){
        int i,j;
        int line = A.length;
        int column = A[0].length;
        //开始打印
        //printf("\n迭代后的矩阵为：\n");
        System.out.print("\tCj ->\t");
        for (j = 3; j < column; j++) {
            A[0][j].print();
            System.out.print("\t");
        }
        System.out.print("\n");
        System.out.print("CB\tXB\tb\t");
        for (i = 0; i < column - 3; i++) {
            System.out.printf("X%d\t", i + 1);
        }
        System.out.print("\n");
        for (i = 0; i < column; i++) {
            System.out.print("-------");
        }
        System.out.print("----");
        System.out.print("\n");
        for (i = 1; i < line - 1; i++) {
            for (j = 0; j < column; j++) {
                if (j == 1) {
                    System.out.print("X");
                    A[i][1].print();
                    //printf("X%d\t", matrixA_up[i][1]);
                }
                else {
                    A[i][j].print();
                }
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        for (i = 0; i < column; i++) {
            System.out.print("-------");
        }
        System.out.print("----");
        System.out.print("\n");
        System.out.print("   \tCj-Zj\t");
        for (j = 3; j < column; j++) {
            A[line-1][j].print();
            System.out.print("\t");
        }
        //打印完成
    }

    /**本函数用于计算单纯形表的检验数*/
    public static UD[][] check(UD[][] matrix){
        int line = matrix.length;
        int column = matrix[0].length;
        int i, j, number_of_unKnows = column - 3, number_of_constraints = line - 1;
        UD [] checking = new UD[column];
        UD [][] matrixA = new UD[line][column];
        for (j = 0; j < number_of_unKnows; j++) {
            checking[j] = new UD(1);
        }
        for (i = 0; i < line; i++) {
            for (j = 0; j < column; j++) {
                matrixA[i][j] = UD.copy(matrix[i][j]);
            }
        }
        //下面开始计算检验数
        for (j = 3; j < column; j++) {
            //checking[j] = UD.multiply(matrixA[1][0], matrixA[1][j]);
            checking[j] = new UD(0);
            for (i = 1; i < line - 1; i++) {
                UD temp = UD.multiply(matrixA[i][0],matrixA[i][j]);
                checking[j] = UD.add(checking[j],temp);
            }
            checking[j] = UD.less(matrixA[0][j],checking[j]);
        }
        //检验数计算完成
        //将检验数放到矩阵的最后一行
        for(j = 0;j<column;j++){
            matrixA[line-1][j] = checking[j];
        }
        return matrixA;
    }

    /**本函数用于将多项式转化为字符串，输入的P是升幂排列的*/
    public static String ATomToString(double[] p){
        int n = p.length;//升幂排列，那么最高次应该是p[n-1]
        if (n == 1){
            return "y = " + abandonZero(p[0]);
        }
        else {
            //先找到第一个不为0的首项放进去
            for (int i = n-1;i>=0;i--){
                if (p[i]!=0){
                    n = i;
                    break;
                }
            }
            //把首项放到字符串里面,显然，这里的n仍然可能等于1
            String fx = "y = ";
            if(n == 1){
                if (p[n]==1) {
                    fx = fx + "x";
                }
                else if(p[n]==-1){
                    fx = fx + "-x";
                }
                else {
                    fx = fx + abandonZero(p[n]) + "x";
                }
                if (p[0]>0){
                    fx = fx + " + " + abandonZero(p[0]);
                    return fx;
                }
                else if (p[0] == 0){
                    return fx;
                }
                else {
                    fx = fx + " - " + abandonZero(-1*p[0]);
                    return fx;
                }
            }
            else {
                //String fx = "y = " + p[n] + "x" + "^" + n;
                if (p[n] == 1){
                    fx = fx +  "x" + "^" + n;
                }
                else if (p[n] == -1){
                    fx = fx +  "-x" + "^" + n;
                }
                else {
                    //p[n]不可能为0
                    fx = fx + abandonZero(p[n]) + "x" + "^" + n;
                }
                for (int i = n - 1; i >= 0; i--) {
                    if (p[i] != 0) {
                        if (i == 1) {
                            if (p[i]>0) {
                                if (p[i]!=1){
                                    fx = fx + " + " + abandonZero(p[i]) + "x";
                                }
                                else {
                                    fx = fx + " + " + "x";
                                }
                            }
                            else {
                                if (p[i]!=-1){
                                    fx = fx + " - " + abandonZero(-1*p[i]) + "x";
                                }
                                else {
                                    fx = fx + " - " + "x";
                                }
                            }
                        } else if (i == 0) {
                            if (p[i]>0){
                                fx = fx + " + " + abandonZero(p[i]);
                            }
                            else {
                                fx = fx + " - " + abandonZero(-1*p[i]);
                            }
                        } else {
                            if (p[i]>0){
                                if (p[i]!=1){
                                    fx = fx + " + " + abandonZero(p[i]) + "x^" + i;
                                }
                                else {
                                    fx = fx + " + " + "x^" + i;
                                }
                            }
                            else {
                                if (p[i]!=-1){
                                    fx = fx + " - " + abandonZero(-1*p[i]) + "x^" + i;
                                }
                                else {
                                    fx = fx + " - " + "x^" + i;
                                }
                            }
                        }
                    }
                }
                return fx;
            }
        }
    }

    /**本函数用于保留四位小数，返回的是字符串*/
    public static String format1(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**本函数用于保留四位小数，返回的是double*/
    public static double format(double value) {
        double D = value*10000;
        if (D-(long)D>=0.5){
            return (double)(long)(D+1)/10000;
        }
        else {
            return (double)(long)(D)/10000;
        }
    }

    /**本函数用于将读取到的字符串转化为一个小数数组*/
    public static double[] StringToDouble(String S){
        //程序思路是这样的：用两个指针p和q，动态地去截取有数字的字符串部分，然后传入到子函数中转化为double，同时计数器加一
        double[] d = new double[100000];//所返回的double数组的最大长度为100000
        int num = 0;
        int i,j;
        char p,q;
        S = S.trim();//删除掉头部和尾部的空格，真舒服，少写一大堆代码
        int n = S.length();
        //System.out.println("\n删掉首尾的空格后为"+S);
        j = 0;
        for (i = 0;i<n;i++){
            p = S.charAt(i);
            //可以设想到用户可能会这样去输入 0.5     3 ， -2及中间空格会有很多，或者逗号前后还有空格，数字之前空格还可能不止一个
            //总结为下面6种情况
            //两个数字之间就一个空格         1
            //两个数字之间就一个逗号         2
            //空格后面还有空格              3
            //空格后面是逗号                4
            //空格后面是逗号，逗号后面还有空格  5
            //逗号，然后后面还有空格          6
            if (type(p)) continue;
            else if (p==' '||p == ','||p == '，'||p == '\n') {
                if (type(S.charAt(i+1))){
                    //这个地方应该不会出现数组下标越界，因为字符串尾部已经没有空格了
                    //说明是第一种情况
                    d[num] = NASA.StringToDouble(S.substring(j,i));
                    num++;
                    j = i+1;
                    i = i+1;
                }
                else {
                    //后面还有空格，说明是第3,4，5,6种情况
                    int ii;
                    for (ii=i+2;ii<n;ii++){
                        //去找到第一个数字
                        if (type(S.charAt(ii))){
                            break;
                        }
                    }
                    if (ii == n){
                        //不太可能会出现这种情况，出现即是bug
                        //System.out.println("!!!");
                        return new double[]{};
                    }
                    else {
                        d[num] = NASA.StringToDouble(S.substring(j,i));
                        //System.out.println(S.substring(j,i));
                        num++;
                        j = ii;
                        i = ii+1;
                    }
                }
            }
            else {
                //System.out.println("???");
                //出现了非法字符，返回一个长度为0的double数组
                return new double[]{};
            }
        }
        d[num] = NASA.StringToDouble(S.substring(j,n));
        num++;
        double[] D = new double[num];
        for (i = 0;i<num;i++){
            D[i] = d[i];
        }
        return D;
    }

    /**本函数用于将字符数组中的下标i（含下标为i的部分）到j（不含下标为j的部分）部分转为整数*/
    public static long charToNum(char [] num,int i,int j){
        //本方法用于将字符转为数字
        //参数说明，字符存储在数组num中，下标从i到j是要转的部分
        int l = j - i + 1;
        long n = Character.getNumericValue(num[j-1]);
        long Ten = 10;
        for (int s = j - 1;s>=i;s--){
            long temp = Character.getNumericValue(num[s]);
            n = n + temp*Ten;
            Ten = Ten*Ten;
        }
        return n;
    }

    /**StringToDouble函数的子函数，用于判断字符*/
    private static boolean type(char c){ return (c >= '0' && c <= '9') || c == '+' || c == '-'|| c == '/'|| c == '.'; }

    /**本函数用于去掉多余的0,并最多保留四位小数*/
    public static String abandonZero(double d){
        d = format(d);
        String s = String.valueOf(d);
        BigDecimal B = new BigDecimal(s);
        BigDecimal noZeros = B.stripTrailingZeros();
        return noZeros.toPlainString();
    }

    /**本函数用于多项式求值，p为多项式系数（降幂排列），x0为待求值的点*/
    private static double polyVal(double[] p, double x0) {
        int n = p.length;//获取p的长度为n，说明是一个n-1次多项式
        //多项式的计算实际上是非常简单的，可以采用秦九韶方法，当为单值时计算的最快，复杂度为n2
        //sum=(sum+a[i])*x
        double sum = 0;
        for (int j = 0; j < n - 1; j++) {
            sum = (sum + p[j]) * x0;
        }
        return sum + p[n - 1];
    }

    /**多项式插值函数的子函数，输入数据点x，y之后，返回一个多项式系数向量（降幂排列）*/
    private static double[] polynomial(double[] x, double[] y) {
        //上述变量中x数组储存的是输入的n+1个点的x值，y则同理,n+1则代表的是输入的点的个数
        int n = x.length;
        //进阶版本可以考虑采用二维数组
        double[] temp = new double[n];
        double[][] tem = new double[n][n];//储存范德蒙德行列式
        double[][] tem1 = new double[n][n];//tem1用来储存替换后的矩阵，计算方法采用的是克拉默法则
        int i, j;
        int k;
        //构建范德蒙行列式（每行从左到右的阶数依次是n阶到0阶递减）
        for (i = 0; i < n; i++) {
            for (j = n - 1; j >= 0; j--) {
                if (j == n - 1) {
                    tem[i][j] = 1;
                } else {
                    tem[i][j] = tem[i][j + 1] * x[i];
                }
            }
        }//构建完毕
        Matrix M = new Matrix(tem);
        double a = M.det().matrix1[0][0];
        for (k = 0; k < n; k++) {
            for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++) {
                    if (j == k) {
                        tem1[i][j] = y[i];
                    } else {
                        tem1[i][j] = tem[i][j];
                    }
                }
            }
            Matrix N = new Matrix(tem1);
            temp[k] = N.det().matrix1[0][0];
        }
        for (i = 0; i < n; i++) {
            temp[i] = temp[i] / a;
        }
        return temp;
    }
}