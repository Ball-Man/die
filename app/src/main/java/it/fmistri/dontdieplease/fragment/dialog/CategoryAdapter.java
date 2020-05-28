package it.fmistri.dontdieplease.fragment.dialog;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;

/**
 * Custom adapter for Category management in spinners.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(@NonNull Context context, int resource, @NonNull Category[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the desired resource if needed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);

        Category category = getItem(position);

        int stringId = getContext().getResources().getIdentifier(category.name,
                "string", getContext().getPackageName());
        String localizedString = getContext().getResources().getString(stringId);
        ((TextView) convertView.findViewById(R.id.text)).setText(localizedString);

        return convertView;
    }
}
