package com.dream.dreamview.test;

/**
 * Created on 2018/8/13.
 */
public class Demo2 {

  public static void main(String[] args) {

  }

  // 常见算法题

  /**
   * 1. 不使用临时变量交换两个数
   */
  static void funSwapTwo(int a, int b) {
    a = a ^ b; // (^位异或运算, 转换二进制之后，0变1,1变0)
    b = b ^ a;
    a = a ^ b;
    System.out.println(a + " " + b);
  }

  /**
   * 判断一个数是否为素数(素数：大于1，只能被1和它本身整除的整数)
   */
  static boolean funIsPrime(int m) {
    boolean flag = true;
    if (m == 1) {
      flag = false;
    } else {
      for (int i = 2; i <= Math.sqrt(m); i++) {// Math.sqrt() 方法用于返回参数的算术平方根(返回值double类型)
        if (m % i == 0) {
          flag = false;
          break;
        }
      }
    }
    return flag;
  }

}
