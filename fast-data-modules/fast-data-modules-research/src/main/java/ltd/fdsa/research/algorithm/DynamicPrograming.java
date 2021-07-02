package ltd.fdsa.research.algorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicPrograming {

    static int max(int a, int b) {
        if (a >= b) {
            return a;
        }
        return b;
    }

    public void demo(String[] args) {

        /*
         * 假设有5个物体和一个背包。物体的重量分别是2,2,6,5,4,即w[]={0,2,2,6,5,4},价值分别是6,3,5,4,6,即v[]={0,6,3,
         * 5,4,6}。背包承重为10。问怎么选择,能实现背包所背物体价值的最大化。
         */
        // int[] w = new int[] { 2, 2, 6, 5, 4 };
        // int[] v = new int[] { 6, 3, 5, 4, 6 };
        // int cap = 10;
        int[] w = new int[]{5, 5, 10, 20, 5};
        int[] v = new int[]{7, 6, 9, 9, 8};
        int cap = 20;
        int[][] dp = dplan(w, v, cap);
        for (int j = 0; j < dp.length; j++) {
            for (int i = 0; i < dp[j].length; i++) {
                System.out.print(String.format("%s \t", dp[j][i]));
            }
            log.info("");
        }
    }

    int[][] dplan(int[] w, int[] v, int cap) {
        // 动态规划
        int[][] dp = new int[w.length + 1][cap + 1];
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                int beforeValue = dp[i - 1][j];
                int currentWeight = w[i - 1];
                if (currentWeight <= j) {
                    int currentValue = v[i - 1] + dp[i - 1][j - currentWeight];
                    dp[i][j] = Math.max(currentValue, beforeValue);
                } else {
                    dp[i][j] = beforeValue;
                }
            }
        }
        return dp;
    }
}
