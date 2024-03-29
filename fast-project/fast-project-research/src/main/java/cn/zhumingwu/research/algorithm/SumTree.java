package cn.zhumingwu.research.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
 *  Breadth-First Search
 */
@Slf4j
public class SumTree {

    public void demo(String[] args) {

        TreeNode root = new TreeNode(5);
        TreeNode node11 = new TreeNode(11);
        node11.left = new TreeNode(7);
        node11.right = new TreeNode(2);
        TreeNode node4 = new TreeNode(4);
        node4.left = node11;

        TreeNode node8 = new TreeNode(8);
        node8.left = new TreeNode(13);
        node8.right = new TreeNode(4);
        node8.right.left = new TreeNode(5);
        node8.right.right = new TreeNode(1);
        root.left = node4;
        root.right = node8;

        String result = hasPathSum(root, 22);

        log.info("result: " + result);
    }

    /*
     * [ [5,4,11,2], [5,8,4,5] ]
     */
    List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();

        return list;
    }

    public String hasPathSum(TreeNode root, int sum) {
        if (root == null) {
            return "";
        }
        sum -= root.val;
        if ((root.left == null) && (root.right == null) && (sum == 0)) {
            return String.valueOf(root.val);
        }
        if (sum < 0) {
            return "";
        }
        String result1 = hasPathSum(root.left, sum);

        String result2 = hasPathSum(root.right, sum);
        String result = "";
        if (!Strings.isNullOrEmpty(result1) && result1.length() > 0) {
            result += root.val + "," + result1 + "；";
        }

        if (!Strings.isNullOrEmpty(result2) && result2.length() > 0) {
            result += root.val + "," + result2 + "；";
        }
        return result;
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
