package com.yw.tingercalc.utils;

public class Quantum {
    int length = 0;
    char [] quantum;
    byte type = 0;
    byte error = 0;
    short tag;

    /*
     * type == 0      说明这是一个异常
     * type == 1      0元(即常数项)
     * type == 2      简单元，例如x,y,z
     * type == 3      函数元，例如cos,sin,tan,In,log;
     * type == 4      数学常数元，例如 π/g， e
     * type == 5      自定义元，例如 hello，future   //暂不开放！--2020.12.15
     * */

    /*error = 0  没有错误
     * error = 1  不支持的字符
     * error = 2  不支持的字符串
     * error = 3  Quarks中，允许degree为负，但是这样做，计算出来的uniqueInt就没法保证是整数了，所以，这个时候，将error置为3，表示现在的不可分割元的指数是负数，同时，将标记置为0
     * error = 4  如果degree过大，比如超过10次，那么就很有可能出现数值溢出
     * error = 41 简单元的次数超过10次！
     * error = 42 函数元的次数超过5次！
     * error = 43 数学常数的次数超过5次！*/


    /*特殊字符提取处：α β γ ε η θ  λ  π  σ   √  ³  ²*/

    /*规定：空格不再兼容任意其他字符 -- 2020.12.27*/

    /*元表 -- Tag
     * 空格     ：1
     * α(#)    ：2
     * β(@)    ：3
     * γ(%)    ：4
     * e        : 5
     * π(?)     ：6
     * √ (,)    : 7
     * ³√ (.)   : 8
     * λ($)     : 9
     * i        : 10
     *
     * 其余a - z 以及 A - Z 从30开始
     *
     * lim      : 100
     * sin      : 101
     * cos      : 102
     * tan      : 103
     * In(in)   : 104
     * log(Log) : 105
     * */

    public Quantum(char ch){
        this.length=1;
        this.tag = tag(ch);
        char [] q = new char[1];
        q[0]=ch;
        this.quantum=q;
        if (tag == 5||tag == 6||tag == 10){
            this.type = 4;//数学常数元
        }
        else if (tag == 7||tag == 8){
            this.type = 3;//根号或者三次根号，为函数元
        }
        else if (tag>=30&&tag<=79||tag>=1&&tag<=9){
            this.type = 2;//简单元
        }
        else {
            this.type = 0;
            this.error = 1;//不支持的字符
        }
    }

    public Quantum(String string){
        this.quantum = string.toCharArray();
        this.length=string.length();
        this.tag = tag(string);
        if (tag == 5||tag == 6||tag == 10){
            this.type = 4;//数学常数元
        }
        else if (tag == 7||tag == 8){
            this.type = 3;//根号或者三次根号，为函数元
        }
        else if (tag>=30&&tag<=79||tag>=1&&tag<=9){
            this.type = 2;//简单元
        }
        else if (tag >=100 && tag <= 105){
            this.type = 3;//函数元
        }
        else {
            this.type = 0;
            this.error = 2;//不支持的字符串
        }
    }

    public Quantum(){ this.type = 0; }

    private short tag(char unary){
        switch (unary){
            case ' ': return 1;//常数项的标记是1
            case '#':
            case 'α': return 2;
            case '@':
            case 'β': return 3;
            case '%':
            case 'γ': return 4;
            case 'e': return 5;
            case '?':
            case 'π': return 6;
            case ',':
            case '√': return 7;
            case '.': return 8;
            case '$':
            case 'λ': return 9;
            case 'i': return 10;
            case 'a': return 30;
            case 'b': return 31;
            case 'c': return 32;
            case 'd': return 33;
            case 'f': return 34;
            case 'g': return 35;
            case 'h': return 36;
            case 'j': return 37;
            case 'k': return 38;
            case 'l': return 39;
            case 'm': return 40;
            case 'n': return 41;
            case 'o': return 42;
            case 'p': return 43;
            case 'q': return 44;
            case 'r': return 45;
            case 's': return 46;
            case 't': return 47;
            case 'u': return 48;
            case 'v': return 49;
            case 'w': return 50;
            case 'x': return 51;
            case 'y': return 52;
            case 'z': return 53;
            case 'A': return 54;
            case 'B': return 55;
            case 'C': return 56;
            case 'D': return 57;
            case 'E': return 58;
            case 'F': return 59;
            case 'G': return 60;
            case 'H': return 61;
            case 'I': return 62;
            case 'J': return 63;
            case 'K': return 64;
            case 'L': return 65;
            case 'M': return 66;
            case 'N': return 67;
            case 'O': return 68;
            case 'P': return 69;
            case 'Q': return 70;
            case 'R': return 71;
            case 'S': return 72;
            case 'T': return 73;
            case 'U': return 74;
            case 'V': return 75;
            case 'W': return 76;
            case 'X': return 77;
            case 'Y': return 78;
            case 'Z': return 79;
            default: {
                return 0;
            }
        }
    }

    private short tag(String str){
        //此处不建议修改为switch语句，因为可能有些版本的jdk不支持
        if (str.length() == 1){
            return tag(str.toCharArray()[0]);
        }
        else {
            if (str.equals("lim"))
                return 100;
            else if (str.equals("sin"))
                return 101;
            else if (str.equals("cos"))
                return 102;
            else if (str.equals("tan"))
                return 103;
            else if (str.equals("In") || str.equals("in"))
                return 104;
            else if (str.equals("log") || str.equals("Log"))
                return 105;
            else if (str.equals("³√"))
                return 8;
            else {
                return 0;
            }
        }
    }

    public static String getError(Quantum Q){
        if (Q.type == 0){
            switch (Q.error){
                case 0: { return "没有异常"; }
                case 1:{ return "不支持的字符"; }
                case 2:{ return "不支持的字符串"; }
                case 3:{ return "次数为负"; }
                case 4:{ return "次数过大，可能会出现数值溢出"; }
                case 41:{ return "简单元的次数超过10次"; }
                case 42:{ return "函数元的次数超过5次"; }
                case 43:{ return "数学常数的次数超过5次"; }
                default:{ return "未定义的错误类型"; }
            }
        }
        else if (Q.error == 0){ return "没有错误"; }
        else { return"发现bug！！！"; }
    }

    /*public static void printError(Quantum Q){
        if (Q.type == 0){
            switch (Q.error){
                case 0: {
                    System.out.print("没有异常");
                    break;
                }
                case 1:{
                    System.out.print("不支持的字符");
                    break;
                }
                case 2:{
                    System.out.print("不支持的字符串");
                    break;
                }
                case 3:{
                    System.out.print("次数为负");
                }
                case 4:{
                    System.out.print("次数过大，可能会出现数值溢出");
                }
                case 41:{
                    System.out.print("简单元的次数超过10次");
                    break;
                }
                case 42:{
                    System.out.print("函数元的次数超过5次");
                    break;
                }
                case 43:{
                    System.out.print("数学常数的次数超过5次");
                    break;
                }
                default:{
                    System.out.print("未定义的错误类型");
                }
            }
        }
        else if (Q.error == 0){
            System.out.print("没有错误");
        }
        else {
            System.out.print("发现bug！！！");
        }
    }
    */
    /*
    public static void print(Quantum Q){
        if (Q.type == 0){
            //Q是一个异常
            printError(Q);
        }
        else if (Q.type != 1){
            //常数项不打印
            if (Q.length == 1) {
                //单个字符
                if (Q.quantum[0] != ' ') {
                    //空格不打印，是用来做标记的
                    //但是这个地方不太可能有空格--2020.12.15
                    System.out.printf("%c", Q.quantum[0]);
                }
            } else {
                //显然，它的字符长度已经不再是1了，说明这是一个长度大于1的不可分割项，那么，应该把他们括起来
                System.out.print("(");
                for (int i = 0; i < Q.length; i++) {
                    System.out.printf("%c", Q.quantum[i]);
                }
                System.out.print(")");
            }
        }
    }
     */

    public static void print(Quantum Q){ System.out.print(Quantum.QuantumToString(Q)); }

    public static String QuantumToString(Quantum Q){
        String s = "";
        if (Q.type == 0){
            s = Quantum.getError(Q);
        }
        else if (Q.type != 1){
            //常数项不打印
            if (Q.length == 1) {
                //单个字符
                if (Q.quantum[0] != ' ') {
                    //空格不打印，是用来做标记的
                    //但是这个地方不太可能有空格--2020.12.15
                    //System.out.printf("%c", Q.quantum[0]);
                    s = String.valueOf(Q.quantum);
                }
            }
            else {
                //显然，它的字符长度已经不再是1了，说明这是一个长度大于1的不可分割项，那么，应该把他们括起来
                //System.out.print("(");
                //for (int i = 0; i < Q.length; i++) {
                //    System.out.printf("%c", Q.quantum[i]);
                //}
                //System.out.print(")");
                s = "(" + String.valueOf(Q.quantum) + ")";
            }
        }
        return s;
    }

    public void print(){ print(this); }

    public boolean equals(Quantum q) {
        //先看他们的长度和次数是不是一样的
        return this.tag == q.tag;
        /*if (this.length==q.length){
            int j=0;
            //如果是一样的，那么就接着判断字符是不是一样的
            if (this.length==1){
                return this.quantum[0] == q.quantum[0];
            }
            else {
                for (int i = 0; i < this.length; i++) {
                    if (this.quantum[i] == q.quantum[i]) {
                        j++;
                    }
                }
                return j == this.length;
            }
        }
        else{
            return false;
        }*/
    }

    public static Quantum copy(Quantum Q){
        if (Q.type != 0){
            int l = Q.length;
            if (l == 1){
                Quantum Q1 = new Quantum(Q.quantum[0]);
                Q1.error = Q.error;
                Q1.type = Q.type;
                return Q1;
            }
            else {
                char [] q = new char[l];
                for (int i = 0;i < l;i++){
                    q[i] = Q.quantum[i];
                }
                String q1 = new String(q);
                Quantum Q1 = new Quantum(q1);
                Q1.type = Q.type;
                Q1.error=Q.error;
                return Q1;
            }
        }
        else {
            return new Quantum();
        }
    }

}
