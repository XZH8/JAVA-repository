package com.knapsack.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作工具类
 * @author 开发者
 * @date 2026-03-16
 */
public class FileUtil {
    /**
     * 导出文本到TXT文件
     */
    public static void exportToTxt(String content, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("TXT导出失败：" + e.getMessage());
        }
    }
}