package it.fmistri.dontdieplease.fragment.notifications;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.arch.core.util.Function;
import androidx.core.view.LayoutInflaterCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.fragment.dialog.CategoryAdapter;
import it.fmistri.dontdieplease.functional.ArrayFunctional;

/**
 * Adapter for {@link it.fmistri.dontdieplease.db.Monitor} instances.
 */
public class MonitorAdapter extends BaseAdapter {
    private Monitor[] monitors;
    private Context context;

    /**
     * Construct an adapter made on the given monitors.
     * @param context The current context.
     * @param monitors The monitors to adapt.
     */
    public MonitorAdapter(Context context, Monitor[] monitors) {
        this.context = context;
        this.monitors = monitors;
    }

    @Override
    public int getCount() {
        return monitors.length;
    }

    @Override
    public Object getItem(int position) {
        return monitors[position];
    }

    @Override
    public long getItemId(int position) {
        return monitors[position].m_id;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monitor_item, parent,
                    false);
        }

        final Monitor monitor = (Monitor) getItem(position);
        Context context = getContext();

        // Get current locale
        Locale loc = context.getResources().getConfiguration().locale;

        // TODO: Move here the MonitorItem instantiation

        // Populate dates
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        ((TextView) convertView.findViewById(R.id.start_date_view))
                .setText(format.format(monitor.start_date));
        ((TextView) convertView.findViewById(R.id.end_date_view))
                .setText(format.format(monitor.end_date));

        // Populate threshold value
        ((EditText) convertView.findViewById(R.id.threshold_edit))
                .setText(String.format(loc, "%f", monitor.threshold));

        return convertView;
    }
}
