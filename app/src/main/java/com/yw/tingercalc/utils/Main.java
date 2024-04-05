package com.yw.tingercalc.utils;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String [][] S = {
                {"x", "2", "3"},
                {"4", "5", "6"},
                {"7", "x-1", "x^(-1)"}
        };
        Matrix M = new Matrix(S);
        System.out.print("\n你输入的矩阵为\n");
        M.print();
        System.out.print("\n化为阶梯型为\n");
        Matrix M1 = M.rowEchelon();
        String [][] A = M1.getString();
        for (short i = 0;i<A.length;i++){
            for (short j = 0;j<A[0].length;j++){
                System.out.print(A[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.print("\n行列式为：");
        M1 = M.det();
        A = M1.getString();
        for (short i = 0;i<A.length;i++){
            for (short j = 0;j<A[0].length;j++){
                System.out.print(A[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.print("\n逆矩阵为 \n");
        M1 = M.inv();
        A = M1.getString();
        for (short i = 0;i<A.length;i++){
            for (short j = 0;j<A[0].length;j++){
                System.out.print(A[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.print("\n(检查是否修参)M = \n");
        M.print();
    }
}