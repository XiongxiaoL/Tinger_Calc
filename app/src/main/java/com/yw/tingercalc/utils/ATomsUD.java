package com.yw.tingercalc.utils;
public class ATomsUD {

    AToms ASU;
    AToms ASD;

    public ATomsUD(String string){
        AToms A1 = new AToms(string);
        AToms A2 = new AToms(1);
        ATomsUD A = new ATomsUD(A1,A2);
        A = A.check();
        this.ASU = A.ASU;
        this.ASD = A.ASD;
    }

    public ATomsUD(AToms u,AToms d){
        this.ASU=u;
        this.ASD=d;
    }

    public ATomsUD(AToms u){
        this.ASU = u;
        this.ASD = new AToms(1);
    }

    public ATomsUD(){
        this.ASU = new AToms();
        this.ASD = new AToms();
    }

    public ATomsUD(UD u){
        AToms A = new AToms(u);
        this.ASU = A;
        this.ASD = new AToms(1);
    }

    public ATomsUD(int x){
        AToms A = new AToms(x);
        this.ASU = A;
        this.ASD = new AToms(1);
    }

    public void print(){
        print(this);
    }

    public ATomsUD add(ATomsUD A){
        return add(this,A);
    }

    public ATomsUD less(ATomsUD A){
        return less(this,A);
    }

    public ATomsUD multiply(ATomsUD A){
        return multiply(this,A);
    }

    public ATomsUD check(){
        return check(this);
    }

    public ATomsUD reverse(){
        return reverse(this);
    }

    public static void print(ATomsUD A){
        A.ASU.print();
        /*
        System.out.print("[");
        A.ASU.print();
        System.out.print("]");
        System.out.print(" / ");
        System.out.print("[");
        A.ASD.print();
        System.out.print("]");*/
    }

    public static ATomsUD add(ATomsUD A1,ATomsUD A2){
        //分式的加法
        AToms AU = AToms.add(AToms.multiply(A1.ASU,A2.ASD),AToms.multiply(A1.ASD,A2.ASU));
        //System.out.printf("\nAToms.multiply(A1.ASU,A2.ASD) = ");
        //AToms.multiply(A1.ASU,A2.ASD).print();
        //System.out.printf("\nAU = ");
        //AU.print();
        //System.out.println();
        AToms AD = AToms.multiply(A1.ASD,A2.ASD);
        ATomsUD A = new ATomsUD(AU,AD);
        A = A.check();
        return A;
    }

    public static ATomsUD less(ATomsUD A1,ATomsUD A2){
        ATomsUD A3 = ATomsUD.reverse(A2);
        A3 = ATomsUD.add(A1,A3);
        return A3;
    }

    public static ATomsUD multiply(ATomsUD A1,ATomsUD A2){
        AToms AU = AToms.multiply(A1.ASU,A2.ASU);
        AToms AD = AToms.multiply(A1.ASD,A2.ASD);
        ATomsUD A = new ATomsUD(AU,AD);
        A = A.check();
        return A;
    }

    public static ATomsUD check(ATomsUD A){
        //由于暂时没有写求两个多元多项式的GCD方法，故暂时无法写此方法 -- 2020.12.7
        return A;
    }

    public static ATomsUD reverse(ATomsUD A){
        AToms a = AToms.reverse(A.ASU);
        AToms d = AToms.copy(A.ASD);
        return new ATomsUD(a,d);
    }

    public static ATomsUD copy(ATomsUD A){
        AToms AU = AToms.copy(A.ASU);
        AToms AD = AToms.copy(A.ASD);
        return new ATomsUD(AU,AD);
    }

}
