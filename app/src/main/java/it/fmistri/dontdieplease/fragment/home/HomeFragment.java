package it.fmistri.dontdieplease.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.fragment.dialog.ReportDialogFragment;

/**
 * Fragment implementation for the home tab of the application.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((FloatingActionButton) view.findViewById(R.id.add_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick();
            }
        });
    }

    /**
     * Callback for the add button click.
     */
    private void addClick() {
        Log.d("HOME", "button pressed");
        ReportDialogFragment.newInstance(false, 0)
                .show(getChildFragmentManager(), "report_dialog");
    }

    //    /**
//     * Create a new instance of HomeFragment.
//     * @return A new fragment.
//     */
//    public static HomeFragment newInstance() {
//        return new HomeFragment();
//    }
}
