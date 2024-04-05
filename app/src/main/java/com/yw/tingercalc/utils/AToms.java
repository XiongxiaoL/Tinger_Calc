package com.yw.tingercalc.utils;

public class AToms {
    int length;
    Quarks[] quarks;
    byte type = 0;
    byte error = 0;

    /*
     * error = 0 表示没有错误
     * error = 1 待操作两个对象均异常
     * */

    /*
     * type = 0 表示这是一个异常
     * type = 1 表示这是一个常数项
     * type = 10 表示这是0多项式
     * type = 11 表示这是1多项式
     * type = 2 表示这是一个一般的多元多项式
     * */

    public AToms(String string){
        /*多项式的构造器，思路是这样的：把字符串扫描一遍，以+号和-号作为标志符，对字符串进行切割，
        然后把切割后的字符串传入到单项式的构造器中即可完成多项式的构造*/
        string = string.replace(" ", "");//去除掉所有的空格
        string = string.replace("*", "");//去除掉所有的*号
        int l = string.length();
        char[] Input = string.toCharArray();
        int i,j = 0,k=0;
        char p;
        Quarks [] Qu = new Quarks[15];
        for (i = 0;i<l;i++){
            p = Input[i];
            if ((p == '-'||p == '+')&& i != 0) {
                //i!=0 是为了排除第一个符号就是负号或者正号的情况
                //System.out.printf("\n j = %d , i = %d\n",j,i);
                if (i == l-1){
                    //System.out.printf("\n j = %d , i = %d 读完了吧，，，\n",j,i);
                    String str = string.substring(j,l);
                    //System.out.println(str);
                    Qu[k] = new Quarks(str);
                    //Qu[k].print();
                    //System.out.println("\n");
                    k++;
                    break;
                }
                else if(Input[i-1]=='('||Input[i-1]=='^'||Input[i-1]=='（') {
                    //这个时候是不能读的
                }
                else {
                    String str = string.substring(j, i);
                    //System.out.println(str);
                    Qu[k] = new Quarks(str);
                    //Qu[k].print();
                    //System.out.println("\n");
                    //System.out.println();
                    k++;
                    j = i;
                }
            }
            else if (i == l-1){
                //System.out.printf("\n j = %d , i = %d 读完了\n",j,i);
                String str = string.substring(j,l);
                //System.out.println(str);
                Qu[k] = new Quarks(str);
                //Qu[k].print();
                //System.out.println("\n");
                k++;
                break;
            }
        }
        Quarks [] Q = new Quarks[k];
        for (i = 0;i < k;i++){
            Q[i] = Qu[i];
        }
        this.length = k;
        if (k == 0){
            this.type = 0;
            this.error = 2;
        }
        else if (k == 1){
            this.quarks = Q;
            //如果只有一项
            //需要判断是否是0多项式或者1多项式
            if (Q[0].type == 1){
                if (Q[0].coe.u == 0){
                    this.type = 10;
                }
                else if (Q[0].coe.u == Q[0].coe.d){
                    this.type = 11;
                }
                else{
                    this.type = 1;
                }
            }
            else if (Q[0].type == 2){
                this.type = 2;
            }
            else {
                /*System.out.println("!!!");
                Q[0].print();
                System.out.println();
                System.out.printf("Q[0].type = ",Q[0].type);
                System.out.println();*/
                this.type = 0;
            }
        }
        else {
            this.type = 2;
            this.quarks = Q;
        }
    }

    public AToms(Quarks [] quarks){
        this.length=quarks.length;
        int k = quarks.length;
        if (k == 0){
            this.type = 0;
            this.error = 2;
        }
        else if (k == 1){
            this.quarks = quarks;
            //如果只有一项
            //需要判断是否是0多项式或者1多项式
            if (quarks[0].type == 1){
                if (quarks[0].coe.u == 0){
                    this.type = 10;
                }
                else if (quarks[0].coe.u == quarks[0].coe.d){
                    this.type = 11;
                }
                else{
                    this.type = 1;
                }
            }
            else if (quarks[0].type == 2){
                this.type = 2;
            }
            else {
                this.type = 0;
            }
        }
        else {
            this.type = 2;
            this.quarks = quarks;
        }
    }

    public AToms(Quarks quarks){
        this.length = 1;
        Quarks[] q = new Quarks[1];
        q[0] = quarks;
        this.quarks = q;
        this.type = 2;
    }

    public AToms(int x){
        if (x==0){
            this.type = 10;
            this.quarks = new Quarks[]{
                    new Quarks(0)
            };
        }
        else if (x == 1){
            this.type = 11;
            this.quarks = new Quarks[]{
                    new Quarks(1)
            };
        }
        else {
            this.type = 1;
            this.quarks = new Quarks[]{
                    new Quarks(x)
            };
        }
        this.length = 1;
    }

    public AToms(UD coe){
        coe = coe.simplify();
        if (coe.u==0){
            this.type = 10;
            this.quarks = new Quarks[]{
                    new Quarks(0)
            };
        }
        else if (coe.u == coe.d){
            this.type = 11;
            this.quarks = new Quarks[]{
                    new Quarks(1)
            };
        }
        else {
            this.type = 1;
            this.quarks = new Quarks[]{
                    new Quarks(coe)
            };
        }
        this.length=1;
    }

    public AToms(){
        this.length=1;
        this.quarks= new Quarks[]{
                new Quarks()
        };
        this.error = 0;
        this.type = 0;
    }

    public void print(){ print(this); }

    public AToms add(AToms A){ return add(this,A); }

    public AToms less(AToms A){ return less(this,A); }

    public AToms multiply(AToms A){ return multiply(this,A); }

    public AToms check(){ return check(this); }

    public AToms reverse(){ return reverse(this); }

    public static void printError(AToms A){
        switch (A.error){
            case 0:{
                System.out.print("没有问题");
                break;
            }
            case 1:{
                System.out.print("待操作的两对象异常");
                break;
            }
            case 2:{
                System.out.print("未定义的类型");
                break;
            }
            case 3:{
                System.out.print("待传递的error");
                break;
            }
            default:{
                System.out.print("未知error");
            }
        }
    }

    public static void print(AToms A){
        if (A.type == 0){
            printError(A);
        }
        else if (A.type == 1||A.type == 10||A.type == 11){
            A.quarks[0].coe.print();
        }
        else if (A.type == 2) {
            A.quarks[0].print();
            for (int i = 1; i < A.length; i++) {
                if (A.quarks[i].coe.u*A.quarks[i].coe.d>0){
                    System.out.print("+");
                }A.quarks[i].print();
            }
        }
        else {
            printError(A);
        }
    }

    public static AToms add(AToms A1,AToms A2){
        A1 = A1.check();
        A2 = A2.check();
        //System.out.println();
        //A1.print();
        //System.out.printf("\tA1.type = %d,A1.error = %d\n",A1.type,A1.error);
        //System.out.println();
        //A2.print();
        //System.out.printf("\tA2.type = %d,A2.error = %d\n",A2.type,A2.error);
        if (A1.type == 0||A2.type == 0){
            if (A1.type == 0&&A2.type!=0){
                AToms A = new AToms();
                A.error = A1.error;
                return A;
            }
            else if (A1.type != 0){
                AToms A = new AToms();
                A.error = A2.error;
                return A;
            }
            else{
                AToms A = new AToms();
                A.error = 1;//待操作的两个对象均异常
                return A;
            }
        }
        else{
            if (A1.type == 10){
                return AToms.copy(A2);
            }
            else if (A2.type == 10){
                //System.out.print("\n\n??");
                //A1.print();
                //System.out.println("\n");
                return AToms.copy(A1);
            }
            else if (A1.type == 11||A1.type == 1){
                //一般来说，常数项都是位于多项式的最后一项的，这是由字典排序法决定的,必须得先搞清楚A2含不含常数项
                if (A2.type == 11||A2.type == 1){
                    //A2也是常数项
                    Quarks [] q = {
                            new Quarks(UD.add(A1.quarks[0].coe,A2.quarks[0].coe))
                    };
                    AToms A = new AToms(q);
                    return check(A);
                }
                else if (A2.type==2){
                    //A2是一般项
                    if (A2.quarks[A2.length - 1].type==1){
                        //A2的最后一项是常数项
                        AToms A = AToms.copy(A2);
                        A.quarks[A.length - 1].coe = UD.add(A1.quarks[0].coe,A.quarks[A.length - 1].coe);
                        return check(A);
                    }
                    else if (A2.quarks[A2.length - 1].type==2){
                        //A2的最后一项不是常数项，需要创建一个
                        Quarks [] q = new Quarks[A2.length + 1];
                        for (int i = 0;i<A2.length;i++){
                            q[i] = Quarks.copy(A2.quarks[i]);
                        }
                        q[A2.length] = new Quarks(A1.quarks[0].coe);
                        AToms A = new AToms(q);
                        return check(A);
                    }
                    else {
                        AToms A = new AToms();
                        A.error = 2;//未定义的类型
                        return A;
                    }
                }
                else {
                    AToms A = new AToms();
                    A.error = 2;//未定义的类型
                    return A;
                }
            }
            else if (A1.type == 2){
                //A1是一般项
                if (A2.type == 11||A2.type == 1){
                    //A2是常数项
                    if (A1.quarks[A1.length - 1].type==1){
                        //A1的最后一项是常数项
                        AToms A = AToms.copy(A1);
                        A.quarks[A.length - 1].coe = UD.add(A2.quarks[0].coe,A.quarks[A1.length - 1].coe);
                        return check(A);
                    }
                    else if (A1.quarks[A1.length - 1].type==2){
                        //A1的最后一项不是常数项，需要创建一个
                        Quarks [] q = new Quarks[A1.length + 1];
                        for (int i = 0;i<A1.length;i++){
                            q[i] = Quarks.copy(A1.quarks[i]);
                        }
                        q[A1.length] = new Quarks(A2.quarks[0].coe);
                        AToms A = new AToms(q);
                        return check(A);
                    }
                    else {
                        AToms A = new AToms();
                        A.error = 2;//未定义的类型
                        return A;
                    }
                }
                else if (A2.type==2){
                    //A2是一般项，核心代码
                    int length = A1.length+A2.length;
                    int i;
                    Quarks [] Q = new Quarks[length];
                    for (i = 0;i<A1.length;i++) {
                        Q[i] = Quarks.copy(A1.quarks[i]);
                    }
                    for (i = A1.length;i<length;i++){
                        Q[i] = Quarks.copy(A2.quarks[i - A1.length]);
                    }
                    //拼接完成
                    Q = Quarks.sort(Q,length);//排序
                    Q = Quarks.merge(Q);//合并
                    AToms A = new AToms(Q);
                    //System.out.print("\n自检函数里面A= \n");
                    //A.print();
                    //System.out.println();
                    A = AToms.check(A);
                    return A;
                }
                else {
                    AToms A = new AToms();
                    A.error = 2;//未定义的类型
                    return A;
                }
            }
            else {
                AToms A = new AToms();
                A.error = 2;//未定义的类型
                return A;
            }
        }
    }

    public static AToms less(AToms A1,AToms A2){
        AToms A3 = AToms.reverse(A2);
        A3 = AToms.add(A1,A3);
        return A3;
    }

    /**本方法用于两个多元多项式相乘，暂时用定义相乘 -- 2020.12.5*/
    public static AToms multiply(AToms A1,AToms A2){
        //System.out.printf("\n自检前  A1.type = %d,A1.error = %d\n",A1.type,A1.error);
        //System.out.printf("\tA2.type = %d,A2.error = %d 自检前结束\n",A2.type,A2.error);
        A1 = A1.check();
        //System.out.printf("\n自检  A1.type = %d,A1.error = %d\n",A1.type,A1.error);
        A2 = A2.check();
        //System.out.printf("\tA2.type = %d,A2.error = %d 自检结束\n",A2.type,A2.error);
        if (A1.type == 0||A2.type == 0){
            if (A1.type == 0&&A2.type!=0){
                return AToms.copy(A1);
            }
            else if (A1.type != 0){
                return AToms.copy(A2);
            }
            else {
                AToms A = new AToms();
                A.type = 0;
                A.error = 1;//待操作的两个对象异常
                return A;
            }
        }
        else if (A1.type == 10||A2.type == 10){
            return new AToms(0);
        }
        else if (A1.type == 11){
            return AToms.copy(A2);
        }
        else if (A2.type == 11){
            return AToms.copy(A1);
        }
        else if (A1.type == 1){
            if (A2.type == 1){
                AToms A = new AToms(UD.multiply(A1.quarks[0].coe,A2.quarks[0].coe));
                A = A.check();
                return A;
            }
            else if (A2.type == 2){
                //A2的每一项都要乘上A1
                AToms A = AToms.copy(A2);
                for (int i = 0;i<A2.length;i++){
                    A.quarks[i].coe = UD.multiply(A1.quarks[0].coe,A2.quarks[i].coe);
                }
                A = A.check();
                return A;
            }
            else {
                AToms A = new AToms();
                A.type = 0;
                A.error = 2;//未定义的类型
                return A;
            }
        }
        else if (A2.type == 1){
            AToms A;
            if (A1.type == 2){
                //A1的每一项都要乘上A2
                A = AToms.copy(A1);
                for (int i = 0;i<A1.length;i++){
                    A.quarks[i].coe = UD.multiply(A2.quarks[0].coe,A1.quarks[i].coe);
                }
                A = A.check();
            }
            else {
                A = new AToms();
                A.type = 0;
                A.error = 2;//未定义的类型
            }
            return A;
        }
        else if (A1.type == 2){
            //这个时候A2的type也应该是2吧
            if (A2.type == 2) {
                //核心代码
                int i, j;
                int l1 = A1.length;
                int l2 = A2.length;
                Quarks[] Q = new Quarks[l1 * l2];
                int k = 0;
                for (i = 0; i < l1; i++) {
                    for (j = 0; j < l2; j++) {
                        /*A1.quarks[i].print();
                        System.out.print(" * ");
                        A2.quarks[j].print();
                        System.out.print(" = ");*/
                        Q[k] = Quarks.multiply(A1.quarks[i], A2.quarks[j]);
                        /*Q[k].print();
                        System.out.println();*/
                        k++;
                    }
                }
                //乘完了，然后排序，合并，自检
                /*System.out.print("\n合并前\n");
                for (i = 0; i < l1 * l2; i++) {
                    Q[i].print();
                    System.out.print("\t");
                }
                System.out.println();*/
                Quarks[] Q1 = Quarks.merge(Q);
                /*System.out.printf("\n合并后\n");
                for (i = 0; i < Q1.length; i++) {
                    Q1[i].print();
                    System.out.printf("\t");
                }
                System.out.println();*/
                AToms A = new AToms(Q1);
                A = A.check();
                return A;
            }
            else{
                AToms A = new AToms();
                A.type = 0;
                A.error = 2;//未定义的类型
                return A;
            }
        }
        else {
            AToms A = new AToms();
            A.type = 0;
            A.error = 2;//未定义的类型
            return A;
        }
    }

    /**本方法用于删去无用的单项式（系数为 0）并检查有没有相同的单项式，如果有，合并他们，本方法暂不排序--2020.11.8,
     * 目前已经做到了给每一个单项式定义一个数值，故可以排序 -- 2020.12.5
     * 目前已经放弃了给单项式定义数值的做法，按照字典排序法排序 -- 2020.12.27*/
    public static AToms check(AToms A){
        if (A.type == 0||A.type == 10||A.type == 11){
            //对系数进行检查
            if (A.length==1) {
                if (A.quarks[0].coe.u == 0) {
                    A.type = 10;//0多项式
                } else if (A.quarks[0].coe.u == 1 && A.quarks[0].coe.d == 1) {
                    A.type = 11;//1多项式
                } else {
                    A.type = 1;
                    return A.check();
                }
                return A;
            }
            else {
                if (A.type==0){
                    return A;
                }
                else {
                    A.type = 2;
                    return A.check();
                }
            }
        }
        else if (A.type == 1){
            //常数项化简一下系数就可以了
            if (A.length==1) {
                /*System.out.print("\n常数项化简\n");
                System.out.print("\n化简前为");
                A.quarks[0].coe.print();
                A.quarks[0].coe = A.quarks[0].coe.simplify();
                System.out.print("\n化简后为");
                A.quarks[0].coe.print();
                System.out.println();*/
                //A.print();System.out.printf("\nA.type = %d,A.error = %d\n",A.type,A.error);
                if (A.quarks[0].coe.u == 0) {
                    A.type = 10;
                } else if (A.quarks[0].coe.u == 1 && A.quarks[0].coe.d == 1) {
                    A.type = 11;
                }
                return A;
            }
            else {
                A.type = 2;
                return A.check();
            }
        }
        else if (A.type == 2){
            if (A.length == 0){
                return new AToms(0);
            }
            else {
                //一般项，核心代码
                int i;
                Quarks[] Q = new Quarks[A.length];
                Quarks[] Q1 = new Quarks[A.length];
                for (i = 0; i < A.length; i++) {
                    Q1[i] = A.quarks[i].check();
                }
                Q = Quarks.merge(Q1);
                //下面要对A的类型进行判断
                if (Q.length == 0) {
                    return new AToms(0);
                }
                else if (Q.length == 1) {
                    if (Q[0].type == 1) {
                        if (Q[0].coe.u == 0) {
                            AToms A1 = new AToms(0);
                            A1.type = 10;//0多项式
                            return A1;
                        }
                        else if (Q[0].coe.u == Q[0].coe.d) {
                            AToms A1 = new AToms(1);
                            A1.type = 11;//1多项式
                            return A1;
                        }
                        else {
                            AToms A1 = new AToms(Q);
                            A1.type = 1;
                            return A1;
                        }
                    }
                    else if (Q[0].type == 2) {
                        return new AToms(Q);
                    }
                    else if (Q[0].type == 0) {
                        AToms A1 = new AToms();
                        A1.error = 3;//待传递的error////////////////////////////
                        return A1;
                    }
                    else {
                        AToms A1 = new AToms();
                        A1.error = 2;//未定义的类型
                        return A1;
                    }
                }
                else {
                    return new AToms(Q);
                }
            }
        }
        else {
            AToms A1 = new AToms();
            A1.error = 2;//未定义的类型
            return A1;
        }
    }

    public static AToms copy(AToms A){
        int l = A.length;
        Quarks [] Q = new Quarks[l];
        for (int i = 0;i<l;i++){
            Q[i] = Quarks.copy(A.quarks[i]);
        }
        return new AToms(Q);
    }

    public static AToms reverse(AToms A){
        if (A.type == 0){
            return A;
        }
        else if (A.type == 10){
            return A;
        }
        else if (A.type == 11||A.type == 1){
            AToms a = AToms.copy(A);
            for (int i = 0; i < A.length; i++) {
                a.quarks[i].coe.u = -1 * a.quarks[i].coe.u;
            }
            a.type = 1;//1多项式取反后就不再是1多项式了
            return a;
        }
        else if (A.type == 2){
            AToms a = AToms.copy(A);
            for (int i = 0; i < A.length; i++) {
                a.quarks[i].coe.u = -1 * a.quarks[i].coe.u;
            }
            return a;
        }
        else {
            return A;
        }
    }

    public static AToms UDToAToms(UD u){ return new AToms(u); }

    /**本函数仅用于多元多项式除以一元多项式，并返回商式和余式,A1是被除式，A2是除式，A2必须是一元多项式！特别的，这个多项式应该只是x - a这种形式（a为常整数）*/
    public static AToms[] division(AToms A1,AToms A2){
        //大体思路和一元多项式的除法是相同的。
        A1 = A1.check();
        A2 = A2.check();//A1和A2都是字典排序法！
        //首先，保证是整系数
        /*UD GCD = new UD(1);//先寻找最小公倍数，把分数化为整数
        for (int i = 0;i<A1.length;i++){
            GCD = UD.GCD(A1.quarks[i].coe,GCD);
        }
        for (int i = 0;i<A2.length;i++){
            GCD = UD.GCD(A2.quarks[i].coe,GCD);
        }
        //求出GCD后，给每个系数都除以这个公因式
        for (int i = 0;i<A1.length;i++){
            A1.quarks[i].coe = UD.division(A1.quarks[i].coe,GCD);
        }
        for (int i = 0;i<A2.length;i++){
            A2.quarks[i].coe = UD.division(A2.quarks[i].coe,GCD);
        }*/
        //完成！现在A1和A2都是整系数多项式了，注意到，这个地方修参了，为什么要修参？因为本函数是ATomsUD类中的check函数的子函数的ATomsGCD函数的子函数，修参是可以的--2021.3.4
        //A2作为一个形如x - 5 的一元多项式，它的文字应该是在A1中是可以找到的，如果不能，报出bug
        //思路是这样的，鉴于每次其实是一个多元单项式去除以一个一元多项，那么每次都需要去检查A1中是否还存在含有这个元的项，程序结束的条件就是A1中不存在含有这个元的项了



        if (A1.length >= A2.length) {
            AToms B1 = AToms.copy(A1);
            int num = 0;//num用来记录商式的项数；
            Quarks[] Q = new Quarks[10000];
            int i;
            for (i = 0; i < B1.length; ) {
                if (Quarks.exist(B1.quarks[i], A2.quarks[0]) != -1) {
                    //找到了含有A2的文字的单项式
                    Q[num] = Quarks.division(B1.quarks[i], A2.quarks[0]);
                    AToms t1 = new AToms(Q[num]);
                    AToms temp = AToms.multiply(A2, t1);
                    B1 = AToms.less(B1, temp);
                    B1 = B1.check();
                    i = 0;
                    num++;
                } else {
                    i++;
                }
            }
            if (num == 0) {
                if (A2.type == 10) {
                    return new AToms[]{new AToms(0), new AToms(0)};
                }
                if (A2.type == 1) {
                    //A2是数字
                    for (i = 0; i < B1.length; i++) {
                        B1.quarks[i].coe = UD.division(B1.quarks[i].coe, A2.quarks[0].coe);
                    }
                }
                return new AToms[]{B1, new AToms(0)};
            } else {
                Quarks[] Q2 = new Quarks[num];
                //System.out.printf("\n num = %d\n",num);
                for (i = 0; i < num; i++) {
                    Q2[i] = Q[i];
                }
                AToms B2 = new AToms(Q2);
                //System.out.print("自检前 ");
                //B2.print();
                B2 = B2.check();
                //System.out.print("\t自检后 ");
                //B2.print();
                //System.out.println();
                return new AToms[]{B2, B1};//返回的AToms数组中，第一个是商式 第二个是余式
            }
        }
        else {
            return new AToms[]{new AToms(0),AToms.copy(A1)};
        }
    }

    /**本函数用于获取一个多元多项式A 针对一个文字Q的次数，也是多元GCD函数的子函数之一*/
    public static int deg(AToms A, Quantum q){
        int deg = 0;
        Quarks Q = new Quarks(q);
        for (int i = 0;i<A.length;i++){
            int index = Quarks.exist(A.quarks[i],Q);
            if (index != -1){
                //在A中找到了含有q的单项式
                int temp = A.quarks[i].degree[index];
                if (temp>deg){
                    deg = temp;
                }
            }
        }
        return deg;
    }

    /**本函数用于，额，不太好描述，CRT函数的子函数，CRT函数是多元GCD函数的子函数之一，用途：C * D = 1 mod P,其中 C，P已知，返回D*/
    public static AToms Euclid(AToms C,AToms P) {
        AToms x = AToms.division(C,P)[1];//获取余式
        //余式的倒数即为逆元
        //一般的，这个余式是一个常数
        UD coe = new UD(x.quarks[0].coe.d,x.quarks[0].coe.u);
        return new AToms(coe);
    }

    /**本函数为通过中国剩余定理来对多项式的元进行提升*/
    public static AToms CRT(int[] Pa,AToms[] R,Quantum xi,int n){
        AToms result = new AToms(0);
        int j;
        for (j = 0;j<n;j++){
            //先构造P[j] = (x - pa[j]);
            AToms P = AToms.getAToms(xi,Pa[j]);
            //得到P后，构造C
            AToms C = new AToms(1);
            for (int jj = 0;jj<n;jj++){
                if (jj!=j){
                    AToms pp = AToms.getAToms(xi,Pa[j]);
                    C = AToms.multiply(C,pp);
                }
            }//C构造完成
            //计算D，使得 C * D = 1 mod(P)
            AToms D = AToms.Euclid(C,P);
            result = AToms.add(result,AToms.multiply(AToms.multiply(C,D),R[j]));
        }
        return result;
    }

    /**本函数用于返回一个这样的多项式: x - a */
    public static AToms getAToms(Quantum x,int a){
        Quarks[] q = new Quarks[2];
        q[0] = new Quarks(x);
        q[1] = new Quarks(-1*a);
        return new AToms(q);
    }

    /**本函数用于返回一个多元多项式的字符集合*/
    public static Quantum[] getQ(AToms A1,AToms A2) {
        //为了提高速度，这样做，将所有的字符集全部存到一个Quantum数组里面，然后对tag进行排序，然后消去相同的就行
        A1 = A1.check();
        A2 = A2.check();
        Quantum[] Q = new Quantum[10000];
        //把A2的字符集找到
        int l = 0;
        if (A1.type != 2) {
            if (A2.type != 2) {
                return new Quantum[]{};
            } else {
                for (int i = 0; i < A2.length; i++) {
                    if (A2.quarks[i].type == 2) {
                        for (int j = 0; j < A2.quarks[i].numQuantum; j++) {
                            for (int k = 0; k < A2.quarks[i].unKnow.length; k++) {
                                Q[l] = Quantum.copy(A2.quarks[i].unKnow[k]);
                                l++;
                            }
                        }
                    }
                }
            }
        }
        else {
            //A1不是数字项
            if (A2.type != 2) {
                for (int i = 0; i < A1.length; i++) {
                    if (A1.quarks[i].type == 2) {
                        for (int j = 0; j < A1.quarks[i].numQuantum; j++) {
                            for (int k = 0; k < A1.quarks[i].unKnow.length; k++) {
                                Q[l] = Quantum.copy(A1.quarks[i].unKnow[k]);
                                l++;
                            }
                        }
                    }
                }
            }
            else {
                for (int i = 0; i < A1.length; i++) {
                    if (A1.quarks[i].type == 2) {
                        for (int j = 0; j < A1.quarks[i].numQuantum; j++) {
                            for (int k = 0; k < A1.quarks[i].unKnow.length; k++) {
                                Q[l] = Quantum.copy(A1.quarks[i].unKnow[k]);
                                l++;
                            }
                        }
                    }
                }
                for (int i = 0; i < A2.length; i++) {
                    if (A2.quarks[i].type == 2) {
                        for (int j = 0; j < A2.quarks[i].numQuantum; j++) {
                            for (int k = 0; k < A2.quarks[i].unKnow.length; k++) {
                                Q[l] = Quantum.copy(A2.quarks[i].unKnow[k]);
                                l++;
                            }
                        }
                    }
                }
            }
        }
        short[] Tag = new short[l];
        for (int i = 0; i < l; i++) {
            Tag[i] = Q[i].tag;
        }
        short current;
        Quantum q;
        for (short i = 0; i < l - 1; i++) {
            current = Tag[i + 1];
            q = Q[i + 1];
            short preIndex = i;
            while (preIndex >= 0 && current < Tag[preIndex]) {
                Tag[preIndex + 1] = Tag[preIndex];
                Q[preIndex + 1] = Q[preIndex];
                preIndex--;
            }
            Tag[preIndex + 1] = current;
            Q[preIndex + 1] = q;
        }//对Tag排序完成，使用插入排序
        int l1 = 0;
        Quantum[] Q1 = new Quantum[l];
        for (int i = 0; i < l - 1; i++) {
            if (Tag[i + 1] != Tag[i]) {
                //后一项和前一项不同
                Q1[l1] = Quantum.copy(Q[i]);
                l1++;
            }
        }
        //System.out.println(l-1);
        //System.out.println(l1-1);
        if (l1==0){
            Q1[l1] = Quantum.copy(Q[l-1]);
            l1++;
        }
        else if (Tag[l-1]!=Q1[l1-1].tag){
            Q1[l1] = Quantum.copy(Q[l-1]);
            l1++;
        }
        Quantum[] Q2 = new Quantum[l1];
        for (int i = 0; i < l1; i++) {
            Q2[i] = Q1[i];
        }
        return Q2;
    }

    /**本函数用于求两个多元多项式的最大公因式；
     * 算法思路来自论文： 中国剩余算法在多元多项式最大公因式提取中的应用，杨宁学，诸昌铃，龚辉
     * 2005年的论文，很古老了，但是这是唯一一篇我能看懂的求多元多项式的GCD的论文，其他论文的数学性太强了，本人能力很有限，很难编程实现--2021.3.7*/
    public static AToms ATomsGCD(AToms A1,AToms A2,Quantum[] x,int i){
        //首先，获取字符集合
        //Quantum[] x = getQ(A1,A2);
        if (i==0){
            //说明是一元多项式了
            ATom B1 = ATom.ATomsToATom(A1);
            ATom B2 = ATom.ATomsToATom(A2);
            ATom GCD = ATom.ATomGCD(B1,B2);
            return ATom.ATomToAToms(GCD);
        }
        //约定：x[0]为主变元
        int M = 1 + Math.min(deg(A1,x[i]),deg(A2,x[i]));
        int a;
        AToms M_gcd,B1,B2;
        while (true){
            a = (int)(10*Math.random());
            if(Math.random()<0.5){
                a = -1*a;
            }
            System.out.println("\n选取的a = "+a);
            B1 = AToms.division(A1,getAToms(x[i],a))[1];//获取余式
            System.out.print("\nB1 = ");
            B1.print();
            B2 = AToms.division(A2,getAToms(x[i],a))[1];//获取余式
            System.out.print("\nB2 = ");
            B2.print();
            System.out.print("\nx[i]= ");
            x[i].print();
            System.out.printf("\ndeg(B1,x[i]) = %d",deg(B1,x[i]));
            System.out.printf("\ndeg(A1,x[i] = %d",deg(A1,x[i]));
            System.out.printf("\ndeg(B2,x[i]) = %d",deg(B2,x[i]));
            System.out.printf("\ndeg(A2,x[i]) = %d",deg(A2,x[i]));
            if (deg(B1,x[i])==deg(A1,x[i])&&deg(B2,x[i])==deg(A2,x[i])) {
                M_gcd = ATomsGCD(B1,B2,x,i-1);
                System.out.println("跳出第一个循环");
                break;
            }
        }
        AToms P = getAToms(x[i],a);
        int n = 0;
        AToms M_gcd1 = M_gcd;
        int[] Pa = new int[1000];
        AToms[] R = new AToms[1000];
        while (n<M){
            a = (int)(10*Math.random());
            B1 = AToms.division(A1,getAToms(x[i],a))[1];//获取余式
            B2 = AToms.division(A2,getAToms(x[i],a))[1];//获取余式
            if (deg(B1,x[i])==deg(A1,x[i])&&deg(B2,x[i])==deg(A2,x[i])){
                M_gcd = ATomsGCD(B1,B2,x,i-1);
                if (deg(M_gcd,x[i])<deg(M_gcd1,x[i])){
                    continue;
                }
                Pa[n] = a;
                R[n] = M_gcd;
                n++;
            }
        }
        return CRT(Pa,R,x[i],n);
    }
}