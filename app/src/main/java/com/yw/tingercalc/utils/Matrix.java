package com.yw.tingercalc.utils;

public class Matrix {

    int line;
    int column;
    byte BasicType;
    byte SecondType;

    byte ErrorType = 0;

    /* **************************************************************
     * BasicType==0  表示这是一个 异常                                  *
     * BasicType==1  表示这是一个 double兼容float                       *
     * BasicType==2  表示这是一个 UD兼容int long                        *
     * BasicType==3  表示这是一个 ATomUD兼容以上所有（除double和float）    *
     * BasicType==4  表示这是一个 ATomsUD兼容以上所有（除double和float）   *
     * **************************************************************/

    /* *******************************
     * SecondType==0 表示这是一个 单对象 *
     * SecondType==1 表示这是一个 行向量 *
     * SecondType==2 表示这是一个 列向量 *
     * SecondType==3 表示这是一个  矩阵  *
     * *******************************/

    /*
     * 本类中的静态方法，均不应该修改传入的参数,自检方法除外
     */

    double[][] matrix1 = new double[this.line][this.column];

    UD[][] matrix2 = new UD[this.line][this.column];

    ATomUD[][] matrix3 = new ATomUD[this.line][this.column];

    ATomsUD[][] matrix4 = new ATomsUD[this.line][this.column];

    String [][] matrix = new String[this.line][this.column];

    public Matrix() {}

    /**本函数用于将输入矩阵界面输入的字符串转化为字符串矩阵*/
    public static String[][] StringToArray(String S){
        S = S.replace(" ","");//去掉空格
        //S = S.replace("\n","");//去掉换行
        String[] S1 = S.split("[;；\n]");
        String[][] S2 = new String[S1.length][];
        for (int i = 0;i<S1.length;i++){
            S2[i] = S1[i].split("[,，]");
        }
        int num = 0;
        String[][] S3 = new String[S1.length][];
        for (int i = 0;i<S1.length;i++){
            if (S2[i].length==1&& S2[i][0].equals("")){
                //那么这一行应该被删掉
            }
            else {
                S3[num] = S2[i];
                num++;
            }
        }
        String[][] S4 = new String[num][];
        for (int i = 0;i<num;i++){
            S4[i] = S3[i];
        }
        return S4;
    }

    /**本函数用于接收一个字符串矩阵，用来判断字符串矩阵是否合法（行数等于列数，每一行的列数都相等）*/
    public static boolean judge(String[][] S){
        int flag1 = S[0].length;
        for (int i = 1;i<S.length;i++){
            if (S[i].length!=flag1){
                return false;
            }
        }
        return true;
    }

    /**如果字符串是纯小数，返回1，如果是错误，返回0，如果是其他，返回-1，如果是整数。返回2*/
    public static byte judgeDouble(String S){
        int i;
        int flag = 0;
        int point = 0;// 小数点的个数超过1，返回0
        for (i = 0;i<S.length();i++){
            if (S.toCharArray()[i]=='.'){
                point = point + 1;
                flag = flag + 1;
            }
            else if (S.toCharArray()[i]>='0'&&S.toCharArray()[i]<='9'){
                flag = flag + 1;
            }
        }
        if (point>1){
            return 0;
        }
        else if (flag == S.length()){
            if (point == 1) {
                return 1;
            }
            else {
                return 2;
            }
        }
        else {
            if (point == 1){
                return 0;
            }
            Quarks Q = new Quarks(S);
            if (Q.type == 0){
                return 0;
            }
            else if (Q.type == 11||Q.type == 10||(Q.type == 1&&Q.coe.d==1)){
                return 2;
            }
            else {
                return -1;
            }
        }
    }

    /**判断一个矩阵是不是纯小数矩阵，
     * 如果是纯小数矩阵，返回1，
     * 如果有小数，又出现了形如x，y等数字，返回0，表示输入错误（里面又有小数又有分数的，，，又有x，y什么的，表示无法识别）
     * 返回-1，表示不是前面两种类型，可以按照一元矩阵来处理*/
    public static byte judgeDouble(String[][] S){
        int first = 0,second = 0,third = 0;
        int i,j;
        int line = S.length, column = S[0].length;
        for (i = 0;i<line;i++){
            for (j = 0;j<column;j++){
                byte b = judgeDouble(S[i][j]);
                if (b==0){
                    return 0;
                }
                else if (b==1){
                    first = first + 1;//纯小数
                }
                else if (b == -1){
                    second = second + 1;//其他
                }
                else if (b == 2){
                    third = third + 1;//整数
                }
            }
        }
        //System.out.println("first = "+first+"    "+"second = "+second + "third = " + third);
        if (first == 0){
            if (second + third == line * column){
                return -1;
            }
            else {
                return 0;
            }
        }
        if (first + third == line * column){
            return 1;//纯小数矩阵
        }
        else if (second + third == line * column){
            return -1;//其他矩阵
        }
        else {
            return 0;//错误
        }
    }

    /*下面这个构造器会将字符串转为ATomsUD类型，然后再酌情转化类型*/
    public Matrix(String[][] string){
        byte judge = judgeDouble(string);
        if (judge==0){
            this.BasicType = 0;
            this.SecondType = 0;
            this.ErrorType = 7;//格式错误
        }
        else {
            int line = string.length;
            int column = string[0].length;
            if (judge == 1){
                //构造小数矩阵
                double[][] D = new double[line][column];
                for (int i = 0;i<line;i++){
                    for (int j = 0;j<column;j++){
                        D[i][j] = Double.parseDouble(string[i][j]);
                    }
                }
                this.BasicType = 1;
                if (line ==1){
                    if (column == 1){
                        this.SecondType = 0;//单对象
                    }
                    else {
                        this.SecondType = 1;//行向量
                    }
                }
                else {
                    if (column == 1){
                        this.SecondType = 2;//列向量
                    }
                    else {
                        this.SecondType = 3;//矩阵
                    }
                }
                this.matrix1 = D;
                this.line = line;
                this.column = column;
            }
            else {
                ATomsUD[][] A = new ATomsUD[line][column];
                for (int i = 0; i < line; i++) {//将其转化为ATomsUD型对象
                    for (int j = 0; j < column; j++) {
                        A[i][j] = new ATomsUD(string[i][j]);
                        //System.out.printf("A[%d][%d].type = %d\t",i,j,A[i][j].ASU.type);
                    }
                    //System.out.println();
                }
                //下面对输入的矩阵类型进行判断
                boolean sign = false;
                for (int i = 0; i < line; i++) {
                    for (int j = 0; j < column; j++) {
                        if (A[i][j].ASU.type == 2 || A[i][j].ASD.type == 2) {
                            sign = true;//出现了某一项是一元的，那么整个矩阵也就是一元的
                            break;
                        }
                    }
                }
                this.line = line;
                this.column = column;
                if (!sign) {
                    UD[][] A2 = new UD[line][column];
                    //纯数值矩阵
                    for (int i = 0; i < line; i++) {
                        for (int j = 0; j < column; j++) {
                            //A2[i][j] = UD.division(A[i][j].ASU.quarks[0].coe, A[i][j].ASD.quarks[0].coe);
                            A2[i][j] = A[i][j].ASU.quarks[0].coe;
                        }
                    }
                    this.BasicType = 2;
                    this.matrix2 = A2;
                } else {
                    //System.out.println("---------");
                    ATomUD[][] B = new ATomUD[line][column];
                    for (int i = 0; i < line; i++) {
                        for (int j = 0; j < column; j++) {
                            //System.out.println("before = "+string[i][j]);
                            B[i][j] = new ATomUD(string[i][j]);
                            if (B[i][j].type==0){
                                this.BasicType = 0;
                                this.SecondType = 0;
                                this.ErrorType = 7;//格式错误
                            }
                            //B[i][j].print();
                        }
                    }
                    //System.out.println("-------------");
                    this.BasicType = 3;
                    this.matrix3 = B;
                }
                if (line == 1 && column == 1) {
                    this.SecondType = 0;
                } else if (line == 1 && column >= 1) {
                    this.SecondType = 1;
                } else if (column == 1 && line >= 1) {
                    this.SecondType = 2;
                } else {
                    this.SecondType = 3;
                }
            }
        }

    }

    /*下面是float对象的构造*/

    public Matrix(float x) {
        this.line = 1;
        this.column = 1;
        double[][] matrix = new double[this.line][this.column];
        matrix[0][0] = x;
        this.matrix1 = matrix;
        this.BasicType = 1;
        this.SecondType = 0;
    }

    public Matrix(float[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            double[][] matrix = new double[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j] = x[j];
            }
            this.matrix1 = matrix;
            this.BasicType = 1;
            this.SecondType = 1;
        }
        else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            double[][] matrix = new double[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0] = x[i];
            }
            this.matrix1 = matrix;
            this.BasicType = 1;
            this.SecondType = 2;
        }
        else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(float[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        double[][] m = new double[this.line][this.column];
        int i, j;
        for (i = 0; i < this.line; i++) {
            for (j = 0; j < this.column; j++) {
                m[i][j] = matrix[i][j];
            }
        }
        this.matrix1 = m;
        this.BasicType = 1;
        this.SecondType = 3;
    }

    /*float型对象构造完成*/

    /*下面是double对象的构造*/

    public Matrix(double x) {
        this.line = 1;
        this.column = 1;
        double[][] matrix = new double[this.line][this.column];
        matrix[0][0] = x;
        this.matrix1 = matrix;
        this.BasicType = 1;
        this.SecondType = 0;
    }

    public Matrix(double[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            double[][] matrix = new double[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j] = x[j];
            }
            this.matrix1 = matrix;
            this.BasicType = 1;
            this.SecondType = 1;
        }
        else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            double[][] matrix = new double[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0] = x[i];
            }
            this.matrix1 = matrix;
            this.BasicType = 1;
            this.SecondType = 2;
        }
        else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(double[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        this.matrix1 = matrix;
        this.BasicType = 1;
        this.SecondType = 3;
    }

    /*double型对象构造完成*/

    /*下面是对int型对象的构造*/

    public Matrix(int x) {
        this.line = 1;
        this.column = 1;
        UD[][] matrix = new UD[this.line][this.column];
        matrix[0][0] = new UD(x, 1L);
        this.matrix2 = matrix;
        this.BasicType = 2;
        this.SecondType = 0;
    }

    public Matrix(int[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            UD[][] matrix = new UD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j].u = x[j];
                matrix[0][j].d = 1L;
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 1;
        }
        else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            UD[][] matrix = new UD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0].u = x[i];
                matrix[i][0].d = 1L;
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 2;
        }
        else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(int[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        UD[][] m = new UD[this.line][this.column];
        int i, j;
        for (i = 0; i < this.line; i++) {
            for (j = 0; j < this.column; j++) {
                m[i][j] = new UD();
                m[i][j].u = matrix[i][j];
                m[i][j].d = 1L;
            }
        }
        this.matrix2 = m;
        this.BasicType = 2;
        this.SecondType = 3;
    }

    /*nt型对象构造完毕*/

    /*下面是是long型对象的构造*/

    public Matrix(long x) {
        this.line = 1;
        this.column = 1;
        UD[][] matrix = new UD[this.line][this.column];
        matrix[0][0] = new UD(x, 1L);
        this.matrix2 = matrix;
        this.BasicType = 2;
        this.SecondType = 0;
    }

    public Matrix(long[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            UD[][] matrix = new UD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j].u = x[j];
                matrix[0][j].d = 1L;
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 1;
        } else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            UD[][] matrix = new UD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0].u = x[i];
                matrix[i][0].d = 1L;
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 2;
        } else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(long[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        UD[][] m = new UD[this.line][this.column];
        int i, j;
        for (i = 0; i < this.line; i++) {
            for (j = 0; j < this.column; j++) {
                m[i][j] = new UD();
                m[i][j].u = matrix[i][j];
                m[i][j].d = 1L;
            }
        }
        this.matrix2 = m;
        this.BasicType = 2;
        this.SecondType = 3;
    }

    /*long型对象构造完毕*/

    /*下面是UD型对象的构造*/

    public Matrix(UD x) {
        this.line = 1;
        this.column = 1;
        UD[][] matrix = new UD[this.line][this.column];
        matrix[0][0] = x;
        this.matrix2 = matrix;
        this.BasicType = 2;
        this.SecondType = 0;
    }

    public Matrix(UD[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            UD[][] matrix = new UD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j] = x[j];
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 1;
        } else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            UD[][] matrix = new UD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0] = x[i];
            }
            this.matrix2 = matrix;
            this.BasicType = 2;
            this.SecondType = 2;
        } else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(UD[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        this.matrix2 = matrix;
        this.BasicType = 2;
        this.SecondType = 3;
    }

    /*UD型对象构造完成*/

    /*下面是ATom对象的构造*/

    public Matrix(ATom x) {
        this.line = 1;
        this.column = 1;
        ATomUD[][] matrix = new ATomUD[this.line][this.column];
        matrix[0][0].AU = x;
        matrix[0][0].AD = new ATom();
        this.matrix3 = matrix;
        this.BasicType = 3;
        this.SecondType = 0;
    }

    public Matrix(ATom[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            ATomUD[][] matrix = new ATomUD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j].AU = x[j];
                matrix[0][j].AD = new ATom();
            }
            this.matrix3 = matrix;
            this.BasicType = 3;
            this.SecondType = 1;
        } else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            ATomUD[][] matrix = new ATomUD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0].AU = x[i];
                matrix[i][0].AD = new ATom();
            }
            this.matrix3 = matrix;
            this.BasicType = 3;
            this.SecondType = 2;
        } else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(ATom[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        ATomUD[][] m = new ATomUD[this.line][this.column];
        int i, j;
        for (i = 0; i < this.line; i++) {
            for (j = 0; j < this.column; j++) {
                m[i][j] = new ATomUD();
                m[i][j].AU = matrix[i][j];
                m[i][j].AD = new ATom(1);
            }
        }
        this.matrix3 = m;
        this.BasicType = 3;
        this.SecondType = 3;
    }

    /*ATom型对象构造完成*/

    /*下面是ATomUD对象的构造*/

    public Matrix(ATomUD x) {
        this.line = 1;
        this.column = 1;
        ATomUD[][] matrix = new ATomUD[this.line][this.column];
        matrix[0][0] = x;
        this.matrix3 = matrix;
        this.BasicType = 3;
        this.SecondType = 0;
    }

    public Matrix(ATomUD[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            ATomUD[][] matrix = new ATomUD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j] = x[j];
            }
            this.matrix3 = matrix;
            this.BasicType = 3;
            this.SecondType = 1;
        } else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            ATomUD[][] matrix = new ATomUD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0] = x[i];
            }
            this.matrix3 = matrix;
            this.BasicType = 3;
            this.SecondType = 2;
        } else {
            this.BasicType = 0;
            this.ErrorType = 3;
        }
    }

    public Matrix(ATomUD[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        this.matrix3 = matrix;
        this.BasicType = 3;
        this.SecondType = 3;
    }

    /*ATomUD型对象构造完成*/

    /*下面是AToms对象的构造*/

    public Matrix(AToms x) {
        this.line = 1;
        this.column = 1;
        ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
        matrix[0][0].ASU = x;
        matrix[0][0].ASD = new AToms();
        this.matrix4 = matrix;
        this.BasicType = 4;
        this.SecondType = 0;
    }

    public Matrix(AToms[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j].ASU = x[j];
                matrix[0][j].ASD = new AToms();
            }
            this.matrix4 = matrix;
            this.BasicType = 4;
            this.SecondType = 1;
        }
        else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0].ASU = x[i];
                matrix[i][0].ASD = new AToms();
            }
            this.matrix4 = matrix;
            this.BasicType = 4;
            this.SecondType = 2;
        }
        else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(AToms[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        ATomsUD[][] m = new ATomsUD[this.line][this.column];
        int i, j;
        for (i = 0; i < this.line; i++) {
            for (j = 0; j < this.column; j++) {
                m[i][j].ASU = matrix[i][j];
                m[i][j].ASD = new AToms();
            }
        }
        this.matrix4 = m;
        this.BasicType = 4;
        this.SecondType = 3;
    }

    /*AToms型对象构造完成*/

    /*下面是ATomsUD对象的构造*/

    public Matrix(ATomsUD x) {
        this.line = 1;
        this.column = 1;
        ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
        matrix[0][0] = x;
        this.matrix4 = matrix;
        this.BasicType = 4;
        this.SecondType = 0;
    }

    public Matrix(ATomsUD[] x, int type) {
        if (type == 1) {
            //构造行向量
            this.line = 1;
            this.column = x.length;
            ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
            for (int j = 0; j < this.column; j++) {
                matrix[0][j] = x[j];
            }
            this.matrix4 = matrix;
            this.BasicType = 4;
            this.SecondType = 1;
        } else if (type == 2) {
            //构造列向量
            this.line = x.length;
            this.column = 1;
            ATomsUD[][] matrix = new ATomsUD[this.line][this.column];
            for (int i = 0; i < this.line; i++) {
                matrix[i][0] = x[i];
            }
            this.matrix4 = matrix;
            this.BasicType = 4;
            this.SecondType = 2;
        } else {
            this.BasicType = 0;
            this.ErrorType = 31;
        }
    }

    public Matrix(ATomsUD[][] matrix) {
        this.line = matrix.length;
        this.column = matrix[0].length;
        this.matrix4 = matrix;
        this.BasicType = 4;
        this.SecondType = 3;
    }

    /*ATomsUD型对象构造完成*/

    public static String [] input(String string){
        int l = string.length();
        char [] Input = string.toCharArray();
        String [] s = new String[100];
        int i,j = 0,k=0,n = 0;
        char p = Input[0];
        for (i = 0;i<l;i++){
            if (p == ','||p == '，') {
                //System.out.printf("\n j = %d , i = %d\n",j,i);
                if (i == l - 1) {
                    s[k] = string.substring(j, i + 1);
                    s[k] = s[k].replace(",", "");
                    s[k] = s[k].replace("，", "");
                    //System.out.println(s[k]);
                    //System.out.println();
                    k++;
                } else {
                    s[k] = string.substring(j, i);
                    //System.out.println(s[k]);
                    s[k] = s[k].replace(",", "");
                    s[k] = s[k].replace("，", "");
                    //System.out.println();
                    k++;
                    j = i;
                }
                n++;
                p = Input[n];
            }
            else {
                n++;
                if (n<l){
                    p = Input[n];
                }
                else {
                    //n>=l;
                    //System.out.printf("\n j = %d , i = %d \n",j,i);
                    s[k] = string.substring(j,l);
                    s[k] = s[k].replace(",", "");
                    s[k] = s[k].replace("，", "");
                    //System.out.println(s[k]);
                    //System.out.println();
                    k++;
                    break;
                }
            }
        }
        String[] s1 = new String[k];
        for (i = 0;i<k;i++){
            s1[i] = s[i];
        }
        return s1;
    }

    public void errorType() { errorType(this); }

    public void type() { type(this); }

    public void print() { print(this); }

    public void empty() { empty(this); }

    public Matrix abs() { return abs(this); }

    public Matrix sum(byte type) { return sum(this, type); }

    public Matrix sum() { return sum(this); }

    public Matrix reverse() { return reverse(this); }

    public Matrix check() { return check(this); }

    /***********************************************************************************************/
    /************************************************************************************************/
    //调用下面这些函数：

    /**获取本对象的行列式*/
    public Matrix det() { return det(this); }

    /**获取本对象的逆矩阵*/
    public Matrix inv() { return inv(this); }

    /**获取本对象的阶梯型*/
    public Matrix rowEchelon() { return rowEchelon(this); }

    /**获取本对象的秩*/
    public Matrix rank() { return rank(this); }

    /**获取本对象的正定性*/
    public String[] positive() { return positive(this); }

    /**获取本对象的正交变换的类型*/
    public String[] orthogonal() { return orthogonal(this); }

    /**获取本对象对应的增广矩阵所对应的解x*/
    public String[] linearSystemEquations(){ return linearSystemEquations(this); }

    /*************************************************************************************************/
    /**************************************************************************************************/

    /**获取本对象的转置*/
    public Matrix trans() { return trans(this); }

    public Matrix eigen() { return eigen(this); }

    public Matrix eigenVector() { return eigenVector(this); }

    /**得到本对象的pow幂次*/
    public Matrix power(byte pow) { return power(this, pow); }

    public Matrix add(Matrix matrix) { return add(this, matrix); }

    public Matrix less(Matrix matrix) { return less(this, matrix); }

    public Matrix multiply(Matrix matrix) { return multiply(this, matrix); }

    public Matrix division(Matrix matrix) { return division(this, matrix); }

    public Matrix max(Matrix matrix) { return max(this, matrix); }

    public Matrix min(Matrix matrix) { return min(this, matrix); }

    /**返回：M1的m1次 乘  M2的m2次的计算结果*/
    public static Matrix multiply(Matrix M1,byte m1,Matrix M2,byte m2){
        if (m1==1){
            if (m2 == 1) {
                return multiply(M1, M2);
            }
            else {
                return multiply(M1,power(M2,m2));
            }
        }
        else {
            return multiply(power(M1,m1),power(M2,m2));
        }
    }

    /******************************************************************************/
    /**获取本对象的字符串形式*/
    public String[][] getString(){ return getString(this); }
    /******************************************************************************/

    public static void errorType(Matrix matrix) {
        switch (matrix.ErrorType) {
            case 0:{
                System.out.println("\n本对象无异常");
                break;
            }
            case 1: {
                System.out.println("\n出现分母为0");
                break;
            }
            case 2: {
                System.out.println("\n矩阵维度不一致");
                break;
            }
            case 21: {
                System.out.println("\n矩阵维度不一致,无法相加");
                break;
            }
            case 22: {
                System.out.println("\n矩阵维度不一致,无法相减");
                break;
            }
            case 23: {
                System.out.println("\n矩阵维度不一致,无法相乘");
                break;
            }
            case 24: {
                System.out.println("\n矩阵维度不一致,无法相除");
                break;
            }
            case 3: {
                System.out.println("\n输入的参数不在允许的范围内");
                break;
            }
            case 31 : {
                System.out.println("\n第二个参数错误，type==1 构造行向量，type==2 构造列向量，不接受其他参数");
                break;
            }
            case 32 : {
                System.out.println("\n第二个参数错误，暂不支持对矩阵计算负指数幂");
                break;
            }
            case 33 : {
                System.out.println("\n第二个参数错误，type==1 按行求和，返回一个列向量；\ntype==2 按列求和，返回一个行向量\ntype==3 所有元素求和\n如果matrix是一个向量，type将不起作用，求和后返回一个值");
                break;
            }
            case 4: {
                System.out.println("\n非方阵");
                break;
            }
            case 41: {
                System.out.println("\n未对非方阵的逆运算进行定义");
                break;
            }
            case 42: {
                System.out.println("\n非方阵，无法求幂");
                break;
            }
            case 43: {
                System.out.println("\n未对非方阵的行列式进行定义");
                break;
            }
            case 5: {
                System.out.println("\n方阵是奇异的，无法求逆");
                break;
            }
            case 6: {
                System.out.println("\n暂不支持的运算");
                break;
            }
        }
    }

    public static String getError(Matrix matrix) {
        switch (matrix.ErrorType) {
            case 0:{
                return "本对象无异常";
            }
            case 1: {
                return "出现有矩阵元素的分母为0";
            }
            case 2: {
                return "矩阵维度不一致";
            }
            case 21: {
                return "矩阵维度不一致,无法相加";
            }
            case 22: {
                return "矩阵维度不一致,无法相减";
            }
            case 23: {
                return "矩阵维度不一致,无法相乘";
            }
            case 24: {
                return "矩阵维度不一致,无法相除";
            }
            case 3: {
                return "输入的参数不在允许的范围内";
            }
            case 31 : {
                return "第二个参数错误，type==1 构造行向量，type==2 构造列向量，不接受其他参数";
            }
            case 32 : {
                return "第二个参数错误，暂不支持对矩阵计算负指数幂";
            }
            case 33 : {
                return "第二个参数错误，type==1 按行求和，返回一个列向量;type==2 按列求和，返回一个行向量;type==3 所有元素求和;如果matrix是一个向量，type将不起作用，求和后返回一个值";
            }
            case 4: {
                return "非方阵";
            }
            case 41: {
                return "未对非方阵的逆运算进行定义";
            }
            case 42: {
                return "非方阵，无法求幂";
            }
            case 43: {
                return "未对非方阵的行列式进行定义";
            }
            case 5: {
                return "方阵是奇异的，无法求逆";
            }
            case 6: {
                return "暂不支持的运算";
            }
            case 7: {
                return "输入的格式不正确";
            }
            default:{
                return "出现未定义的错误";
            }
        }
    }

    public static void type(Matrix matrix) {
        System.out.print("\n本对象是一个");
        switch (matrix.BasicType) {
            case 1:
                System.out.print("double");
                break;
            case 2:
                System.out.print("UD（分数）");
                break;
            case 3:
                System.out.print("ATomUD（一元多项式的分式）");
                break;
            case 4:
                System.out.print("ATomsUD（多元多项式的分式）");
                break;
        }
        System.out.print("型的");
        switch (matrix.SecondType) {
            case 0:
                System.out.print("单结构\n");
                break;
            case 1:
                System.out.print("行向量\n");
                break;
            case 2:
                System.out.print("列向量\n");
                break;
            case 3:
                System.out.print("矩阵\n");
                break;
        }
    }

    public static void print(Matrix matrix) {
        int line = matrix.line;
        int column = matrix.column;
        int i, j;
        switch (matrix.BasicType) {
            case 0:{
                errorType(matrix);
                break;
            }
            case 1: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        String [][] A = new String[line][column];
                        A[i][j] = String.valueOf(matrix.matrix1[i][j]);
                        //System.out.printf("%f\t", matrix.matrix1[i][j]);
                        matrix.matrix = A;
                        System.out.print(matrix.matrix[i][j]);
                        System.out.print("\t");
                    }
                    System.out.print("\n");
                }
                break;
            }
            case 2: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        String [][] A = new String[line][column];
                        A[i][j] = UD.UDToString(matrix.matrix2[i][j]);
                        matrix.matrix = A;
                        //matrix.matrix2[i][j].print();
                        System.out.print(matrix.matrix[i][j]);
                        System.out.print("\t");
                    }
                    System.out.print("\n");
                }
                break;
            }
            case 3: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        String [][] A = new String[line][column];
                        A[i][j] = ATomUD.ATomUDToString(matrix.matrix3[i][j]);
                        //matrix.matrix3[i][j].print();
                        matrix.matrix = A;
                        System.out.print(matrix.matrix[i][j]);
                        System.out.print("\t");
                    }
                    System.out.print("\n");
                }
                break;
            }
            case 4: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        matrix.matrix4[i][j].print();
                        System.out.print("\t");
                    }
                    System.out.print("\n");
                }
                break;
            }
        }
    }

    public static void empty(Matrix matrix) {
        int line = matrix.line;
        int column = matrix.column;
        int i, j;
        switch (matrix.BasicType) {
            case 1: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        matrix.matrix1[i][j] = 0;
                    }
                }
                break;
            }
            case 2: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        matrix.matrix2[i][j].u = 0;
                        matrix.matrix2[i][j].d = 1L;
                    }
                }
                break;
            }
            case 3: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        matrix.matrix3[i][j] = new ATomUD();
                    }
                }
                break;
            }
            case 4: {
                for (i = 0; i < line; i++) {
                    for (j = 0; j < column; j++) {
                        matrix.matrix4[i][j] = new ATomsUD();
                    }
                }
                break;
            }
        }
    }

    /**本方法用于求绝对值*/
    public static Matrix abs(Matrix matrix) {
        int l = matrix.line;
        int c = matrix.column;
        int i, j;
        Matrix m = matrix;
        switch (matrix.BasicType) {
            case 1: {
                for (i = 0; i < l; i++) {
                    for (j = 0; j < c; j++) {
                        if (matrix.matrix1[i][j] < 0) {
                            m.matrix1[i][j] = -1 * matrix.matrix1[i][j];
                        }
                    }
                }
            }
            case 2: {
                for (i = 0; i < l; i++) {
                    for (j = 0; j < c; j++) {
                        if (matrix.matrix2[i][j].u * matrix.matrix2[i][j].d < 0) {
                            if (matrix.matrix2[i][j].u < 0) {
                                m.matrix2[i][j].u = -1 * matrix.matrix2[i][j].u;
                            } else {
                                //matrix.matrix2[i][j].u>0
                                m.matrix2[i][j].d = -1 * matrix.matrix2[i][j].d;
                            }
                        }
                    }
                }
            }
            case 3: {
                //暂时不写
            }
            case 4: {
                //暂时不写
            }
        }

        return m;
    }

    /**如果matrix是一个矩阵，type==1 按行求和，返回一个列向量；
     *                    type==2 按列求和，返回一个行向量
     *                    type==3 所有元素求和
     * 如果matrix是一个向量，type将不起作用，求和后返回一个值*/
    public static Matrix sum(Matrix matrix, int type) {
        if ((type != 1 && type != 2 && type != 3)&&(matrix.line==1||matrix.column==1)){
            //参数type的输入不正确,而且它不是向量
            Matrix M = new Matrix();
            M.BasicType = 0;
            M.ErrorType = 33;
            return M;
        }
        else {
            switch (matrix.SecondType) {
                case 0:{
                    //一个数字
                    return matrix;
                }
                case 1: {
                    //行向量
                    switch (matrix.BasicType){
                        case 0:{
                            //异常矩阵，原地打回
                            return matrix;
                        }
                        case 1:{
                            //小数型行向量
                            int j;
                            double sum = 0;
                            for (j=0;j<matrix.column;j++){
                                sum += matrix.matrix1[0][j];
                            }
                            return new Matrix(sum);
                        }
                        case 2:{
                            //分数型行向量
                            int j;
                            UD sum = new UD();
                            for (j=0;j<matrix.column;j++){
                                //遇到分数，就要判断一下会不会出现分母为0
                                if (matrix.matrix2[0][j].d==0){
                                    Matrix M = new Matrix();
                                    M.BasicType = 0;
                                    M.ErrorType = 1;
                                    return M;
                                }
                                else{
                                    sum = UD.add(sum,matrix.matrix2[0][j]);
                                }
                            }
                            return new Matrix(sum);
                        }
                        case 3:{
                            //一元行向量

                        }
                        case 4:{
                            //多元行向量
                        }
                        default:{
                            //不支持的运算
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 6;
                            return M;
                        }
                    }
                }
                case 2: {
                    //列向量
                    switch (matrix.BasicType){
                        case 0:{
                            //异常矩阵，原地打回
                            return matrix;
                        }
                        case 1:{
                            //小数型列向量
                            int i;
                            double sum = 0;
                            for (i=0;i<matrix.line;i++){
                                sum += matrix.matrix1[i][0];
                            }
                            return new Matrix(sum);
                        }
                        case 2:{
                            //分数型列向量
                            int i;
                            UD sum = new UD();
                            for (i=0;i<matrix.line;i++){
                                if (matrix.matrix2[i][0].d==0){
                                    //判断一下，是否会出现分母为0
                                    Matrix M = new Matrix();
                                    M.BasicType = 0;
                                    M.ErrorType = 1;
                                    return M;
                                }
                                else{
                                    sum = UD.add(sum,matrix.matrix2[i][0]);
                                }
                            }
                            return new Matrix(sum);
                        }
                        case 3:{
                            //一元列向量
                        }
                        case 4:{
                            //多元列向量
                        }
                        default:{
                            //不支持的运算
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 6;
                            return M;
                        }
                    }
                }
                case 3: {
                    //矩阵
                    switch (matrix.BasicType){
                        case 0:{
                            //异常矩阵，原地打回
                            return matrix;
                        }
                        case 1:{
                            //小数型矩阵
                            if (type == 1){
                                //按行求和，返回一个列向量
                                int i,j;
                                int line = matrix.line;
                                int column = matrix.column;
                                double [] sum = new double[line];
                                for (i=0;i<line;i++){
                                    for (j=0;j<column;j++){
                                        sum[i] += matrix.matrix1[i][j];
                                    }
                                }
                                return new Matrix(sum,2);
                            }
                            else if(type == 2) {
                                //按列求和，返回一个行向量
                                int i,j;
                                int line = matrix.line;
                                int column = matrix.column;
                                double [] sum = new double[column];
                                for (j=0;j<column;j++){
                                    for (i=0;i<line;i++){
                                        sum[j] += matrix.matrix1[i][j];
                                    }
                                }
                                return new Matrix(sum,1);
                            }
                            else{
                                //这个时候，type一定等于3，不可能等于别的
                                //全部元素之和
                                //先对matrix按行求和，再对返回的列向量进行求和
                                return Matrix.sum(Matrix.sum(matrix, 1));
                            }
                        }
                        case 2:{
                            //分数型矩阵
                            if (type == 1){
                                //按行求和
                                int i,j;
                                int line = matrix.line;
                                int column = matrix.column;
                                UD [] sum = new UD[line];
                                for (i=0;i<line;i++){
                                    sum[i] = new UD();
                                    for (j=0;j<column;j++){
                                        if (matrix.matrix2[i][j].d==0){
                                            //判断一下，是否会出现分母为0
                                            Matrix M = new Matrix();
                                            M.BasicType = 0;
                                            M.ErrorType = 1;
                                            return M;
                                        }
                                        else{
                                            sum[i] = UD.add(sum[i],matrix.matrix2[i][j]);
                                        }
                                    }
                                }
                                return new Matrix(sum,2);
                            }
                            else if(type == 2) {
                                //按列求和,返回一个行向量
                                int i,j;
                                int line = matrix.line;
                                int column = matrix.column;
                                UD [] sum = new UD[column];
                                for (j=0;j<column;j++){
                                    sum[j] = new UD();
                                    for (i=0;i<line;i++){
                                        if (matrix.matrix2[i][j].d==0){
                                            //判断一下，是否会出现分母为0
                                            Matrix M = new Matrix();
                                            M.BasicType = 0;
                                            M.ErrorType = 1;
                                            return M;
                                        }
                                        else{
                                            sum[j] = UD.add(sum[j],matrix.matrix2[i][j]);
                                        }
                                    }
                                }
                                return new Matrix(sum,1);
                            }
                            else{
                                //这个时候，type一定等于3，不可能等于别的
                                //全部元素之和，先按行求和，再对返回的列向量求和
                                return Matrix.sum(Matrix.sum(matrix, 1));
                            }
                        }
                        case 3:{
                            //一元矩阵
                            if (type == 1){
                                //按行求和
                            }
                            else if(type == 2) {
                                //按列求和
                            }
                            else{
                                //这个时候，type一定等于3，不可能等于别的
                                //全部元素之和
                            }
                        }
                        case 4:{
                            //多元矩阵
                            if (type == 1){
                                //按行求和
                            }
                            else if(type == 2) {
                                //按列求和
                            }
                            else{
                                //这个时候，type一定等于3，不可能等于别的
                                //全部元素之和
                            }
                        }
                        default:{
                            //不支持的运算
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 6;
                            return M;
                        }
                    }
                }
                default:{
                    //不支持的运算
                    Matrix M = new Matrix();
                    M.BasicType = 0;
                    M.ErrorType = 6;
                    return M;
                }
            }

        }
    }

    /**不指定求和方式，如果是行向量，按行求和，如果是列向量，按列求和，如果是矩阵，所有元素求和，返回一个值*/
    public static Matrix sum(Matrix matrix) {
        switch (matrix.SecondType){
            case 0:{
                //一个数
                return matrix;
            }
            case 1:{
                //行向量
                return Matrix.sum(matrix,2);
            }
            case 2:{
                //列向量
                return Matrix.sum(matrix,1);
            }
            case 3:{
                //矩阵
                return Matrix.sum(matrix, 3);
            }
            default:{
                //不支持的运算类型
                Matrix M = new Matrix();
                M.BasicType = 0;
                M.ErrorType = 6;
                return M;
            }
        }
    }

    public static Matrix reverse(Matrix matrix) {
        int l = matrix.line;
        int c = matrix.column;
        int i, j;
        switch (matrix.BasicType) {
            case 1: {
                //说明是小数
                Matrix m = matrix;
                for (i = 0; i < l; i++) {
                    for (j = 0; j < c; j++) {
                        m.matrix1[i][j] = -1 * m.matrix1[i][j];
                    }
                }
                return m;
            }
            case 2: {
                //说明是分数
                UD[][] m = new UD[l][c];
                for (i = 0; i < l; i++) {
                    for (j = 0; j < c; j++) {
                        m[i][j] = matrix.matrix2[i][j].reverse();
                    }
                }
                Matrix M = new Matrix(m);
                return M;
            }
            default: {
                Matrix m = matrix;
                return m;
            }
        }
    }

    public static Matrix check(Matrix matrix) { return new Matrix(); }

    /**求秩，计算方法为初等行变换*/
    public static Matrix rank(Matrix matrix) {
        switch (matrix.BasicType){
            case 0:{
                //异常矩阵
                return matrix;
            }
            case 1:{
                //小数矩阵
                Matrix m = matrix.rowEchelon();
                int i, j, rank = 0;
                int line = matrix.line;
                int column = matrix.column;
                for (i = 0, j = 0; i < line && j < column; i++, j++) {
                    if (m.matrix1[i][j] != 0) rank++;
                }
                return new Matrix(rank);
            }
            case 2:{
                //分数矩阵
                Matrix m = matrix.rowEchelon();
                int i, j, rank = 0;
                int line = matrix.line;
                int column = matrix.column;
                for (i = 0, j = 0; i < line && j < column; i++, j++) {
                    if (m.matrix2[i][j].u != 0) rank++;
                }
                return new Matrix(rank);
            }
            default:{
                //不支持的运算类型
                Matrix M = new Matrix();
                M.BasicType = 0;
                M.ErrorType = 6;
                return M;
            }
        }
    }

    public static Matrix det(Matrix matrix) {
        if (matrix.line==matrix.column){
            switch (matrix.BasicType) {
                case 0: {
                    //异常矩阵，原地打回
                    return matrix;
                }
                case 1: {
                    //小数型矩阵
                    int i, j, s, nonzero, m, n, t,flag=0;
                    double temp, value_of_matrix = 1.0;
                    int order = matrix.line;
                    double [][] U = new double[order][order];
                    for (i = 0;i<order;i++){
                        for (j=0;j<order;j++){
                            U[i][j]=matrix.matrix1[i][j];
                        }
                    }
                    i = 0;
                    j = 0;
                    for (s = 0; s < order - 1; s++) {
                        nonzero = Looking(U, i, j, order);
                        if (nonzero == -1) {
                            i++;
                            j++;
                        }
                        else if (nonzero == i) {
                            for (m = i + 1; m < order; m++) {
                                if (U[m][j] != 0){
                                    for (n = j + 1; n < order; n++) {
                                        U[m][n] = U[m][n] - U[i][n] * U[m][j] / U[i][j];
                                    }
                                    U[m][j] = 0;
                                }
                            }
                            i++;
                            j++;
                        }
                        else {
                            for (t = j; t < order; t++) {
                                temp = U[nonzero][t];
                                U[nonzero][t] = U[i][t];
                                U[i][t] = temp;
                            }
                            flag++;
                            for (m = i + 1; m < order; m++) {
                                if (U[m][j] != 0){
                                    for (n = j + 1; n < order; n++) {
                                        U[m][n] = U[m][n] - U[i][n] * U[m][j] / U[i][j];
                                    }
                                    U[m][j] = 0;
                                }
                            }
                            i++;
                            j++;
                        }
                    }
                    for (i = 0; i < order; i++) {
                        value_of_matrix *= U[i][i];
                    }
                    if (flag % 2 == 1) value_of_matrix=value_of_matrix*(-1);
                    return new Matrix(value_of_matrix);
                }
                case 2: {
                    //UD型矩阵
                    int i, j, s, t, nonzero, m, n, flag = 0;
                    int order = matrix.line;
                    UD value_of_determinant = new UD(1,1);
                    UD [][] U = new UD[order][order];
                    for (i=0;i<order;i++){
                        for (j=0;j<order;j++){
                            U[i][j] = UD.copy(matrix.matrix2[i][j]);
                            if (U[i][j].d == 0){
                                //出现有分母为0
                                Matrix M = new Matrix();
                                M.BasicType = 0;
                                M.ErrorType = 1;
                                return M;
                            }
                        }
                    }
                    i = 0; j = 0;
                    for (s = 0; s < order - 1; s++) {
                        nonzero = Looking(U, i, j, order);
                        if (nonzero == -1) {
                            i++; j++;
                        }
                        else if (nonzero == i) {
                            for (m = i + 1; m < order; m++) {
                                if (U[m][j].u != 0){
                                    UD temp;
                                    for (n = j + 1; n < order; n++) {
                                        temp = UD.multiply(U[i][n],U[m][j]);
                                        temp = UD.division(temp,U[i][j]);
                                        U[m][n] = UD.less(U[m][n],temp);
                                        //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    }
                                    U[m][j]=new UD();
                                }
                            }
                            i++; j++;
                        }
                        else {
                            for (t = j; t < order; t++) {
                                U[nonzero][t] = matrix.matrix2[i][t];
                                U[i][t] = matrix.matrix2[nonzero][t];
                            }
                            flag++;
                            for (m = i + 1; m < order; m++) {
                                if (matrix.matrix2[m][j].u != 0){
                                    UD temp;
                                    for (n = j + 1; n < order; n++) {
                                        temp = UD.multiply(U[i][n],U[m][j]);
                                        temp = UD.division(temp,U[i][j]);
                                        U[m][n] = UD.less(U[m][n],temp);
                                        //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    }
                                    U[m][j]=new UD();
                                }
                            }
                            i++; j++;
                        }
                    }
                    for (i = 0; i < order; i++) {
                        value_of_determinant = UD.multiply(value_of_determinant,U[i][i]);
                    }
                    //printf("本次计算行列式一共进行了%d次行交换", flag);
                    if (flag % 2 == 1) value_of_determinant=value_of_determinant.reverse();
                    return new Matrix(value_of_determinant);
                }
                case 3: {
                    //一元矩阵
                    //按照化阶梯型的方法求行列式
                    int i, j, s, t, nonzero, m, n, flag = 0;
                    int order = matrix.line;
                    ATomUD value_of_determinant = new ATomUD("1");
                    ATomUD [][] U = new ATomUD[order][order];
                    for (i=0;i<order;i++){
                        for (j=0;j<order;j++){
                            U[i][j] = ATomUD.copy(matrix.matrix3[i][j]);
                            if (U[i][j].AD.type == 10){
                                //出现有分母为0
                                Matrix M = new Matrix();
                                M.BasicType = 0;
                                M.ErrorType = 1;
                                return M;
                            }
                        }
                    }
                    i = 0; j = 0;
                    for (s = 0; s < order - 1; s++) {
                        nonzero = Looking(U, i, j, order);
                        if (nonzero == -1) {
                            i++; j++;
                        }
                        else if (nonzero == i) {
                            for (m = i + 1; m < order; m++) {
                                if (U[m][j].type != 0&&U[m][j].type != 10){
                                    for (n = j + 1; n < order; n++) {
                                        ATomUD temp = ATomUD.multiply(U[i][n],U[m][j]);
                                        temp = ATomUD.division(temp,U[i][j]);
                                        U[m][n] = ATomUD.less(U[m][n],temp);
                                        //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    }
                                    U[m][j]=new ATomUD("0");
                                }
                            }
                            i++; j++;
                        }
                        else {
                            for (t = j; t < order; t++) {
                                U[nonzero][t] = matrix.matrix3[i][t];
                                U[i][t] = matrix.matrix3[nonzero][t];
                            }
                            flag++;
                            for (m = i + 1; m < order; m++) {
                                if (matrix.matrix2[m][j].u != 0){
                                    for (n = j + 1; n < order; n++) {
                                        ATomUD temp = ATomUD.multiply(U[i][n],U[m][j]);
                                        temp = ATomUD.division(temp,U[i][j]);
                                        U[m][n] = ATomUD.less(U[m][n],temp);
                                        //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    }
                                    U[m][j]=new ATomUD("0");
                                }
                            }
                            i++; j++;
                        }
                    }
                    for (i = 0; i < order; i++) {
                        value_of_determinant = ATomUD.multiply(value_of_determinant,U[i][i]);
                    }
                    //printf("本次计算行列式一共进行了%d次行交换", flag);
                    if (flag % 2 == 1) value_of_determinant=value_of_determinant.reverse();
                    return new Matrix(value_of_determinant);
                }
                default:{
                    Matrix M = new Matrix();
                    M.BasicType = 0;
                    M.ErrorType = 6;
                    return M;
                }
            }
        }
        else{
            Matrix M = new Matrix();
            M.BasicType = 0;
            M.ErrorType = 43;
            return M;
        }
    }

    public static Matrix inv(Matrix matrix) {
        int line = matrix.line;
        int column = matrix.column;
        if (line == column){
            switch (matrix.BasicType){
                case 0: {
                    //异常矩阵，原地打回
                    return matrix;
                }
                case 1: {
                    //小数型矩阵
                    //经测试，利用初等变换的方法求取小数矩阵的逆矩阵的误差是很大的，现利用LU分解求逆 -- 2021.2.4
                    int i, j, s,line_matrix, column_matrix;
                    line_matrix = line; column_matrix = 2 * line;
                    double [][] U = new double[line_matrix][column_matrix];
                    for (i = 0; i < line_matrix; i++) {
                        for (j=0; j< line; j++){
                            U[i][j]=matrix.matrix1[i][j];
                        }
                        for (j = line; j < column_matrix; j++) {
                            U[i][j] = 0;
                        }
                        U[i][i + line] = 1;
                    }//在原来矩阵的右边加上一个单位矩阵
                    Matrix M1 = new Matrix(U);
                    M1 = M1.rowEchelon();//先将增加了一个单位矩阵的原矩阵化为上三角形
                    for (i = line - 1; i >= 0; i--) {
                        if (M1.matrix1[i][i] != 0) {
                            for (j = i + 1; j < column_matrix; j++) {
                                M1.matrix1[i][j]=M1.matrix1[i][j]/M1.matrix1[i][i];
                            }
                            M1.matrix1[i][i] = 1;
                        }
                        else {
                            //方阵是奇异的，无法求逆，为什么？主对角线上出现了0，那么矩阵就一定是奇异的
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 5;
                            return M;
                        }
                    }
                    //至此 将原矩阵的左边的主对角线元素变为1
                    for (s = line - 1; s > 0; s--) {
                        for (i = s - 1; i >= 0; i--) {
                            if (M1.matrix1[i][s] != 0){
                                double temp = 0;
                                for (j = s + 1; j < column_matrix; j++) {
                                    temp = -1*M1.matrix1[i][s]*M1.matrix1[s][j];
                                    M1.matrix1[i][j]=temp+M1.matrix1[i][j];
                                }//这里要表达的逻辑是 matrix[i][j] = matrix[s][j] * (-matrix[i][s]) + matrix[i][j];
                                M1.matrix1[i][s] = 0;//虽然这一句写不写都一样，但我还是觉得写了好
                            }
                        }
                    }
                    double [][] U1 = new double[line][line];
                    for (i = 0; i < line; i++) {
                        for (j = line; j < column_matrix; j++) {
                            U1[i][j - line] = M1.matrix1[i][j];
                        }
                    }
                    return new Matrix(U1);
                }
                case 2: {
                    //分数型矩阵
                    int i, j, s,line_matrix, column_matrix;
                    line_matrix = line; column_matrix = 2 * line;
                    UD [][] U = new UD[line_matrix][column_matrix];
                    for (i = 0; i < line_matrix; i++) {
                        for (j=0; j< line; j++){
                            U[i][j]=matrix.matrix2[i][j];
                            if (U[i][j].d == 0){
                                //出现有分母为0
                                Matrix M = new Matrix();
                                M.BasicType = 0;
                                M.ErrorType = 1;
                                return M;
                            }
                        }
                        for (j = line; j < column_matrix; j++) {
                            U[i][j] = new UD();
                        }
                        U[i][i + line] = new UD(1,1);
                    }//在原来矩阵的右边加上一个单位矩阵
                    Matrix M1 = new Matrix(U);
                    M1 = M1.rowEchelon();//先将增加了一个单位矩阵的原矩阵化为上三角形
                    for (i = line - 1; i >= 0; i--) {
                        if (M1.matrix2[i][i].u != 0) {
                            for (j = i + 1; j < column_matrix; j++) {
                                M1.matrix2[i][j]=UD.division(M1.matrix2[i][j],M1.matrix2[i][i]);
                            }
                            M1.matrix2[i][i] = new UD(1,1);
                        }
                        else {
                            //方阵是奇异的，无法求逆
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 5;
                            return M;
                        }
                    }
                    //至此 将原矩阵的左边的主对角线元素变为1
                    for (s = line - 1; s > 0; s--) {
                        for (i = s - 1; i >= 0; i--) {
                            if (M1.matrix2[i][s].u != 0){
                                UD temp;
                                for (j = s + 1; j < column_matrix; j++) {
                                    temp = M1.matrix2[i][s].reverse();
                                    temp = UD.multiply(M1.matrix2[s][j],temp);
                                    M1.matrix2[i][j]=UD.add(temp,M1.matrix2[i][j]);
                                }//这里要表达的逻辑是 matrix[i][j] = matrix[s][j] * (-matrix[i][s]) + matrix[i][j];
                                M1.matrix2[i][s] = new UD();//虽然这一句写不写都一样，但我还是觉得写了好
                            }
                        }
                    }
                    UD [][] U1 = new UD[line][line];
                    for (i = 0; i < line; i++) {
                        for (j = line; j < column_matrix; j++) {
                            U1[i][j - line] = M1.matrix2[i][j];
                        }
                    }
                    return new Matrix(U1);
                }
                case 3: {
                    //一元矩阵
                    int i, j, s,line_matrix, column_matrix;
                    line_matrix = line; column_matrix = 2 * line;
                    ATomUD [][] U = new ATomUD[line_matrix][column_matrix];
                    for (i = 0; i < line_matrix; i++) {
                        for (j=0; j< line; j++){
                            U[i][j]=ATomUD.copy(matrix.matrix3[i][j]);
                            if (U[i][j].AD.type == 10){
                                //出现有分母为0
                                Matrix M = new Matrix();
                                M.BasicType = 0;
                                M.ErrorType = 1;
                                return M;
                            }
                        }
                        for (j = line; j < column_matrix; j++) {
                            U[i][j] = new ATomUD("0");
                        }
                        U[i][i + line] = new ATomUD("1");
                    }//在原来矩阵的右边加上一个单位矩阵
                    Matrix M1 = new Matrix(U);
                    //System.out.print("\n添加了单位矩阵后的M1 = \n");
                    //M1.print();
                    M1 = M1.rowEchelon();//先将增加了一个单位矩阵的原矩阵化为上三角形
                    //System.out.print("\n化为了阶梯型后 M1 = \n");
                    //M1.print();
                    for (i = line - 1; i >= 0; i--) {
                        if (M1.matrix3[i][i].type != 10) {
                            for (j = i + 1; j < column_matrix; j++) {
                                M1.matrix3[i][j]=ATomUD.division(M1.matrix3[i][j],M1.matrix3[i][i]);
                            }
                            M1.matrix3[i][i] = new ATomUD("1");
                        }
                        else {
                            //方阵是奇异的，无法求逆
                            Matrix M = new Matrix();
                            M.BasicType = 0;
                            M.ErrorType = 5;
                            return M;
                        }
                    }
                    //至此 将原矩阵的左边的主对角线元素变为1
                    //System.out.print("\n将左边的主对角线元素变为1后 \n");
                    //M1.print();
                    for (s = line - 1; s > 0; s--) {
                        for (i = s - 1; i >= 0; i--) {
                            if (U[i][s].type != 10){
                                for (j = s + 1; j < column_matrix; j++) {
                                    ATomUD temp = M1.matrix3[i][s].reverse();
                                    temp = ATomUD.multiply(M1.matrix3[s][j],temp);
                                    M1.matrix3[i][j]=ATomUD.add(temp,M1.matrix3[i][j]);
                                }//这里要表达的逻辑是 matrix[i][j] = matrix[s][j] * (-matrix[i][s]) + matrix[i][j];
                                M1.matrix3[i][s] = new ATomUD("0");//虽然这一句写不写都一样，但我还是觉得写了好
                            }
                        }
                        /*System.out.println();
                        M1.print();
                        System.out.println();*/
                    }
                    ATomUD [][] U1 = new ATomUD[line][line];
                    for (i = 0; i < line; i++) {
                        for (j = line; j < column_matrix; j++) {
                            U1[i][j - line] = M1.matrix3[i][j];
                        }
                    }
                    return new Matrix(U1);
                }
                default: {
                    //不支持的运算类型
                    Matrix M = new Matrix();
                    M.BasicType = 0;
                    M.ErrorType = 6;
                    return M;
                }
            }
        }
        else{
            //非方阵无法求逆矩阵
            Matrix M = new Matrix();
            M.BasicType = 0;
            M.ErrorType = 41;
            return M;
        }
    }

    public static Matrix trans(Matrix matrix) {
        int line = matrix.line;
        int column = matrix.column;
        switch (matrix.BasicType){
            case 0: {
                //异常矩阵，原地打回
                return matrix;
            }
            case 1:{
                //小数型矩阵
                double [][] U = new double[column][line];
                int i,j;
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[j][i] = matrix.matrix1[i][j];
                    }
                }
                return new Matrix(U);
            }
            case 2:{
                //分数型矩阵
                UD [][] U = new UD[column][line];
                int i,j;
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[j][i] = matrix.matrix2[i][j];
                    }
                }
                return new Matrix(U);
            }
            case 3:{
                //一元矩阵
                ATomUD [][] U = new ATomUD[column][line];
                int i,j;
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[j][i] = matrix.matrix3[i][j];
                    }
                }
                return new Matrix(U);
            }
            case 4:{
                //多元矩阵
                ATomsUD [][] U = new ATomsUD[column][line];
                int i,j;
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[j][i] = matrix.matrix4[i][j];
                    }
                }
                return new Matrix(U);
            }
            default:{
                System.out.println("暂不支持的运算");
                return new Matrix();
            }
        }
    }

    /**此方法用于求矩阵matrix的阶梯型*/
    public static Matrix rowEchelon(Matrix matrix) {
        switch (matrix.BasicType){
            case 0: {
                //异常矩阵，原地打回
                return matrix;
            }
            case 1:{
                //小数型矩阵
                int i, j, m, n, s, t, nonzero;
                double temp;
                int line = matrix.line;
                int column = matrix.column;
                double [][] U = matrix.matrix1;
                i=0;
                j=0;
                for (s = 0; s < line - 1&&j<column; s++) {
                    nonzero = Looking(U, i, j, line);
                    if (nonzero == -1) {
                        i++; j++;
                    }
                    else if (nonzero == i) {
                        for (m = i + 1; m < line; m++) {
                            if (U[m][j] != 0){
                                for (n = j + 1; n < column; n++) {
                                    U[m][n] = U[m][n] - U[i][n] * U[m][j] / U[i][j];
                                }
                                U[m][j] = 0;
                            }
                        }
                        i++; j++;
                    }
                    else {
                        for (t = j; t < column; t++) {
                            temp = U[nonzero][t];
                            U[nonzero][t] = U[i][t];
                            U[i][t] = temp;
                        }
                        for (m = i + 1; m < line; m++) {
                            if (U[m][j] != 0){
                                for (n = j + 1; n < column; n++) {
                                    U[m][n] = U[m][n] - U[i][n] * U[m][j] / U[i][j];
                                }
                            }
                            U[m][j] = 0;
                        }
                    }
                    i++; j++;
                }
                return new Matrix(U);
            }
            case 2:{
                //分数型矩阵
                int i, j, m, n, s, t, nonzero;
                int line = matrix.line;
                int column = matrix.column;
                UD [][] U = new UD[line][column];
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[i][j] = new UD(matrix.matrix2[i][j].u,matrix.matrix2[i][j].d);
                        //U[i][j] = matrix.matrix2[i][j];
                    }
                }
                i=0;
                j=0;
                for (s = 0; s < line - 1&&j<column; s++) {
                    nonzero = Looking(U, i, j, line);
                    if (nonzero == -1) {
                        i++;
                        j++;
                    }
                    else if (nonzero == i) {
                        for (m = i + 1; m < line; m++) {
                            if (U[m][j].u != 0){
                                UD temp;
                                for (n = j + 1; n < column; n++) {
                                    //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    temp = UD.multiply(U[i][n],U[m][j]);
                                    temp = UD.division(temp,U[i][j]);
                                    U[m][n] = UD.less(U[m][n],temp);
                                }
                                U[m][j]=new UD();
                            }
                        }
                        i++;
                        j++;
                    }
                    else {
                        for (t = j; t < column; t++) {
                            U[nonzero][t] = UD.copy(matrix.matrix2[i][t]);
                            U[i][t] = UD.copy(matrix.matrix2[nonzero][t]);
                        }
                        for (m = i + 1; m < line; m++) {
                            if (U[m][j].u != 0){
                                UD temp;
                                for (n = j + 1; n < column; n++) {
                                    temp = UD.multiply(U[i][n],U[m][j]);
                                    temp = UD.division(temp,U[i][j]);
                                    U[m][n] = UD.less(U[m][n],temp);
                                    //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                }
                                U[m][j]=new UD();
                            }
                        }
                        i++;
                        j++;
                    }
                }
                return new Matrix(U);
            }
            case 3:{
                //一元矩阵
                int i, j, m, n, s, t, nonzero;
                int line = matrix.line;
                int column = matrix.column;
                ATomUD [][] U = new ATomUD[line][column];
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[i][j] = ATomUD.copy(matrix.matrix3[i][j]);
                    }
                }
                /*System.out.println("\n展示U矩阵");
                for (i=0;i<line;i++){
                    for (j=0;j<column;j++){
                        U[i][j].print();
                        System.out.print("\t");
                    }
                   System.out.print("\n");
                }
                System.out.println("\n展示完毕");*/
                i=0;
                j=0;
                for (s = 0; s < line - 1&&j<column; s++) {
                    nonzero = Looking(U, i, j, line);
                    if (nonzero == -1) {
                        i++;
                        j++;
                    }
                    else if (nonzero == i) {
                        for (m = i + 1; m < line; m++) {
                            ATomUD temp = ATomUD.division(U[m][j],U[i][j]);
                            //System.out.print("\ntemp = ");
                            //temp.print();
                            //System.out.println();
                            if (U[m][j].type!=0&&U[m][j].type != 10){
                                for (n = j + 1; n < column; n++) {
                                    //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    ATomUD temp1 = ATomUD.multiply(U[i][n],temp);
                                    //System.out.print("\nless方法查错\n");
                                    //U[m][n].print();
                                    //System.out.print("-");
                                    //temp1.print();
                                    //System.out.print("=");
                                    U[m][n] = ATomUD.less(U[m][n],temp1);
                                    //U[m][n].print();
                                    //System.out.println();
                                    U[m][n] = U[m][n].check();
                                }
                                U[m][j].AU = new ATom(0);
                                U[m][j].AD = new ATom(1);
                                /*System.out.println();
                                for (int ii= 0 ;ii<line;ii++){
                                    for (int jj = 0;jj<column;jj++){
                                        U[ii][jj].print();
                                        System.out.print("\t");
                                    }
                                    System.out.println();
                                }
                                System.out.println();*/
                            }
                        }
                        i++;
                        j++;
                    }
                    else {
                        for (t = j; t < column; t++) {
                            U[nonzero][t] = ATomUD.copy(matrix.matrix3[i][t]);
                            U[i][t] = ATomUD.copy(matrix.matrix3[nonzero][t]);
                        }
                        for (m = i + 1; m < line; m++) {
                            if (U[m][j].type!=0&&U[m][j].type != 10) {
                                for (n = j + 1; n < column; n++) {
                                    ATomUD temp = ATomUD.multiply(U[i][n],U[m][j]);
                                    ATomUD temp1 = ATomUD.division(temp,U[i][j]);
                                    U[m][n] = ATomUD.less(U[m][n],temp1);
                                    //m[m][n]=m[m][n]-m[i][n]*m[m][j]/m[i][j];
                                    U[m][n] = U[m][n].check();
                                }
                                U[m][j].AU = new ATom(0);
                                U[m][j].AD = new ATom(1);
                                /*System.out.println();
                                for (int ii= 0 ;ii<line;ii++){
                                    for (int jj = 0;jj<column;jj++){
                                        U[ii][jj].print();
                                        System.out.print("\t");
                                    }
                                    System.out.println();
                                }
                                System.out.println();*/
                            }
                        }
                        i++;
                        j++;
                    }
                }
                return new Matrix(U);
            }
            case 4:{
                //多元矩阵
            }
            default:{
                System.out.println("暂不支持的运算");
                return new Matrix();
            }
        }
    }

    /**获取本对象的正定性，目前也只支持对分数矩阵进行判断--2021.3.17
     * 现在可以支持小数矩阵了 -- 2021.4.1
     * 由于要做多语言版本，需要修改本函数的返回值，修改前，返回的是一个字符串变量，现在改成返回一个长度为2的字符串数组，下标为0存储的正交变换的类别
     * 类别为0 该矩阵是正定的
     * 类别为1 该矩阵是负定的
     * 类别为2 恕我才疏学浅，我也不知道顺序主子式全为0的矩阵是个啥，去问老师吧
     * 类别为3 该矩阵是半正定的
     * 类别为4 该矩阵是半负定的
     * 类别为5 该矩阵是不定的
     * 然后，下标为1存储的是顺序主子式的值 -- 2021.4.12*/
    public static String[] positive(Matrix matrix) {
        switch (matrix.BasicType){
            case 1:{
                String S1 = "";
                if (matrix.line!=0){
                    if (matrix.line==matrix.column){
                        int n = matrix.line;
                        Matrix[] D = new Matrix[n];
                        int i,j,t;
                        for (t = 1; t <= n; t++) {
                            double[][] m = new double[t][t];
                            Matrix M;
                            for (i = 0; i < t; i++) {
                                for (j = 0; j < t; j++) {
                                    m[i][j] = matrix.matrix1[i][j];
                                }
                            }
                            M = new Matrix(m);
                            D[t-1]=det(M);
                        }
                        int x = 0,y = 0;i=0;j=0;
                        for (t = 1; t <= n; t = t + 2) {
                            if (D[t-1].BasicType==1) {
                                if (D[t - 1].matrix1[0][0] > 0) j++;
                                if (D[t - 1].matrix1[0][0] < 0) x++;
                                if (D[t - 1].matrix1[0][0] == 0) y++;
                            }
                            else {
                                //return "行列式求值发生错误";
                                return new String[]{"行列式求值发生错误",""};
                            }
                        }
                        for (t = 2; t <= n; t = t + 2) {
                            if (D[t - 1].matrix1[0][0] > 0) i++;
                            if (D[t - 1].matrix1[0][0] == 0) y++;
                        }
                        //String S = "正定的：顺序主子式全大于零\n负定的：奇数阶顺序主子式小于零，偶数阶顺序主子式大于零\n半正(负)定的：顺序主子式大（小）于等于零\n不定的：既不是半正定也不是半负定\n";
                        //String S = "";
                        //S = S + "该矩阵的顺序主子式从1阶到" + n + "阶依次为：\n";
                        for (int ii = 0;ii<n;ii++){
                            S1 = S1 + D[ii].matrix1[0][0] + "\t";
                        }
                        //S = S + "\n所以，";
                        if (i + j == n) {
                            //return S + "该矩阵是正定的";
                            return new String[]{"0",S1};
                        }
                        else if (x + i == n) {
                            //return S + "该矩阵是负定的";
                            return new String[]{"1",S1};
                        }
                        else if (y == n) {
                            //return S + "恕我才疏学浅，我也不知道顺序主子式全为0的矩阵是个啥，去问老师吧";
                            return new String[]{"2",S1};
                        }
                        else if (i + j + y == n) {
                            //return S + "该矩阵是半正定的";
                            return new String[]{"3",S1};
                        }
                        else if (x + y + i == n) {
                            //return S + "该矩阵是半负定的";
                            return new String[]{"4",S1};
                        }
                        else {
                            //return S + "该矩阵是不定的";
                            return new String[]{"5",S1};
                        }
                    }
                    else {
                        //return "请输入方阵";
                        return new String[]{"请输入方阵",""};
                    }
                }
                else {
                    //return "数据错误";
                    return new String[]{"数据错误",""};
                }
            }
            case 2:{
                //分数型矩阵
                if (matrix.line!=0){
                    if (matrix.line==matrix.column){
                        int n = matrix.line;
                        Matrix[] D = new Matrix[n];
                        int i,j,t;
                        for (t = 1; t <= n; t++) {
                            UD[][] m = new UD[t][t];
                            Matrix M;
                            for (i = 0; i < t; i++) {
                                for (j = 0; j < t; j++) {
                                    m[i][j] = UD.copy(matrix.matrix2[i][j]);
                                }
                            }
                            M = new Matrix(m);
                            D[t-1]=det(M);
                        }
                        int x = 0,y = 0;i=0;j=0;
                        for (t = 1; t <= n; t = t + 2) {
                            if (D[t-1].BasicType==2) {
                                if ((D[t - 1].matrix2[0][0].u * D[t - 1].matrix2[0][0].d) > 0) j++;
                                if (D[t - 1].matrix2[0][0].u * D[t - 1].matrix2[0][0].d < 0) x++;
                                if (D[t - 1].matrix2[0][0].u == 0) y++;
                            }
                            else {
                                //return "行列式求值发生错误";
                                return new String[]{"行列式求值发生错误",""};
                            }
                        }
                        for (t = 2; t <= n; t = t + 2) {
                            if ((D[t - 1].matrix2[0][0].u * D[t - 1].matrix2[0][0].d) > 0) i++;
                            if (D[t - 1].matrix2[0][0].u == 0) y++;
                        }
                        //String S = "正定的：顺序主子式全大于零\n负定的：奇数阶顺序主子式小于零，偶数阶顺序主子式大于零\n半正(负)定的：顺序主子式大（小）于等于零\n不定的：既不是半正定也不是半负定\n";
                        //S = S + "该矩阵的顺序主子式从1阶到" + n + "阶依次为：\n";
                        String S1 = "";
                        for (int ii = 0;ii<n;ii++){
                            S1 = S1 + UD.UDToString(D[ii].matrix2[0][0]) + "\t";
                        }
                        //S = S + "\n所以，";
                        if (i + j == n) {
                            //return S + "该矩阵是正定的"
                            return new String[]{"0",S1};
                        }
                        else if (x + i == n) {
                            //return S + "该矩阵是负定的";
                            return new String[]{"1",S1};
                        }
                        else if (y == n) {
                            //return S + "恕我才疏学浅，我也不知道顺序主子式全为0的矩阵是个啥，去问老师吧";
                            return new String[]{"2",S1};
                        }
                        else if (i + j + y == n) {
                            //return S + "该矩阵是半正定的";
                            return new String[]{"3",S1};
                        }
                        else if (x + y + i == n) {
                            //return S + "该矩阵是半负定的";
                            return new String[]{"4",S1};
                        }
                        else {
                            //return S + "该矩阵是不定的";
                            return new String[]{"5",S1};
                        }
                    }
                    else {
                        //return "请输入方阵";
                        return new String[]{"请输入方阵",""};
                    }
                }
                else {
                    //return "数据错误";
                    return new String[]{"数据错误",""};
                }
            }
            default:{
                //return "暂不支持的运算";
                return new String[]{"暂不支持的运算",""};
            }
        }
    }

    /**本方法用于获取正交变换的类型，目前仅支持对分数矩阵进行判断--2021.3.17
     * 现在支持小数矩阵了 -- 2021.4.1
     * 由于要做多语言版本，需要修改本函数的返回值，修改前，返回的是一个字符串变量，现在改成返回一个长度为2的字符串数组，下标为0存储的正交变换的类别*/
    public static String[] orthogonal(Matrix matrix) {
        switch (matrix.BasicType){
            case 1:{
                //小数型矩阵
                if (matrix.line!=0) {
                    Matrix m1 = trans(matrix);//获取原矩阵的转置
                    Matrix m2 = multiply(matrix, m1);//获取原矩阵与它转置的乘积
                    if (m2.line!=0){
                        if (m2.line==m2.column){
                            //获取该矩阵的行列式，若为1则为第一种正交变换，也称为旋转，为-1为第二种正交变换，也称为镜面反射，为其他数则不是正交变换
                            Matrix m3 = det(m2);
                            if (m3.BasicType == 1) {
                                if (m3.matrix1[0][0] == 1) {
                                    //行列式为1，
                                    return new String[]{"该矩阵与其转置的乘积为1，属于第一种正交变换，也称为旋转",""};
                                } else if (m3.matrix1[0][0] == -1) {
                                    return new String[]{"该矩阵与其转置的乘积为-1，属于第二种正交变换，也称为镜面反射",""};
                                } else {
                                    return new String[]{"这不是正交变换，其与其转置的乘积的行列式为" , MathCal.abandonZero(m3.matrix1[0][0])};
                                }
                            }
                        }
                        else {
                            return new String[]{"出现错误，不能判断正交变换的类型",""};
                        }
                    }
                    else {
                        return new String[]{"发生错误",""};
                    }
                }
                else {
                    return new String[]{"数据错误",""};
                }
            }
            case 2:{
                //分数型矩阵
                if (matrix.line!=0) {
                    Matrix m1 = trans(matrix);//获取原矩阵的转置
                    Matrix m2 = multiply(matrix, m1);//获取原矩阵与它转置的乘积
                    if (m2.line!=0){
                        if (m2.line==m2.column){
                            //获取该矩阵的行列式，若为1则为第一种正交变换，也称为旋转，为-1为第二种正交变换，也称为镜面反射，为其他数则不是正交变换
                            Matrix m3 = det(m2);
                            if (m3.BasicType == 2) {
                                if (m3.matrix2[0][0].u == 1 && m3.matrix2[0][0].d == 1) {
                                    //行列式为1，
                                    return new String[]{"该矩阵与其转置的乘积为1，属于第一种正交变换，也称为旋转",""};
                                } else if (m3.matrix2[0][0].u * m3.matrix2[0][0].d == -1) {
                                    return new String[]{"该矩阵与其转置的乘积为-1，属于第二种正交变换，也称为镜面反射",""};
                                } else {
                                    return new String[]{"这不是正交变换，其与其转置的乘积的行列式为", UD.UDToString(m3.matrix2[0][0])};
                                }
                            }
                        }
                        else {
                            return new String[]{"出现错误，不能判断正交变换的类型",""};
                        }
                    }
                    else {
                        return new String[]{"发生错误",""};
                    }
                }
                else {
                    return new String[]{"数据错误",""};
                }
            }
            default:{
                return new String[]{"暂不支持的运算",""};
            }
        }
    }

    public static Matrix eigen(Matrix matrix) { return new Matrix(); }

    public static Matrix eigenVector(Matrix matrix) { return new Matrix(); }

    public static Matrix power(Matrix matrix, int pow) {
        int line = matrix.line;
        int column = matrix.column;
        //先看看这个矩阵是不是异常
        if (matrix.BasicType==0){
            //这是一个异常矩阵，原地打回
            return matrix;
        }
        else {
            if (pow < 0) {
                //指数是负？
                Matrix M = new Matrix();
                M.BasicType = 0;
                M.ErrorType = 32;
                return M;
            }
            else if (pow == 1){
                return matrix;
            }
            else {
                if (line == column) {
                    if (pow == 0) {
                        //方阵的0次方是单位阵
                        switch (matrix.BasicType) {
                            case 1: {
                                int i, j;
                                double[][] U = new double[line][column];
                                //构建单位矩阵
                                for (i = 0; i < line; i++) {
                                    for (j = 0; j < column; j++) {
                                        if (i == j) U[i][j] = 1;
                                        else U[i][j] = 0;
                                    }
                                }
                                //构建完成
                                return new Matrix(U);
                            }
                            case 2:
                            case 3:
                            case 4:{
                                int i, j;
                                UD[][] U = new UD[line][column];
                                //构建单位矩阵
                                for (i = 0; i < line; i++) {
                                    for (j = 0; j < column; j++) {
                                        if (i == j) U[i][j] = new UD(1, 1);
                                        else U[i][j] = new UD();
                                    }
                                }
                                //构建完成
                                return new Matrix(U);
                            }
                            default: {
                                //不支持的运算类型
                                Matrix M = new Matrix();
                                M.BasicType = 0;
                                M.ErrorType = 6;
                                return M;
                            }
                        }
                    }
                    else if (pow == 1) {
                        return matrix;
                    }
                    else {
                        //至少两次
                        Matrix M = Matrix.multiply(matrix, matrix);
                        //判断一下，这个M是不是异常矩阵
                        if (M.BasicType == 0) {
                            //一般来说，这个地方是出现了分母为0
                            return M;
                        } else {
                            if (pow == 2) {
                                return M;
                            } else {
                                //开始正文 pow>=3
                                int x = 0;
                                while (x < (pow - 2)) {
                                    M = Matrix.multiply(matrix, M);
                                    x++;
                                }
                                return M;
                            }
                        }
                    }
                }
                else {
                    //非方阵
                    Matrix M = new Matrix();
                    M.BasicType = 0;
                    M.ErrorType = 42;
                    return M;
                }
            }
        }
    }

    public static Matrix add(Matrix matrixA, Matrix matrixB) {
        int LA = matrixA.line, CA = matrixA.column;
        int LB = matrixB.line, CB = matrixB.column;
        int i, j;
        if (LA == LB && CA == CB) {
            switch (matrixA.BasicType) {
                case 1: {
                    //A矩阵是double型的
                    switch (matrixB.BasicType) {
                        case 1: {
                            double[][] C = new double[LA][CB];
                            //B矩阵也是double型的
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] += matrixA.matrix1[i][j] + matrixB.matrix1[i][j];
                                }
                            }
                            return new Matrix(C);
                        }
                        case 2: {
                            //B矩阵是UD型的
                            //要先判断，看看是把小数矩阵化为分数矩阵，还是说把分数矩阵化为小数矩阵
                            long[][] C = new long[LA][CB];

                            return new Matrix(C);
                        }
                        case 3: {
                            //B矩阵是ATomUD型的，要先把A矩阵化为ATomUD型，再相加
                            ATomUD[][] C = new ATomUD[LA][CB];


                            return new Matrix(C);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LA][CB];

                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }

                    }
                }
                case 2: {
                    //A矩阵是UD型的
                    switch (matrixB.BasicType) {
                        case 1: {
                            return Matrix.add(matrixB, matrixA);
                        }
                        case 2: {
                            //B矩阵是UD型的
                            UD[][] C = new UD[LA][CB];
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = UD.add(matrixA.matrix2[i][j], matrixB.matrix2[i][j]);
                                }
                            }
                            return new Matrix(C);
                        }
                        case 3: {
                            //B矩阵是ATomUD型的
                            ATomUD[][] C = new ATomUD[LA][CB];

                            return new Matrix(C);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LA][CB];

                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                case 3: {
                    //A矩阵是ATomUD型的
                    switch (matrixB.BasicType) {
                        case 1:
                        case 2: {
                            return Matrix.add(matrixB, matrixA);
                        }
                        case 3: {
                            //B矩阵是ATomUD型的
                            ATomUD[][] C = new ATomUD[LA][CB];
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = ATomUD.add(matrixA.matrix3[i][j], matrixB.matrix3[i][j]);
                                }
                            }
                            return new Matrix(C);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的，先对A矩阵进行类型提升
                            ATomsUD[][] C = new ATomsUD[LA][CB];

                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                case 4: {
                    //A矩阵是ATomUD型的
                    switch (matrixB.BasicType) {
                        case 1:
                        case 2:
                        case 3: {
                            return Matrix.add(matrixB, matrixA);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LA][CB];
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = ATomsUD.add(matrixA.matrix4[i][j], matrixB.matrix4[i][j]);
                                }
                            }
                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                default: {
                    //暂不支持的运算
                    Matrix matrix = new Matrix();
                    matrix.BasicType = 0;
                    matrix.ErrorType = 6;
                    return matrix;
                }
            }
        }
        else {
            //矩阵维度不一致
            System.out.printf("\n第一个矩阵的行数为%d,列数为%d\n", LA, CA);
            System.out.printf("\n第二个矩阵的行数为%d,列数为%d\n", LB, CB);
            Matrix matrix = new Matrix();
            matrix.BasicType = 0;
            matrix.ErrorType = 21;
            return matrix;
        }
    }

    public static Matrix less(Matrix matrixA, Matrix matrixB) {
        Matrix m = new Matrix();
        m = matrixB.reverse();
        m = Matrix.add(matrixA, m);
        return m;
    }

    public static Matrix multiply(Matrix matrixA, Matrix matrixB) {
        int LA = matrixA.line, CA = matrixA.column;
        int LB = matrixB.line, CB = matrixB.column;
        int i, j, c, d;
        if (LA == 1 && CA == LA) {
            //即A矩阵是一个单对象
            //那么就把这个对象挨个乘上B矩阵的所有元素了
            switch (matrixA.BasicType) {
                case 0: {
                    //异常矩阵，原地打回
                    return matrixA;
                }
                case 1: {
                    //A矩阵是double型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 1: {
                            //B矩阵也是double型的
                            double [][] C = new double[LB][CB];
                            double A = matrixA.matrix1[0][0];
                            for (i = 0; i < LB; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = A * matrixB.matrix1[i][j];
                                }
                            }
                            return new Matrix(C);
                        }
                        default: {
                            Matrix m = new Matrix();
                            m.BasicType = 0;
                            m.SecondType = 6;//不支持的运算
                            return m;
                        }
                    }
                }
                case 2: {
                    //A矩阵是UD型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 1:
                        case 2: {
                            //B矩阵是UD型的
                            UD[][] C = new UD[LB][CB];
                            for (i = 0; i < LB; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = new UD();
                                }
                            }
                            UD A = matrixA.matrix2[0][0];
                            for (c = 0; c < LB; c++) {
                                for (d = 0; d < CB; d++) {
                                    //这里要判断分母为0
                                    if (A.d == 0 || matrixB.matrix2[c][d].d == 0) {
                                        //出现分母为0
                                        Matrix M = new Matrix();
                                        M.BasicType = 0;
                                        M.ErrorType = 1;
                                        return M;
                                    }
                                    else {
                                        C[c][d] = UD.multiply(A, matrixB.matrix2[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        case 3: {
                            //B矩阵是一元矩阵
                            //先将分数矩阵提升为一元矩阵
                            ATomUD A = ATomUD.UDToATomUD(matrixA.matrix2[0][0]);
                            ATomUD[][] C  =new ATomUD[LA][CB];
                            for (c = 0; c < LB; c++) {
                                for (d = 0; d < CB; d++) {
                                    C[c][d] = ATomUD.multiply(A, matrixB.matrix3[c][d]);
                                }
                            }
                            return new Matrix(C);
                        }
                        /*case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LB][CB];
                            AToms u = new AToms(matrixA.matrix2[0][0]);
                            ATomsUD U = new ATomsUD(u);
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = new ATomsUD(0);
                                }
                            }
                            for (c = 0; c < LB; c++) {
                                for (d = 0; d < CB; d++) {
                                    //这里要判断分母为0
                                    if (matrixA.matrix2[0][0].d == 0) {
                                        //出现分母为0
                                        Matrix M = new Matrix();
                                        M.BasicType = 0;
                                        M.ErrorType = 1;
                                        return M;
                                    }
                                    else {
                                        C[c][d] = ATomsUD.multiply(U,matrixB.matrix4[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }*/
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                case 3: {
                    //A矩阵是ATomUD型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 2:{
                            //B矩阵是UD型的
                            ATomUD[][] B = new ATomUD[LB][CB];
                            ATomUD[][] C = new ATomUD[LB][CB];
                            for (i = 0;i<LB;i++){
                                for (j = 0;j<CB;j++){
                                    B[i][j] = ATomUD.UDToATomUD(matrixB.matrix2[i][j]);
                                    C[i][j] = new ATomUD("0");
                                }
                            }
                            for (c = 0; c < LB; c++) {
                                for (d = 0; d < CB; d++) {
                                    C[c][d] = ATomUD.multiply(matrixA.matrix3[0][0], B[c][d]);
                                }
                            }
                            return new Matrix(C);
                        }
                        case 3: {
                            //B矩阵是ATomUD型的
                            ATomUD[][] C = new ATomUD[LB][CB];
                            for (c = 0; c < LB; c++) {
                                for (d = 0; d < CB; d++) {
                                    C[c][d] = ATomUD.multiply(matrixA.matrix3[0][0], matrixB.matrix3[c][d]);
                                }
                            }
                            return new Matrix(C);
                        }
                        //B矩阵是ATomsUD型的，先对A矩阵进行类型提升
                        default: {
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                case 4: {
                    //A矩阵是ATomsUD型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 2:{
                            ATomsUD[][] C = new ATomsUD[LB][CB];
                            for (i = 0;i<LB;i++){
                                for (j = 0;j<CB;j++){
                                    C[i][j] = new ATomsUD(matrixB.matrix2[i][j]);
                                    C[i][j] = ATomsUD.multiply(matrixA.matrix4[0][0],C[i][j]);
                                }
                            }
                            return new Matrix(C);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LB][CB];
                            for (i = 0;i<LB;i++){
                                for (j = 0;j<CB;j++){
                                    C[i][j] = new ATomsUD(0);
                                    C[i][j] = ATomsUD.multiply(matrixA.matrix4[0][0],matrixB.matrix4[i][j]);
                                }
                            }
                            return new Matrix(C);
                        }
                        default: {
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                default: {
                    //暂不支持的运算
                    Matrix matrix = new Matrix();
                    matrix.BasicType = 0;
                    matrix.ErrorType = 6;
                    return matrix;
                }
            }
        }
        else if (LB==1&&CB==LB){
            //即B矩阵是一个数字
            //那么就把这个数字挨个乘上A矩阵的所有元素了
            return Matrix.multiply(matrixB,matrixA);
        }
        else if (CA == LB) {
            switch (matrixA.BasicType) {
                case 0: {
                    //异常矩阵，原地打回
                    return matrixA;
                }
                case 1: {
                    //A矩阵是double型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 1: {
                            double[][] C = new double[LA][CB];
                            //B矩阵也是double型的
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        C[c][d] += matrixA.matrix1[c][j] * matrixB.matrix1[j][d];
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                case 2: {
                    //A矩阵是UD型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 2: {
                            //B矩阵是UD型的
                            UD[][] C = new UD[LA][CB];
                            for (i = 0; i < LA; i++) {
                                for (j = 0; j < CB; j++) {
                                    C[i][j] = new UD();
                                }
                            }
                            UD temp;
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        temp = UD.multiply(matrixA.matrix2[c][j], matrixB.matrix2[j][d]);
                                        C[c][d] = UD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        case 3: {
                            //B矩阵是一元矩阵
                            //先将分数矩阵提升为一元矩阵
                            ATomUD[][] A = new ATomUD[LA][CA];
                            for (i = 0;i<LA;i++){
                                for (j = 0;j<CA;j++){
                                    A[i][j] = ATomUD.UDToATomUD(matrixA.matrix2[i][j]);
                                }
                            }
                            ATomUD[][] C  =new ATomUD[LA][CB];
                            for (i = 0;i<LA;i++){
                                for (j = 0;j<CB;j++){
                                    C[i][j] = new ATomUD("0");
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        ATomUD temp = ATomUD.multiply(A[c][j], matrixB.matrix3[j][d]);
                                        C[c][d] = ATomUD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        /*case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD [][] b = new ATomsUD[LA][CA];
                            for (c = 0;c<LA;c++){
                                for (d = 0;d<CA;d++){
                                    b[c][d] = new ATomsUD(matrixA.matrix2[c][d]);
                                }
                            }
                            ATomsUD[][] C = new ATomsUD[LA][CB];
                            for (c=0;c<LA;c++){
                                for (d=0;d<CB;d++){
                                    C[c][d] = new ATomsUD(0);
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        ATomsUD temp = ATomsUD.multiply(b[c][j], matrixB.matrix4[j][d]);
                                        C[c][d] = ATomsUD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }*/
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                /*case 4: {
                    //A矩阵是ATomsUD型的
                    switch (matrixB.BasicType) {
                        case 0: {
                            //异常矩阵，原地打回
                            return matrixB;
                        }
                        case 2:{
                            //B矩阵是UD型的
                            ATomsUD [][] b = new ATomsUD[LB][CB];
                            for (c = 0;c<LB;c++){
                                for (d = 0;d<CB;d++){
                                    b[c][d] = new ATomsUD(matrixB.matrix2[c][d]);
                                }
                            }
                            ATomsUD[][] C = new ATomsUD[LA][CB];
                            for (c=0;c<LA;c++){
                                for (d=0;d<CB;d++){
                                    C[c][d] = new ATomsUD(0);
                                    //C[c][d].ASU = new AToms(0);
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        //ATomsUD temp = ATomsUD.multiply(b[c][j], matrixB.matrix4[j][d]);
                                        ATomsUD temp = ATomsUD.multiply(matrixA.matrix4[c][j], b[j][d]);
                                        //matrixA.matrix4[c][j].print();
                                            //System.out.print(" * ");
                                            //b[j][d].print();
                                            //System.out.print(" = ");
                                            //temp.print();
                                            //System.out.print("\t");
                                        C[c][d] = ATomsUD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        case 4: {
                            //B矩阵是ATomsUD型的
                            ATomsUD[][] C = new ATomsUD[LA][CB];
                            for (c=0;c<LA;c++){
                                for (d=0;d<CB;d++){
                                    C[c][d] = new ATomsUD(0);
                                    //C[c][d].ASU = new AToms(0);
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //temp = ATomsUD.multiply(matrixA.matrix4[c][j], matrixB.matrix4[j][d]);
                                        AToms t = AToms.multiply(matrixA.matrix4[c][j].ASU, matrixB.matrix4[j][d].ASU);
                                        //C[c][d] = ATomsUD.add(temp, C[c][d]);
                                        C[c][d].ASU = AToms.add(t,C[c][d].ASU);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        default: {
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }*/
                case 3:{
                    //A矩阵是一元矩阵
                    switch (matrixB.BasicType){
                        case 2: {
                            //B是分数矩阵
                            //需要先将B矩阵提升为一元矩阵
                            ATomUD[][] B = new ATomUD[LB][CB];
                            for (i = 0;i<LB;i++){
                                for (j = 0;j<CB;j++){
                                    B[i][j] = ATomUD.UDToATomUD(matrixB.matrix2[i][j]);
                                }
                            }
                            ATomUD[][] C  =new ATomUD[LA][CB];
                            for (i = 0;i<LA;i++){
                                for (j = 0;j<CB;j++){
                                    C[i][j] = new ATomUD("0");
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        ATomUD temp = ATomUD.multiply(matrixA.matrix3[c][j], B[j][d]);
                                        C[c][d] = ATomUD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        case 3: {
                            ATomUD[][] C  =new ATomUD[LA][CB];
                            for (i = 0;i<LA;i++){
                                for (j = 0;j<CB;j++){
                                    C[i][j] = new ATomUD("0");
                                }
                            }
                            for (c = 0; c < LA; c++) {
                                for (d = 0; d < CB; d++) {
                                    for (j = 0; j < CA; j++) {
                                        //这里要判断分母为0
                                        ATomUD temp = ATomUD.multiply(matrixA.matrix3[c][j], matrixB.matrix3[j][d]);
                                        C[c][d] = ATomUD.add(temp, C[c][d]);
                                    }
                                }
                            }
                            return new Matrix(C);
                        }
                        default:{
                            //暂不支持的运算
                            Matrix matrix = new Matrix();
                            matrix.BasicType = 0;
                            matrix.ErrorType = 6;
                            return matrix;
                        }
                    }
                }
                default: {
                    //暂不支持的运算
                    Matrix matrix = new Matrix();
                    matrix.BasicType = 0;
                    matrix.ErrorType = 6;
                    return matrix;
                }
            }
        }
        else {
            //矩阵维度不相同，不可以相乘
            Matrix matrix = new Matrix();
            matrix.BasicType = 0;
            matrix.ErrorType = 23;
            return matrix;
        }
    }

    public static Matrix division(Matrix matrixA, Matrix matrixB) {
        //A要除B，就需要对B矩阵求逆，然后A去乘B的逆矩阵
        //所以需要先判断B矩阵能不能求逆
        if (matrixB.line!=matrixB.column){
            //非方阵，无法求逆
            Matrix M = new Matrix();
            M.BasicType = 0;
            M.ErrorType = 24;
            return M;
        }
        else {
            //对B矩阵求逆
            Matrix B1 = Matrix.inv(matrixB);
            //判断B1的类型，看看是不是异常
            if (B1.BasicType == 0){
                //求逆异常，直接返回
            }
            else{
                B1 = Matrix.multiply(matrixA,B1);
            }
            return B1;
        }
    }

    public static Matrix max(Matrix matrixA, Matrix matrixB) { return new Matrix(); }

    public static Matrix min(Matrix matrixA, Matrix matrixB) { return new Matrix(); }

    /**用于寻找一个行数为line的矩阵的处于矩阵位置[x,y]下的第一个非零数，返回此非零数的行数，如果下面全是0，返回-1
     * 2020.9.19，我打算写列主元素法的时候，发现我需要一个寻找一列中绝对值最大的元素，这对于减小误差是有利的，我想到，对于计算分数矩阵的时候，这样做是否也有好处？*/
    private static int Looking(UD[][] matrix, int x, int y, int line) {
        int i;
        if (x == line - 1) return -1;//x=m-1，说明到了底部，不管是否为0，直接返回
        else {
            //UD 型的矩阵
            for (i = x; i < line; i++) {
                if (matrix[i][y].u != 0) return i;
            }
            return -1;//说明该列全为0；
        }

    }

    /**用于寻找一个行数为line的矩阵的处于矩阵位置[x,y]下的绝对值最大的元素，返回此非绝对值最大的数的行数，如果下面全是0，返回-1*/
    private static int Looking(double[][] matrix, int x, int y, int line){
        int i=x,f=i;
        double max = abs(matrix[x][y]);
        if (x == line - 1) return -1;//x=line-1，说明到了底部，不管是否为0，直接返回
        else {
            for (i = x; i < line; i++) {
                if (abs(matrix[i][y])>max) {
                    max=matrix[i][y];
                    f=i;
                }
            }
            if (max==0) return -1;//说明该列全为0
            else return f;
        }
    }

    private static int Looking(ATomUD[][] matrix, int x, int y, int line){
        int i;
        if (x == line - 1) return -1;//x=m-1，说明到了底部，不管是否为0，直接返回
        else {
            //ATomUD 型的矩阵
            for (i = x; i < line; i++) {
                if (matrix[i][y].type!=0&&matrix[i][y].type!=10) return i;
            }
            return -1;//说明该列全为0；
        }
    }

    private static int abs(int a){
        if (a>=0) return a;
        else return -a;
    }

    public static long abs(long a){
        if (a>=0) return a;
        else return -a;
    }

    private static double abs(double a){
        if (a>=0) return a;
        else return -a;
    }

    private static UD abs(UD a){
        if (a.u>=0&&a.d>0) return a;
        else if (a.u>=0&&a.d<0) return new UD(a.u,-a.d);
        else if (a.u<0&&a.d>0) return new UD(-a.u,a.d);
        else return new UD(-a.u,-a.d);
    }

    /**返回两个数里面的最大值*/
    public static int max(int a1,int a2){ return Math.max(a1, a2); }

    /**返回两个数里面的最大值*/
    private static long max(long a1,long a2){ return Math.max(a1, a2); }

    /**返回两个数里面的最大值*/
    private static UD max(UD a1,UD a2){
        double b1 = (double)a1.u/a1.d;
        double b2 = (double)a2.u/a2.d;
        if (b1>b2){
            return a1;
        }
        else{
            return a2;
        }
    }

    /**返回两个数里面的最大值*/
    private static double max(double a1,double a2){ return Math.max(a1, a2); }

    /**返回两个数的最小值*/
    public static int min(int a1,int a2){ return Math.min(a1, a2); }

    /**返回两个数的最小值*/
    private static long min(long a1,long a2){ return Math.min(a1, a2); }

    /**返回两个数里面的最大值*/
    private static UD min(UD a1,UD a2){
        double b1 = (double)a1.u/a1.d;
        double b2 = (double)a2.u/a2.d;
        if (b1<b2){
            return a1;
        }
        else{
            return a2;
        }
    }

    /**返回两个数的最小值*/
    private static double min(double a1,double a2){ return Math.min(a1, a2); }

    public static String[][] getString(Matrix M){
        //获取本Matrix对象的字符串
        int line = M.line;
        int column = M.column;
        String[][] A = new String[line][column];
        switch (M.BasicType){
            case 0:{
                return new String[][]{{Matrix.getError(M)}};
            }
            case 1:{
                //小数型矩阵
                for (short i = 0;i<line;i++){
                    for (short j = 0;j<column;j++){
                        A[i][j] = String.valueOf(M.matrix1[i][j]);
                    }
                }
                return A;
            }
            case 2:{
                //分数型矩阵
                for (short i = 0;i<line;i++){
                    for (short j = 0;j<column;j++){
                        A[i][j] = UD.UDToString(M.matrix2[i][j]);
                    }
                }
                return A;
            }
            case 3:{
                //一元矩阵
                for (short i = 0;i<line;i++){
                    for (short j = 0;j<column;j++){
                        A[i][j] = ATomUD.ATomUDToString(M.matrix3[i][j]);
                    }
                }
                return A;
            }
            default:{
                line = 1;
                column = 1;
                A[0][0] = "暂不支持的运算";
                return A;
            }
        }
    }

    /*private static byte lookNum(ATomsUD A){
        //此方法用于寻找一个ATomsUD型对象到底是几元的，实际上，我只需要找到它是一元的，还是0元的
        byte num = 0;
        byte i,j;
        for (i = 0;i<A.ASU.length;i++){
            if (A.ASU.quarks[i].unique>1){
                //说明至少是一元的！
                if(A.ASU.quarks[i].numQuantum==1){

                }
            }
        }
        return num;
    }*/

    /**LU分解函数,返回的是一个紧凑格式的矩阵*/
    public static double[][] LUDecomposition(double[][] M){
        int i, j, up, down, x, t, k;
        double T;
        int n = M.length;
        double[][] A = new double[n][n];
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                A[i][j] = M[i][j];
            }
        }
        //下面先算第一列吧 好像是这一列除以a11来着 第一行没有变 不算了
        L[0][0] = 1;
        for (i = 1; i < n; i++) {
            //L_up[i][0] = matrixA_up[i][0] * matrixA_down[0][0];
            //L_down[i][0] = matrixA_down[i][0] * matrixA_up[0][0];
            L[i][0] = A[i][0]/A[0][0];
        }
        //下面开始计算第二行和第二列及以后的行和列了
        for (t = 1; t < n; t++) {
            //先算主元所在行
            for (j = t; j < n; j++) {
                T = 0;
                for (k = 0; k < t; k++) {
                    //T_up = T_up * matrixA_down[k][j] * matrixA_down[t][k] + T_down * matrixA_up[k][j] * matrixA_up[t][k];
                    //T_down = T_down * matrixA_down[k][j] * matrixA_down[t][k];
                    T = T + A[k][j] * A[t][k];
                }
                //U_up[t][j] = matrixA_up[t][j] * T_down - matrixA_down[t][j] * T_up;
                //U_down[t][j] = matrixA_down[t][j] * T_down;
                U[t][j] = A[t][j] - T;
            }
            //再算主元所在列
            for (i = t + 1; i < n; i++) {
                T = 0;
                for (k = 0; k < t; k++) {
                    //T_up = T_up * matrixA_down[i][k] * matrixA_down[k][t] + T_down * matrixA_up[i][k] * matrixA_up[k][t];
                    //T_down = T_down * matrixA_down[i][k] * matrixA_down[k][t];
                    T = T + A[i][k] * A[k][t];
                }
                //L_up[i][t] = matrixA_up[i][t] * T_down - matrixA_down[i][t] * T_up;
                //L_down[i][t] = matrixA_down[i][t] * T_down;
                L[i][t] = A[i][t] - T;
                //L_up[i][t] = L_up[i][t] * matrixA_down[t][t];
                //L_down[i][t] = L_down[i][t] * matrixA_up[t][t];
                L[i][t] = L[i][t]/A[t][t];
                //matrixA_up[i][t] = L_up[i][t]; matrixA_down[i][t] = L_down[i][t];
                A[i][t] = L[i][t];
            }
        }
        return A;
    }

    /**线性方程组求解函数，m是一个增广矩阵,返回对应的x值*/
    public static String[] linearSystemEquations(Matrix m){
        int n = m.line;
        String[] S = new String[n];
        if (m.BasicType == 1){
            if (n != m.column - 1) {
                return new String[]{"请保证你所输入的是增广矩阵(矩阵列数=矩阵行数+1)"};
            } else {
                //将此矩阵化为阶梯型
                Matrix M1 = m.rowEchelon();
                //System.out.println("---------");
                //M1.print();
                //System.out.println("---------");
                double[][] U = M1.matrix1;
                if (U[n-1][n-1]==0){
                    return new String[]{"该系数矩阵是奇异的(行列式为0),故原方程组有无穷多解"};
                }
                double[] X = new double[n];
                X[n-1] = U[n-1][n]/U[n-1][n-1];
                S[n-1] = "x" + (n)+" = "+X[n-1];
                int i,j;
                for (i=n-2;i>=0;i--){
                    double sum = 0;
                    for (j=i+1;j<n;j++){
                        sum = sum+U[i][j]*X[j];
                    }
                    X[i] = (U[i][n] - sum) / U[i][i];
                    S[i] = "x" + (i+1)+" = "+ X[i];
                }
                return S;
            }
        }
        if (m.BasicType==2) {
            if (n != m.column - 1) {
                return new String[]{"请保证你所输入的是增广矩阵(矩阵列数=矩阵行数+1)"};
            } else {
                //将此矩阵化为阶梯型
                Matrix M1 = m.rowEchelon();
                //System.out.println("---------");
                //M1.print();
                //System.out.println("---------");
                UD[][] U = M1.matrix2;
                if (U[n-1][n-1].u==0){
                    return new String[]{"该系数矩阵是奇异的(行列式为0),故原方程组有无穷多解"};
                }
                UD[] X = new UD[n];
                X[n-1] = UD.division(U[n-1][n],U[n-1][n-1]);
                S[n-1] = "x" + (n)+" = "+UD.UDToString(X[n-1]);
                int i,j;
                for (i=n-2;i>=0;i--){
                    UD sum = new UD(0);
                    for (j=i+1;j<n;j++){
                        sum = UD.add(sum,UD.multiply(U[i][j],X[j]));
                    }
                    X[i] = UD.division(UD.less(U[i][n],sum),U[i][i]);
                    S[i] = "x" + (i+1)+" = "+UD.UDToString(X[i]);
                }
                return S;
            }
        }
        else if (m.BasicType==3){
            //一元矩阵
            if (n != m.column - 1) {
                return new String[]{"请保证你所输入的是增广矩阵(矩阵列数=矩阵行数+1)"};
            } else {
                //将此矩阵化为阶梯型
                Matrix M1 = m.rowEchelon();
                //System.out.println("---------");
                //M1.print();
                //System.out.println("---------");
                ATomUD[][] U = M1.matrix3;
                if (U[n-1][n-1].type==10){
                    return new String[]{"该系数矩阵是奇异的(行列式为0),故原方程组有无穷多解"};
                }
                ATomUD[] X = new ATomUD[n];
                X[n-1] = ATomUD.division(U[n-1][n],U[n-1][n-1]);
                S[n-1] = "x" + (n)+" = "+ATomUD.ATomUDToString(X[n-1]);
                int i,j;
                for (i=n-2;i>=0;i--){
                    ATomUD sum = new ATomUD("0");
                    for (j=i+1;j<n;j++){
                        sum = ATomUD.add(sum,ATomUD.multiply(U[i][j],X[j]));
                    }
                    X[i] = ATomUD.division(ATomUD.less(U[i][n],sum),U[i][i]);
                    S[i] = "x" + (i+1)+" = "+ATomUD.ATomUDToString(X[i]);
                }
                return S;
            }
        }
        else {
            return new String[]{"暂不支持的运算"};
        }
    }
    /**获取本对象的BasicType*/
    public byte getBasicType(){
        return BasicType;
    }

    /**获取本对象的ErrorType*/
    public byte getErrorType(){
        return ErrorType;
    }

    /**获取本对象的行数*/
    public int getLine(){
        return line;
    }
}