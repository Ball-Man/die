package it.fmistri.dontdieplease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    static String FRAGMENT_MAIN_TAG = "fragment_main";

    DieDatabase db;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init database
        db = DieDatabase.getInstance(this);

        // Populate drawer
        drawerLayout = findViewById(R.id.activity_main);

        // Attach and configure the toolbar to the actionbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Log.d("TOOLBAR", ((Toolbar) findViewById(R.id.toolbar)).toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add the default fragment
        replaceFragment(HomeFragment.class);

        // Add listener for the navigation view(when an item is selected)
        ((NavigationView) findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        navigationItemSelected(item);
                        return true;
                    }
                }
        );

        db.categoryDAO().getCategories().observe(this, new Observer<Category[]>() {
            @Override
            public void onChanged(Category[] categories) {
                Log.d("DieDB", ((Integer)categories.length).toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open or close the drawer
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback for navigation items.
     * @param item The selected item.
     */
    private void navigationItemSelected(MenuItem item) {
        Log.d("NAV VIEW", "Item id: " + item.getItemId());

        // Select the fragment type to instantiate
        Class cl;
        switch (item.getItemId()) {
            default:
                cl = HomeFragment.class;
        }

        // Create and replace the fragment
        replaceFragment(cl);

        // Set title and close drawer
        drawerLayout.closeDrawers();
        setTitle(item.getTitle());
    }

    /**
     * Fragment transaction that replaces R.id.fragment_main with an instance
     * of the given class.
     * The stack will be cleared.
     * @param frag_class The class of the fragment to create.
     */
    private void replaceFragment(@NonNull Class frag_class) {
        // Clear the backstack
        getSupportFragmentManager()
                .popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main, (Fragment) frag_class.newInstance())
                    .commit();
        }
        catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
