package it.fmistri.dontdieplease.fragment.summary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import it.fmistri.dontdieplease.db.AverageEntry;

/**
 * Adapter for average reports(accepts {@link it.fmistri.dontdieplease.db.AverageEntry}
 * collections).
 */
public class AverageReportAdapter extends BaseAdapter {
    private Context context;
    private AverageEntry[] entries;

    public AverageReportAdapter(Context context, AverageEntry[] entries) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
