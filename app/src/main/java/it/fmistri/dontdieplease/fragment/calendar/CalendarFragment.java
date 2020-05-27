package it.fmistri.dontdieplease.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.dialog.ReportDialogFragment;

/**
 * Fragment implementation for the calendar tab of the application.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((CalendarView) view.findViewById(R.id.calendar))
                .setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Log.d("CALENDAR", year + " " + month + " " + dayOfMonth);
    }
}