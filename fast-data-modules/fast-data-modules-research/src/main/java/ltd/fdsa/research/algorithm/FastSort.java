package ltd.fdsa.research.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/*
 *  Fast Sort
 */
@Slf4j
public class FastSort {

    /*
     * 从后往前搜索，把第一个元素作为基线
     */
    public void quickSort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int key = array[left];
        int i = left;
        int j = right;

        while (i < j) {
            // 从后往前找到第一个比key小的数与array[i]交换；
            while (i < j && array[j] >= key) {
                j--;
            }
            array[i] = array[j];
            // 从前往后找到第一个比key大的数与array[j]交换；
            while (i < j && array[i] <= key) {
                i++;
            }
            array[j] = array[i];
        }
        // 一趟快排之后已经将key的位置找到
        array[i] = key;

        // 对key左边的进行排序
        quickSort(array, left, i - 1);
        // 对key右边的进行排序
        quickSort(array, i + 1, right);
    }

    /*
     * 从前往后搜索把最后一个元素作为基线
     */
    public void quickSort2(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int key = array[right];
        int i = left;
        int j = right;

        while (i < j) {
            while (i < j && array[i] <= key) {
                i++;
            }
            array[j] = array[i];

            while (i < j && array[j] >= key) {
                j--;
            }
            array[i] = array[j];
        }
        array[j] = key;
        quickSort2(array, left, j - 1);
        quickSort2(array, j + 1, right);
    }
}
