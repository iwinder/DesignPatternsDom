package Others.base;

import Utills.PrintUtill;

/***
 * [Java调用函数传递参数到底是值传递还是引用传递](https://www.cnblogs.com/wutianqi/p/8723582.html)
 * [java之语法之方法调用之地址传值之空对象（null）](https://lixh1986.iteye.com/blog/2354853)
 * [Java参数引用传递之例外：null](https://blog.csdn.net/stevesun13/article/details/51922169)
 */

public class StringDemo3 {
    public static void main(String[] args) {
        String s = "aaa";
        add(s);
        PrintUtill.println(s);
    }

    public static void add(String s){
        PrintUtill.printlnRule();
        s = "bbb";
        PrintUtill.println(s);
        PrintUtill.printlnRule();
    }
}