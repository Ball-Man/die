package it.fmistri.dontdieplease.dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;

/**
 * Adapt database model to the view(ReportDialogFragment).
 */
public class ReportDialogModel extends ViewModel {
    LiveData<Category[]> categories;

    /**
     * @return An observable of the accepted categories.
     */
    public LiveData<Category[]> getCategories() {
        if (categories == null)
            categories = DieDatabase.getInstance(null).categoryDAO().getCategories();
        return categories;
    }
}
