package it.fmistri.dontdieplease.fragment.calendar;

import android.os.Bundle;

import androidx.annotation.Nullable;

import it.fmistri.dontdieplease.fragment.summary.SummaryFragment;

/**
 * Implementation of {@link SummaryFragment} used for daily inspection of reports(from calendar).
 */
public class DailySummaryFragment extends SummaryFragment {
    /**
     * @return the ViewModel type associated to this fragment, which is
     * {@link DailySummaryViewModel}.
     */
    @Override
    protected Class getViewModelClass() {
        return DailySummaryViewModel.class;
    }

    /**
     * Create a new instance of {@link DailySummaryFragment} with the given arguments(which specify
     * a day in the gregorian calendar).
     * @param year The gregorian year.
     * @param month The gregorian month(0 based).
     * @param day The gregorian day of the month.
     * @return A new instance of this fragment.
     */
    public static DailySummaryFragment newInstance(int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt(DailySummaryViewModel.YEAR, year);
        bundle.putInt(DailySummaryViewModel.MONTH, month);
        bundle.putInt(DailySummaryViewModel.DAY, day);

        DailySummaryFragment fragment = new DailySummaryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViewModel().setArgs(getArguments());
    }
}
