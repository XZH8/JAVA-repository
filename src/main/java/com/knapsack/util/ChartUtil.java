package com.knapsack.util;

import com.knapsack.entity.Item;
import com.knapsack.entity.ItemSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;

/**
 * 图表生成工具类
 * @author 开发者
 * @date 2026-03-16
 */
public class ChartUtil {
    /**
     * 绘制重量-价值散点图
     */
    public static void drawScatterChart(List<ItemSet> itemSetList) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("物品数据");
        
        // 遍历所有项集的物品，添加到散点图
        for (ItemSet itemSet : itemSetList) {
            series.add(itemSet.getmItem1().getmWeight(), itemSet.getmItem1().getmValue());
            series.add(itemSet.getmItem2().getmWeight(), itemSet.getmItem2().getmValue());
            series.add(itemSet.getmItem3().getmWeight(), itemSet.getmItem3().getmValue());
        }
        dataset.addSeries(series);
        
        // 创建图表
        JFreeChart chart = ChartFactory.createScatterPlot(
                "D{0-1}KP物品重量-价值散点图",
                "重量",
                "价值",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        // 展示图表
        ChartFrame frame = new ChartFrame("散点图", chart);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
