package ltd.fdsa.demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.springframework.util.StringUtils;

/*
 *  Fast Sort   
 */
public class FastSort {

	public void demo(String[] args) {

		int[] array = { 25, 12, 2, 34, 35, 25, 12, 2, 34, 35 };

		quickSort(array, 0, array.length - 1);

		for (int i = 0; i < array.length; i++) {
			System.out.printf("%d:%d \n", i, array[i]);
		}
	}

	/*
	 * 从后往前搜索，把第一个元素作为基线
	 */
	void quickSort(int[] array, int left, int right) {
		System.out.println(Arrays.toString(array));
		System.out.printf("left:%d, right:%d \n\r", left, right);
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
	void quickSort2(int[] array, int left, int right) {
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