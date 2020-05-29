package it.fmistri.dontdieplease.fragment.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Entry;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.view.AdaptableLinearLayout;

/**
 * Adapter for reports(accepts {@link it.fmistri.dontdieplease.db.ReportWithEntries}).
 * Internally will generate a {@link CategorizedAdapter} for each collection of entries in
 * the given reports.
 */
public class ReportAdapter extends BaseAdapter {
    private Context context;
    private ReportWithEntries[] reports;

    public ReportAdapter(Context context, ReportWithEntries[] reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public int getCount() {
        return reports.length;
    }

    @Override
    public Object getItem(int position) {
        return reports[position];
    }

    @Override
    public long getItemId(int position) {
        return reports[position].hashCode();
    }

    /**
     * @return The context associated to this adapter.
     */
    public Context getContext() {
        return context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the desired resource if needed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.resource_item, parent,
                    false);

        ReportWithEntries report = (ReportWithEntries) getItem(position);
        Context context = getContext();

        // Get current locale
        Locale loc = context.getResources().getConfiguration().locale;

        // Set the UI
        ((AdaptableLinearLayout) convertView.findViewById(R.id.list_entries)).populateWithAdapter(
                new CategorizedAdapter(context, report.entries.toArray(new Entry[0]))
        );

        ((TextView) convertView.findViewById(R.id.notes)).setText(report.report.note);

        return convertView;
    }
}
