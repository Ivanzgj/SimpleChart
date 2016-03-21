package com.ivan.chart.simplechart.chart.provider;

import com.ivan.chart.simplechart.chart.Chart;

import java.util.ArrayList;

/**
 * 图表内容的提供者
 * Created by Ivan on 16/3/16.
 */
public abstract class ChartAdapter {

    public Chart chart;

    /**
     * x轴标签的个数
     * @return x轴标签的个数
     */
    public abstract int getXLabelsCount();

    /**
     * x轴指定位置的内容
     * @param position 指定位置
     * @return x轴指定位置的内容
     */
    public abstract String getXLabel(int position);

    /**
     * 获得图例个数
     * @return 图例个数
     */
    public abstract int getLegendCount();

    /**
     * 获得图例
     * @return 图例
     */
    public abstract String getLegend(int position);

    /**
     * 获得图例的颜色
     * @return 图例的颜色
     */
    public abstract int getColorId(int position);

    public void notifyDataSetChanged() {
        if (chart != null) {
            chart.clearAll();
            chart.postInvalidate();
        }
    }

}
