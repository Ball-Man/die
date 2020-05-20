package it.fmistri.dontdieplease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;

public class MainActivity extends AppCompatActivity {
    DieDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DieDatabase.getInstance(this);

        Button button = findViewById(R.id.button);
        db.categoryDAO().getCategories().observe(this, new Observer<Category[]>() {
            @Override
            public void onChanged(Category[] categories) {
                Log.d("DieDB", ((Integer)categories.length).toString());
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
