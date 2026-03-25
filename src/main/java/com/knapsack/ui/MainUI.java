package com.knapsack.ui;

import com.knapsack.algorithm.DynamicProgrammingSolver;
import com.knapsack.entity.ItemSet;
import com.knapsack.util.ChartUtil;
import com.knapsack.util.DataParserUtil;
import com.knapsack.util.ExcelUtil;
import com.knapsack.util.FileUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * D{0-1}KP问题求解主界面（修复Excel导出版）
 * @author 开发者
 * @date 2026-03-22
 */
public class MainUI extends JFrame {
    // 项集列表
    private List<ItemSet> mItemSetList = new ArrayList<>();
    // 背包最大载重输入框
    private JTextField mMaxWeightTextField;
    // 结果展示文本域
    private JTextArea mResultTextArea;
    // 数据文件路径
    private String mDataFilePath;

    public MainUI() {
        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        // 窗口基本设置
        setTitle("D{0-1}KP动态规划求解系统");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 顶部面板：功能按钮
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // 导入数据按钮
        JButton importBtn = new JButton("导入D{0-1}KP数据集");
        importBtn.addActionListener(this::importDataAction);
        topPanel.add(importBtn);

        // 输入最大载重
        topPanel.add(new JLabel("背包最大载重："));
        mMaxWeightTextField = new JTextField(10);
        topPanel.add(mMaxWeightTextField);

        // 排序按钮（按第三项价值重量比非递增）
        JButton sortBtn = new JButton("项集排序（第三项价值重量比）");
        sortBtn.addActionListener(this::sortItemSetAction);
        topPanel.add(sortBtn);

        // 绘制散点图按钮
        JButton chartBtn = new JButton("绘制散点图");
        chartBtn.addActionListener(this::drawChartAction);
        topPanel.add(chartBtn);

        // 求解最优解按钮
        JButton solveBtn = new JButton("求解最优解");
        solveBtn.addActionListener(this::solveAction);
        topPanel.add(solveBtn);

        // 导出结果按钮
        JButton exportBtn = new JButton("导出结果（TXT/Excel）");
        exportBtn.addActionListener(this::exportResultAction);
        topPanel.add(exportBtn);

        add(topPanel, BorderLayout.NORTH);

        // 中间面板：结果展示
        mResultTextArea = new JTextArea();
        mResultTextArea.setEditable(false);
        mResultTextArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(mResultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * 导入数据集文件
     */
    private void importDataAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件(*.txt)", "txt"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            mDataFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            // 解析数据集
            mItemSetList = DataParserUtil.parseItemSet(mDataFilePath);
            mResultTextArea.append("✅ 成功导入数据集：" + mDataFilePath + "\n");
            mResultTextArea.append("📊 导入项集数量：" + mItemSetList.size() + "\n");
        }
    }

    /**
     * 项集排序（按第三项价值重量比非递增）
     */
    private void sortItemSetAction(ActionEvent e) {
        if (mItemSetList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先导入数据集！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 1. 保存排序前的原始数据（记录项集ID和第三项的价值重量比）
        mResultTextArea.append("\n===== 排序前（项集ID → 第三项价值重量比）=====\n");
        for (ItemSet itemSet : mItemSetList) {
            double ratio = itemSet.getThirdItemRatio();
            // 保留两位小数，更易读
            mResultTextArea.append("项集" + itemSet.getmId() + "：" + String.format("%.2f", ratio) + "（价值" 
                    + itemSet.getmItem3().getmValue() + "/重量" + itemSet.getmItem3().getmWeight() + "）\n");
        }
        
        // 2. 按第三项价值重量比非递增排序
        Collections.sort(mItemSetList, Comparator.comparing(ItemSet::getThirdItemRatio).reversed());
        
        // 3. 输出排序后的详细结果
        mResultTextArea.append("\n===== 排序后（按第三项价值重量比非递增）=====\n");
        int rank = 1;
        for (ItemSet itemSet : mItemSetList) {
            double ratio = itemSet.getThirdItemRatio();
            mResultTextArea.append("第" + rank + "位：项集" + itemSet.getmId() + " → 价值重量比：" 
                    + String.format("%.2f", ratio) + "（价值" + itemSet.getmItem3().getmValue() 
                    + "/重量" + itemSet.getmItem3().getmWeight() + "）\n");
            rank++;
        }
        mResultTextArea.append("=============================================\n");
    }

    /**
     * 绘制散点图
     */
    private void drawChartAction(ActionEvent e) {
        if (mItemSetList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先导入数据集！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // 生成散点图并展示
        ChartUtil.drawScatterChart(mItemSetList);
        mResultTextArea.append("✅ 散点图已生成\n");
    }

    /**
     * 求解最优解
     */
    private void solveAction(ActionEvent e) {
        if (mItemSetList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先导入数据集！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String maxWeightStr = mMaxWeightTextField.getText().trim();
        if (maxWeightStr.isEmpty() || !maxWeightStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "请输入有效的最大载重（正整数）！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maxWeight = Integer.parseInt(maxWeightStr);
        
        // 执行动态规划求解
        DynamicProgrammingSolver solver = new DynamicProgrammingSolver(mItemSetList, maxWeight);
        solver.solve();
        
        // 展示结果
        mResultTextArea.append("\n===== 最优解结果 =====\n");
        mResultTextArea.append("最大价值：" + solver.getmOptimalValue() + "\n");
        mResultTextArea.append("求解耗时：" + solver.getmSolveTime() + "ms\n");
        mResultTextArea.append("选择的物品：\n");
        for (String selection : solver.getmOptimalSelection()) {
            mResultTextArea.append("  - " + selection + "\n");
        }
        mResultTextArea.append("======================\n");
    }

    /**
     * 导出结果（TXT/Excel）- 修复版，含完整校验和异常处理
     */
    private void exportResultAction(ActionEvent e) {
        // 1. 前置校验
        if (mItemSetList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ 请先导入数据集！", "导出失败", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (mResultTextArea.getText().isEmpty() || !mResultTextArea.getText().contains("最优解结果")) {
            JOptionPane.showMessageDialog(this, "❌ 请先求解最优解！", "导出失败", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. 选择导出格式
        String[] exportOptions = {"📝 导出为TXT文件", "📊 导出为Excel文件"};
        int formatChoice = JOptionPane.showOptionDialog(
                this,
                "请选择导出文件格式",
                "选择导出格式",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                exportOptions,
                exportOptions[0]
        );

        // 3. 选择保存路径
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存路径");
        // 设置默认文件名（带时间戳，避免重复）
        fileChooser.setSelectedFile(new File("D01-KP-最优解结果_" + System.currentTimeMillis()));

        // 4. 导出TXT
        if (formatChoice == 0) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
            int saveResult = fileChooser.showSaveDialog(this);
            if (saveResult == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                // 自动补全.txt后缀
                if (!savePath.endsWith(".txt")) {
                    savePath += ".txt";
                }
                try {
                    FileUtil.exportToTxt(mResultTextArea.getText(), savePath);
                    JOptionPane.showMessageDialog(this,
                            "✅ TXT文件导出成功！\n保存路径：" + savePath,
                            "导出成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "❌ TXT导出失败：" + ex.getMessage() + "\n💡 建议：检查保存路径权限",
                            "导出失败", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // 控制台打印异常，方便排查
                }
            }
        }
        // 5. 导出Excel
        else if (formatChoice == 1) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
            int saveResult = fileChooser.showSaveDialog(this);
            if (saveResult == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                // 自动补全.xlsx后缀
                if (!savePath.endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }
                try {
                    // 确保传递有效结果文本
                    String resultText = mResultTextArea.getText().trim();
                    if (resultText.isEmpty()) {
                        resultText = "已导入" + mItemSetList.size() + "个项集，最优解结果为空";
                    }
                    // 调用修复后的ExcelUtil
                    ExcelUtil.exportToExcel(mItemSetList, resultText, savePath);
                    JOptionPane.showMessageDialog(this,
                            "✅ Excel文件导出成功！\n保存路径：" + savePath + "\n" +
                            "📋 Sheet说明：\n1. 项集原始数据（所有导入项集）\n2. 最优解计算结果",
                            "导出成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "❌ Excel导出失败：" + ex.getMessage() + "\n💡 建议：1. 关闭已打开的同名Excel 2. 检查路径权限",
                            "导出失败", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // 控制台打印异常，方便排查
                }
            }
        }
    }
}