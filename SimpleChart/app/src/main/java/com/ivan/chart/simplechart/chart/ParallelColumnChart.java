package com.ivan.chart.simplechart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import com.ivan.chart.simplechart.Compat;
import com.ivan.chart.simplechart.chart.provider.ColumnChartAdapter;
import java.util.ArrayList;

/**
 * 平行柱状图
 * @author Ivan
 */
public class ParallelColumnChart extends StackedColumnChart {

    /**
     * 柱状图柱距
     */
    private static final float columnSpace = 0.1f;

    private ColumnChartAdapter mAdapter = new ColumnChartAdapter() {

        @Override
        public int getXLabelsCount() {
            return 0;
        }

        @Override
        public String getXLabel(int position) {
            return null;
        }

        @Override
        public int getLegendCount() {
            return 0;
        }

        @Override
        public String getLegend(int position) {
            return null;
        }

        @Override
        public int getColorId(int position) {
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 0;
        }

        @Override
        public ArrayList<Float> getColumnData(int index) {
            return null;
        }

        @Override
        public int getColumnColor(int index) {
            return 0;
        }
    };

    private Context context;

    public ParallelColumnChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setAdapter(mAdapter);
    }

    public ParallelColumnChart(Context context) {
        super(context);
        this.context = context;
        setAdapter(mAdapter);
    }

    @Override
    public void onDrawData(Canvas canvas, Paint paint) {

        float animateRate = getAnimateRate();
        int animateType = getAnimateType();

        int xCount = mAdapter.getColumnData(0).size();
        int columnCount = mAdapter.getColumnCount();
        float columnWidth = (1-columnSpace*2)/columnCount;

        paint.setStyle(Paint.Style.FILL);

        if (animateType == ANIMATE_X_FLAG || animateType == ANIMATE_NON_FLAG)	animateRate = 1.f;	// 柱状图没有x轴方向的动画

        if (animateRate > 1) {
            animateRate = 1.f;
        }
        for (int k=0;k<xCount;k++){
            // 画矩形条
            for (int i=0;i<columnCount;i++){
                ArrayList<Float> data = mAdapter.getColumnData(i);
                paint.setColor(Compat.getColor(context, mAdapter.getColumnColor(i)));
                float top = data.get(k);
                drawRect(canvas, paint, k + columnSpace + columnWidth * i, top * animateRate, columnWidth);
            }
        }
    }

    /**
     * 设置数据适配器
     * @param adapter 数据适配器
     */
    public void setAdapter(@NonNull ColumnChartAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        setColumnDataSet();
    }

    private void setColumnDataSet(){

        if (mAdapter.getColumnCount() == 0) {
            return;
        }

        int maxY = 0;

        for (int i = 0; i < mAdapter.getColumnCount(); i++) {
            for (int j = 0;j < mAdapter.getColumnData(i).size(); j++) {
                float value = mAdapter.getColumnData(i).get(j);
                if (maxY < value) {
                    maxY = (int) Math.ceil(value);
                }
            }
        }

        int yStep = getYStep();
        if (selfAdaptive) {
            setYStep(10);
            yStep = 10;
            if (maxY <= 100) maxY = 100;
            else if (maxY > 100 && maxY <= 1000) maxY = (int) (Math.ceil((float)maxY/100.f)*100);
            else if (maxY > 1000 && maxY < 10000) maxY = (int) (Math.ceil((float)maxY/1000.f)*1000);
            ArrayList<Float> yLabels = new ArrayList<>();
            for (int i=0;i<=yStep;i++){
                yLabels.add((float) (i*maxY/yStep));
            }
            setYlabels(yLabels);
            return;
        }

        if (maxY % yStep != 0)		maxY = (int) (Math.ceil(maxY/yStep))*yStep;
        ArrayList<Float> yLabels = new ArrayList<>();
        for (int i=0;i<=yStep;i++){
            yLabels.add((float) (i*maxY/yStep));
        }
        setYlabels(yLabels);
    }

}
