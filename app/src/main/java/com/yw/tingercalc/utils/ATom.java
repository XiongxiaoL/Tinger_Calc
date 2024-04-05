package com.yw.tingercalc.utils;

public class ATom {
    int length;
    Quantum unaryATom;
    Quark [] quarkATom;
    byte type = 0;
    byte error = 0;

    /*
     * type = 0 异常
     * type = 1 常数项
     * type = 10 0多项式，只有一项，且是常数项0
     * type = 11 1多项式，只有一项，且是常数项1
     * type = 2 普通的一元多项式
     * */

    /*
     * error == 0 没有异常
     * error == 1 不是同类项，不能相加
     * error == 2 不可以给不可分割元赋值（特殊情况：根号）
     * error == 3 不是同类项，不可以相乘
     * error == 4 未定义的类型
     * error == 5 因式中出现了异常
     * error == 6 最大公因式求解错误
     * error == 7 对待操作的两个对象进行检查
     * error == 8 分母为0(0多项式)
     * */

    /*
     * 规定：常数项的次数为1
     * 更新：常数项的次数为0 -- 2020.12.28 这是因为，Quarks转Quark的时候遇到了问题
     * */

    public static ATom copy(ATom A){
        if (A.type == 0){
            //异常对象
            ATom A1 = new ATom();
            A1.error = A.error;
            A1.type = A.type;
            return A1;
        }
        else if (A.type == 1||A.type == 10||A.type == 11){
            //常数项的复制
            UD coe = UD.copy(A.quarkATom[0].coe);
            return new ATom(coe);
        }
        else {
            //普通的一元多项式
            int l = A.length;
            Quantum u = Quantum.copy(A.unaryATom);
            Quark [] q = new Quark[l];
            for (int i = 0;i<l;i++){
                q[i]=Quark.copy(A.quarkATom[i]);
            }
            ATom A1 = new ATom(q,u);
            A1.type = A.type;
            A1.error = A.error;
            return A1;
        }
    }

    public ATom(Quark [] quark,Quantum unary){
        this.length=quark.length;
        this.quarkATom=quark;
        this.unaryATom=unary;
        this.type = 2;
    }

    public ATom(UD coe) {
        //构建常数项！
        this.quarkATom = new Quark[]{new Quark(coe)};
        this.length = 1;
        if (coe.u == 0) {
            this.type = 10;
        } else if (coe.u == coe.d) {
            this.type = 11;
        } else if (coe.d == 0) {
            //一般不会遇到的
            this.type = 0;
            this.error = 8;
        } else {
            this.type = 1;
        }
    }

    public ATom(int x){
        //构建常数项
        this.quarkATom = new Quark[]{new Quark(new UD(x))};
        this.length = 1;
        this.type = 1;
        if (x == 0){
            this.type = 10;
        }
        else if (x == 1){
            this.type = 11;
        }
    }

    public ATom(Quark [] quark){
        this.length=quark.length;
        this.quarkATom=quark;
        if (quark.length == 1&&quark[0].degree == 0){
            if (quark[0].coe.u == 0){
                this.type = 10;
            }
            else if (quark[0].coe.u == quark[0].coe.d){
                this.type = 11;
            }
            else {
                this.type = 1;
            }
        }
        this.unaryATom= new Quantum();
    }

    public ATom(){
        this.quarkATom = new Quark[]{ new Quark(new UD(),0)};
        this.unaryATom = new Quantum();
    }

    public static ATom UDToATom(UD coe){
        //将一个UD类型的对象转化为ATom型的
        Quark [] q = {new Quark(coe)};
        ATom A = new ATom(q);
        A.type = 1;
        return A;
    }

    public static void print(ATom A) {
        System.out.print(ATom.ATomToString(A));
       /* if (A.type == 0) {
            printError(A);
        }
        else if (A.type == 1||A.type == 10||A.type == 11) {
            //常数项
            A.quarkATom[0].coe.print();
        }
        else if (A.type == 2) {
            if (A.quarkATom[0].coe.d!=1) {
                System.out.print("(");
                A.quarkATom[0].coe.print();
                System.out.print(")");
            }
            else if (A.quarkATom[0].coe.u == 1||A.quarkATom[0].coe.u == A.quarkATom[0].coe.d){
                //1不打印出来
            }
            else if (A.quarkATom[0].coe.u == -1||A.quarkATom[0].coe.u*A.quarkATom[0].coe.d==-1){
                System.out.print("-");
            }
            else {
                A.quarkATom[0].coe.print();
            }
            A.unaryATom.print();
            if (A.quarkATom[0].degree==1){
                ;//一次不打印
            }
            else if (A.quarkATom[0].degree == 2){
                System.out.print("^2");
            }
            else if (A.quarkATom[0].degree == 3){
                System.out.print("^3");
            }
            else if (A.quarkATom[0].degree>3){
                System.out.printf("^%d",A.quarkATom[0].degree);
            }
            else {
                System.out.printf("^(%d)",A.quarkATom[0].degree);
            }
            for (int i = 1;i<A.length;i++){
                if (A.quarkATom[i].degree == 0){
                    if (A.quarkATom[i].coe.u>0||A.quarkATom[i].coe.u*A.quarkATom[i].coe.d>0){
                        System.out.print("+");
                    }
                    A.quarkATom[i].coe.print();
                }
                else {
                    if (A.quarkATom[i].coe.d != 1) {
                        if (A.quarkATom[i].coe.u>0||A.quarkATom[i].coe.u*A.quarkATom[i].coe.d>0){
                            System.out.print("+");
                            System.out.print("(");
                            A.quarkATom[i].coe.print();
                        }
                        else {
                            System.out.print("-");
                            System.out.print("(");
                            UD coe = A.quarkATom[i].coe.reverse();
                            coe.print();
                        }
                        System.out.print(")");
                    } else if (A.quarkATom[i].coe.u == 1 || A.quarkATom[i].coe.u == A.quarkATom[i].coe.d) {
                        System.out.print("+");
                    } else if (A.quarkATom[i].coe.u == -1 || A.quarkATom[i].coe.u * A.quarkATom[i].coe.d == -1) {
                        System.out.print("-");
                    } else {
                        if (A.quarkATom[i].coe.u>0||A.quarkATom[i].coe.u*A.quarkATom[i].coe.d>0){
                            System.out.print("+");
                        }
                        A.quarkATom[i].coe.print();
                    }
                    A.unaryATom.print();
                    if (A.quarkATom[i].degree == 1) {
                        ;//一次不打印
                    } else if (A.quarkATom[i].degree == 2) {
                        System.out.print("^2");
                    } else if (A.quarkATom[i].degree == 3) {
                        System.out.print("^3");
                    } else if (A.quarkATom[i].degree > 3) {
                        System.out.printf("^%d", A.quarkATom[i].degree);
                    } else {
                        System.out.printf("^(%d)", A.quarkATom[i].degree);
                    }
                }
            }
        }

        */
    }

    public static void printError(ATom A){
        switch (A.error){
            case 0:{
                System.out.print("没有异常");
                break;
            }
            case 1:{
                System.out.print("不是同类项，不能相加");
                break;
            }
            case 2:{
                System.out.print("不可以给不可分割项赋值");
                break;
            }
            case 3:{
                System.out.print("不是同类项，不能相乘");
                break;
            }
            case 4:{
                System.out.print("未定义的类型");
                break;
            }
            case 5:{
                System.out.print("因式中出现了异常");
                break;
            }
            case 6:{
                System.out.print("最大公因式求解错误");
                break;
            }
            case 7: {
                System.out.print("对待操作的两个对象进行检查");
                break;
            }
            case 8:{
                System.out.print("分母为0(0多项式)");
                break;
            }
            default:{
                System.out.print("未知错误，出现bug");
            }
        }
    }

    /**两个ATom相加，先自检，然后把A1和A2拼接起来，然后按照次数进行插入排序，再挨个扫描一遍合并同类项*/
    public static ATom add(ATom A1,ATom A2){
        if (A1.type == 0 || A2.type == 0) {
            //对象异常，报错
            ATom A = new ATom();
            A.type = 0;
            if (A1.type == 0 && A2.type != 0) {
                A.error = A1.error;
            } else if (A2.type == 0 && A1.type != 0) {
                A.error = A2.error;
            } else {
                A.error = 7;
            }
            return A;
        }
        else if (A1.type == 10){
            return ATom.copy(A2);
        }
        else if (A2.type == 10){
            return ATom.copy(A1);
        }
        else if (A1.type == 1||A1.type == 11) {
            if (A2.type == 1||A2.type == 11) {
                //两个常数项相加
                UD coe = UD.add(A1.quarkATom[0].coe, A2.quarkATom[0].coe);
                return new ATom(coe);
            }
            else if (A2.type == 2) {
                //A1是常数项，A2却是一般的一元多项式
                //A2是非数字项，那么就要把A1加到A2的0次项上，如果A2没有0次项，那么就创建一个！
                if (A2.quarkATom[A2.length - 1].degree == 0){
                    //A2的最后一项为常数项，那么就把A1加上去
                    A2.quarkATom[A2.length - 1].coe = UD.add(A1.quarkATom[0].coe,A2.quarkATom[A2.length - 1].coe);
                    A2 = A2.check();
                    return ATom.copy(A2);
                }
                else {
                    //A2的最后一项不是常数项，那么要把A1作为它的常数项拼接上去
                    Quark [] Q = new Quark[A2.length+1];
                    for (int i = 0;i<A2.length;i++){
                        Q[i] = new Quark(A2.quarkATom[i].coe,A2.quarkATom[i].degree);
                    }
                    Q[A2.length] = new Quark(A1.quarkATom[0].coe,0);
                    ATom A = new ATom(Q,A2.unaryATom);
                    A = ATom.check(A);
                    return A;
                }
            }
            else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else if (A1.type == 2) {
            if (A2.type == 1||A2.type == 11) {
                //A2是常数项，A1是一般的一元多项式
                if (A1.quarkATom[A1.length - 1].degree == 0){
                    //A1的最后一项是数字
                    ATom A = ATom.copy(A1);
                    A.quarkATom[A1.length - 1].coe = UD.add(A1.quarkATom[A1.length - 1].coe,A2.quarkATom[0].coe);
                    A = A.check();
                    return A;
                }
                else {
                    //A1的最后一项不是常数项
                    Quark [] Q = new Quark[A1.length+1];
                    for (int i = 0;i<A1.length;i++){
                        Q[i] = new Quark(A1.quarkATom[i].coe,A1.quarkATom[i].degree);
                    }
                    Q[A1.length] = new Quark(A2.quarkATom[0].coe,0);
                    ATom A = new ATom(Q,A1.unaryATom);
                    A = ATom.check(A);
                    return A;
                }
            }
            else if (A2.type == 2) {
                //核心代码，
                if (A1.unaryATom.equals(A2.unaryATom)) {
                    //只有当他们是同类项才能相加
                    int l = A1.length + A2.length;
                    Quark[] Q = new Quark[l];
                    int i;
                    //开始拼接
                    for (i = 0; i < A1.length; i++) {
                        Q[i] = Quark.copy(A1.quarkATom[i]);
                    }
                    for (i = A1.length; i < l; i++) {
                        Q[i] = Quark.copy(A2.quarkATom[i - A1.length]);
                    }
                    //拼接完成
                    //开始排序
                    Q = Quark.sort(Q, l);
                    //排序完成
                    //开始合并同类项
                    Q = Quark.merge(Q);
                    //合并完成
                    if (Q.length == 0) {
                        return new ATom(0);
                        //自检后，发现一项都没了，这个时候，需要返回0多项式
                    }
                    else if (Q.length == 1&&Q[0].degree == 0){
                        ATom A = new ATom(Q,A1.unaryATom);
                        A = A.check();
                        if (A.quarkATom[0].coe.u == 0){
                            A.type = 10;
                        }
                        else if (A.quarkATom[0].coe.u == 1||A.quarkATom[0].coe.u==A.quarkATom[0].coe.d){
                            A.type = 11;
                        }
                        else {
                            A.type = 1;
                        }
                        return A;
                    }
                    else {
                        ATom A = new ATom(Q,A1.unaryATom);
                        A = A.check();
                        A.type = 2;
                        return A;
                    }
                }
                else {
                    //不能相加
                    ATom A = new ATom();
                    A.error = 1;
                    return A;
                }
            }
            else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else {
            //未定义的类型
            ATom A = new ATom();
            A.type = 0;
            A.error = 4;//出现了未定义的类型
            return A;
        }
    }

    public static ATom multiply(ATom A1,ATom A2){
        //先按照定义来乘，同样的，这里要注意传入的参数可能是一个常数项
        A1 = ATom.check(A1);
        A2 = ATom.check(A2);
        if (A1.type == 0 || A2.type == 0) {
            //对象异常，报错
            ATom A = new ATom();
            A.type = 0;
            if (A1.type == 0 && A2.type != 0) {
                A.error = A1.error;
            } else if (A2.type == 0 && A1.type != 0) {
                A.error = A2.error;
            } else {
                A.error = 7;//待操作的两对象异常
            }
            return A;
        }
        else if (A1.type == 10||A2.type == 10){
            return new ATom(0);
        }
        else if (A1.type == 11){
            return A2;
        }
        else if (A2.type == 11){
            return A1;
        }
        else if (A1.type == 1) {
            if (A2.type == 1) {
                //两个常数项的乘积
                UD coe = UD.multiply(A1.quarkATom[0].coe, A2.quarkATom[0].coe);
                return new ATom(coe);
            }
            else if (A2.type == 2) {
                //A1是常数项，A2却是一般的一元多项式
                //A2是非数字项，那么就要把A1挨个乘上A2的每一项
                Quark [] Q = new Quark[A2.length];
                for (int i = 0;i<A2.length;i++){
                    UD coe = UD.multiply(A1.quarkATom[0].coe,A2.quarkATom[i].coe);
                    Q[i] = new Quark(coe,A2.quarkATom[i].degree);
                }
                ATom A = new ATom(Q,A2.unaryATom);
                A = ATom.check(A);
                return A;
            }
            else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else if (A1.type == 2) {
            if (A2.type == 1) {
                //A2是常数项，A1是一般的一元多项式
                //A1不是常数项，A2乘上A1的每一项
                Quark [] Q = new Quark[A1.length];
                for (int i = 0;i<A1.length;i++){
                    UD coe = UD.multiply(A1.quarkATom[i].coe,A2.quarkATom[0].coe);
                    Q[i] = new Quark(coe,A1.quarkATom[i].degree);
                }
                ATom A = new ATom(Q,A1.unaryATom);
                A = ATom.check(A);
                return A;
            }
            else if (A2.type == 2) {
                //核心代码
                if (A1.unaryATom.equals(A2.unaryATom)) {
                    int i, j;
                    int l1 = A1.length;
                    int l2 = A2.length;
                    Quark[] Q = new Quark[l1 * l2];
                    int k = 0;
                    for (i = 0; i < l1; i++) {
                        for (j = 0; j < l2; j++) {
                            Q[k] = Quark.multiply(A1.quarkATom[i], A2.quarkATom[j]);
                            k++;
                        }
                    }
                    //乘完了，然后排序，合并，自检
                    Q = Quark.sort(Q, l1 * l2);
                    Q = Quark.merge(Q);
                    if (Q.length == 0) {
                        return new ATom(0);
                        //自检后，发现一项都没了，这个时候，需要返回0多项式
                    }
                    else if (Q.length == 1&&Q[0].degree == 0){
                        ATom A = new ATom(Q,A1.unaryATom);
                        A = A.check();
                        if (A.quarkATom[0].coe.u == 0){
                            A.type = 10;
                        }
                        else if (A.quarkATom[0].coe.u==A.quarkATom[0].coe.d){
                            A.type = 11;
                        }
                        else {
                            A.type = 1;
                        }
                        return A;
                    }
                    else {
                        ATom A = new ATom(Q,A1.unaryATom);
                        A = A.check();
                        A.type = 2;
                        return A;
                    }
                }
                else {
                    //不是同类项，不可以相乘，为什么？因为现在搭建的是一元矩阵的快车道
                    ATom A = new ATom();
                    A.error = 3;
                    return A;
                }
            }
            else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else {
            //未定义的类型
            ATom A = new ATom();
            A.type = 0;
            A.error = 4;//出现了未定义的类型
            return A;
        }



/*
        A1 = ATom.check(A1);
        A2 = ATom.check(A2);
        if (A1.unaryATom.type == 0){
            //说明A1是数字
            if (A2.unaryATom.type == 0){
                //A2也是数字
                UD coe = UD.multiply(A1.quarkATom[0].coe,A2.quarkATom[0].coe);
                Quark [] Q = {new Quark(coe,0)};
                ATom A = new ATom(Q,A1.unaryATom);
                A = ATom.check(A);
                return A;
            }
            else {
                //A2是非数字项，那么就要把A1挨个乘上A2的每一项
                Quark [] Q = new Quark[A2.length];
                for (int i = 0;i<A2.length;i++){
                    UD coe = UD.multiply(A1.quarkATom[0].coe,A2.quarkATom[i].coe);
                    Q[i] = new Quark(coe,A2.quarkATom[i].degree);
                }
                ATom A = new ATom(Q,A2.unaryATom);
                A = ATom.check(A);
                return A;
            }
        }
        else if (A2.unaryATom.type == 0){
            //如果A2是数字
            if (A1.unaryATom.type == 0){
                //A1是常数项
                UD coe = UD.multiply(A1.quarkATom[0].coe,A2.quarkATom[0].coe);
                Quark [] Q = {new Quark(coe,0)};
                ATom A = new ATom(Q,A1.unaryATom);
                A = ATom.check(A);
                return A;
            }
            else {
                //A1不是常数项，A2乘上A1的每一项
                Quark [] Q = new Quark[A1.length];
                for (int i = 0;i<A1.length;i++){
                    UD coe = UD.multiply(A1.quarkATom[i].coe,A2.quarkATom[0].coe);
                    Q[i] = new Quark(coe,A1.quarkATom[i].degree);
                }
                ATom A = new ATom(Q,A1.unaryATom);
                A = ATom.check(A);
                return A;
            }
        }
        else {
            if (A1.unaryATom.equals(A2.unaryATom)) {
                int i, j;
                int l1 = A1.length;
                int l2 = A2.length;
                Quark[] Q = new Quark[l1 * l2];
                int k = 0;
                for (i = 0; i < l1; i++) {
                    for (j = 0; j < l2; j++) {
                        Q[k] = Quark.multiply(A1.quarkATom[i], A2.quarkATom[j]);
                        k++;
                    }
                }
                //乘完了，然后排序，合并，自检
                Q = Quark.sort(Q, l1 * l2);
                Q = Quark.merge(Q);
                if (Q.length == 0) {
                    return new ATom(0);
                } else {
                    if (A1.unaryATom.type == 0) {
                        if (A2.unaryATom.type == 0) {
                            //没办法，两个单项式都是数字
                            return new ATom(Q, A1.unaryATom);
                        } else {
                            return new ATom(Q, A2.unaryATom);
                        }
                    } else {
                        return new ATom(Q, A1.unaryATom);
                    }

                }
            } else {
                //不是同类项，不可以相乘
                ATom A = new ATom();
                A.error = 3;
                return A;
            }
        }*/
    }

    public static ATom less(ATom A1,ATom A2){
        ATom A = A2.reverse();
        return ATom.add(A1,A);
    }

    public static ATom check(ATom A){
        //自检，是为了删掉那些不必要存在的项，比如系数为0的项，以及计算一些可以计算的项，比如根号的平方，并进行排序和合并同类项，注意了，如果传入一个项数为0的ATom，那么要返回一个0多项式
        if (A.type == 0||A.type == 10||A.type == 11){
            if (A.type != 0){
                A.quarkATom[0].coe = A.quarkATom[0].coe.simplify();
                if (A.quarkATom[0].coe.u==0){
                    return A;
                }
                else if (A.quarkATom[0].coe.u==1){
                    if (A.quarkATom[0].coe.d==1){
                        A.type = 11;
                    }
                    else {
                        A.type = 1;
                    }
                    return A;
                }
                else {
                    A.type = 1;
                    return A;
                }
            }
            return A;
        }
        else if (A.type == 1){
            if (A.length>1){
                A.type = 2;
                return A.check();
            }
            else if (A.length == 1) {
                A.quarkATom[0].coe = A.quarkATom[0].coe.simplify();
                if (A.quarkATom[0].coe.u == 1 && A.quarkATom[0].coe.d == 1) {
                    A.type = 11;
                } else if (A.quarkATom[0].coe.u == 0) {
                    A.type = 10;
                }
                return A;
            }
            else {
                A.type = 0;
                A.error = 4;//未定义的类型
                return A;
            }
        }
        else if (A.type == 2) {
            int i, j = 0;
            //先删掉系数为0的项
            int l = 0;
            Quark[] q = new Quark[A.length];
            //普通的一元多项式
            for (i = 0; i < A.length; i++) {
                if (A.quarkATom[i].coe.u != 0) {
                    //出现有某一项的系数不为0
                    q[j] = Quark.copy(A.quarkATom[i]);
                    j++;
                    l++;
                }
            }
            //System.out.println("l = "+l);
            //for (int ii = 0;ii<l;ii++){
            //    System.out.printf("\nUD = %d/%d   deg = %d\n",q[ii].coe.u,q[ii].coe.d,q[ii].degree);
            //}
            //删完了，进行排序
            Quark[] q1 = Quark.sort(q, l);
            //for (int ii = 0;ii<q1.length;ii++){
            //    System.out.printf("\nUD = %d/%d   deg = %d\n",q1[ii].coe.u,q1[ii].coe.d,q1[ii].degree);
            //}
            //排序完成
            q1 = Quark.merge(q1);
            //for (int ii = 0;ii<q1.length;ii++){
            //    System.out.printf("\nUD = %d/%d   deg = %d\n",q1[ii].coe.u,q1[ii].coe.d,q1[ii].degree);
            //}
            ATom A1 = new ATom(q1, A.unaryATom);
            //这个地方，要注意0多项式
            if (A1.quarkATom!=null) {
                if (A1.length == 0) {
                    return new ATom(0);
                    //自检后，发现一项都没了，这个时候，需要返回0多项式
                } else if (A1.length == 1 && A1.quarkATom[0].degree == 0) {
                    if (A1.quarkATom[0].coe.u == 0) {
                        A1.type = 10;
                    } else if (A1.quarkATom[0].coe.u == 1 || A1.quarkATom[0].coe.u == A1.quarkATom[0].coe.d) {
                        A1.type = 11;
                    } else {
                        A1.type = 1;
                    }
                    return A1;
                } else {
                    A1.type = 2;
                    return A1;
                }
            }
            else{
                return new ATom(0);
            }
        }
        else {
            ATom A1 = new ATom();
            A1.type = 0;//出现了未定义的类型
            A1.error = 4;
            return A1;
        }
    }

    public static ATom reverse(ATom A){
        //A的每一个Quark取反
        if (A.type!=0){
            if (A.type == 10){
                return new ATom(0);
            }
            else if (A.type == 11){
                return new ATom(-1);
            }
            else if (A.type == 1) {
                UD coe = A.quarkATom[0].coe.reverse();
                if (coe.u * coe.d == 1) {
                    return new ATom(1);//取反之后 == 1，是1多项式
                } else {
                    return new ATom(coe);
                }
            }
            else if (A.type == 2) {
                int i;
                Quark[] Q = new Quark[A.length];
                for (i = 0; i < A.length; i++) {
                    Q[i] = Quark.reverse(A.quarkATom[i]);
                }
                return new ATom(Q, A.unaryATom);
            }
            else {
                ATom A1 = new ATom();
                A.type = 0;
                A.error = 4;//恶性bug，出现了未定义的类型
                return A1;
            }
        }
        else {
            return A;
        }
    }

    public static ATom division(ATom A1, ATom A2){
        //!注意：A2必须是A1的因式！而且必须是严格的多项式，仅用于找到GCD后进行化简！
        if(A1.type == 0 ||A2.type == 0) {
            ATom A = new ATom();
            A.type = 0;
            A.error = 5;//因式中出现了异常
            return A;
        }
        else if (A1.type == 10){
            return new ATom(0);
        }
        else if (A2.type == 10){
            ATom A = new ATom();
            A.type = 0;
            A.error = 8;//分母为0
            return A;
        }
        else if (A2.type == 11){
            return A1;
        }
        else if (A1.type == 11){
            if (A2.type == 1){
                UD u = new UD(A2.quarkATom[0].coe.d,A2.quarkATom[0].coe.u);
                return new ATom(u);
            }
            else if (A2.type == 2){
                //一般是不会出现这种情况的，因为这种情况理论上不存在，，，
                //System.out.print("出现了很奇怪的bug！因为理论上不会存在这种错误，除非开发者故意为之");
                ATom A = new ATom();
                A.type = 0;
                A.error = 6;
                return A;
            }
            else {
                ATom A = new ATom();
                A.type = 0;
                A.error = 6;
                return A;
            }
        }
        else if (A1.type == 1) {
            if (A2.type == 1) {
                //两个常数项相除
                UD coe = UD.division(A1.quarkATom[0].coe, A2.quarkATom[0].coe);
                return new ATom(coe);
            }
            else {
                //异常！，A1是常数项，它的因式也一定是常数项！
                ATom A = new ATom();
                A.type = 0;
                A.error = 6;//GCD求解错误！常数项的GCD只能是常数项
                return A;
            }
        }
        else if (A1.type == 2) {
            if (A2.type == 1) {
                int i;
                ATom A = ATom.copy(A1);
                for (i = 0;i<A1.length;i++) {
                    A.quarkATom[i].coe = UD.division(A.quarkATom[i].coe,A2.quarkATom[0].coe);
                }
                A = A.check();
                return A;
            }
            else if (A2.type == 2) {
                //核心代码，两个一般的一元多项式的除法
                int i;
                A1 = A1.check();
                A2 = A2.check();
                ATom A11 = ATom.copy(A1);//不能修参！
                int l = A1.quarkATom[0].degree - A2.quarkATom[0].degree + 1;
                Quark[] q = new Quark[l];
                //先给q进行初始化
                for (i = 0; i < l; i++) {
                    q[i] = new Quark();
                }
                i = 0;
                /*System.out.print("\n ---------- \n");
                System.out.print("\n A1 = ");
                A1.print();
                System.out.printf("    A1.type = %d,A1.error = %d   \n",A1.type,A1.error);
                System.out.print("\n A2 = ");
                A2.print();
                System.out.printf("    A2.type = %d,A2.error = %d   \n",A2.type,A2.error);*/
                while (A11.quarkATom[0].degree > 0&&i<l) {
                    //System.out.printf(" i = %d\n",i);
                    //A11.print();
                    ///System.out.println();
                    q[i] = new Quark();
                    q[i].degree = A11.quarkATom[0].degree - A2.quarkATom[0].degree;
                    q[i].coe = UD.division(A11.quarkATom[0].coe, A2.quarkATom[0].coe);
                    Quark[] q1 = new Quark[1];
                    q1[0] = q[i];
                    ATom A = new ATom(q1, A11.unaryATom);
                    ATom A3 = ATom.multiply(A2, A);
                    A3 = A3.check();
                    A11 = ATom.less(A11, A3);
                    i++;
                }
                if (q.length == 1){
                    if (q[0].degree == 0){
                        //常数项
                        return new ATom(q);
                    }
                    else {
                        ATom a = new ATom(q, A1.unaryATom);
                        a  = a.check();
                        return a;
                    }
                }
                else {
                    ATom a = new ATom(q, A1.unaryATom);
                    a = a.check();
                    /*System.out.println("\n测试用");
                    System.out.print("\nA1 = ");
                    A1.print();
                    System.out.print("\nA2 = ");
                    A2.print();
                    System.out.println();
                    System.out.printf("A2.unary.type = %d   A2.type = %d\n",A2.unaryATom.type,A2.type);
                    System.out.printf("A2.type = %d\n",A2.type);
                    System.out.printf("A1.unary.type = %d\n",A1.unaryATom.type);
                    A1.unaryATom.print();
                    a.print();
                    System.out.println("\n-----");*/
                    return a;
                }
            }
            else {
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else {
            ATom A = new ATom();
            A.type = 0;
            A.error = 4;//出现了未定义的类型
            return A;
        }
    }

    public static ATom ATomGCD(ATom A1, ATom A2) {
        //构建一个矩阵,认为输入的A1和A2都是严格的多项式，而且是自检过的
        //构建他们的系数向量
        if (A1.type == 0 || A2.type == 0) {
            //对象异常，报错
            ATom A = new ATom();
            A.type = 0;
            if (A1.type == 0 && A2.type != 0) {
                A.error = A1.error;
            } else if (A2.type == 0 && A1.type != 0) {
                A.error = A2.error;
            } else {
                A.error = 7;//对待求最大公因式的两项进行检查！
            }
            return A;
        }
        else if (A1.type == 10||A2.type == 10){
            //有0多项式
            return new ATom(0);
        }
        else if (A1.type == 11||A2.type == 11){
            return new ATom(1);
        }
        else if (A1.type == 1) {
            if (A2.type == 1) {
                //两个常数项的最大公因式，，，
                //要求两个分数的最大公因式么，还是写在UD类里面吧
                UD coe = UD.GCD(A1.quarkATom[0].coe, A2.quarkATom[0].coe);
                return new ATom(coe);
            } else if (A2.type == 2) {
                //A1是常数项，A2却是一般的一元多项式，直接返回常数项1
                return new ATom(1);
            } else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else if (A1.type == 2) {
            if (A2.type == 1) {
                //A2是常数项，A1是一般的一元多项式，直接返回常数项1
                return new ATom(1);
            }
            else if (A2.type == 2) {
                //核心代码，寻找两个一般的一元多项式的GCD
                int max1 = A1.quarkATom[0].degree;
                int max2 = A2.quarkATom[0].degree;
                int l = Matrix.max(max1, max2) + 1;
                UD[][] coeA = new UD[2][l];
                int i, j = 0;
                for (i = 0; i <= max1; i++) {
                    if (j < A1.length&&max1 - i == A1.quarkATom[j].degree) {
                        //说明这一项是存在的
                        coeA[0][i] = A1.quarkATom[j].coe;
                        j++;
                    } else {
                        //说明这一项不存在，系数为0
                        coeA[0][i] = new UD();
                    }
                }
                for (i = max1 + 1; i < l; i++) {
                    coeA[0][i] = new UD();
                }
                j = 0;
                for (i = 0; i <= max2; i++) {
                    if (j<A2.length&&max2 - i == A2.quarkATom[j].degree) {
                        //说明这一项是存在的
                        coeA[1][i] = A2.quarkATom[j].coe;
                        j++;
                    } else {
                        //说明这一项不存在，系数为0
                        coeA[1][i] = new UD();
                    }
                }
                for (i = max2 + 1; i < l; i++) {
                    coeA[1][i] = new UD();
                }
                /*System.out.print("\n本注释位于ATom 的ATomGCD中\n");
                System.out.println("\n展示coe矩阵");
                for (int iii = 0;iii<2;iii++){
                    for (int jjj = 0;jjj<l;jjj++){
                        coeA[iii][jjj].print();
                        System.out.print("\t");
                    }
                    System.out.println();
                }
                System.out.println("\n展示完毕");*/
                int flag1, flag2;
                int l1 = max1 + max2 + 2;
                for (i = 0; i < 2*l1; i++) {
                    //用次数低的去做掉次数高的
                    if (max1 >= max2) {
                        //A1次数更高
                        //A2的当前最高项一定不为0
                        UD temp = UD.reverse(UD.division(coeA[0][0], coeA[1][0]));
                        for (j = 0; j < l; j++) {
                            coeA[0][j] = UD.add(UD.multiply(coeA[1][j], temp), coeA[0][j]);
                        }
                        //coeA[0][l - 1] = new UD(0);
                        //现在，做掉了A1当前的最高项，
                        //max1--;
                        if (coeA[0][0].u == 0) {
                            //如果A1的当前最高项还是0，那么我要找到A1的第一个非零项
                            flag1 = 0;
                            for (j = 0; j < l; j++) {
                                if (coeA[0][j].u != 0) {
                                    break;
                                } else {
                                    flag1++;
                                }
                            }
                            if (flag1 == l) {
                                //找不到A1的第一个非零项，说明第一行全是0，第二行就是最大公因式，程序结束
                                Quark[] q = new Quark[l];
                                for (j = 0; j < l; j++) {
                                    q[j] = new Quark();//要初始化
                                    q[j].coe = coeA[1][j];
                                    q[j].degree = max2 - j;
                                }
                                ATom A = new ATom(q, A1.unaryATom);
                                return ATom.check(A);
                            }
                            else {
                                //找到了A1的第一个非零项，把它放在最前面
                                for (j = 0; j < l - flag1; j++) {
                                    coeA[0][j] = coeA[0][j + flag1];
                                }
                                for (j = l - flag1;j<l;j++) {
                                    coeA[0][j] = new UD(0);
                                }
                                max1 = max1 - flag1;
                            }
                        }
                    }
                    else {
                        //A2次数更高
                        //A1的当前最高项一定不为0
                        UD temp = UD.reverse(UD.division(coeA[1][0], coeA[0][0]));
                        for (j = 1; j < l; j++) {
                            coeA[1][j - 1] = UD.add(UD.multiply(coeA[0][j], temp), coeA[1][j]);
                        }
                        coeA[1][l - 1] = new UD(0);
                        /*System.out.println("\n展示coe矩阵");
                        for (int iii = 0;iii<2;iii++){
                            for (int jjj = 0;jjj<l;jjj++){
                                coeA[iii][jjj].print();
                                System.out.print("\t");
                            }
                            System.out.println();
                        }
                        System.out.println("\n展示完毕");*/
                        //现在，做掉了A2当前的最高项，
                        max2--;
                        if (coeA[1][0].u == 0) {
                            //如果A2的当前最高项为0，那么我要找到A2的第一个非零项
                            flag2 = 0;
                            for (j = 0; j < l; j++) {
                                if (coeA[1][j].u != 0) {
                                    break;
                                } else {
                                    flag2++;
                                }
                            }
                            if (flag2 == l) {
                                //找不到A2的第一个非零项，说明第二行全是0，第一行就是最大公因式，程序结束
                                Quark[] q = new Quark[l];
                                for (j = 0; j < l; j++) {
                                    q[j]=new Quark();
                                    q[j].coe = coeA[0][j];
                                    q[j].degree = max1 - j;
                                }
                                ATom A = new ATom(q, A1.unaryATom);
                                return ATom.check(A);
                            } else {
                                //找到了A2的第一个非零项，把它放在最前面
                                for (j = 0; j < l - flag2; j++) {
                                    coeA[1][j] = coeA[1][j + flag2];
                                }
                                for (j = l - flag2;j<l;j++) {
                                    coeA[1][j] = new UD(0);
                                }
                                max2 = max2 - flag2;
                            }
                        }
                    }
                    /*System.out.println("\n展示coe矩阵");
                    for (int iii = 0;iii<2;iii++){
                        for (int jjj = 0;jjj<l;jjj++){
                            coeA[iii][jjj].print();
                            System.out.print("\t");
                        }
                        System.out.println();
                    }
                    System.out.println("\n展示完毕");*/
                }
                //System.out.print("\n注释完毕\n");
                //如果在上述循环中未能找到最大公因式，那么就直接返回1吧
                return new ATom(1);
            } else {
                //未定义的类型
                ATom A = new ATom();
                A.type = 0;
                A.error = 4;//出现了未定义的类型
                return A;
            }
        }
        else {
            //未定义的类型
            ATom A = new ATom();
            A.type = 0;
            A.error = 4;//出现了未定义的类型
            return A;
        }
    }

    public static ATom ATomsToATom(AToms A){
        //使用本方法意味着可以进行类型转换
        A = A.check();
        if (A.type == 10){
            return new ATom(0);
        }
        else if (A.type == 11){
            return new ATom(1);
        }
        else if (A.type == 1){
            UD coe = UD.copy(A.quarks[0].coe);
            return new ATom(coe);
        }
        else if (A.type == 2){
            ATom A1;
            Quark [] q = new Quark[A.length];
            for (int i = 0;i<A.length;i++){
                q[i] = Quark.QuarksToQuark(A.quarks[i]);
                //System.out.printf("\nUD = %d/%d   deg = %d",q[i].coe.u,q[i].coe.d,q[i].degree);
            }
            //System.out.printf("%s",A.quarks[0].unKnow[0].quantum[0]);
            A1 = new ATom(q,A.quarks[0].unKnow[0]);
            //A1.print();
            //System.out.println("------------");
            A1.type = 2;
            return A1;
        }
        else {
            return new ATom();
        }
    }

    public static AToms ATomToAToms(ATom A){
        //调用本函数意味着该一元多项式的确可以转换为多元多项式
        if (A.type == 10||A.type == 0){
            return new AToms(0);
        }
        else if (A.type == 11){
            return new AToms(1);
        }
        else if (A.type == 1){
            UD U = UD.copy(A.quarkATom[0].coe);
            return new AToms(U);
        }
        else {
            Quarks[] Q = new Quarks[A.length];
            Quantum[] q = {A.unaryATom};
            for (int i = 0;i<A.length;i++){
                int[] d = new int[]{A.quarkATom[i].degree};
                Q[i] = new Quarks(A.quarkATom[i].coe,q,d);
            }
            return new AToms(Q);
        }
    }

    /**一元多项式转字符串*/
    public static String ATomToString(ATom A){
        //System.out.println("check");
        //A.print();
        //System.out.println("check!");
        A = A.check();
        switch (A.type){
            case 0:return "错误";
            case 10:return "0";
            case 11:return "1";
            case 1:return UD.UDToString(A.quarkATom[0].coe);
            case 2:{
                String s = "";
                if (A.quarkATom[0].coe.d==1){
                    //3x,3x^2,65x,注意，如果是第一项，那么前面不应该有+号
                    //注意：尽量不出现 1x 1x^2
                    if (A.quarkATom[0].coe.u ==1){
                        // x
                    }
                    else if (A.quarkATom[0].coe.u == -1){
                        s = s + "-";
                    }
                    else {
                        s = s + UD.UDToString(A.quarkATom[0].coe);
                    }
                    if (A.quarkATom[0].degree == 1){
                        s = s + Quantum.QuantumToString(A.unaryATom);
                    }
                    else if (A.quarkATom[0].degree == -1){
                        //3/x   1/x
                        UD coe  = new UD();
                        if (A.quarkATom[0].coe.u<0){
                            coe = new UD(-1*(A.quarkATom[0].coe.u),A.quarkATom[0].coe.d);
                        }
                        s = s + UD.UDToString(coe);
                        s = s + "/" + Quantum.QuantumToString(A.unaryATom);
                    }
                    else if (A.quarkATom[0].degree == 0){
                        //3x^0 = 3
                        if (A.quarkATom[0].coe.u == 1||A.quarkATom[0].coe.u == -1){
                            s = s + "1";
                        }
                    }
                    else if (A.quarkATom[0].degree>0){
                        s = s + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[0].degree);
                    }
                    else {
                        //degree<0
                        s = s + Quantum.QuantumToString(A.unaryATom) + "^" + "(" + String.valueOf(A.quarkATom[0].degree) + ")";
                    }
                }
                else {
                    //系数是分数
                    //需要达到的目标是：3/(5x) 3x/5   (3/5)x^2   -(1/2)x^3   -(3/5)x^(-2)
                    //之所以注释掉这部分的代码，是因为系数不会是分数，只会是整数
                    if (A.quarkATom[0].degree == 1) {
                        //3x/5   -3x/5   x/2   -x/2
                        if (A.quarkATom[0].coe.u == 1) {
                            //x/2
                            s = s + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[0].coe.d);
                        } else if (A.quarkATom[0].coe.u == -1) {
                            //-x/2
                            s = s + "-" + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[0].coe.d);
                        } else {
                            //3x/5   -3x/5
                            s = s + String.valueOf(A.quarkATom[0].coe.u) + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[0].coe.d);
                        }
                    }
                    else if (A.quarkATom[0].degree == -1){
                        // 3/(5x)   -3/(5x)
                        s = s + String.valueOf(A.quarkATom[0].coe.u) + "/(" + String.valueOf(A.quarkATom[0].coe.d) + Quantum.QuantumToString(A.unaryATom) + ")";
                    }
                    else if (A.quarkATom[0].degree == 0){
                        // -3/5
                        if (A.quarkATom[0].coe.u>0){
                            s = s + "+" + UD.UDToString(A.quarkATom[0].coe);
                        }
                        else if (A.quarkATom[0].coe.u == 0){
                            //
                        }
                        else {
                            //u<0
                            s = s + UD.UDToString(A.quarkATom[0].coe);
                        }
                    }
                    else if (A.quarkATom[0].degree > 0){
                        //  (3/5)x^2   -(3/5)x^3
                        if (A.quarkATom[0].coe.u<0){
                            s = s + "-(" + String.valueOf(-1*A.quarkATom[0].coe.u) + "/" + String.valueOf(A.quarkATom[0].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[0].degree);
                        }
                        else {
                            //u>0
                            s = s + "(" + String.valueOf(A.quarkATom[0].coe.u) + "/" + String.valueOf(A.quarkATom[0].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[0].degree);
                        }
                    }
                    else {
                        //degree<0
                        // (3/5)x^(-2)    -(3/8)x^(-2)
                        if (A.quarkATom[0].coe.u<0){
                            s = s + "-(" + String.valueOf(-1*A.quarkATom[0].coe.u) + "/" + String.valueOf(A.quarkATom[0].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^(" + String.valueOf(A.quarkATom[0].degree) + ")";
                        }
                        else {
                            //u>0
                            s = s + "(" + String.valueOf(A.quarkATom[0].coe.u) + "/" + String.valueOf(A.quarkATom[0].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^(" + String.valueOf(A.quarkATom[0].degree) + ")";
                        }
                    }
                    //s = s + "错误";
                }
                for (short i = 1;i<A.quarkATom.length;i++){
                    if (A.quarkATom[i].coe.d==1){
                        //3x,3x^2,65x,注意，如果是第一项，那么前面不应该有+号
                        //注意：尽量不出现 1x 1x^2
                        if (A.quarkATom[i].coe.u ==1){
                            s = s + "+";
                        }
                        else if (A.quarkATom[i].coe.u == -1){
                            s = s + "-";
                        }
                        else {
                            if (A.quarkATom[i].coe.u > 0){
                                s = s + "+";
                            }
                            s = s + UD.UDToString(A.quarkATom[i].coe);
                        }
                        if (A.quarkATom[i].degree == 1){
                            s = s + Quantum.QuantumToString(A.unaryATom);
                        }
                        else if (A.quarkATom[i].degree == -1){
                            //3/x   1/x
                            UD coe  = new UD();
                            if (A.quarkATom[i].coe.u<0){
                                coe = new UD(-1*(A.quarkATom[i].coe.u),A.quarkATom[i].coe.d);
                            }
                            s = s + UD.UDToString(coe);
                            s = s + "/" + Quantum.QuantumToString(A.unaryATom);
                        }
                        else if (A.quarkATom[i].degree == 0){
                            //3x^0 = 3
                            if (A.quarkATom[i].coe.u == 1||A.quarkATom[i].coe.u == -1){
                                s = s + "1";
                            }
                        }
                        else if (A.quarkATom[i].degree>0){
                            s = s + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[i].degree);
                        }
                        else {
                            //degree<0
                            s = s + Quantum.QuantumToString(A.unaryATom) + "^" + "(" + String.valueOf(A.quarkATom[i].degree) + ")";
                        }
                    }
                    else {
                        //系数是分数，实际上，这种情况基本不会出现
                        //需要达到的目标是：3/(5x) 3x/5   (3/5)x^2   -(1/2)x^3   -(3/5)x^(-2)
                        if (A.quarkATom[i].degree == 1) {
                            //3x/5   -3x/5   x/2   -x/2
                            if (A.quarkATom[i].coe.u == 1) {
                                //x/2
                                s = s + "+" + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[i].coe.d);
                            } else if (A.quarkATom[i].coe.u == -1) {
                                //-x/2
                                s = s + "-" + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[i].coe.d);
                            } else {
                                //3x/5   -3x/5
                                if (A.quarkATom[i].coe.u>0){
                                    s = s + "+";
                                }
                                s = s + String.valueOf(A.quarkATom[i].coe.u) + Quantum.QuantumToString(A.unaryATom) + "/" + String.valueOf(A.quarkATom[i].coe.d);
                            }
                        }
                        else if (A.quarkATom[i].degree == -1){
                            // 3/(5x)   -3/(5x)
                            if (A.quarkATom[i].coe.u>0){
                                s = s + "+";
                            }
                            s = s + String.valueOf(A.quarkATom[i].coe.u) + "/(" + String.valueOf(A.quarkATom[i].coe.d) + Quantum.QuantumToString(A.unaryATom) + ")";
                        }
                        else if (A.quarkATom[i].degree == 0){
                            // -3/5
                            if (A.quarkATom[i].coe.u>0){
                                s = s + "+" + UD.UDToString(A.quarkATom[i].coe);
                            }
                            else if (A.quarkATom[i].coe.u == 0){
                                //
                            }
                            else {
                                //u<0
                                s = s + UD.UDToString(A.quarkATom[i].coe);
                            }
                        }
                        else if (A.quarkATom[i].degree > 0){
                            //  (3/5)x^2   -(3/5)x^3
                            if (A.quarkATom[i].coe.u<0){
                                s = s + "-(" + String.valueOf(-1*A.quarkATom[i].coe.u) + "/" + String.valueOf(A.quarkATom[i].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[i].degree);
                            }
                            else {
                                //u>0
                                s = s + "+(" + String.valueOf(A.quarkATom[i].coe.u) + "/" + String.valueOf(A.quarkATom[i].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^" + String.valueOf(A.quarkATom[i].degree);
                            }
                        }
                        else {
                            //degree<0
                            // (3/5)x^(-2)    -(3/8)x^(-2)
                            if (A.quarkATom[i].coe.u<0){
                                s = s + "-(" + String.valueOf(-1*A.quarkATom[i].coe.u) + "/" + String.valueOf(A.quarkATom[i].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^(" + String.valueOf(A.quarkATom[i].degree) + ")";
                            }
                            else {
                                //u>0
                                s = s + "+(" + String.valueOf(A.quarkATom[i].coe.u) + "/" + String.valueOf(A.quarkATom[i].coe.d) + ")" + Quantum.QuantumToString(A.unaryATom) + "^(" + String.valueOf(A.quarkATom[i].degree) + ")";
                            }
                        }
                        //s = s + "错误";
                    }
                }
                return s;
            }
            default:{
                return "未定义的类型";
            }
        }
    }

    public void print(){ print(this); }

    public void empty(){
        this.length = 1;
        this.quarkATom[0]=new Quark();
    }

    public ATom add(ATom A){ return add(this,A); }

    public ATom less(ATom A){ return less(this,A); }

    public ATom multiply(ATom A){ return multiply(this,A); }

    public ATom check(){ return check(this); }

    public ATom reverse(){ return reverse(this); }
}