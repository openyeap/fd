package cn.zhumingwu.research.algorithm;


import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/*
 *  Breadth-First Search
 */
@Slf4j
public class Solution {
    public int removeDuplicates(int[] nums) {

        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    // [1,2,3,4,5] [7,1,5,3,6,4]
    public int maxProfit(int[] prices) {
        int max = 0;
        int buy = 0;
        int oldPrice = prices[0];
        int newPrice = 0;
        for (int i = 1; i < prices.length; i++) {
            newPrice = prices[i];
            if (oldPrice < newPrice) {
                //持有
                if (buy == 0) {
                    buy = oldPrice;
                }
                max += newPrice - oldPrice;

            } else {
                //卖出
                buy = 0;
            }
            oldPrice = prices[i];
        }
        return max;
    }

    //   [1,2,3,4,5,6,7] 和 k = 3
    //   [5,6,7,1,2,3,4]
    //   [-1,-100,3,99] 和 k = 2
    public void rotate(int[] nums, int k) {
        int l = nums.length;
        int[] arrays = new int[l];
        for (int i = 0; i < l; i++) {
            arrays[i] = nums[(l - k + i) % l];
        }
        for (int i = 0; i < l; i++) {
            nums[i] = arrays[i];
        }
    }

    public void rotate2(int[] nums, int k) {
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);

    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }


    public boolean containsDuplicate(int[] nums) {
        var set = new HashSet<Integer>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if (!set.add(nums[i])) {
                return true;
            }
        }
        return false;
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            var sb = new ArrayList<String>();
            sb.add(this.val + "");
            var next = this.next;
            while (next != null) {
                sb.add(next.val + "");
                next = next.next;
            }
            return "[" + String.join(",", sb) + "]";
        }
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        int len = 1;
        var next = head.next;
        while (next != null) {
            len++;
            next = next.next;
        }
        int x = len - n;
        if (x == 0) {
            return head.next;
        }
        if (x < 0) {
            return null;
        }
        next = head;
        for (int i = 0; i < x - 1; i++) {
            next = next.next;
        }
        if (x + 1 >= len) {
            next.next = null;
        } else {
            next.next = next.next.next;
        }
        return head;
    }

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode node = null;

        int carry = 0;
        ListNode next = null;
        while (true) {
            if (l1 == null && l2 == null && carry == 0) {
                break;
            }
            var left = 0;

            if (l1 == null) {
                if (carry == 0) {
                    next.val = l2.val;
                    next.next = l2.next;
                    break;
                }
            } else {
                left = l1.val;
            }
            var right = 0;
            if (l2 == null) {
                if (carry == 0) {
                    next.val = l1.val;
                    next.next = l1.next;
                    break;
                }
            } else {
                right = l2.val;
            }
            var s = left + right + carry;

            if (node == null) {
                node = new ListNode(s % 10, null);
                carry = s / 10;
                l1 = l1 == null ? null : l1.next;
                l2 = l2 == null ? null : l2.next;
                if (l1 == null && l2 == null && carry == 0) {
                    break;
                }
                node.next = new ListNode(0, null);
                next = node.next;
                continue;
            }
            if (next != null) {
                next.val = s % 10;
            }
            carry = s / 10;
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
            if (l1 == null && l2 == null && carry == 0) {
                break;
            }
            next.next = new ListNode(0, null);
            next = next.next;

        }

        return node;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        var list = new ArrayList<List<Integer>>();
        if (nums.length < 3) {
            return list;
        }
        FastSort sort = new FastSort();
        sort.quickSort(nums, 0, nums.length - 1);
        log.info(Arrays.toString(nums));

        for (int index = 0; index < nums.length - 2; index++) {
            if (nums[index] == nums[index + 1]) {
                continue;
            }
            int left = index + 1;
            int right = nums.length - 1;
            int sum = nums[index] + nums[left] + nums[right];
            while (left < right && sum != 0) {
                if (sum > 0) {
                    right--;
                    sum = nums[index] + nums[left] + nums[right];
                    continue;
                }
                if (sum < 0) {
                    left++;
                    sum = nums[index] + nums[left] + nums[right];
                    continue;
                }
            }
            if (sum == 0) {
                var l = new ArrayList<Integer>();
                l.add(nums[index]);
                l.add(nums[left]);
                l.add(nums[right]);
                list.add(l);
            }

        }
        return list;
    }
}
