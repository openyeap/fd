package cn.zhumingwu.research.base;

import cn.zhumingwu.research.algorithm.BitMap;
import cn.zhumingwu.research.algorithm.Solution;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;


/**
 * @author zhumingwu
 * @ClassName:
 * @description:
 * @since 2020-10-28
 */
@RunWith(SpringRunner.class)
@Slf4j
public class TestJavaBase {

    //    @Test
//    public void TestJavaClassLayout() {
//        testObject obj = new testObject();
//        log.info("test================================");
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//        log.info("test================================");
//    }
//
//    class testObject {
//        private String name;
//        private int age;
//
//    }

    @Test
    public void TestmaxProfit() {
        int[] nums = new int[]{1, 2, 3, 4, 5};
        nums = new int[]{7, 1, 5, 3, 6, 4};
        nums = new int[]{7, 6, 4, 3, 1};
        System.out.println(Arrays.toString(nums));
        Solution solution = new Solution();
        System.out.println(solution.maxProfit(nums));
        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void TestremoveDuplicates() {
        int[] nums = new int[]{1, 2, 3, 4, 5};
        nums = new int[]{7, 1, 5, 3, 6, 4};

        System.out.println(Arrays.toString(nums));
        Solution solution = new Solution();
        System.out.println(solution.removeDuplicates(nums));
        System.out.println(Arrays.toString(nums));
    }


    @Test
    public void Test_rotate() {
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        int k = 3;
        System.out.println(Arrays.toString(nums));
        Solution solution = new Solution();
//        solution.rotate(nums, k);
        solution.rotate2(nums, k);
        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void Test_ThreeSum() {
        int[] nums;

        Solution solution = new Solution();

        nums = new int[]{-1, 0, 1, 2, -1, -4};
        System.out.println(Arrays.toString(nums));
        for (var item : solution.threeSum(nums)) {
            System.out.println(Arrays.toString(item.toArray()));
        }
        nums = new int[]{0, 0, 0, 0};
        System.out.println(Arrays.toString(nums));
        for (var item : solution.threeSum(nums)) {
            System.out.println(Arrays.toString(item.toArray()));
        }

    }

    @Test
    public void Test_removeNthFromEnd() {
        Solution solution = new Solution();
//        Solution.ListNode node = new Solution.ListNode(1, new Solution.ListNode(2, new Solution.ListNode(3, new Solution.ListNode(4, new Solution.ListNode(5, null)))));
//        solution.removeNthFromEnd(node, 2);
        Solution.ListNode node = new Solution.ListNode(1, new Solution.ListNode(2, null));
        node = solution.removeNthFromEnd(node, 2);
        var next = node.next;
        System.out.println((node.val));
        while (next != null) {
            System.out.println((next.val));
            next = next.next;
        }
    }

    @Test
    public void Test_twoSum() {
        var nums = new int[]{2, 7, 11, 15};
        int target = 9;
        Solution solution = new Solution();
        nums = solution.twoSum(nums, target);
        System.out.println(Arrays.toString(nums));

        nums = new int[]{3, 7, 5, 4, 5};
        nums = solution.twoSum(nums, target);
        System.out.println(Arrays.toString(nums));

        nums = new int[]{3,3};
        target = 6;
        nums = solution.twoSum(nums, target);
        System.out.println(Arrays.toString(nums));
    }

    @Test
    public void Test_addTwoNumbers() {
        Solution solution = new Solution();
        Solution.ListNode node1;
        Solution.ListNode node2;
        Solution.ListNode node3;


        node1 = new Solution.ListNode(0, null);
        node2 = new Solution.ListNode(7, new Solution.ListNode(3, null));
        System.out.println(node1.toString() + "+" + node2.toString());
        System.out.println("============");
        node3 = solution.addTwoNumbers(node1, node2);
        System.out.println((node3.toString()));


        node1 = new Solution.ListNode(0, new Solution.ListNode(2, null));
        node2 = new Solution.ListNode(0, null);
        System.out.println(node1.toString() + "+" + node2.toString());
        System.out.println("============");
        node3 = solution.addTwoNumbers(node1, node2);
        System.out.println((node3.toString()));


        node1 = new Solution.ListNode(7, null);
        node2 = new Solution.ListNode(3, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, new Solution.ListNode(9, null))))))))));
        System.out.println(node1.toString() + "+" + node2.toString());
        System.out.println("============");
        node3 = solution.addTwoNumbers(node1, node2);
        System.out.println((node3.toString()));

        node1 = new Solution.ListNode(1, new Solution.ListNode(8, null));
        node2 = new Solution.ListNode(0, null);
        System.out.println(node1.toString() + "+" + node2.toString());
        System.out.println("============");
        node3 = solution.addTwoNumbers(node1, node2);
        System.out.println((node3.toString()));
    }

    @Test
    public void Test_containsDuplicate() {
        int[] nums;
        var startTime = System.nanoTime();
        Solution solution = new Solution();
        BitMap bitMap = new BitMap();

        nums = new int[]{0};
        startTime = System.nanoTime();
        Assert.assertTrue("[0]", !solution.containsDuplicate(nums));
        log.info("solution {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));

        startTime = System.nanoTime();
        Assert.assertTrue("[0]", !bitMap.containsDuplicate(nums));
        log.info("bitMap {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));


        nums = new int[]{0, 0};
        startTime = System.nanoTime();
        Assert.assertTrue("[0]", solution.containsDuplicate(nums));
        log.info("solution {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));

        startTime = System.nanoTime();
        Assert.assertTrue("[0]", bitMap.containsDuplicate(nums));
        log.info("bitMap {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));

//[1,2,3,1]
        nums = new int[]{1, 2, 3, 1};
        startTime = System.nanoTime();
        Assert.assertTrue("[0]", solution.containsDuplicate(nums));
        log.info("solution {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));

        startTime = System.nanoTime();
        Assert.assertTrue("[0]", bitMap.containsDuplicate(nums));
        log.info("bitMap {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));
//[-1200000005,-1200000005]


        nums = new int[]{1200000005, -1200000005};
        startTime = System.nanoTime();
        Assert.assertTrue("[0]", solution.containsDuplicate(nums));
        log.info("solution {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));

        startTime = System.nanoTime();
        Assert.assertTrue("[0]", bitMap.containsDuplicate(nums));
        log.info("bitMap {}：{} ns", Arrays.toString(nums), (System.nanoTime() - startTime));
    }
}
