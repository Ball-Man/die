package it.fmistri.dontdieplease.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.fmistri.dontdieplease.R;

/**
 * Fragment implementation for the home tab of the application.
 */
public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

//    /**
//     * Create a new instance of HomeFragment.
//     * @return A new fragment.
//     */
//    public static HomeFragment newInstance() {
//        return new HomeFragment();
//    }
}
