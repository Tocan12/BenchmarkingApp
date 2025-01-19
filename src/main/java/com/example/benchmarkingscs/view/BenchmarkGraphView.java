package com.example.benchmarkingscs.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

public class BenchmarkGraphView extends View {

    private Map<String, Double> benchmarkResults;
    private Paint paint;

    public BenchmarkGraphView(Context context, Map<String, Double> benchmarkResults) {
        super(context);
        this.benchmarkResults = benchmarkResults;
        paint = new Paint();
    }

    public BenchmarkGraphView(Context context, AttributeSet attrs, Map<String, Double> benchmarkResults) {
        super(context, attrs);
        this.benchmarkResults = benchmarkResults;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (benchmarkResults == null || benchmarkResults.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / (benchmarkResults.size() + 1);
        int maxHeight = height - 100;

        paint.setColor(Color.BLUE);
        int index = 0;

        for (Map.Entry<String, Double> entry : benchmarkResults.entrySet()) {
            double value = entry.getValue();
            int barHeight = (int) (value * maxHeight / 1000);

            canvas.drawRect(barWidth * (index + 1), height - barHeight, barWidth * (index + 2), height, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText(entry.getKey(), barWidth * (index + 1) + 10, height - barHeight - 10, paint);
            index++;
        }
    }
}
