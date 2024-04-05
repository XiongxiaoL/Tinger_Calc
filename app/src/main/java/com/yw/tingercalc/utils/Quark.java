package com.yw.tingercalc.utils;

public class Quark {
    UD coe;//系数
    int degree;//次数

    public Quark(UD u,int degree){
        this.coe = u;
        this.degree=degree;
    }

    public Quark(UD u){
        this.coe = u;
        this.degree=0;
    }

    public Quark(int u,int degree){
        this.coe = new UD(u);
        this.degree = degree;
    }

    public Quark() {
        this.coe = new UD();
        this.degree = 0;
    }

    public boolean isSimilar(Quark q){
        return isSimilar(this,q);
    }

    public void empty(){
        this.coe = new UD();
        this.degree = 0;
    }

    public void print(int flag){
        print(this,flag);
    }

    public void print(){
        print(this,0);
    }


    /*public Quark add(Quark A){
        return this.add(this,A);
    }

    public Quark less(Quark A){
        return this.less(this,A);
    }*/

    public Quark multiply(Quark A){
        return multiply(this,A);
    }

   /*public Quark check(){
        return this.check(this);
    }*/

    public Quark reverse(){
        return reverse(this);
    }

    public static Quark copy(Quark Q){
        UD c = UD.copy(Q.coe);
        return new Quark(c,Q.degree);
    }

    public static Quark [] merge(Quark [] Q){
        //合并同类项
        int i;
        int l = 0;
        if (Q.length<=1){
            return Q;
        }
        else {
            for (i = 0; i < Q.length - 1; i++) {
                if (Q[i].degree == Q[i + 1].degree) {
                    //前面这一项的次数等于后面这一项的次数就把他们加起来
                    Q[i + 1].coe = UD.add(Q[i + 1].coe, Q[i].coe);
                    //然后把Q[i]置空
                    Q[i].coe = new UD();
                }
            }
            int j = 0;
            Quark[] q = new Quark[Q.length];
            for (i = 0; i < Q.length; i++) {
                if (Q[i].coe.u != 0) {
                    //出现有某一项的系数不为0
                    q[j] = Q[i];
                    j++;
                    l++;
                }
            }
            Quark[] q1 = new Quark[l];
            for (i = 0; i < l; i++) {
                q1[i] = q[i];
            }
            Q = q1;//修参
            return q1;
        }
    }

    public static void print(Quark A,int flag) {
        if (A.coe.u>1){
            if (A.coe.d == 1){
                if (flag == 1){System.out.print(" + ");}
                System.out.printf("%d",A.coe.u);
            }
            else if (A.coe.d<0){
                //A.coe.d<0
                System.out.print(" - ");
                System.out.printf("(%d/%d)",A.coe.u,-1*A.coe.d);
            }
            else if (A.coe.d == 0){
                System.out.println("出现0！");
            }
            else {//A.coe>1
                if (flag == 1){System.out.print(" + ");}
                System.out.print("(");
                System.out.printf("%d/%d",A.coe.u,A.coe.d);
                System.out.print(")");
            }
        }
        else if (A.coe.u == 1) {
            if (A.coe.d>1){
                if (flag == 1){System.out.print(" + ");}
                System.out.printf("(1/%d)",A.coe.d);
            }
            else if (A.coe.d == 1) {
                if (flag == 1) {
                    System.out.print(" + ");
                }
            }
            else if (A.coe.d == 0){
                System.out.printf("!!!0!!!");
            }
            else {
                //A.coe.d<0
                System.out.printf(" - (1/%d)", -1 * A.coe.d);
            }
        }
        else if (A.coe.u==0){ }
        else if (A.coe.u == -1){
            if (A.coe.d>1){
                System.out.printf(" - ");
                System.out.printf("(1/%d)",A.coe.d);
            }
            else if (A.coe.d == 1) {
                System.out.printf(" - ");
            }
            else if (A.coe.d == 0){
                System.out.printf("!!!0!!!");
            }
            else {
                //A.coe.d<0
                if (flag == 1) {
                    System.out.printf(" + ");
                }
                System.out.printf("(1/%d)", -1 * A.coe.d);
            }
        }
        else {
            //A.coe.u<0
            if (A.coe.d == 1){
                System.out.printf(" - ");
                System.out.printf("%d",-1*A.coe.u);
            }
            else if (A.coe.d>0){
                System.out.printf(" - ");
                System.out.printf("(%d/%d)",-1*A.coe.u,A.coe.d);
            }
            else {
                //A.coe.d<0
                if (flag == 1){System.out.printf(" + ");}
                System.out.printf("(%d/%d)",-1*A.coe.u,-1*A.coe.d);
            }
        }
    }

    public static boolean isSimilar(Quark q1,Quark q2){
        if (q1.degree==q2.degree){
            return true;
        }
        else{
            return false;
        }
    }

    /*public static Quark add(Quark A1,Quark A2){
        Quark A = new Quark();

        return A;
    }*/

    public static Quark multiply(Quark A1,Quark A2){
        //两个Quark相乘
        //先系数相乘
        UD coe = UD.multiply(A1.coe,A2.coe);
        //再次数相加
        int deg = A1.degree + A2.degree;
        Quark A = new Quark(coe,deg);
        return A;
    }

    /*public static Quark less(Quark A1,Quark A2){
        Quark A = new Quark();
        return A;
    }*/

    /*public static Quark check(Quark A){
        Quark a = new Quark();

        return a;
    }*/

    public static Quark reverse(Quark A){
        //只要系数取反即可
        UD coe = UD.reverse(A.coe);
        return new Quark(coe,A.degree);
    }

    //使用本方法意味着Q可以进行类型转换
    public static Quark QuarksToQuark(Quarks Q){
        if (Q.type == 1){
            //常数项的话
            return new Quark(Q.coe);
        }
        else if (Q.type == 2){
            Quark q = new Quark(Q.coe);
            q.degree = Q.degree[0];
            return q;
        }
        else {
            return new Quark();
        }
    }

    public static Quark [] sort(Quark [] quark,int length) {
        //插入排序
        if (length == 0) {
            return quark;
        }
        else {
            Quark current;
            for (int i = 0; i < length - 1; i++) {
                current = quark[i + 1];
                int preIndex = i;
                while (preIndex >= 0 && current.degree < quark[preIndex].degree) {
                    quark[preIndex + 1] = quark[preIndex];
                    preIndex--;
                }
                quark[preIndex + 1] = current;
            }
            Quark[] q = new Quark[length];
            for (int i = 0; i < length; i++) {
                q[length - 1 - i] = quark[i];
            }
            return q;
        }
    }

}
