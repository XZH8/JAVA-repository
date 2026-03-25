package com.knapsack.entity;

/**
 * 项集实体类：每个项集包含3个物品，至多选择1个
 * @author 开发者
 * @date 2026-03-16
 */
public class ItemSet {
    // 项集编号
    private int mId;
    // 3个物品
    private Item mItem1;
    private Item mItem2;
    private Item mItem3;

    public ItemSet(int id, Item item1, Item item2, Item item3) {
        this.mId = id;
        this.mItem1 = item1;
        this.mItem2 = item2;
        this.mItem3 = item3;
    }

    // Getter方法
    public int getmId() {
        return mId;
    }

    public Item getmItem1() {
        return mItem1;
    }

    public Item getmItem2() {
        return mItem2;
    }

    public Item getmItem3() {
        return mItem3;
    }

    // 获取第三项的价值重量比（用于排序）
    public double getThirdItemRatio() {
        return mItem3.getValueWeightRatio();
    }
}
