package it.fmistri.dontdieplease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import it.fmistri.dontdieplease.fragment.calendar.CalendarFragment;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.fragment.home.HomeFragment;
import it.fmistri.dontdieplease.fragment.notifications.NotificationsFragment;
import it.fmistri.dontdieplease.fragment.statistics.StatisticsFragment;
import it.fmistri.dontdieplease.util.NotificationBuilder;

public class MainActivity extends AppCompatActivity {
    private static String FRAGMENT_MAIN_TAG = "fragment_main";

    private DieDatabase db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init notification channel
        NotificationBuilder.createChannel(this);

        // Init database
        db = DieDatabase.getInstance(this);

        // Populate drawer
        drawerLayout = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        // Attach and configure the toolbar to the actionbar
        setSupportActionBar(toolbar);
        Log.d("TOOLBAR", ((Toolbar) findViewById(R.id.toolbar)).toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add the default fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.class);
        }

        // Setup actionbar toggle
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);

        // Add listener for the navigation view(when an item is selected)
        navigationView.setNavigationItemSelectedListener(
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Open or close the drawer
        if (drawerToggle.onOptionsItemSelected(item)) {
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
            case R.id.item_calendar:
                cl = CalendarFragment.class;
                break;

            case R.id.item_statistics:
                cl = StatisticsFragment.class;
                break;

            case R.id.item_notifications:
                cl = NotificationsFragment.class;
                break;

            default:
                cl = HomeFragment.class;
                break;
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

    /**
     * Create and return the actionbar toggle.
     * @return The actionbar toggle.
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);

    }
}
