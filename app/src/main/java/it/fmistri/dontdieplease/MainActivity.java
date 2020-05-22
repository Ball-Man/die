package it.fmistri.dontdieplease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.Objects;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;

public class MainActivity extends AppCompatActivity {
    DieDatabase db;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Populate drawer
//        drawerLayout = findViewById(R.id.activity_main);
//
        // Attach and configure the toolbar to the actionbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Log.d("TOOLBAR", ((Toolbar) findViewById(R.id.toolbar)).toString());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // Init database
//        db = DieDatabase.getInstance(this);
//
//        Button button = findViewById(R.id.button);
//        db.categoryDAO().getCategories().observe(this, new Observer<Category[]>() {
//            @Override
//            public void onChanged(Category[] categories) {
//                Log.d("DieDB", ((Integer)categories.length).toString());
//            }
//        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
