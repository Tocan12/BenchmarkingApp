package com.example.benchmarkingscs.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.benchmarkingscs.R;
import com.example.benchmarkingscs.model.CPUModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.Map;

public class CPUChartActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_view);

        lineChart = findViewById(R.id.lineChart);


        CPUModel cpuModel = new CPUModel(this);
        Map<String, Double> logData = cpuModel.readLogData();

        ArrayList<Entry> entries = new ArrayList<>();
        int index = 0;


        for (Map.Entry<String, Double> entry : logData.entrySet()) {
            entries.add(new Entry(index++, entry.getValue().floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "CPU Operations Time (ms)");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);

        // Create LineData object with the dataset
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // Enable X and Y axis labels
        lineChart.getXAxis().setEnabled(true);
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setTextSize(12f);
        lineChart.getAxisLeft().setTextSize(12f);

        lineChart.invalidate();
    }
}
