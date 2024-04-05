package com.yw.tingercalc.utils;

public class ATomUD {
    ATom AU;
    ATom AD;
    //注意:在ATom中定义的一元多项式，是不严格的，是允许负指数存在的，但是在这里，在ATomUD中，必须是严格的一元多项式，
    //因为没有对于含有负幂次项的多项式找最大公因式的方法，也可能是我浅薄了，没有听闻过。

    byte error = 0;
    byte type = 0;

    /*
     * type == 0 异常对象
     * type == 1 常数项
     * type == 10 0多项式
     * type == 11 1多项式
     * type == 2 一般项
     * */

    /*
     * error == 0 没有错误
     * error == 1 不是同类项
     * error == 2 分母为0（0多项式）
     * error == 3 未定义的类型
     * error == 4 因式错误
     * */

    public ATomUD(String string){
        AToms A = new AToms(string);
        ATom A1 = ATom.ATomsToATom(A);
        ATomUD A2 = new ATomUD(A1);

        this.AU = A2.AU;
        this.AD = A2.AD;
        if (A2.AU.type == 0||A2.AD.type == 0){
            this.type = 0;
        }
        else if (A2.AU.type == 1){
            if (A2.AD.type == 1||A2.AD.type == 11){
                this.type = 1;
            }
            else if(A2.AD.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (A2.AD.type == 2){
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else if (A2.AU.type == 10){
            if (A2.AD.type == 10){
                this.type = 0;
                this.error = 2;//分母为0（0多项式）
            }
            else {
                this.type = 10;
                this.AD = new ATom(1);
            }
        }
        else if (A2.AU.type == 11){
            if (A2.AD.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (A2.AD.type == 11){
                this.type = 11;
            }
            else if (A2.AD.type == 1){
                this.type = 1;
            }
            else if (A2.AD.type == 2) {
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else if(A2.AU.type == 2){
            if (A2.AD.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (A2.AD.type == 11||A2.AD.type == 1){
                this.type = 2;
            }
            else if (A2.AD.type == 2) {
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else {
            this.type = 0;
            this.error = 3;
        }
    }

    public ATomUD(ATom u,ATom d){
        this.AU=u;
        this.AD=d;
        if (u.type == 0||d.type == 0){
            this.type = 0;
        }
        else if (u.type == 1){
            if (d.type == 1||d.type == 11){
                this.type = 1;
            }
            else if(d.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (d.type == 2){
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else if (u.type == 10){
            if (d.type == 10){
                this.type = 0;
                this.error = 2;//分母为0（0多项式）
            }
            else {
                this.type = 10;
                this.AD = new ATom(1);
            }
        }
        else if (u.type == 11){
            if (d.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (d.type == 11){
                this.type = 11;
            }
            else if (d.type == 1){
                this.type = 1;
            }
            else if (d.type == 2) {
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else if(u.type == 2){
            if (d.type == 10){
                this.type = 0;
                this.error = 2;
            }
            else if (d.type == 11||d.type == 1){
                this.type = 2;
            }
            else if (d.type == 2) {
                this.type = 2;
            }
            else {
                this.type = 0;
                this.error = 3;
            }
        }
        else {
            this.type = 0;
            this.error = 3;
        }
    }

    public ATomUD(ATom u){
        this.AU = u;
        this.AD = new ATom(1);//分母不能为0，此时AD是1x^0
        this.type = u.type;
    }

    public ATomUD(){
        this.AU = new ATom(0);
        this.AD = new ATom(1);
        this.type = 10;
    }

    public void empty(){
        this.AU = new ATom();
        this.AD = new ATom();
    }

    public void print(){
        print(this);
    }

    public ATomUD add(ATomUD A){
        return add(this,A);
    }

    public ATomUD less(ATomUD A){
        return less(this,A);
    }

    public ATomUD multiply(ATomUD A){
        return multiply(this,A);
    }

    public ATomUD check(){ return check(this); }

    public ATomUD reverse(){
        return reverse(this);
    }

    public static void printError(ATomUD A){
        switch (A.error){
            case 0:{
                System.out.print("没有异常");
                break;
            }
            case 1:{
                System.out.print("不是同类项");
                break;
            }
            case 2:{
                System.out.print("分母为0（0多项式）");
                break;
            }
            case 3:{
                System.out.print("未定义的类型");
                break;
            }
            case 4:{
                System.out.print("因式错误");
                break;
            }
            default:{
                System.out.print("出现恶性bug");
            }
        }
    }

    public static void print(ATomUD A){
        /*if (A.type!=0) {
            if (A.AD.type == 11) {
                A.AU.print();
            }
            else {
                System.out.print("(");
                A.AU.print();
                System.out.print(")");
                System.out.print("/");
                System.out.print("(");
                A.AD.print();
                System.out.print(")");
            }
        }
        else{
            printError(A);
        }
         */
        System.out.print(ATomUD.ATomUDToString(A));
    }

    public static ATomUD copy(ATomUD A){
        ATom A1 = ATom.copy(A.AU);
        ATom A2 = ATom.copy(A.AD);
        ATomUD A3 = new ATomUD(A1,A2);
        A3.error = A.error;
        return A3;
    }

    public static ATomUD add(ATomUD A1,ATomUD A2){
        /*A1.AU = A1.AU.check();
        A2.AU = A2.AU.check();
        ATom A = ATom.add(A1.AU,A2.AU);
        return new ATomUD(A);*/
        A1 = A1.check();
        //System.out.printf("自检后的A1 = ");
        //A1.print();
        //System.out.print("显示完毕\n");
        A2 = A2.check();
        //System.out.printf("自检后的A2 = ");
        //A2.print();
        //System.out.print("显示完毕\n");
        ATom AU = ATom.multiply(A1.AU,A2.AD);
        /*System.out.print("\n-------\n");
        A1.AU.print();
        System.out.printf("  A1.AU.type = %d ,A1.AU.error = %d  ",A1.AU.type,A1.AU.error);
        System.out.print("  *  ");
        A2.AD.print();
        System.out.printf("  A2.AD.type = %d ,A2.AD.error = %d  ",A2.AD.type,A2.AD.error);
        System.out.println("  =  ");
        AU.print();
        System.out.printf("  AU.type = %d ,AU.error = %d  ",AU.type,AU.error);
        System.out.print("\n-------\n");*/
        //System.out.printf("第一次计算后的A1 = ");
        //A1.print();
        //System.out.print("显示完毕\n");
        //System.out.printf("第一次计算后A2 = ");
        //A2.print();
        //System.out.print("显示完毕\n");
        ATom temp = ATom.multiply(A1.AD,A2.AU);
        //System.out.printf("第二次计算后的A1 = ");
        //A1.print();
        //System.out.print("显示完毕\n");
        //System.out.printf("第二次计算后A2 = ");
        //A2.print();
        //System.out.print("显示完毕\n");
        /*System.out.print("\nAU = AU + temp\n");
        System.out.println();
        AU.print();
        System.out.printf("  AU.type = %d ,AU.error = %d  ",AU.type,AU.error);
        System.out.print(" + ");
        temp.print();
        System.out.printf("  temp.type = %d ,temp.error = %d  ",temp.type,temp.error);*/
        AU = ATom.add(AU,temp);
        /*System.out.print(" = ");
        AU.print();
        System.out.printf("  AU.type = %d ,AU.error = %d  ",AU.type,AU.error);
        System.out.println();*/
        //System.out.printf("第三次计算后的A1 = ");
        //A1.print();
        //System.out.print("显示完毕\n");
        //System.out.printf("第三次计算后A2 = ");
        //A2.print();
        //System.out.print("显示完毕\n");
        ATom AD = ATom.multiply(A1.AD,A2.AD);
        //System.out.printf("第四次计算后的A1 = ");
        //A1.print();
        //System.out.print("显示完毕\n");
        //System.out.printf("第四次计算后A2 = ");
        //A2.print();
        //System.out.print("显示完毕\n");
        ATomUD A = new ATomUD(AU,AD);
        return ATomUD.check(A);
    }

    public static ATomUD less(ATomUD A1,ATomUD A2){
        A1 = A1.check();
        A2 = A2.check();
        ATomUD t = ATomUD.copy(A2);
        t = t.reverse();
        ATomUD A = ATomUD.add(A1,t);
        return ATomUD.check(A);
    }

    public static ATomUD multiply(ATomUD A1,ATomUD A2){
        A1.AU = A1.AU.check();
        A2.AU = A2.AU.check();
        /*System.out.print("\n本注释位于ATomUD 中的multify方法中\n分式的分子 = ");
        A1.print();
        System.out.print("\n分式的分母 = ");
        A2.print();
        System.out.print("\n注释完毕\n");*/
        ATom AU = ATom.multiply(A1.AU,A2.AU);
        //System.out.print("\nAU = ");
        //AU.print();
        //System.out.print("\nAD = ");
        ATom AD = ATom.multiply(A1.AD,A2.AD);
        //AD.print();
        // System.out.println();
        ATomUD A = new ATomUD(AU,AD);
        A = ATomUD.check(A);
        return A;
    }

    public static ATomUD check(ATom A1, ATom A2) {
        //此函数用于将两个不严格的多项式化成一个严格的分式
        //如果A1或者A2是数字，那么不需要做
        //先判断一下他们的类型
        //自检函数，允许修参
        //return new ATomUD(A1,A2);
        if (A1.type == 0||A2.type == 0){
            ATomUD A = new ATomUD();
            A.type = 0;
            A.error = 4;
            return A;
        }
        else if (A1.type == 10){
            if (A2.type == 10){
                ATomUD A = new ATomUD();
                A.type = 0;
                A.error = 2;
                return A;
            }
            else{
                return new ATomUD("0");
            }
        }
        else if (A2.type == 10){
            ATomUD A = new ATomUD();
            A.type = 0;
            A.error = 2;
            return A;
        }
        else if (A1.type == 11&&A2.type == 11){
            return new ATomUD("1");
        }
        else if(A1.type == 11&&A2.type == 1){
            UD coe = new UD(A2.quarkATom[0].coe.d,A2.quarkATom[0].coe.u);
            ATom A = new ATom(coe);
            return new ATomUD(A);
        }
        else if(A1.type == 1&&A2.type == 11){
            return new ATomUD(A1);
        }
        else if(A1.type == 1&&A2.type == 1){
            UD coe = UD.division(A2.quarkATom[0].coe,A2.quarkATom[0].coe);
            ATom A = new ATom(coe);
            return new ATomUD(A);
        }
        else if (A1.type == 2||A2.type == 2) {
            //核心代码
            A1 = ATom.check(A1);
            //System.out.printf("\n特别展示\nA2.type = %d A2.error = %d  展示完毕\n",A2.type,A2.error);
            A2 = ATom.check(A2);
            //看看他们的最低次数是多少，如果都>=0，那么就不用化了
            if (A1.quarkATom[A1.quarkATom.length - 1].degree >= 0 && A2.quarkATom[A2.quarkATom.length - 1].degree >= 0) {
                //只需要再找一下他们的最大公因式，看看能不能化简，就可以了
                ATom GCD = ATom.ATomGCD(A1, A2);
                //System.out.print("\nGCD = ");
                //GCD.print();
                //System.out.println();
                if (GCD.type == 1 || GCD.type == 11) {//这个地方原本是if (GCD.length == 1 && GCD.quarkATom[0].degree == 0 && GCD.quarkATom[0].coe.u == GCD.quarkATom[0].coe.d) -- 2021.1.21
                    //说明是常数项，不用化简了，直接返回
                } else {
                    A1 = ATom.division(A1, GCD);
                    A2 = ATom.division(A2, GCD);
                }
            } else {
                //需要消去次数低于0的项
                //很简单，找出A1和A2的最低次数的最小值，以此次数的绝对值为次数，构建一个单项式，分别乘上A1和A2即可
                int deg = -1 * Matrix.min(A1.quarkATom[A1.length - 1].degree, A2.quarkATom[A2.length - 1].degree);
                Quark[] q = {
                        new Quark(1, deg)
                };
                ATom A3 = new ATom(q, A1.unaryATom);
                A1 = ATom.multiply(A1, A3);
                A2 = ATom.multiply(A2, A3);
                //然后再找一下他们的最大公因式就行了
                ATom GCD = ATom.ATomGCD(A1, A2);
                //System.out.print(" 公因式 = ");
                //GCD.print();
                //System.out.println();
                A1 = ATom.division(A1, GCD);
                A2 = ATom.division(A2, GCD);
            }
            //在这里，需要对分数进行化简，即找到分式的分子和分母的分母的最小公倍数
            //先找分子的最小公倍数
            if (A2.type == 1){
                if (A1.type == 10){
                    return new ATomUD("0");
                }
                else if (A1.type == 11){
                    UD coe = new UD(A2.quarkATom[0].coe.d,A2.quarkATom[0].coe.u);
                    ATom A = new ATom(coe);
                    return new ATomUD(A);
                }
                else if (A1.type == 1){
                    UD coe = UD.division(A2.quarkATom[0].coe,A2.quarkATom[0].coe);
                    ATom A = new ATom(coe);
                    return new ATomUD(A);
                }
                else if (A1.type == 2){
                    for (int j = 0;j<A1.length;j++){
                        A1.quarkATom[j].coe = UD.division(A1.quarkATom[j].coe,A2.quarkATom[0].coe);
                    }
                    return new ATomUD(A1);
                }
                else {
                    ATomUD A = new ATomUD();
                    A.type = 0;
                    A.error = 3;//未定义的类型
                    return A;
                }
            }
            else if (A2.type == 11){
                return new ATomUD(A1);
            }
            else if (A2.type == 10){
                ATomUD A = new ATomUD();
                A.type = 0;
                A.error = 2;//分母为0
                return A;
            }
            else if (A2.type == 2){
                if (A1.type == 10){
                    return new ATomUD("0");
                }
                else if (A1.type == 11||A1.type == 1){
                    //去寻找A1和A2的分式的分母的最小公倍数
                    long LCM = UD.LCM(A1.quarkATom[0].coe.d,A2.quarkATom[0].coe.d);
                    for (int j = 1;j<A2.length;j++){
                        LCM = UD.LCM(LCM,A2.quarkATom[j].coe.d);
                    }
                    //找到LCM后，给分子分母都乘上去
                    UD c = new UD(LCM);
                    A1.quarkATom[0].coe = UD.multiply(A1.quarkATom[0].coe,c);
                    for (int j = 0;j<A2.length;j++){
                        A2.quarkATom[j].coe = UD.multiply(A2.quarkATom[j].coe,c);
                    }
                    return new ATomUD(A1,A2);
                }
                else if (A1.type == 2){
                    long LCM = 1;
                    for (int j = 0;j<A1.length;j++){
                        LCM = UD.LCM(LCM,A1.quarkATom[j].coe.d);
                    }
                    for (int j = 0;j<A2.length;j++){
                        LCM = UD.LCM(LCM,A2.quarkATom[j].coe.d);
                    }
                    UD c = new UD(LCM);
                    for (int j = 0;j<A1.length;j++){
                        A1.quarkATom[j].coe = UD.multiply(A1.quarkATom[j].coe,c);
                    }
                    for (int j = 0;j<A2.length;j++){
                        A2.quarkATom[j].coe = UD.multiply(A2.quarkATom[j].coe,c);
                    }
                    return new ATomUD(A1,A2);
                }
                else {
                    ATomUD A = new ATomUD();
                    A.type = 0;
                    A.error = 3;
                    return A;
                }
            }
            else {
                ATomUD A = new ATomUD();
                A.type = 0;
                A.error = 3;
                return A;
            }
        }
        else {
            ATomUD A = new ATomUD();
            A.type = 0;
            A.error = 3;
            return A;
        }
    }

    public static ATomUD division(ATomUD A1, ATomUD A2) {
        //两个一元多项式的分式的除法
        if (A2.type == 10){
            //分母为0多项式
            ATomUD A = new ATomUD();
            A.type = 0;
            A.error = 2;
            return A;
        }
        else {
            ATomUD B2 = ATomUD.copy(A2);
            B2 = ATomUD.reciprocal(B2);
            /*System.out.print("\n本注释位于ATomUD中的div方法，分母 = ");
            A2.print();
            System.out.print("\n 它的倒数为 ");
            B2.print();
            System.out.print("\n注释完毕\n");*/
            //A = ATomUD.check(A);multiply计算完以后会进行自检的，所以这里不用再自检了
            return ATomUD.multiply(A1, B2);
        }
    }

    public static ATomUD reciprocal(ATomUD A) {
        //对A取倒数
        return new ATomUD(A.AD,A.AU);
    }

    public static ATomUD check(ATomUD A){ return ATomUD.check(A.AU,A.AD); }

    public static ATomUD reverse(ATomUD A){
        ATom AU = ATom.reverse(A.AU);
        ATom AD = ATom.copy(A.AD);
        return new ATomUD(AU,AD);
    }

    public static String ATomUDToString(ATomUD A){
        A = A.check();
        String s = "";
        if (A.AD.type == 11){
            s = s +  ATom.ATomToString(A.AU);
        }
        else if (A.AD.type == 2){
            if (A.AD.quarkATom[0].coe.u<0){
                //尽量保证分母的第一项不出现负号
                for (short i = 0;i<A.AD.length;i++){
                    A.AD.quarkATom[i].coe.u = -1*A.AD.quarkATom[i].coe.u;
                }
                for (short i = 0;i<A.AU.length;i++){
                    A.AU.quarkATom[i].coe.u = -1*A.AU.quarkATom[i].coe.u;
                }
            }
            if (A.AD.length==1){
                if (A.AD.quarkATom[0].coe.u == 1){
                    //分母是  x这样的项
                    if (A.AU.type == 2) {
                        //(x+1)/x
                        if (A.AU.length == 1) {
                            s = s + ATom.ATomToString(A.AU) + "/" + ATom.ATomToString(A.AD);
                        } else {
                            s = s + "(" + ATom.ATomToString(A.AU) + ")" + "/" + ATom.ATomToString(A.AD);
                        }
                    }
                    else {
                        //常数项 而且几乎不可能是分数
                        //2/x
                        s = s + ATom.ATomToString(A.AU) + "/" + ATom.ATomToString(A.AD);
                    }
                }
                else {
                    if (A.AU.type == 2) {
                        if (A.AU.length == 1) {
                            s = s +  ATom.ATomToString(A.AU) + "/(" + ATom.ATomToString(A.AD) + ")";
                        } else {
                            s = s + "(" + ATom.ATomToString(A.AU) + ")" + "/(" + ATom.ATomToString(A.AD) + ")";
                        }
                    }
                    else {
                        s = s + ATom.ATomToString(A.AU) + "/(" + ATom.ATomToString(A.AD) + ")";
                    }
                }
            }
            else {
                //分母是长度不为1的一般多项式
                if (A.AU.type == 2) {
                    if (A.AU.length == 1) {
                        s = s + ATom.ATomToString(A.AU) + "/(" + ATom.ATomToString(A.AD) + ")";
                    } else {
                        s = s + "(" + ATom.ATomToString(A.AU) + ")/(" + ATom.ATomToString(A.AD) + ")";
                    }
                }
                else {
                    s = s + ATom.ATomToString(A.AU) + "/(" +ATom.ATomToString(A.AD) + ")";
                }
            }
        }
        else if (A.AD.type == 1) {
            //可以肯定的是 没有分数
            if (A.AU.type == 2) {
                if (A.AU.length == 1) {
                    s = s + ATom.ATomToString(A.AU) + "/" + ATom.ATomToString(A.AD);
                } else {
                    s = s + "(" + ATom.ATomToString(A.AU) + ")/" + ATom.ATomToString(A.AD);
                }
            }
            else if (A.AU.type == 1||A.AU.type == 11){
                s = s + ATom.ATomToString(A.AU) + "/" + ATom.ATomToString(A.AD);
            }
            else if (A.AU.type == 10){
                s = "0";
            }
        }
        return s;
    }

    public static ATomUD UDToATomUD(UD U){
        UD U1 = UD.copy(U);
        ATom A = new ATom(U1);
        return new ATomUD(A);
    }
}