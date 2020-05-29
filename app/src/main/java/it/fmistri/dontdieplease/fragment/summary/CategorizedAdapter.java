package it.fmistri.dontdieplease.fragment.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Categorized;

/**
 * Adapter for categorized entries from reports
 * (accepts {@link it.fmistri.dontdieplease.db.Categorized}
 * collections).
 */
public class CategorizedAdapter extends BaseAdapter {
    private Context context;
    private Categorized[] entries;

    public CategorizedAdapter(Context context, Categorized[] entries) {
        this.entries = entries;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return entries[position];
    }

    @Override
    public long getItemId(int position) {
        return entries[position].hashCode();
    }

    /**
     * Retrieve the context initially given by the user.
     * @return The context for this adapter.
     */
    private Context getContext() {
        return context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the desired resource if needed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent,
                    false);

        Categorized entry = (Categorized) getItem(position);
        Context context = getContext();

        // Retrieve string using some reflection(so that it matches current locale).
        int stringId = context.getResources().getIdentifier(entry.getCategoryName(),
                "string", context.getPackageName());
        String localizedString = context.getResources().getString(stringId);

        // Get current locale
        Locale loc = context.getResources().getConfiguration().locale;

        // Actually update the view
        ((TextView) convertView.findViewById(R.id.text)).setText(
                String.format(loc, "%s:\t%f", localizedString, entry.getValue()));

        return convertView;
    }
}
