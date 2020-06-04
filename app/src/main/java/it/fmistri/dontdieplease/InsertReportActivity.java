package it.fmistri.dontdieplease;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.fragment.dialog.ReportDialogFragment;

/**
 * The sole purpose for this activity is instantiating the db and
 * open a {@link ReportDialogFragment}.
 */
public class InsertReportActivity extends AppCompatActivity
        implements ReportDialogFragment.OnDismissListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate the DB
        DieDatabase.getInstance(this);

        // Start the dialog
        ReportDialogFragment dialog = ReportDialogFragment.newInstance(false, 0);
        dialog.setOnDismissListener(this);

        dialog.show(getSupportFragmentManager(), "report_dialog");
    }

    @Override
    public void onDismiss(DialogFragment fragment) {
        // On dialog completion, exit
        finish();
    }
}
