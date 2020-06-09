package it.fmistri.dontdieplease.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;

/**
 * Built to work with {@link HomeFragment}.
 */
public class HomeViewModel extends ViewModel {
    private LiveData<Category[]> categories;

    /**
     * @return An observable collection of all the categories.
     */
    public LiveData<Category[]> getCategories() {
        if (categories == null)
            categories = DieDatabase.getInstance(null).categoryDAO().getCategories();
        return categories;
    }
}
