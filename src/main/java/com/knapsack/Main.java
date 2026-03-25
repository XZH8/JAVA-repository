package com.knapsack;

import com.knapsack.ui.MainUI;

import javax.swing.SwingUtilities;

/**
 * 程序入口类
 * @author 开发者
 * @date 2026-03-16
 */
public class Main {
    public static void main(String[] args) {
        // Swing界面需在EDT线程中运行
        SwingUtilities.invokeLater(MainUI::new);
    }
}
