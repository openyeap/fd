package ltd.fdsa.research.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/*
 *  Fast Sort
 */
@Slf4j
public class TopNSearch {

    public void demo(String[] args) {

        int[] array = {1, 2, 8, 5, 9, 3, 7, 4, 6};
        int n = 5;
        quickSort2(array, 0, array.length - 1);
        quickSearchN(array, 0, array.length - 1, n);

        log.info(Arrays.toString(array));
    }

    /*
     * /* 从前往后搜索把最后一个元素作为基线
     */
    void quickSort2(int[] array, int left, int right) {
        log.info(Arrays.toString(array));
        System.out.printf("left:%d, right:%d \n\r", left, right);

        if (left >= right) {
            return;
        }
        int key = array[right];
        int i = left;
        int j = right;

        while (i < j) {
            while (i < j && array[i] >= key) {
                i++;
            }
            array[j] = array[i];

            while (i < j && array[j] <= key) {
                j--;
            }
            array[i] = array[j];
        }
        array[j] = key;
        quickSort2(array, left, j - 1);
        quickSort2(array, j + 1, right);
    }

    /*
     * 从前往后搜索把最后一个元素作为基线
     */
    void quickSearchN(int[] array, int left, int right, int top) {
        log.info(Arrays.toString(array));
        System.out.printf("left:%d, right:%d, top:%d \n\r", left, right, top);

        if (top <= 0) {
            return;
        }
        if (left >= right) {
            return;
        }

        int key = array[right];
        int i = left;
        int j = right;
        while (i < j) {
            while (i < j && array[i] >= key) {
                i++;
            }
            array[j] = array[i];

            while (i < j && array[j] <= key) {
                j--;
            }
            array[i] = array[j];
        }
        array[j] = key;

        if (j > top) {
            // 结果集多再过滤一下
            log.info("结果集多再过滤一下");
            quickSearchN(array, left, j - 1, top);
        } else {
            // 结果集少到右边再找一些
            log.info("结果集少到右边再找一些");
            quickSearchN(array, j + 1, right, top - j);
        }
    }
}
