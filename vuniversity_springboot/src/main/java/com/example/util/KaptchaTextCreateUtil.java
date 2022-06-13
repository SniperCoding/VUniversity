package com.example.util;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

public class KaptchaTextCreateUtil extends DefaultTextCreator {
    private static final String[] CNUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

    @Override
    public String getText() {
        int result = 0;  // 存储计算表达式的结果
        Random random = new Random();
        int x = random.nextInt(10);  // 随机的第一个操作数
        int y = random.nextInt(10);  // 随机的第二个操作数
        StringBuilder suChinese = new StringBuilder();  // 存储表达式
        int randomOperands = (int) Math.round(Math.random() * 3);  // 随机运算符 取 0 1 2 3 四个整数
        if (randomOperands == 0) {  // 乘法
            result = x * y;
            suChinese.append(CNUMBERS[x]).append("*").append(CNUMBERS[y]);
        } else if (randomOperands == 1 && x != 0 && y % x== 0) {  // 除法，要求 x 不等于0，且 y / x 为整除,则将除法改变为加法
            result = y / x;
            suChinese.append(CNUMBERS[y]).append("/").append(CNUMBERS[x]);
        } else if (randomOperands == 2) {   // 减法
            int m = Math.max(x, y);
            int n = Math.min(x, y);
            result = m - n;
            suChinese.append(CNUMBERS[m]).append("-").append(CNUMBERS[n]);
        } else {  // 加法
            result = x + y;
            suChinese.append(CNUMBERS[x]).append("+").append(CNUMBERS[y]);
        }
        suChinese.append("@" + result);  // 返回运算表达式和结果，用@符号分割
        return suChinese.toString();
    }
}
