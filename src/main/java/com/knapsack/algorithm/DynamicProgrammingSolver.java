package com.knapsack.algorithm;

import com.knapsack.entity.Item;
import com.knapsack.entity.ItemSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 动态规划求解D{0-1}KP问题
 * @author 开发者
 * @date 2026-03-16
 */
public class DynamicProgrammingSolver {
    // 项集列表
    private List<ItemSet> mItemSetList;
    // 背包最大载重
    private int mMaxWeight;
    // 最优解价值
    private int mOptimalValue;
    // 最优解选择的物品（项集ID+物品编号）
    private List<String> mOptimalSelection;
    // 求解耗时（毫秒）
    private long mSolveTime;

    public DynamicProgrammingSolver(List<ItemSet> itemSetList, int maxWeight) {
        this.mItemSetList = itemSetList;
        this.mMaxWeight = maxWeight;
    }

    /**
     * 动态规划求解最优解
     */
    public void solve() {
        long startTime = System.currentTimeMillis();
        
        // 动态规划数组：dp[i][j]表示前i个项集，背包容量j时的最大价值
        int itemSetCount = mItemSetList.size();
        int[][] dp = new int[itemSetCount + 1][mMaxWeight + 1];

        // 初始化dp[0][j] = 0（无项集时价值为0）
        for (int j = 0; j <= mMaxWeight; j++) {
            dp[0][j] = 0;
        }

        // 遍历每个项集
        for (int i = 1; i <= itemSetCount; i++) {
            ItemSet currentSet = mItemSetList.get(i - 1);
            // 遍历所有可能的背包容量
            for (int j = 0; j <= mMaxWeight; j++) {
                // 不选当前项集的情况
                dp[i][j] = dp[i - 1][j];
                
                // 尝试选择当前项集的3个物品（满足重量限制）
                Item[] items = {currentSet.getmItem1(), currentSet.getmItem2(), currentSet.getmItem3()};
                for (int k = 0; k < 3; k++) {
                    Item item = items[k];
                    if (item.getmWeight() <= j) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - item.getmWeight()] + item.getmValue());
                    }
                }
            }
        }

        // 回溯获取最优解选择的物品
        mOptimalValue = dp[itemSetCount][mMaxWeight];
        mOptimalSelection = backtrackSelection(dp, itemSetCount, mMaxWeight);
        
        mSolveTime = System.currentTimeMillis() - startTime;
    }

    /**
     * 回溯获取最优解的物品选择
     */
    private List<String> backtrackSelection(int[][] dp, int i, int j) {
        List<String> selection = new ArrayList<>();
        while (i > 0 && j > 0) {
            ItemSet currentSet = mItemSetList.get(i - 1);
            Item[] items = {currentSet.getmItem1(), currentSet.getmItem2(), currentSet.getmItem3()};
            boolean isSelected = false;

            // 检查是否选择了当前项集的某个物品
            for (int k = 0; k < 3; k++) {
                Item item = items[k];
                if (item.getmWeight() <= j 
                        && dp[i][j] == dp[i - 1][j - item.getmWeight()] + item.getmValue()) {
                    selection.add("项集" + currentSet.getmId() + "-物品" + (k + 1) 
                            + "（重量：" + item.getmWeight() + "，价值：" + item.getmValue() + "）");
                    j -= item.getmWeight();
                    isSelected = true;
                    break;
                }
            }
            if (!isSelected) {
                i--;
            } else {
                i--;
            }
        }
        Collections.reverse(selection);
        return selection;
    }

    // Getter方法
    public int getmOptimalValue() {
        return mOptimalValue;
    }

    public List<String> getmOptimalSelection() {
        return mOptimalSelection;
    }

    public long getmSolveTime() {
        return mSolveTime;
    }
}
