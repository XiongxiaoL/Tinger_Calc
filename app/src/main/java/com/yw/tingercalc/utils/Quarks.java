package com.yw.tingercalc.utils;

import com.yw.tingercalc.utils.Quantum;

public class Quarks {
    UD coe;
    int numQuantum;//保存未知数的个数
    Quantum[] unKnow;//保存Quantum对象，每一个Quantum对象都是一个不可分割的char数组
    int [] degree;
    byte error = 0;
    byte type = 0;

    /*
     * type == 0  这是一个异常
     * type == 1  这是一个常数项
     * type == 2  这是一个除了常数项以外的正常单项式
     * */

    /*
     * error == 0  没有问题
     * error == 1  非同类项，不能相加
     * error == 21 缺失匹配的括号
     * error == 22 请保持输入完整
     * error == 23 遇到了无法识别的符号
     * error == 24 请不要让分母为0
     * error == 25 不接受形如 (1/x^(-2)) 或者 1/x^(-2) 的式子
     * error == 26 不接受形如 (x^-2) 或者 x^-2 的式子
     * error == 27 请检查是否输入了0
     * error == 3  待操作的两对象异常
     * error == 4  出现未定义的多元单项式类型
     * error == 5  Q2的文字不在Q1中
     * */

    /*单项式中元的排列顺序是根据Quantum中的Tag表来的，自检之后，自动排序*/

    public Quarks(){
        this.type = 0;
        this.error = 0;
    }

    public Quarks(String str) {
        str = str.replace(" ", "");//去除掉所有的空格
        str = str.replace("*", "");//去除掉所有的*号
        str = str.replace("+", "");//去除掉所有的+号
        char[] input = str.toCharArray();
        boolean firstWord = true;
        if (input[0]=='-'){
            firstWord = false;
            str = str.substring(1);//去除掉第一个 -号
        }
        char[] Input = str.toCharArray();
        int l = str.length();
        //先读系数  如果有系数的话！
        int n;
        UD coe = new UD(1);
        byte numQuantum = 0;
        byte q = 0;//初始状态
        char p = Input[0];
        n = 0;
        Quantum[] qu = new Quantum[50];
        int[] deg = new int[50];
        for (int ii = 0; ii < 11; ii++) {
            qu[ii] = new Quantum(' ');//全部初始化为空格，方便后面处理
            deg[ii] = 0;//同上
        }
        if (p == '(' || p == '（') {
            p = Input[1];
            n++;
            int i;
            for (i = 1; i < l; i++) {
                p = Input[i];
                n++;
                if (p == ')' || p == '）') {
                    q = 4;
                    break;
                } else if (p <= '9' && p >= '0') {
                    //虽然这样的判断条件看起来很傻，但是很容易理解，额，至少我觉得更好理解，，，这个循环就是用来找分子
                } else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                    n = n - 2;//退出去！，说明没有系数！（系数 = 1）
                    p = Input[n];
                    q = 4;
                    break;
                } else {
                    q = -1;//说明输入输错了
                    break;
                }
            }
            long u = charToNum(Input, 1, i - 1);//分子找到了，存在了u中
            if (p == '/') {
                q = 11;
                //说明用户要输入分母了，不然输入 ‘/’ 干啥，，，
                int i1;
                for (i1 = i + 1; i1 < l; i1++) {
                    p = Input[i1];
                    n++;
                    if (p == ')' || p == '）') {
                        break;
                    }
                    else if (p <= '9' && p >= '0') {
                        ;
                    }
                    else {
                        q = -1;//说明输入输错了
                        break;
                    }
                }
                long d = charToNum(Input, i + 1, i1 - 1);//分母也找到了，存在d中
                //这个时候，Input[i] == ')' ；
                if (p== ')'||p=='）'){
                    q = 4;
                    coe = new UD(u, d);
                }
                else {
                    q = 21;//缺失匹配的括号
                }
            }
            else if (p == ')' || p == '）') {
                //说明用户就输入了一个分子，但是还是加了一个括号，很好，很严谨，，，
                coe = new UD(u);
            }
            else {
                q = 22;//请保持输入规范
            }
        }
        else if (p >= '0' && p <= '9') {
            //上来就是一个数字，emm
            int i;
            for (i = 1; i < l; i++) {
                p = Input[i];
                n++;
                if (p <= '9' && p >= '0') {
                    //虽然这样的判断条件看起来很傻，但是很容易理解，额，至少我觉得更好理解，，，这个循环就是用来找分子
                }
                else if (p == '/') {
                    q = 3;
                    break;
                }
                else {
                    q = -1;
                    break;
                }
            }
            long u;
            u = charToNum(Input, 0, i - 1);//分子找到了，存在了u中
            coe = new UD(u);
            if (n==l){
                q = -1;
            }
            else {
                p = Input[n];
                if (q == 3) {
                    // p = '/'
                    int i1;
                    for (i1 = i + 1; i1 < l; i1++) {
                        p = Input[i1];
                        n++;
                        if (p <= '9' && p >= '0') {
                            ;
                        } else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                            q = -2;
                            break;
                        } else {
                            q = -1;
                            break;
                        }
                    }
                    if (q == -2) {
                        //用户要进行1/x^2的输入
                        //System.out.println("诶诶诶");
                        numQuantum++;
                        qu[0] = new Quantum(p);
                        deg[0] = -1;
                        n++;
                        if (n == l) {
                            q = -1;
                        }
                        else {
                            p = Input[n];
                            if (p == '^') {
                                n++;
                                if (n == l) {
                                    q = -1;
                                }
                                else {
                                    p = Input[n];
                                    if (p == '(' || p == '（') {
                                        n++;
                                        if (n == l) {
                                            q = -1;
                                        }
                                        else {
                                            p = Input[n];
                                            if (p >= '0' && p <= '9') {
                                                int i11, n1 = n;
                                                for (i11 = n1 + 1; i11 < l; i11++) {
                                                    p = Input[i11];
                                                    n++;
                                                    if (p <= '9' && p >= '0') {
                                                        ;
                                                    } else {
                                                        q = -1;
                                                        break;
                                                    }
                                                }
                                                deg[0] = -1 * (int) charToNum(Input, n1, i11 - 1);
                                                if (p == ')' || p == '）') {
                                                    n++;//这个时候应该是读完了
                                                    if (n < l) {
                                                        //没有读完？
                                                        q = 22;//请保持输入规范
                                                    }
                                                    else {
                                                        q = -1;//形如(2/x)的式子读完了
                                                    }
                                                }

                                            }
                                            else if (p == '-') {
                                                q = 25;
                                            } else {
                                                q = 22;//请保持输入规范
                                            }
                                        }
                                    }
                                    else if (p >= '0' && p <= '9') {
                                        int i11, n1 = n;
                                        for (i11 = n1 + 1; i11 < l; i11++) {
                                            p = Input[i11];
                                            n++;
                                            if (p <= '9' && p >= '0') {
                                                ;
                                            }
                                            else {
                                                q = -1;//说明输入输错了
                                                break;
                                            }
                                        }
                                        //System.out.println("\n运行到此？");
                                        deg[0] = -1*(int) charToNum(Input, n1, i11 - 1);
                                        n++;
                                        if (n==l){
                                            q = -1;
                                        }
                                        else {
                                            p = Input[n];
                                            q = -1;
                                        }
                                    }
                                    else {
                                        q = 22;//请保持输入规范
                                    }
                                }
                            }
                            else {
                                q = -1;
                            }
                        }
                    }
                    else {
                        long d = charToNum(Input, i + 1, i1 - 1);//分母也找到了，存在d中
                        coe = new UD(u, d);
                    }
                }
            }
        }
        /*else if (p == '0'){
            coe = new UD();
            if (n<l){
                q = 27;//分子都为0了，还要输入个啥
            }
            else {
                q = -1;
            }
        }*/
        else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z'){
            q = 4;
        }
        else {
            q = 23;//无法识别的符号
        }
        //开始读未知元了
        //此时，p=Input[n] 指向了系数的后面一项，这个地方可能是'('，也可能是x,y,z这种“元”，同时，这个地方也可能是已经读完了！
        if (q >= 21 && q <= 28){
            this.type = 0;
            this.error = q;
            if (q == 27){
                this.type = 1;
                this.error = 0;
                this.coe = new UD(0);
            }
        }
        else {
            if (n >= l) {
                //说明没有未知元，在2020.12.15之前，我会给常数项加上了一个空格，现在，我决定取消这一愚蠢的行为 -- 2020.12.15
                //Quantum[] qqq = new Quantum[1];
                //qqq[0] = new Quantum(' ');
                if (firstWord == false){
                    coe.u=-1* coe.u;
                }
                this.coe = coe;
                if (numQuantum != 0) {
                    //System.out.printf("\nnumQuantum = %d\n",numQuantum);
                    this.numQuantum = numQuantum;
                    Quantum[] Qu = new Quantum[numQuantum];
                    int[] degree = new int[numQuantum];
                    for (int ii = 0; ii < numQuantum; ii++) {
                        Qu[ii] = qu[ii];
                        degree[ii] = deg[ii];
                    }
                    this.unKnow = Qu;
                    this.degree = degree;
                    this.type = 2;
                }
                else {
                    this.type = 1;
                }
            }
            else {
                //System.out.printf("现在 n = %d，然后Input[%d] = %c\n", n, n, Input[n]);
                p = Input[n];
                int i;
                for (i = 0; ; i++) {
                    if (p == '(' || p == '（') {
                        q = 5;
                        n++;
                        p = Input[n];
                        if (p >= '0' && p <= '9') {
                            //说明用户这个时候要输入形如(1/x^2)或者(1/x)的式子
                            n++;
                            p = Input[n];//现在p应该等于'/'
                            if (p >= '0' && p <= '9') {
                                int i1, n1 = n;
                                for (i1 = n1 + 1; i1 < l; i1++) {
                                    p = Input[i1];
                                    n++;
                                    if (p <= '9' && p >= '0') {
                                        ;
                                    } else {
                                        q = -1;//说明输入输错了
                                        break;
                                    }
                                }
                                coe = UD.multiply(coe, charToNum(Input, n1, i1 - 1));
                            }
                            if (p == '/') {
                                //System.out.println("\n11111111111111");
                                n++;
                                p = Input[n];
                                if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {//如果要扩展字母表，应修改此处的判断条件
                                    //System.out.println("hello?\n");////////////////////////
                                    qu[i] = new Quantum(p);//实例化
                                    deg[i] = -1;
                                    //System.out.printf("\n现在 n = %d,p = Input[%d] = %c \n",n,n,p);
                                    numQuantum++;
                                    n++;
                                    p = Input[n];
                                    if (p == '^') {
                                        n++;
                                        p = Input[n];
                                        if (p >= '0' && p <= '9') {
                                            //说明用户要输入指数了！
                                            int i1, n1 = n;
                                            for (i1 = n1 + 1; i1 < l; i1++) {
                                                p = Input[i1];
                                                n++;
                                                if (p <= '9' && p >= '0') {
                                                    ;
                                                } else {
                                                    q = -1;//说明输入输错了
                                                    break;
                                                }
                                            }
                                            deg[i] = -1 * (int) charToNum(Input, n1, i1 - 1);
                                            if (p == ')' || p == '）') {
                                                q = 4;//形如(1/x^2)的式子已经输入完成
                                            } else {
                                                //不知道用户想要干什么，直接退出了，，，
                                                q = -1;
                                                break;
                                            }
                                        } else if (p == '(' || p == '（') {
                                            //说明用户想要输入形如(1/x^(2))，，，也可能想输入(1/x^(-2)),后者虽然可以做到，但是我并不希望用户这样的输入，
                                            //莫名其妙，，，所以不接受形如后者的式子
                                            n++;
                                            p = Input[n];
                                            if (p == '-') {
                                                //并不是不可以做到接受这样的式子，而是我不希望用户这样输入
                                                q = -1;
                                                break;
                                            } else if (p >= '0' && p <= '9') {
                                                //看来用户要进行(1/x^(2))这样的输入了，，，
                                                int i1, n1 = n;
                                                for (i1 = n + 1; i1 < l; i1++) {
                                                    p = Input[i1];
                                                    n++;
                                                    if (p <= '9' && p >= '0') {
                                                        ;
                                                    } else {
                                                        q = -1;//说明输入输错了
                                                        break;
                                                    }
                                                }
                                                deg[i] = -1 * (int) charToNum(Input, n1, i1 - 1);
                                                if (p == ')' || p == '）') {
                                                    n++;
                                                    p = Input[n];
                                                    if (p == ')' || p == '）') {
                                                        q = 4;//形如(1/x^(2))的式子已经输入完成
                                                    } else {
                                                        q = -1;
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            q = -1;
                                            break;
                                        }
                                    } else if (p == ')' || p == '）') {
                                        q = 4;//形如(1/x)的式子已经输入完成
                                    } else {
                                        q = -1;
                                        break;
                                    }
                                } else {
                                    //暂不被允许的输入方式！
                                    q = -1;
                                    break;
                                }
                            } else {
                                //暂不被允许的输入方式！
                                q = -1;
                                break;
                            }
                    /*n++;
                    p = Input[n];
                    else if(p == ')'){
                        q=4;//形如(1/x)的式子已经输入完成
                        break;
                    }*/
                        }
                        else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                            //说明用户希望输入形如 (x)   (x^2)  (x^(-2))  (x^(2))这样的式子
                            //System.out.printf("\nn = %d  p = Input[%d] = %c\ni = %d \n",n,n,p,i);
                            numQuantum++;
                            qu[i] = new Quantum(p);
                            deg[i] = 1;
                            n++;
                            p = Input[n];
                            if (p == ')' || p == '）') {
                                q = 4;//形如(x)的式子输入完成！
                            }
                            else if (p == '^') {
                                n++;
                                p = Input[n];
                                if (p == '(' || p == '（') {
                                    //说明用户想要输入(x^(-2)) 或者 (x^2) 这样的式子
                                    n++;
                                    p = Input[n];
                                    if (p >= '0' && p <= '9') {
                                        int i1, n1 = n;
                                        for (i1 = n1 + 1; i1 < l; i1++) {
                                            p = Input[i1];
                                            n++;
                                            if (p <= '9' && p >= '0') {
                                                ;
                                            }
                                            else {
                                                q = -1;//说明输入输错了
                                                break;
                                            }
                                        }
                                        deg[i] = (int) charToNum(Input, n1, i1 - 1);
                                        if (p == ')' || p == '）') {
                                            n++;
                                            p = Input[n];
                                            if (p == ')' || p == '）') {
                                                q = 4;//形如(x^(2))的式子已经输入完成
                                            }
                                            else {
                                                q = -1;
                                                break;
                                            }
                                        }
                                    }
                                    else if (p == '-') {
                                        n++;
                                        p = Input[n];
                                        int i1, n1 = n;
                                        for (i1 = n1 + 1; i1 < l; i1++) {
                                            p = Input[i1];
                                            n++;
                                            if (p <= '9' && p >= '0') {
                                                ;
                                            }
                                            else {
                                                q = -1;//说明输入输错了
                                                break;
                                            }
                                        }
                                        deg[i] = -1 * (int) charToNum(Input, n1, i1 - 1);
                                        if (p == ')' || p == '）') {
                                            n++;
                                            p = Input[n];
                                            if (p == ')' || p == '）') {
                                                q = 4;//形如(x^(-2))的式子已经输入完成
                                            }
                                            else {
                                                q = -1;
                                                break;
                                            }
                                        }
                                    }
                                }
                                else if (p >= '0' && p <= '9') {
                                    int i1, n1 = n;
                                    for (i1 = n1 + 1; i1 < l; i1++) {
                                        p = Input[i1];
                                        n++;
                                        if (p <= '9' && p >= '0') {
                                            ;
                                        } else {
                                            q = -1;//说明输入输错了
                                            break;
                                        }
                                    }
                                    deg[i] = (int) charToNum(Input, n1, i1 - 1);
                                    //n++;
                                    //p = Input[n];
                                    if (p == ')' || p == '）') {
                                        q = 4;//形如 (x^2)的式子输入完成！
                                    } else {
                                        q = -1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                        qu[i] = new Quantum(p);
                        numQuantum++;
                        deg[i] = 1;
                        n++;
                        if (n == l) {
                            break;
                        }
                        else {
                            p = Input[n];
                            if (p == '(' || p == '（') {
                                //n--;//这里是个bug，我不打算删掉它，用来记录自己的粗心
                                //p = Input[n];
                            }
                            else if (p == '^') {
                                n++;
                                p = Input[n];
                                if (p == '(' || p == '（') {
                                    n++;
                                    p = Input[n];
                                    if (p >= '0' && p <= '9') {
                                        int i1, n1 = n;
                                        for (i1 = n1 + 1; i1 < l; i1++) {
                                            p = Input[i1];
                                            n++;
                                            if (p <= '9' && p >= '0') {
                                                ;
                                            } else {
                                                q = -1;
                                                break;
                                            }
                                        }
                                        deg[i] = (int) charToNum(Input, n1, i1 - 1);
                                        if (p == ')' || p == '）') {
                                            q = 4;
                                        } else {
                                            q = -1;
                                            break;
                                        }
                                    } else if (p == '-') {
                                        n++;
                                        p = Input[n];
                                        if (p >= '0' && p <= '9') {
                                            int i1, n1 = n;
                                            for (i1 = n1 + 1; i1 < l; i1++) {
                                                p = Input[i1];
                                                n++;
                                                if (p <= '9' && p >= '0') {
                                                    ;
                                                } else {
                                                    q = -1;//说明输入输错了
                                                    break;
                                                }
                                            }
                                            deg[i] = -1 * (int) charToNum(Input, n1, i1 - 1);
                                            if (p == ')' || p == '）') {
                                                q = 4;
                                            } else {
                                                q = -1;
                                                break;
                                            }
                                        }
                                    } else {
                                        q = -1;
                                        break;
                                    }
                                } else if (p >= '0' && p <= '9') {
                                    int i1, n1 = n;
                                    for (i1 = n1 + 1; i1 < l; i1++) {
                                        p = Input[i1];
                                        n++;
                                        if (p <= '9' && p >= '0') {
                                            ;
                                        } else {
                                            q = -1;//说明输入输错了
                                            break;
                                        }
                                    }
                                    deg[i] = (int) charToNum(Input, n1, i1 - 1);
                                    if (p == '(' || p == '（') {
                                        n--;
                                        p = Input[n];///////////////////////////////
                                        q = 4;//形如x^2的式子输入完成！
                                    } else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                                        n--;
                                        p = Input[n];
                                        q = 4;
                                    } else {
                                        q = -1;
                                        break;
                                    }
                                } else {
                                    q = -1;
                                    break;
                                }
                            }
                            /*else if (p >= 'x' && p <= 'z' || p >= 'a' && p <= 'd' || p >= 'l' && p <= 'n') {
                                //System.out.printf("\nn = %d  p = Input[%d] = %c   i = %d \n",n,n,p,i);
                                n--;
                                p = Input[n];
                                q = 4;
                            }*/
                            else if (p == '/') {
                                //用户打算进行形如 x/2 的输入
                                n++;
                                if (n == l) {
                                    q = -1;
                                    break;
                                } else {
                                    p = Input[n];
                                    if (p >= '0' && p <= '9') {
                                        //用户要进行形如 x/2 的输入
                                        int i1, n1 = n;
                                        for (i1 = n1 + 1; i1 < l; i1++) {
                                            p = Input[i1];
                                            n++;
                                            if (p <= '9' && p >= '0') {
                                                ;
                                            } else {
                                                q = -1;//说明输入输错了
                                                break;
                                            }
                                        }
                                        coe = UD.division(coe, charToNum(Input, n1, i1 - 1));
                                    } else if (p >= 'a' && p <= 'z' || p >= 'A' && p <= 'Z') {
                                        //用户要进行形如 x/y的输入
                                        //本来不可以进行形如 x/y 的输入 但在这里强行让i自增一个试试？
                                        i++;
                                        qu[i] = new Quantum(p);
                                        numQuantum++;
                                        deg[i] = -1;
                                        n++;
                                        if (n == l) {
                                            q = -1;
                                            break;
                                        } else {
                                            q = 4;
                                            p = Input[n];
                                        }
                                    } else {
                                        q = 22;//请保证输入规范
                                        break;
                                    }
                                }
                            }
                            else {
                                q = -1;
                            }
                        }
                    }
                    else {
                        //q = -1;
                        break;
                    }
                    if (q == 4) {
                        n++;
                        if (n == l) {
                            break;
                        } else {
                            p = Input[n];
                        }
                    }
                }
                Quantum[] Qu = new Quantum[numQuantum];
                int[] degree = new int[numQuantum];
                int j = 0;
                for (int ii = 0; ii < numQuantum; ii++) {
                    Qu[j] = qu[ii];
                    degree[j] = deg[ii];
                    j++;
                }
                if (firstWord == false){
                    coe.u=-1* coe.u;
                }
                this.coe = coe;
                if (numQuantum == 0) {
                    this.type = 1;
                }
                else {
                    this.numQuantum = numQuantum;
                    this.unKnow = Qu;
                    this.degree = degree;
                    this.type = 2;
                }
            }
        }
    }

    /**构造一个这样的多项式：（3/5）xyz^2*/
    public Quarks(UD coe, Quantum [] u, int [] deg){
        this.coe=coe;
        this.numQuantum=u.length;
        this.unKnow=u;
        this.degree = deg;
        this.type = 2;
    }

    /**构造一个这样的单项式：（3/5）x^3*/
    public Quarks(long coe_up, long coe_down, char ch, int degree){
        this.coe=new UD(coe_up,coe_down);
        this.numQuantum=1;
        Quantum [] qu = new Quantum [1];
        qu[0]=new Quantum(ch);
        this.unKnow=qu;
        int [] deg = new int [1];
        deg[0] = degree;
        this.degree = deg;
        this.type = 2;
    }

    /**构造一个这样的单项式：（3/5）x*/
    public Quarks(long coe_up, long coe_down, char ch){
        this.coe=new UD(coe_up,coe_down);
        this.numQuantum=1;
        Quantum [] qu = new Quantum [1];
        qu[0]=new Quantum(ch);
        this.unKnow=qu;
        int [] deg = new int [1];
        deg[0] = 1;
        this.degree = deg;
        this.type = 2;
    }

    /**构造一个这样的单项式：3x^2*/
    public Quarks(long coe_up, char ch, int degree){
        this.coe=new UD(coe_up,1);
        this.numQuantum=1;
        Quantum [] qu = new Quantum [1];
        qu[0]=new Quantum(ch);
        this.unKnow=qu;
        int [] deg = new int [1];
        deg[0] = degree;
        this.degree = deg;
        this.type = 2;
    }

    /**构造一个这样的单项式：3x*/
    public Quarks(long coe_up, char ch){
        this.coe=new UD(coe_up,1);
        this.numQuantum=1;
        Quantum [] qu = new Quantum [1];
        qu[0]=new Quantum(ch);
        this.unKnow=qu;
        int [] deg = new int [1];
        deg[0] = 1;
        this.degree = deg;
        this.type = 2;
    }

    public Quarks(Quantum q){
        UD coe = new UD(1);
        this.numQuantum = 1;
        Quantum[] Q = new Quantum[1];
        Q[0] = q;
        this.unKnow = Q;
        this.type = 2;
        this.coe = coe;
        int[] deg = {1};
        this.degree = deg;
    }

    /**构造一个这样的单项式：x*/
    public Quarks(char ch){
        this.coe=new UD(1,1);
        this.numQuantum=1;
        Quantum [] qu = new Quantum [1];
        qu[0]=new Quantum(ch);
        this.unKnow=qu;
        int [] deg = new int [1];
        deg[0] = 1;
        this.degree = deg;
        this.type = 2;
    }

    /**构造一个这样的单项式：3/5*/
    public Quarks(long up, long down){
        this.coe=new UD(up,down);
        this.type = 1;
    }

    /**构造一个这样的单项式：3/5*/
    public Quarks(UD coe){
        this.coe=new UD(coe.u, coe.d);
        this.type = 1;
    }

    /**构造一个这样的单项式：3*/
    public Quarks(long up){
        this.coe=new UD(up,1);
        this.type = 1;
    }

    public boolean equal(Quarks q){ return equal(this,q); }

    public Quarks check(){ return check(this); }

    public void print(){ print(this); }

    public boolean isSimilar(Quarks q){ return isSimilar(this,q); }

    public Quarks eval(Quantum[] u, int[] ev_up, int[] ev_down){ return eval(this,u,ev_up,ev_down); }

    public Quarks eval(Quantum[] u, int[] ev){ return eval(this, u,ev); }

    public Quarks eval(Quantum[] u, UD[] ev){ return eval(this,u,ev); }

    public void Print(){ print(this); }

    public static Quarks copy(Quarks q){
        if (q.type==1){
            //常数项
            UD c = UD.copy(q.coe);
            return new Quarks(c);
        }
        else if (q.type == 2){
            UD c = UD.copy(q.coe);
            int l = q.numQuantum;
            Quantum [] q1 = new Quantum[l];
            int [] deg = new int [l];
            for (int i = 0;i<l;i++){
                q1[i]=Quantum.copy(q.unKnow[i]);
                deg[i]=q.degree[i];
            }
            return new Quarks(c,q1,deg);
        }
        else {
            //异常对象
            Quarks Q = new Quarks();
            Q.type = 0;
            Q.error = q.error;
            return Q;
        }
    }

    /**判断两个单项式是否相同*/
    public static boolean equal(Quarks q1, Quarks q2){
        //先判断他们是不是同类项
        if (q1.isSimilar(q2)) {
            //再判断系数是不是一样的
            return q1.coe.equal(q2.coe);
        }
        else{
            return false;
        }
    }

    /**自检，删去次数为0的未知数，并给未知数排序，以及合并相同的未知数，每次算完加法或者乘法都必须要自检*/
    public static Quarks check(Quarks q){
        //先判断一下，coe_up是否为0，如果是0，那就很好办了
        //注意0多项式！
        if (q.type==2) {
            if (q.coe.u == 0) {
                return new Quarks(0);
            }
            else {
                int num = q.numQuantum;
                if (num == 1){
                    //只有一项，如果这一项的次数不为0的话，确实不需要自检，但是需要提防次数为0的特殊情况
                    if (q.degree[0]!=0){
                        return q;
                    }
                    else {
                        return new Quarks(q.coe);
                    }
                }
                else {
                    //先针对tag进行排序，然后degree同步排序，然后删去degree为0的quantum
                    int j;
                    //排序，依据之前给每一个quantum赋的数字来排序
                    for (int i = 0; i < num; i++) {
                        for (j = 0; j < i; j++) {
                            if (q.unKnow[i].tag < q.unKnow[j].tag) {
                                Quantum temp = q.unKnow[i];int t = q.degree[i];
                                q.unKnow[i] = q.unKnow[j];q.degree[i] = q.degree[j];
                                q.unKnow[j] = temp;q.degree[j] = t;
                            }
                        }
                    }
                    //这就排序完成了
                    //尽管在一个多元单项式中出现未知数相同的情况几乎不可能出现，但是这个地方仍然需要做这一步
                    int l = num;
                    for (int i = 0; i < num - 1; i++) {
                        if (q.unKnow[i].tag == q.unKnow[i + 1].tag) {
                            q.degree[i + 1] += q.degree[i];//合并相同的未知数，注意：这个地方可能会出现0
                            q.degree[i] = 0;
                        }
                    }
                    //合并完成，下面开始消0
                    Quantum[] Q = new Quantum[l];
                    int[] deg = new int[l];
                    j = 0;
                    for (int i = 0; i < l; i++) {
                        if (q.degree[i] != 0) {
                            deg[j] = q.degree[i];
                            Q[j] = q.unKnow[i];
                            j++;
                        }
                    }
                    l = j;
                    //现在给分子和分母化简
                    UD u = new UD(q.coe.u, q.coe.d);//构造的时候就会化简
                    if (l == 0) {
                        //说明是常数项
                        return new Quarks(u);
                    }
                    else {
                        Quantum[] Q1 = new Quantum[l];
                        int[] deg1 = new int[l];
                        for (int i = 0; i < l; i++) {
                            Q1[i] = Quantum.copy(Q[i]);
                            deg1[i] = deg[i];
                        }
                        return new Quarks(u, Q1, deg1);
                    }
                }
            }
        }
        else if(q.type == 1) {
            //常数项只要化简一下系数就可以了
            q.coe = UD.simplify(q.coe);
            return q;
        }
        else {
            //说明是一个异常，直接返回
            return q;
        }
    }

    /**判断两个单项式是否是同类项*/
    public static boolean isSimilar(Quarks q1, Quarks q2){
        if (q1.type==q2.type) {
            if (q1.type == 0||q1.type == 1){
                //异常或者常数项
                return true;
            }
            else {
                //type==2
                int flag = 0;
                int flag1 = 0;
                //先自检一下
                q1 = check(q1);
                q2 = check(q2);
                //再判断
                if (q1.numQuantum == q2.numQuantum) {
                    //未知数个数相等了
                    for (int i = 0; i < q1.numQuantum; i++) {
                        //挨个判断他们每一项是否相同，注意，他们已经排过序了，所以不要在这里找什么bug
                        for (int j = 0; j < q1.unKnow[i].length; j++) {
                            //先判断次数，次数要不一样，那肯定不一样
                            if (q1.degree[i] == q2.degree[i]) {
                                if (q1.unKnow[i].quantum[j] == q2.unKnow[i].quantum[j]) {
                                    flag++;
                                }
                            }
                            flag1++;
                        }
                    }
                    return flag == flag1;
                } else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    public static Quarks reverse(Quarks q){
        //取反
        Quarks Q = Quarks.copy(q);
        Q.coe = Q.coe.reverse();
        return Q;
    }

    /**用于两个多元单项式相乘*/
    public static Quarks multiply(Quarks q1,Quarks q2){
        /*思路是这样的，先将q1和q2的“元”拼接起来，然后再排序，
        这样，就可以把相同的“元”排到一起，再扫描一遍，就可以完成合并，
        复杂度取决于排序的复杂度，本函数暂时使用插入排序*/
        q1 = q1.check();
        q2 = q2.check();
        if (q1.type == 0||q2.type == 0){
            Quarks Q = new Quarks();
            Q.type = 0;
            if (q1.type == 0&&q2.type!=0){
                Q.error = q1.error;
            }
            else if (q2.type == 0&&q1.type!=0){
                Q.error = q2.error;
            }
            else {
                Q.error = 3;
            }
            return Q;
        }
        if (q1.coe.u==0||q2.coe.u==0){
            return new Quarks(0);
        }
        else if (q1.type == 1){
            //q1是常数项
            if (q2.type == 1){
                //q2也是常数项
                UD coe = UD.multiply(q1.coe,q2.coe);
                return new Quarks(coe);
            }
            else if (q2.type == 2){
                //q2不是常数项
                Quarks q3 = Quarks.copy(q2);
                q3.coe = UD.multiply(q1.coe,q2.coe);
                return q3;
            }
            else {
                Quarks Q = new Quarks();
                Q.type = 0;
                Q.error = 4;//出现了未定义的多元单项式类型
                return Q;
            }
        }
        else if (q1.type == 2){
            //q1不是常数项
            if(q2.type == 1){
                //q2是常数项
                Quarks q3 = Quarks.copy(q1);
                q3.coe = UD.multiply(q1.coe,q2.coe);
                return q3;
            }
            else if (q2.type == 2) {
                //q1和q2都不是常数项,核心代码
                int i;
                UD c = UD.multiply(q1.coe, q2.coe);
                int l = q1.numQuantum + q2.numQuantum;
                int l1 = q1.numQuantum;
                Quantum[] Q = new Quantum[l];
                int[] deg = new int[l];
                for (i = 0; i < q1.numQuantum; i++) {
                    Q[i] = Quantum.copy(q1.unKnow[i]);
                    deg[i] = q1.degree[i];
                }
                int j;
                for (i = 0; i < q2.numQuantum; i++) {
                    boolean sign = false;
                    for (j = 0; j < q1.numQuantum; j++) {
                        if (q2.unKnow[i].equals(q1.unKnow[j])) {
                            //找到了同类项，次数相加
                            deg[j] = q1.degree[j] + q2.degree[i];//注意，这个地方不排除出现0次的可能
                            sign = true;
                        }
                    }
                    if (sign == false) {
                        //说明q2的这一个元在q1中没有出现过，那么直接拼接到q1的后面
                        Q[l1] = Quantum.copy(q2.unKnow[i]);
                        deg[l1] = q2.degree[i];
                        l1++;
                    }
                }
                //l1才是有效长度
                /*System.out.print("\n Q =  \n");
                for (i = 0;i<l1;i++){
                    Q[i].print();
                    System.out.printf("  deg[%d] = %d",i,deg[i]);
                    System.out.print("\t");
                }
                System.out.print("\n ------------------ \n");*/
                j = 0;
                Quantum[] q = new Quantum[l1];
                int[] deg1 = new int[l1];
                for (i = 0; i < l; i++) {
                    if (deg[i] != 0) {
                        //出现有某一项的次数不为0
                        //注意：在经过上一步之后，即合并同类项之后，
                        //有可能出现相加之后次数为0的情况
                        //所以需要进行下一个循环，因为本循环中的j其实也记录了次数不为0的“元”的个数
                        q[j] = Q[i];
                        deg1[j] = deg[i];
                        j++;
                    }
                }
                int length = j;
                Quantum[] q3 = new Quantum[length];
                int[] deg3 = new int[length];
                for (int j1 = 0; j1 < length; j1++) {
                    q3[j1] = q[j1];
                    deg3[j1] = deg1[j1];
                }
                //排个序
                for (i = 0; i < length; i++) {
                    for (j = 0; j < length - i - 1; j++) {
                        if (q3[j].tag > q3[j + 1].tag) {
                            Quantum temp = q3[j];int C = deg3[j];
                            q3[j] = q3[j + 1];deg3[j] = deg3[j + 1];
                            q3[j + 1] = temp;deg3[j + 1] = C;
                        }
                    }
                }
                return new Quarks(c, q3, deg3);
            }
            else {
                Quarks Q = new Quarks();
                Q.type = 0;
                Q.error = 4;//出现了未定义的多元单项式类型
                return Q;
            }
        }
        else{
            Quarks Q = new Quarks();
            Q.type = 0;
            Q.error = 4;//出现了未定义的多元单项式类型
            return Q;
        }
    }

    public static void printError(Quarks q){
        if (q.type == 0){
            switch (q.error){
                case 0:{
                    System.out.print("没有异常");
                    break;
                }
                case 1:{
                    System.out.print("不是同类项，无法相加减");
                    break;
                }
                case 21:{
                    System.out.print("缺失匹配的括号");
                    break;
                }
                case 22:{
                    System.out.print("请保持输入完整");
                    break;
                }
                case 23:{
                    System.out.print("遇到了无法识别的符号");
                    break;
                }
                case 24:{
                    System.out.print("请不要让分母为0");
                    break;
                }
                case 25:{
                    System.out.print("不接受形如 1/(x^-2) 或者 1/x^(-2) 的式子");
                    break;
                }
                case 26:{
                    System.out.print("不接受形如 (x^-2) 或 (x^-2) 的式子");
                    break;
                }
                case 27:{
                    System.out.print("前面已经有0了");
                    break;
                }
                default:{
                    System.out.print("未定义的异常类型");
                }
            }
        }
        else {
            System.out.print("没有异常");
        }
    }

    public static void print(Quarks q){
        //这个随便写写
        //先打印系数
        if (q.type==0) {
            Quarks.printError(q);
        }
        else if (q.type == 1) {
            //常数项
            q.coe.print();
        }
        else if (q.type == 2){
            if (q.coe.d!=1) {
                System.out.print("(");
                q.coe.print();
                System.out.print(")");
            }
            else if (q.coe.u == 1||q.coe.u == q.coe.d){
                //1不打印出来
            }
            else if (q.coe.u == -1||q.coe.u*q.coe.d==-1){
                System.out.print("-");
            }
            else {
                q.coe.print();
            }
            //再挨个打印后面的
            for (int i = 0; i < q.numQuantum; i++) {
                q.unKnow[i].print();
                if (q.degree[i] == 2){
                    //System.out.print("²");
                    System.out.print("^2");
                }
                else if (q.degree[i] == 3){
                    //System.out.print("³");
                    System.out.print("^3");
                }
                else if (q.degree[i] == 1){

                }
                else if (q.degree[i]>=3){
                    System.out.printf("^%d",q.degree[i]);
                }
                else {
                    System.out.printf("^(%d)", q.degree[i]);
                }
            }

        }
        else {
            System.out.println("\n出现bug！");
        }
    }

    public static Quarks eval(Quarks q, Quantum[] u, int[] ev_up, int[] ev_down){
        int num = u.length;
        UD [] E = new UD[num];
        for (int i=0;i<num;i++){
            E[i].u=ev_up[i];
            E[i].d=ev_down[i];
        }
        return Quarks.eval(q,u,E);
    }

    public static Quarks eval(Quarks q, Quantum[] u, int[] ev){
        int [] ev_d = new int [u.length];
        for (int i=0;i<u.length;i++){
            ev_d[i]=1;
        }
        return Quarks.eval(q,u,ev,ev_d);
    }

    /**求单项式在ev处的值*/
    public static Quarks eval(Quarks q, Quantum[] u, UD[] ev){
        if (q.type == 0||q.type == 1){
            return q;
        }
        else if (q.type == 2) {
            if (q.coe.u == 0) {
                return new Quarks(0);
            } else {
                int num = u.length;
                //单项式求值应该是这样的，传入两个数组，一个数组记录Quantum，一个记录对应的值，x->1   y->2    z->3如此这般
                //先对q进行自检
                q = q.check();
                //是否应当对Quantum数组进行排序？我希望对两个数组只扫描一遍，决定了，对传入的Quantum参数也进行排序
                //假如q的字符集是空格，代表它无未知数，那么把系数返回就是了
                int i, j, j1;
                //从q的字符集中找，挨个在传入的参数u中寻找q的字符集
                //先对Quantum数组进行排序
                char[] s = new char[num];
                for (i = 0; i < num; i++) {
                    s[i] = u[i].quantum[0];
                }
                for (i = 0; i < num; i++) {
                    for (j = 0; j < i; j++) {
                        if (s[i] < s[j]) {
                            char temp = s[i];UD t = ev[i];Quantum q_temp = u[i];
                            s[i] = s[j];ev[i] = ev[j];u[i] = u[j];
                            s[j] = temp;ev[j] = t;u[j] = q_temp;
                        }
                    }
                }
                //排序完成
                //开始提取出需要的ev
                UD[] E = new UD[q.numQuantum];
                //int [] E_up = new int [q.numQuantum];
                //int [] E_down = new int [q.numQuantum];
                for (i = 0; i < q.numQuantum; i++) {
                    E[i] = new UD();
                    E[i].d = 0;//为什么要初始化为0？这样的话，我才能判断到底是q中的哪个未知数没有在quantum中找到对应的ev
                }
                i = 0;
                int l = q.numQuantum;
                for (j1 = 0; j1 < num; j1++) {
                    if (q.unKnow[i].equals(u[j1])) {
                        if (ev[j1].u == 0) {
                            return new Quarks(0);
                        }
                        E[i] = ev[j1];
                        l--;
                        break;
                    }
                }
                if (j1 == num) j1 = -1;
                for (i = 1; i < q.numQuantum; i++) {
                    for (j = j1 + 1; j < num; j++) {
                        if (q.unKnow[i].equals(u[j])) {
                            if (ev[j].u == 0) {
                                return new Quarks(0);
                            }
                            E[i] = ev[j];
                            j1 = j;//保证对quantum数组只遍历一次
                            l--;
                            break;
                        }
                        //if (j==num)
                        //   j1=-1;
                    }
                }
                //提取完成
                //开始计算
                //这个地方，需要好好斟酌一下，如果传入的quantum数组中不都有q中的unknown的未知数，那么也就意味着算不出具体的数值，算出的就还是单项式
                UD coe1 = q.coe;
                //long coe_up=q.coe_up,coe_down=q.coe_down;
                Quantum[] qu = new Quantum[l];
                int[] deg = new int[l];
                for (i = 0, j = 0; i < q.numQuantum; i++) {
                    if (E[i].d == 0) {
                        //说明这一项没有找到对应的值
                        qu[j] = q.unKnow[i];
                        deg[j] = q.degree[i];
                        j++;
                    } else {
                        //开始连乘
                        for (j1 = 0; j1 < q.degree[i]; j1++) {
                            coe1 = coe1.multiply(E[i]);
                            //coe_up=coe_up*E_up[i];
                            //coe_down=coe_down*E_down[i];
                        }
                        if (i % 3 == 0) {
                            //每乘三次，就化简一下
                            coe1 = coe1.simplify();
                        }
                    }
                }
                coe1 = coe1.simplify();//最后再化简一次
                if (l == 0) {
                    //说明全部找到了，返回的应该是数值
                    return new Quarks(coe1);
                } else {
                    //返回的应该是单项式
                    return new Quarks(coe1, qu, deg);
                }
            }
        }
        else {
            Quarks Q = new Quarks();
            Q.type = 0;
            Q.error = 4;//出现了未定义的多元单项式类型
            return Q;
        }
    }

    public static Quarks[] sort(Quarks[] quarks,int length) {
        //先检查一下，有没有异常对象
        for (int i = 0;i<length;i++){
            quarks[i] = quarks[i].check();
            if (quarks[i].type == 0||quarks[i].type>2){
                Quarks [] Q = {new Quarks()};
                Q[0].type = 0;
                Q[0].error = 4;//出现了未定义的多元单项式类型
                return Q;
            }
        }
        //检查完成，这个地方的排序方法是字典排序法
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (Quarks.lexicographicalOrder(quarks[i],quarks[j])==1) {
                    Quarks temp = quarks[i];
                    quarks[i] = quarks[j];
                    quarks[j] = temp;
                }
            }
        }
        return quarks;
    }

    /**字典排序法，如果q1应该在q2前面，那么返回1，否则返回-1，有异常，返回0，不过一般不会返回0，在使用这个方法之前会进行检查的*/
    public static byte lexicographicalOrder(Quarks q1,Quarks q2){
        //常数项一定是排在后面的
        if (q1.type == 0||q2.type == 0){
            return 0;
        }
        else if (q1.type == 1) {
            if (q2.type == 1) {
                return 1;
            }
            else if (q2.type == 2){
                return -1;
            }
            else {
                return 0;//异常！
            }
        }
        else if (q2.type == 1) {
            if (q1.type == 2) {
                return 1;
            }
            else {
                return 0;
            }
        }
        else {
            //核心代码
            //获取q1和q2的共同字符集的tag
            //依次检索q1和q2的字符集合，在Tag中找到相同的就把把对应的deg赋给序列，否则置为0
            int l = q1.numQuantum + q2.numQuantum;
            int l1 = q1.numQuantum;
            short[] Tag = new short[l];
            //先获取q1和q2的共同字符集的tag
            for (int i = 0; i < q1.numQuantum; i++) {
                Tag[i] = q1.unKnow[i].tag;
            }
            for (int i = 0; i < q2.numQuantum; i++) {
                short sign = 0;
                for (int j = 0; j < q1.numQuantum; j++) {
                    if (q2.unKnow[i].tag != q1.unKnow[j].tag) {
                        sign++;
                    }
                }
                if (sign == q1.numQuantum){
                    Tag[l1] = q2.unKnow[i].tag;
                    l1++;
                }
            }
            //完成！，此时，有效长度为l1，但是我不打算重新再设置一个数组保存有效的Tag，同时，还是需要对Tag进行排序的
            short current;
            for (short i = 0; i < l1 - 1; i++) {
                current = Tag[i + 1];
                short preIndex = i;
                while (preIndex >= 0 && current < Tag[preIndex]) {
                    Tag[preIndex + 1] = Tag[preIndex];
                    preIndex--;
                }
                Tag[preIndex + 1] = current;
            }//对Tag排序完成，使用插入排序
            /*System.out.println("\n显示Tag：");
            for (int i = 0;i<l1;i++){
                System.out.printf("%d\t",Tag[i]);
            }
            System.out.println("\nTag显示完毕");*/
            int [] xuq1 = new int[l1];
            int [] xuq2 = new int[l1];
            /*for (int i = 0;i<q1.numQuantum;i++){
                System.out.printf("q1.deg[%d] = %d,q1.Tag[%d] = %d\t",i,q1.degree[i],i,q1.unKnow[i].tag);
            }
            System.out.println();
            for (int i = 0;i<q2.numQuantum;i++){
                System.out.printf("q2.deg[%d] = %d,q2.Tag[%d] = %d\t",i,q2.degree[i],i,q2.unKnow[i].tag);
            }
            System.out.println();*/
            //下面开始对序列进行赋值
            for (int i = 0; i < l1; i++) {
                for (int j = 0; j < q1.numQuantum; j++) {
                    if (q1.unKnow[j].tag == Tag[i]) {
                        xuq1[i] = q1.degree[j];
                        break;
                    }
                    else {
                        xuq1[i] = 0;
                    }
                }
            }
            for (int i = 0; i < l1; i++) {
                for (int j = 0; j < q2.numQuantum; j++) {
                    if (q2.unKnow[j].tag == Tag[i]) {
                        xuq2[i] = q2.degree[j];
                        break;
                    }
                    else {
                        xuq2[i] = 0;
                    }
                }
            }
            //序列赋值完成
            /*System.out.println();
            for (int i = 0;i<l1;i++){
                System.out.printf("%d\t",xuq1[i]);
            }
            System.out.println();
            for (int i = 0;i<l1;i++){
                System.out.printf("%d\t",xuq2[i]);
            }
            System.out.println();*/
            int i;
            for (i = 0; i < l1; i++) {
                if (xuq1[i] > xuq2[i]) {
                    return 1;
                } else if (xuq1[i] < xuq2[i]) {
                    return -1;
                }
                //其余情况就是两个序列对应的值相等，那么就比较下一个
            }
            return 1;//两个序列完全相同，那么就认为q1在前
        }
    }

    public static Quarks[] merge(Quarks[] Q){
        //先排序，再合并同类项
        //合并同类项
        if (Q.length<=1){
            return Q;
        }
        else {
            for (int i = 0;i<Q.length;i++){
                /*System.out.printf("\ni = %d, Q[%d].type = %d\n",i,i,Q[i].type);
                Q[i].print();
                System.out.println();*/
                Q[i]=Q[i].check();
            }
            Quarks [] Q1 = Quarks.sort(Q,Q.length);
            for (int i = 0; i < Q.length - 1; i++) {
                if (Q[i].isSimilar(Q[i+1])) {
                    Q1[i + 1]=Quarks.copy(Q[i + 1]);
                    Q1[i + 1].coe = UD.add(Q[i + 1].coe, Q[i].coe);//此处可能导致出现新的0，
                    //然后把Q[i]置空
                    Q1[i].coe.u = 0;
                }
            }
            int j = 0;
            int l = 0;
            Quarks [] q = new Quarks [Q.length];
            for (int i = 0; i < Q1.length; i++) {
                if (Q1[i].coe != null &&Q1[i].coe.u != 0) {/////// 2021.1.21修改，之前为if(Q[i].coe.u != 0)
                    //出现有某一项的系数不为0
                    q[j] = Q1[i];
                    j++;
                    l++;
                }
            }
            if (l == 0){
                //合并完成之后，全为0,需要返回一个常数项
                return new Quarks[]{
                        new Quarks(0)
                };
            }
            else {
                Quarks[] q1 = new Quarks[l];
                for (int i = 0; i < l; i++) {
                    q1[i] = q[i];
                }
                return q1;
            }
        }
    }

    public static long charToNum(char[] num,int i,int j){
        //本方法用于将字符转为数字
        int l = j - i + 1;
        long n = Character.getNumericValue(num[j]);
        long Ten = 10;
        for (int s = j - 1;s>=i;s--){
            long temp = Character.getNumericValue(num[s]);
            n = n + temp*Ten;
            Ten = Ten*Ten;
        }
        return n;
    }

    public static Quarks stringToQuarks(String str){ return new Quarks(str); }

    /**本函数是AToms类中的division函数的子函数，用返回Q2中的文字在Q1中的下标，如果Q1中没有，返回-1*/
    public static int exist(Quarks Q1,Quarks Q2){
        if (Q2.type==1||Q1.type == 1){
            return -1;
        }
        for (int i = 0;i<Q1.numQuantum;i++){
            if (Q1.unKnow[i].equals(Q2.unKnow[0])){
                return i;
            }
        }
        return -1;
    }

    /**两个多元单项式的除法，设计本函数的初衷是为多元多项式除一元多项式函数服务的*/
    public static Quarks division(Quarks Q1,Quarks Q2){
        //程序应该需要先判断Q2中的元在Q1中可以找到，如果没有，返回错误
        //实际上，在调用此函数之前，AToms类的div函数会调用exist函数来进行判断，所以这个地方在理论上是不会出现Q2中的文字不在Q1中的问题的
        int index = exist(Q1,Q2);
        Quarks Q;
        if (index!=-1){
            //只需要把Q2在Q1中对应的文字的次数减去Q2的文字的次数即可
            Q = Quarks.copy(Q1);
            //首先，先把系数确定下
            Q.coe = UD.division(Q1.coe,Q2.coe);
            Q.degree[index] = Q.degree[index] - Q2.degree[0];
            Q = Q.check();
        }
        else {
            Q = new Quarks();
            Q.type = 0;
            Q.error = 5;
        }
        return Q;
    }

/*
    public static String QuarksToString(Quarks Q){
        Q = Q.check();
        switch (Q.type){
            case 0:return "错误";
            case 10:return "0";
            case 11:return "1";
            case 1:return UD.UDToString(Q.coe);
            case 2:{
                //如果系数是整数，就不需要加括号，例如2x，3sin这种，
                if (Q.coe.d==1){
                    String s = String.valueOf(Q.coe.u);
                    for (byte i = 0;i<Q.unKnow.length;i++){

                    }
                }
                else {
                    //分数的话，需要把括号加上，注意，如果系数为负，那么负号应该在括号外面
                }
            }
        }
    }
 */

}
