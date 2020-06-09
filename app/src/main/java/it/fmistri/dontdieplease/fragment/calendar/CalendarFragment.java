package it.fmistri.dontdieplease.fragment.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.R;

/**
 * Fragment implementation for the calendar tab of the application.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {
    // Useful views
    private DatePicker calendarPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // For simplicity, use this as listener
        ((FloatingActionButton) view.findViewById(R.id.select_date))
                .setOnClickListener(this);

        calendarPicker = (DatePicker) view.findViewById(R.id.calendar);
    }

    /**
     * Get the current selected date and instantiate a
     * {@link it.fmistri.dontdieplease.fragment.summary.SummaryFragment} for the given day.
     * @param v
     */
    @Override
    public void onClick(View v) {
//        GregorianCalendar calendar = new GregorianCalendar();
//        calendar.setTimeInMillis(calendarView.getDate());
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, DailySummaryFragment.newInstance(
                        calendarPicker.getYear(),
                        calendarPicker.getMonth(), calendarPicker.getDayOfMonth()))
                .addToBackStack(null)
                .commit();
    }
}