package com.knapsack.entity;

/**
 * 物品实体类：封装单个物品的重量、价值
 * @author 开发者
 * @date 2026-03-16
 */
public class Item {
    // 物品重量
    private int mWeight;
    // 物品价值
    private int mValue;

    public Item(int weight, int value) {
        this.mWeight = weight;
        this.mValue = value;
    }

    // Getter方法
    public int getmWeight() {
        return mWeight;
    }

    public int getmValue() {
        return mValue;
    }

    // 计算价值重量比
    public double getValueWeightRatio() {
        return mWeight == 0 ? 0 : (double) mValue / mWeight;
    }
}