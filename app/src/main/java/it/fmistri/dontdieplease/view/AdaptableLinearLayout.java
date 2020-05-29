package it.fmistri.dontdieplease.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import it.fmistri.dontdieplease.R;


/**
 * Linear layout used with an {@link Adapter} to manage inner views.
 * Note that for the sake of simplicity, this won't implement dynamic changes based on the adapter.
 * The adapter is simply used to populate the inner elements.
 * (This project is not using dynamic changes on single elements anyway).
 */
public class AdaptableLinearLayout extends LinearLayout {
    public AdaptableLinearLayout(Context context) {
        super(context);
    }

    public AdaptableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdaptableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Set an adapter and show adapted views.
     * @param adapter The adapter.
     */
    public void populateWithAdapter(Adapter adapter) {
        removeViews(0, getChildCount());

        for (int i = 0; i < adapter.getCount(); i++)
            addView(adapter.getView(i, null, this));
    }
}
