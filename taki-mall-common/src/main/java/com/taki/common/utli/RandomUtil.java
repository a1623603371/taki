package com.taki.common.utli;

import java.util.Random;

public class RandomUtil {

    /**
     * 生成指定长度的随机数
     *
     * @param length
     * @return
     */
    public static String genRandomNumber(int length) {

        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            sb.append(sources.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }


    private static final String ALL_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String genRandomNumberStr(int length) {
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        return saltString.toString();
    }


//    public static void main(String[] args) {
//        System.out.println(genRandomNumber(20));
//    }
    /*** 
     * @description: 生成随机数
     * @param bond
     * @return  int
     * @author Long
     * @date: 2023/2/18 21:01
     */ 
    public static  int genRandInt(int  bond){
        return new Random().nextInt(bond);
    }

    /*** 
     * @description:  生成区间范围的随机数
     * @param min 最小数
     * @param max 最大数
     * @return  int
     * @author Long
     * @date: 2023/2/18 21:03
     */ 
    public static  int genRandInt(int  min,int max){
        return new Random().nextInt(max - min) + min;
    }

}