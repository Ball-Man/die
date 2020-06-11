package it.fmistri.dontdieplease.fragment.statistics;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.db.StatisticEntry;

/**
 * Fragment used for statistics. Will show data in graphs.
 */
public class StatisticsFragment extends Fragment {
    private LineChart lineChart;
    private PieChart pieChart;
    private StatisticsViewModel viewModel;

    // Temporary data
    int triggerMonitorCount = 0;
    int totalMonitorCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(StatisticsViewModel.class);

        lineChart = view.findViewById(R.id.line_chart);
        pieChart = view.findViewById(R.id.pie_chart);

        // Initialize charts
        setupLineChart();
        setupPieChart();

        // Observe ViewModel and update charts
        viewModel.getLastWeeksEntries("heart").observe(getViewLifecycleOwner(),
                new Observer<StatisticEntry[]>() {
            @Override
            public void onChanged(StatisticEntry[] entries) {
                lineChart.clear();
                setLineChartData(entries);
            }
        });

        viewModel.getAllMonitors().observe(getViewLifecycleOwner(), new Observer<Monitor[]>() {
            @Override
            public void onChanged(Monitor[] allMonitors) {
                totalMonitorCount = allMonitors.length;
                setPieChartData();
            }
        });

        viewModel.getTriggeredMonitors().observe(getViewLifecycleOwner(),
                new Observer<Monitor[]>() {
            @Override
            public void onChanged(Monitor[] triggeredMonitors) {
                triggerMonitorCount = triggeredMonitors.length;
                setPieChartData();
            }
        });
    }

    /**
     * Set style for the line chart(showing weekly heart values).
     */
    private void setupLineChart() {
        // General style
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDrawGridBackground(false);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        // X axis
        XAxis xAxis;
        xAxis = lineChart.getXAxis();

        // Set date a value formatter to print dates instead of timestamps
        final Locale loc = getResources().getConfiguration().locale;
        final DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return format.format(new Date((long) value));
            }
        });
        xAxis.setLabelCount(4, true);
        xAxis.setLabelRotationAngle(90);

        // Y axis
        YAxis yAxis;
        yAxis = lineChart.getAxisLeft();
        lineChart.getAxisRight().setEnabled(false); // Make sure there is only one Y axis
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        // axis range
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisMinimum(0f);
    }

    private void setLineChartData(StatisticEntry[] entries) {
        ArrayList<com.github.mikephil.charting.data.Entry> values = new ArrayList<>();
        LineDataSet set1;

        // Generate data for the chart
        for (StatisticEntry entry : entries) {
            values.add(new com.github.mikephil.charting.data.Entry(entry.date.getTime(),
                    entry.entry.value.floatValue()));
        }

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // Create dataset
            set1 = new LineDataSet(values, getString(R.string.heart));
            set1.setDrawIcons(false);

            // Line and points
            set1.enableDashedLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // Legend
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // Text
            set1.setValueTextSize(9f);

            // Fill area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            // Set the data to the chart
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            lineChart.setData(data);
        }

        // Set animation
        lineChart.animateY(500);

        // Setup the legend
        Legend l = lineChart.getLegend();
        l.setForm(LegendForm.LINE);
    }

    /**
     * Setup the pie chart used for monitor statistics.
     */
    private void setupPieChart() {
        pieChart.setDescription(null);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.animateXY(300, 300);
    }

    /**
     * Reset the pie chart data by using {@link StatisticsFragment#triggerMonitorCount} and
     * {@link StatisticsFragment#totalMonitorCount}.
     */
    private void setPieChartData() {
        // It's physiological for the model to have totalMonitorCount >= triggeredMonitorCount.
        // However, since changes are observed over time, there might be inconsistencies.
        // In this case, prevent negative values to appear.
        int safeMonitorCount = Math.max(0, totalMonitorCount - triggerMonitorCount);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(safeMonitorCount, getString(R.string.safe)));
        entries.add(new PieEntry(triggerMonitorCount, getString(R.string.warning)));

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.monitors));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setData(new PieData(dataSet));
        pieChart.invalidate();
    }
}
