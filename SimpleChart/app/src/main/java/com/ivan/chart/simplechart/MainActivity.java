package com.ivan.chart.simplechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ivan.chart.simplechart.chart.Chart;
import com.ivan.chart.simplechart.chart.StackedColumnChart;
import com.ivan.chart.simplechart.chart.provider.ColumnChartAdapter;
import com.ivan.chart.simplechart.chart.theme.ChartTheme;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    StackedColumnChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = (StackedColumnChart) findViewById(R.id.chart);

        final ArrayList<Float> y1 = new ArrayList<>();
        final ArrayList<Float> y2 = new ArrayList<>();
        final ArrayList<String> x = new ArrayList<>();
        float j=0;
        for (float i=21;i>=0;i--) {
            x.add(String.valueOf(j));
            j++;
            y1.add(i);
            y2.add(i*2);
        }

        final ArrayList<String> legends = new ArrayList<>();
        final ArrayList<Integer> colors = new ArrayList<>();
        legends.add("1 hello, Charts!");
        legends.add("2 hello, Charts!");
        colors.add(R.color.default_main_color);
        colors.add(R.color.chart_deep_blue);

        chart.setYAxisValuesFormatter(new Chart.YAxisValueFormatter() {
            @Override
            public String YvaluesString(float v) {
                return (int)v + "-";
            }
        });
        chart.setTheme(ChartTheme.THEME_DARK);
        chart.setAdapter(new ColumnChartAdapter() {
            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public ArrayList<Float> getColumnData(int index) {
                if (index == 0) return y1;
                else            return y2;
            }

            @Override
            public int getColumnColor(int index) {
                return colors.get(index);
            }

            @Override
            public int getXLabelsCount() {
                return x.size();
            }

            @Override
            public String getXLabel(int position) {
                return x.get(position);
            }

            @Override
            public int getLegendCount() {
                return 2;
            }

            @Override
            public String getLegend(int position) {
                return legends.get(position);
            }

            @Override
            public int getColorId(int position) {
                return colors.get(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        chart.animateY(3000, 2000);
    }
}
