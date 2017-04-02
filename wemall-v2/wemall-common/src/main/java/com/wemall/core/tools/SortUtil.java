package com.wemall.core.tools;

/**
 * 排序工具类
 */
public class SortUtil {
    /**
     * 冒泡排序
     *
     * @paramsrc待排序数组
     */
    public static void doBubbleSort(int[] src){
        int len = src.length;
        for (int i = 0; i < len; i++){
            for (int j = i + 1; j < len; j++){
                int temp;
                if (src[i] > src[j]){
                    temp = src[j];
                    src[j] = src[i];
                    src[i] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     *
     * @paramsrc待排序的数组
     */
    public static void doChooseSort(int[] src){
        int len = src.length;
        int temp;
        for (int i = 0; i < len; i++){
            temp = src[i];
            int j;
            int samllestLocation = i;// 最小数的下标
            for (j = i + 1; j < len; j++){
                if (src[j] < temp){
                    temp = src[j];// 取出最小值
                    samllestLocation = j;// 取出最小值所在下标
                }
            }
            src[samllestLocation] = src[i];
            src[i] = temp;
        }
    }

    /**
     * 插入排序
     *
     * @paramsrc待排序数组
     */
    public static void doInsertSort1(int[] src){
        int len = src.length;
        for (int i = 1; i < len; i++){
            int temp = src[i];
            int j = i;

            while (src[j - 1] > temp){
                src[j] = src[j - 1];
                j--;
                if (j <= 0)
                    break;
            }
            src[j] = temp;
        }
    }
}
