package com.yw.tingercalc.utils;

public class UD {

    long u;
    long d;

    public UD(long u,long d){
        long up1,down1,x;
        up1 = u; down1 = d;
        while (down1 != 0) {
            x = up1 % down1;
            up1 = down1;
            down1 = x;
        }
        u = u / up1; d = d / up1;
        if (d<0){
            u = -1*u;
            d = -1*d;
        }
        this.u=u;
        this.d=d;
    }

    public UD(long u,int d){
        long up1,down1,x;
        long d1=(long)d;
        up1 = u; down1 = d1;
        while (down1 != 0) {
            x = up1 % down1;
            up1 = down1;
            down1 = x;
        }
        u = u / up1; d1 = d1 / up1;
        if (d<0){
            u = -1*u;
            d1 = -1*d1;
        }
        this.u=u;
        this.d=d1;
    }

    public UD(int u,long d){
        long up1,down1,x;
        long u1=(long)u;
        up1 = u1; down1 = d;
        while (down1 != 0) {
            x = up1 % down1;
            up1 = down1;
            down1 = x;
        }
        u1 = u1 / up1; d = d / up1;
        if (d<0){
            u1 = -1*u1;
            d = -1*d;
        }
        this.u=u1;
        this.d=d;
    }

    public UD(int u,int d){
        long up1,down1,x;
        long u1=u;
        long d1=d;
        up1 = u1; down1 = d1;
        while (down1 != 0) {
            x = up1 % down1;
            up1 = down1;
            down1 = x;
        }
        u1 = u1 / up1; d1 = d1 / up1;
        if (d1<0){
            u1 = -1*u1;
            d1 = -1*d1;
        }
        this.u=u1;
        this.d=d1;
    }

    public UD(long u){
        this.u=u;
        this.d= 1L;
    }

    public UD(int u){
        this.u=(long)u;
        this.d= 1L;
    }

    public UD() {
        this.u= 0L;
        this.d= 1L;
    }

    public void empty(){
        this.u = 0L;
        this.d = 1L;
    }

    public static void empty(UD U){
        U.u = 0L;
        U.d = 1L;
    }

    public boolean equal(UD u){ return equal(this,u); }

    public UD abs(){ return abs(this); }

    public UD add(UD u){ return add(this,u); }

    public UD less(UD u){ return less(this,u); }

    public UD multiply(UD u){ return multiply(this,u); }

    public UD reverse(){ return reverse(this); }

    public double UDToDouble(){ return UDToDouble(this); }

    public UD simplify(){ return simplify(this); }

    public void print(){ print(this); }

    public static boolean equal(UD u1,UD u2){
        //先化简一下
        u1 = u1.simplify();
        u2 = u2.simplify();
        return u1.u == u2.u && u1.d == u2.d;
    }

    public static UD abs(UD u){
        UD U = new UD(u.u,u.d);
        if (u.u<0&&u.d>0){
            U.u=-1*u.u;
            return U;
        }
        else if (u.u>0&&u.d<0){
            U.d=-1*u.d;
            return U;
        }
        else if (u.u<0&&u.d<0){
            U.u=-1*u.u;
            U.d=-1*u.d;
            return U;
        }
        else {
            return U;
        }
    }

    public static UD add(UD u1,UD u2){
        long u = u1.u*u2.d+u2.u*u1.d;
        long d = u1.d*u2.d;
        return new UD(u,d);
    }

    public static UD add(int u1,UD u2){
        long u = u1*u2.d+u2.u;
        long d = u2.d;
        return new UD(u,d);
    }

    public static UD add(UD u1,int u2){
        long u = u1.u+u2*u1.d;
        long d = u1.d;
        return new UD(u,d);
    }

    public static UD add(long u1,UD u2){
        long u = u1*u2.d+u2.u;
        long d = u2.d;
        return new UD(u,d);
    }

    public static UD add(UD u1,long u2){
        long u = u1.u+u2*u1.d;
        long d = u1.d;
        return new UD(u,d);
    }

    public static UD less(UD u1,UD u2){
        UD u = new UD(UD.reverse(u2).u,UD.reverse(u2).d);
        return UD.add(u1,u);
    }

    public static UD multiply(UD u1,UD u2){
        long u = u1.u*u2.u;
        long d = u1.d*u2.d;
        return new UD(u,d);
    }

    public static UD multiply(int u1,UD u2){
        long u = u1*u2.u;
        long d = u2.d;
        return new UD(u,d);
    }

    public static UD multiply(UD u1,int u2){
        long u = u1.u*u2;
        long d = u1.d;
        return new UD(u,d);
    }

    public static UD multiply(long u1,UD u2){
        long u = u1*u2.u;
        long d = u2.d;
        return new UD(u,d);
    }

    public static UD multiply(UD u1,long u2){
        long u = u1.u*u2;
        long d = u1.d;
        return new UD(u,d);
    }

    public static UD division(UD u1,UD u2){
        long u = u1.u*u2.d;
        long d = u1.d*u2.u;
        return new UD(u,d);
    }

    public static UD division(UD u1,long u2){
        long d = u1.d * u2;
        return new UD(u1.u,d);
    }

    public static double UDToDouble(UD u){ return (double)u.u/(double)u.d; }

    public static UD reverse(UD u){
        long u1 = -1*u.u;
        return new UD(u1,u.d);
    }

    public static UD simplify(UD u){
        //化简目标：1分子分母都除以最大公因数，2如果有负号，负号应该在分子上，3如果分子分母都为负，那么应该摘掉分子分母的负号
        if (u.d<0){
            return new UD(-1*u.u, -1*u.d);
        }
        else {
            return new UD(u.u, u.d);
        }
    }

    public static void print(UD u){
        u = UD.simplify(u);
        String s = UD.UDToString(u);
        System.out.print(s);
    }

    public static UD copy(UD U){ return new UD(U.u,U.d); }

    public static UD GCD(UD u1,UD u2){
        //求两个分数的最大公因式，，，
        long d = UD.LCM(u1.d,u2.d);
        long u = UD.GCD(u1.u*d/u1.d,u2.u*d/u2.d);
        return new UD(u,d);
    }

    public static long GCD(long u,long d){
        //求两个数的GCD
        long up1,down1,x;
        long u1=u;
        long d1=d;
        up1 = u1; down1 = d1;
        while (down1 != 0) {
            x = up1 % down1;
            up1 = down1;
            down1 = x;
        }
        return up1;
    }

    public static long LCM(long l1,long l2){
        //求两个数的最小公倍数
        //最小公倍数 = 两个数相乘 / 两个数的GCD
        return l1*l2/UD.GCD(l1,l2);
    }

    /**将分数转化为字符*/
    public static String UDToString(UD u){
        String s = "error";
        if (u.d==0){
            s = "错误";
        }
        else if (u.d>0){
            if (u.d==1){
                //只转分子
                s = String.valueOf(u.u);
            }
            else {
                //分子/分母
                String s1 = String.valueOf(u.u);
                String s2 = String.valueOf(u.d);
                String s3 = "/";
                s = s1+s3+s2;
            }
        }
        else {
            //u.d<0
            if (u.d == -1){
                //分子 = 分子 * -1，然后转
                s = String.valueOf(-1*u.u);
            }
            else {
                //转为 -分子/（-分母）
                String s1 = String.valueOf(-1*u.u);
                String s2 = String.valueOf(-1*u.d);
                String s3 = "/";
                s = s1+s3+s2;
            }
        }
        return s;
    }

}
