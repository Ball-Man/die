package it.fmistri.dontdieplease.fragment.statistics;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Entry;

/**
 * Fragment used for statistics. Will show data in graphs.
 */
public class StatisticsFragment extends Fragment {
    LineChart chart;
    StatisticsViewModel viewModel;

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

        chart = view.findViewById(R.id.line_chart);

        // Initialize charts
        setupLineChart();

        // Observe ViewModel and update charts
        viewModel.getLastWeeksEntries("heart").observe(getViewLifecycleOwner(),
                new Observer<Entry[]>() {
            @Override
            public void onChanged(Entry[] entries) {
                setLineChartData(entries);
            }
        });
    }

    /**
     * Set style for the line chart(showing weekly heart values).
     */
    private void setupLineChart() {
        /* ***  Chart Style *** */
        // background color
        chart.setBackgroundColor(Color.WHITE);

        // disable description text
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);

        chart.setDrawGridBackground(false);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        // // X-Axis Style // //
        XAxis xAxis;
        xAxis = chart.getXAxis();

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

        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis yAxis;
        // // Y-Axis Style // //
        yAxis = chart.getAxisLeft();

        // disable dual axis (only use LEFT axis)
        chart.getAxisRight().setEnabled(false);

        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        // axis range
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisMinimum(0f);
    }

    private void setLineChartData(Entry[] entries) {
        ArrayList<com.github.mikephil.charting.data.Entry> values = new ArrayList<>();

        for (int i = 0; i < entries.length; i++) {
            values.add(new com.github.mikephil.charting.data.Entry(i, entries[i].value.floatValue()));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, getString(R.string.heart));

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }

        // draw points over time
        chart.animateY(500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(LegendForm.LINE);
    }
}
