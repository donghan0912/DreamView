package com.dream.dreamview.test;


import java.lang.reflect.Array;
import java.util.Arrays;

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
//    search(ints2, 88);
//    int[] arr = {8, 2, 1, 4,6,7, 3, 5, 9, 6,11,19,13,55,67,32,22};
    int[] arr = {8, 2, 1, 9, 4};
//    quickSort(arr, 0, ints2.length);
    funQuickSort(arr, 0, arr.length - 1);
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
  static boolean search(int[] array, int value) {
    int start = 0;
    int end = array.length - 1;
    while (start <= end) {
      int mid = (start + end) / 2;
      if (value == array[mid]) {
        return true;
      } else if (value < array[mid]) {
        end = mid - 1;
      } else {
        start = mid + 1;
      }
    }
    return false;
  }

  static void quickSort(int[] array, int start, int end) {
    for (int i = 0; i < array.length; i++) {

    }
    quick(array, 0, array.length - 1);
    System.out.println("快速排序算法：" + Arrays.toString(array));
  }

  static void quick(int[] array, int start, int end) {
    int base = array[start];
    int n = start;
    for (int i = start + 1; i < array.length; i++) {
      if (array[i] < base) {
        int temp = array[i];
        array[i] = array[n];
        array[n] = temp;
        n++;
      }
    }
  }

  // 快速排序
  static void funQuickSort(int[] mdata, int start, int end) {
    if (end > start) {
      int pivotIndex = quickSortPartition(mdata, start, end);
      System.out.println("快速排序算法：" + Arrays.toString(mdata));
      funQuickSort(mdata, start, pivotIndex - 1);
      funQuickSort(mdata, pivotIndex + 1, end);
    }
  }

  // 快速排序前的划分
  static int quickSortPartition(int[] list, int first, int last) {
    // {8, 2, 9, 4,10} {8, 2, 4, 9,10}
    int pivot = list[first];
    int low = first + 1;
    int high = last;

    while (high > low) {
      while (low <= high && list[low] <= pivot) {
        low++;
      }

      while (low <= high && list[high] > pivot) {
        high--;
      }

      if (high > low) {
        int temp = list[high];
        list[high] = list[low];
        list[low] = temp;
      }
    }

    while (high > first && list[high] >= pivot) {
      high--;
    }

    if (pivot > list[high]) {
      list[first] = list[high];
      list[high] = pivot;
      return high;
    } else {
      return first;
    }
  }


}

