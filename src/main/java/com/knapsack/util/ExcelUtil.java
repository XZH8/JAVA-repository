package com.knapsack.util;

import com.knapsack.entity.Item;
import com.knapsack.entity.ItemSet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Excel导出工具类（修复版，确保数据写入）
 * @author 开发者
 * @date 2026-03-22
 */
public class ExcelUtil {
    /**
     * 导出项集数据和最优解结果到Excel
     */
    public static void exportToExcel(List<ItemSet> itemSetList, String resultText, String savePath) {
        // 1. 创建Workbook
        Workbook workbook = new XSSFWorkbook();
        
        // 2. Sheet1：项集原始数据（核心数据，必写）
        Sheet sheet1 = workbook.createSheet("项集原始数据");
        // 表头样式（加粗）
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        // 表头行
        Row headerRow = sheet1.createRow(0);
        String[] headers = {"项集ID", "物品1重量", "物品1价值", "物品2重量", "物品2价值", "物品3重量", "物品3价值", "第三项价值重量比"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充项集数据（逐行写入）
        int rowNum = 1;
        for (ItemSet itemSet : itemSetList) {
            Row row = sheet1.createRow(rowNum++);
            // 项集ID
            row.createCell(0).setCellValue(itemSet.getmId());
            // 物品1
            Item item1 = itemSet.getmItem1();
            row.createCell(1).setCellValue(item1.getmWeight());
            row.createCell(2).setCellValue(item1.getmValue());
            // 物品2
            Item item2 = itemSet.getmItem2();
            row.createCell(3).setCellValue(item2.getmWeight());
            row.createCell(4).setCellValue(item2.getmValue());
            // 物品3
            Item item3 = itemSet.getmItem3();
            row.createCell(5).setCellValue(item3.getmWeight());
            row.createCell(6).setCellValue(item3.getmValue());
            // 第三项价值重量比（保留两位小数）
            double ratio = item3.getValueWeightRatio();
            row.createCell(7).setCellValue(String.format("%.2f", ratio));
        }
        
        // 3. Sheet2：最优解计算结果（处理空值）
        Sheet sheet2 = workbook.createSheet("最优解计算结果");
        String finalResultText = (resultText == null || resultText.trim().isEmpty())
                ? "暂无最优解结果（请先执行求解操作）"
                : resultText;
        // 逐行写入结果
        String[] resultLines = finalResultText.split("\n");
        for (int i = 0; i < resultLines.length; i++) {
            Row row = sheet2.createRow(i);
            Cell cell = row.createCell(0);
            String line = resultLines[i].trim();
            cell.setCellValue(line.isEmpty() ? "——" : line);
        }
        
        // 4. 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet1.autoSizeColumn(i);
        }
        sheet2.autoSizeColumn(0);
        
        // 5. 写入文件并关闭资源
        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            workbook.write(fos);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        }
    }
}