package it.fmistri.dontdieplease.fragment.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;

/**
 * Custom adapter for Category management in spinners.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private Category[] categories;

    public CategoryAdapter(Context context, Category[] categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return categories[position].hashCode();
    }

    /**
     * @return The array of categories encapsulated.
     */
    public Category[] getCategories() {
        return categories;
    }

    /**
     * @return The context associated to this adapter.
     */
    public Context getContext() {
          return context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the desired resource if needed
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);

        Category category = (Category) getItem(position);

        int stringId = getContext().getResources().getIdentifier(category.name,
                "string", getContext().getPackageName());
        String localizedString = getContext().getResources().getString(stringId);
        ((TextView) convertView.findViewById(R.id.text)).setText(localizedString);

        return convertView;
    }
}
