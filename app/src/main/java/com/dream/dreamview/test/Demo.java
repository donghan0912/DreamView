package com.dream.dreamview.test;


import java.lang.reflect.Array;

/**
 * Created on 2018/8/9.
 */
public class Demo {
  static Node<String> head; // 头部
  static Node<String> tail; // 尾部

  public static void main(String[] args) {
    Node<String> node = new Node<String>("111111");
    Node<String> node2 = new Node<String>("222222");
    Node<String> node3 = new Node<String>("333333");
    Node<String> node4 = new Node<String>("444444");
    init(node);
    insert(node2);
    insert(node3);
    insert(node4);
    Node<String> current = head;
    while (current != null) {
      System.out.println(current.item);
      current = current.next;
    }
//    System.out.println("ok");
    int[] ints = {4, 1, 6, 3, 9, 8};
    int[] ints2 = {1, 11, 22, 33, 44, 55, 66, 77, 88};
//    funBubbleSort(ints);
//    selectSort(ints);
    search(ints2, 88);
  }

  private static void init(Node node) {
    head = node;
    tail = head;
  }

  private static void insert(Node node) {
    tail.next = node;
    tail = tail.next;
  }

  public static class Node<E> {
    E item;
    Node<E> next;

    //构造函数
    Node(E element) {
      this.item = element;
      this.next = null;
    }
  }

  // 冒泡排序 注意 flag 的作用
  static void funBubbleSort(int[] array) {

    /*boolean flag = true;
    for (int i = 0; i < array.length - 1 && flag; i++) {
      flag = false;
      for (int j = 0; j < array.length - 1 - i; j++) {
        if (array[j] > array[j + 1]) {
          int temp = array[j];
          array[j] = array[j + 1];
          array[j + 1] = temp;
          flag = true;
        }
      }
    }*/
    for (int i = 0; i < array.length - 1; i++) {
      for (int j = 0; j < array.length - 1 - i; j++) {
        if (array[j] > array[j + 1]) {
          int temp = array[j];
          array[j] = array[j + 1];
          array[j + 1] = temp;
        }
      }
    }
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i] + "==");
    }
  }

  // 选择排序
  static void selectSort(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      int mark = i;
      // 第一步，从未排序的数组中，找到最小值的角标
      for (int j = i + 1; j < array.length; j++) {
        if (array[j] < array[mark]) {
          mark = j;
        }
      }
      // 第二步，将最小值放到前面
      if (mark != i) {
        int temp = array[mark];
        array[mark] = array[i];
        array[i] = temp;
      }
    }
    System.out.print("选择排序");
    for (int i = 0; i < array.length; i++) {
      System.out.print(array[i] + "==");
    }
  }

  // 二分查找
  static void search(int[] array, int value) {
    int low = 0;
    int high = array.length - 1;
    int tttt = -1;
    while (low <= high) {
      int mid = (low + high) / 2;
      if (value == array[mid]) {
        tttt = mid;
        break;
      } else if (value < array[mid]) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }
    System.out.print(tttt + "======");
  }

}

