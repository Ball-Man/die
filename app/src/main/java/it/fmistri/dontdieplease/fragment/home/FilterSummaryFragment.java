package it.fmistri.dontdieplease.fragment.home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import it.fmistri.dontdieplease.fragment.summary.SummaryFragment;

/**
 * Fragment used by the home fragment to display filtered queries.
 */
public class FilterSummaryFragment extends SummaryFragment {
    public static String CATEGORY_NAME_KEY = "category_name";
    public static String MINVALUE_KEY = "minvalue";
    public static String MAXVALUE_KEY = "maxvalue";

    @Override
    protected Class getViewModelClass() {
        return FilterSummaryViewModel.class;
    }

    public static FilterSummaryFragment newInstance(String categoryName, double minValue,
                                                    double maxValue) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME_KEY, categoryName);
        bundle.putDouble(MINVALUE_KEY, minValue);
        bundle.putDouble(MAXVALUE_KEY, maxValue);

        FilterSummaryFragment fragment = new FilterSummaryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViewModel().setArgs(getArguments());
    }
}
