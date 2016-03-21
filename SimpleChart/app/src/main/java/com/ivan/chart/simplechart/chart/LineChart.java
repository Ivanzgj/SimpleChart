package com.ivan.chart.simplechart.chart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import com.ivan.chart.simplechart.AppContext;
import com.ivan.chart.simplechart.Compat;
import com.ivan.chart.simplechart.chart.provider.LineChartAdapter;

import java.util.ArrayList;

/**
 * 折线图
 */
public class LineChart extends Chart {

	/**
	 * 图表线宽
	 */
	private final int lineWidth = AppContext.dp2px(2.5f);
	/**
	 * 折线上的原点半径
	 */
	private final int circleRadius = AppContext.dp2px(2.5f);

	private Context context;

	private LineChartAdapter mAdapter = new LineChartAdapter() {
		@Override
		public int getLineCount() {
			return 0;
		}

		@Override
		public ArrayList<Float> getLineData(int index) {
			return null;
		}

		@Override
		public int getLineColor(int index) {
			return 0;
		}

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


	};

	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setAdapter(mAdapter);
		setMode(MODE_LINE_CHART);
	}
	
	public LineChart(Context context) {
		super(context);
		this.context = context;
		setAdapter(mAdapter);
		setMode(MODE_LINE_CHART);
	}

	@Override
	public void onDrawData(Canvas canvas, Paint paint) {

		if (mAdapter.getLineCount() == 0)	return;

		int animateType = getAnimateType();

		if (animateType == ANIMATE_NON_FLAG) {

			drawData(canvas, paint);
        }
		else if (animateType == ANIMATE_Y_FALG) {	// y轴动画

			drawYAnimateData(canvas, paint);
		}
		else if (animateType == ANIMATE_X_FLAG) {    // x轴动画

			drawXAnimateData(canvas, paint);
		}
	}

	/**
	 * 设置数据适配器
	 * @param adapter 数据源适配器
	 */
	public void setAdapter(@NonNull LineChartAdapter adapter) {
		mAdapter = adapter;
		super.setAdapter(adapter);
		setLineDataSet();
	}

	/**
	 * 设置折线图数据
	 */
	private void setLineDataSet(){

		if (mAdapter.getLegendCount() == 0) {
			return;
		}

		int maxY = 0;

		for (int i=0;i<mAdapter.getLineCount();i++) {
			for (int j=0;j<mAdapter.getLineData(i).size();j++) {
				float value = mAdapter.getLineData(i).get(j);
				if (maxY < value)	maxY = (int) value;
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

		if (maxY % yStep != 0)		maxY = (maxY/yStep+1)*yStep;
		ArrayList<Float> yLabels = new ArrayList<>();
		for (int i=0;i<=yStep;i++){
			yLabels.add((float) (i*maxY/yStep));
		}
		setYlabels(yLabels);
	}

	/**
	 * 没有动画，绘制数据
	 */
	private void drawData(Canvas canvas, Paint paint) {

		for (int k=0;k<mAdapter.getLineCount();k++) {
			// 画点
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(0);
			paint.setColor(Compat.getColor(context, mAdapter.getLineColor(k)));

			ArrayList<Float> yData = mAdapter.getLineData(k);
			int count = yData.size();
			for (int i=0;i<count;i++){
				drawPoint(canvas, paint, i, yData.get(i), circleRadius);
			}

			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(lineWidth);
			// 画线
			for (int i=0;i<count-1;i++){
				drawLine(canvas, paint, i, yData.get(i), i+1, yData.get(i+1));
			}
		}
	}

	/**
	 * y轴动画，绘制数据
	 */
	private void drawYAnimateData(Canvas canvas, Paint paint) {

		float animateRate = getAnimateRate();

		if (animateRate > 1.f) {
			animateRate = 1.f;
		}
		for (int k=0;k<mAdapter.getLineCount();k++) {
			// 画点
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(0);
			paint.setColor(Compat.getColor(context, mAdapter.getLineColor(k)));

			ArrayList<Float> yData = mAdapter.getLineData(k);
			int count = yData.size();
			for (int i=0;i<count;i++){
				drawPoint(canvas, paint, i, yData.get(i)*animateRate, circleRadius);
			}

			// 画线
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(lineWidth);
			for (int i=0;i<count-1;i++){
				drawLine(canvas, paint, i, yData.get(i) * animateRate, i + 1, yData.get(i+1) * animateRate);
			}
		}
	}

	/**
	 * x轴动画，绘制数据
	 */
	private void drawXAnimateData(Canvas canvas, Paint paint) {

		float animateRate = getAnimateRate();

		for (int k = 0; k < mAdapter.getLineCount(); k++) {

			boolean outOfIndex = false;
			// 画点
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(0);
			paint.setColor(Compat.getColor(context, mAdapter.getLineColor(k)));

			ArrayList<Float> yData = mAdapter.getLineData(k);
			int yCount = yData.size();
			if (animateRate > yCount) {
				animateRate = yCount;
			}
			for (int i = 0; i < Math.floor(animateRate); i++) {
				drawPoint(canvas, paint, i, yData.get(i), circleRadius);
			}

			// 画线
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(lineWidth);
			for (int i=0;i<Math.floor(animateRate)-1;i++) {
				if (i >= yCount-1) {
					outOfIndex = true;
					break;
				}
				drawLine(canvas, paint, i, yData.get(i), i + 1, yData.get(i+1));
			}
			if (outOfIndex) {
				continue;
			}
			int i = (int) Math.floor(animateRate);
			if (i >= yCount) {
				continue;
			}

			float point1 = yData.get(i-1);
			float point2 = yData.get(i);
			float kRate = (point2 - point1) / 1;
			float detaX = (float) (1 * (animateRate - Math.floor(animateRate)));
			float detaY = detaX * kRate;
			float x1 = i - 1;
			float x2 = x1 + detaX;
			float y2 = point1 + detaY;
			drawLine(canvas, paint, x1, point1, x2, y2);
		}
	}

	/**
	 * Y轴动画
	 *
	 * @param delay  延迟，ms单位
	 * @param millis 动画总时间，ms单位
	 */
	@Override
	public void animateY(long delay, final long millis) {

		valueAnimator = ValueAnimator.ofFloat(0.f, 1.f);
		valueAnimator.setDuration(millis);
		valueAnimator.setStartDelay(delay);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				setAnimateRate(animation.getAnimatedFraction());
				postInvalidateChartData();
			}
		});
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				setAnimateType(ANIMATE_Y_FALG);
				setAnimateRate(0.f);
			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				setAnimateType(ANIMATE_NON_FLAG);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				setAnimateType(ANIMATE_Y_FALG);
				setAnimateRate(0.f);
			}
		});
		valueAnimator.start();
	}

	/**
	 * X轴动画
	 *
	 * @param delay  延迟，ms单位
	 * @param millis 动画总时间，ms单位
	 */
	@Override
	public void animateX(long delay, final long millis) {

		final int count = mAdapter.getXLabelsCount();
		valueAnimator = ValueAnimator.ofFloat(1.f, count);
		valueAnimator.setDuration(millis);
		valueAnimator.setStartDelay(delay);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				setAnimateRate((Float) animation.getAnimatedValue());
				postInvalidateChartData();
			}
		});
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				setAnimateType(ANIMATE_X_FLAG);
				setAnimateRate(1.f);
			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				setAnimateType(ANIMATE_NON_FLAG);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				setAnimateType(ANIMATE_X_FLAG);
				setAnimateRate(1.f);
			}
		});
		valueAnimator.start();
	}

}
