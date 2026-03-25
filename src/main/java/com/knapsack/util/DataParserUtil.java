package com.knapsack.util;

import com.knapsack.entity.Item;
import com.knapsack.entity.ItemSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据解析工具类：解析D{0-1}KP数据集TXT文件
 * @author 开发者
 * @date 2026-03-16
 */
public class DataParserUtil {
    /**
     * 解析数据集文件
     * 数据集格式（每行）：项集ID 物品1重量 物品1价值 物品2重量 物品2价值 物品3重量 物品3价值
     * 示例：1 10 20 15 30 20 45
     */
    public static List<ItemSet> parseItemSet(String path) {
        List<ItemSet> itemSetList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 去除空格、空行
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // 按空格分割
                String[] parts = line.split("\\s+");
                if (parts.length != 7) {
                    System.err.println("数据格式错误，跳过该行：" + line);
                    continue;
                }
                // 解析数据
                int id = Integer.parseInt(parts[0]);
                int w1 = Integer.parseInt(parts[1]);
                int v1 = Integer.parseInt(parts[2]);
                int w2 = Integer.parseInt(parts[3]);
                int v2 = Integer.parseInt(parts[4]);
                int w3 = Integer.parseInt(parts[5]);
                int v3 = Integer.parseInt(parts[6]);
                // 创建物品和项集
                Item item1 = new Item(w1, v1);
                Item item2 = new Item(w2, v2);
                Item item3 = new Item(w3, v3);
                ItemSet itemSet = new ItemSet(id, item1, item2, item3);
                itemSetList.add(itemSet);
            }
        } catch (IOException e) {
            throw new RuntimeException("解析数据集失败：" + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("数据格式错误（非数字）：" + e.getMessage(), e);
        }
        return itemSetList;
    }
}
