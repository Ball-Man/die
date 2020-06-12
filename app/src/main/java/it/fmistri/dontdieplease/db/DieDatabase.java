package it.fmistri.dontdieplease.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.fmistri.dontdieplease.db.dao.CategoryDAO;
import it.fmistri.dontdieplease.db.dao.EntryDAO;
import it.fmistri.dontdieplease.db.dao.MonitorDAO;
import it.fmistri.dontdieplease.db.dao.NotificationsDAO;
import it.fmistri.dontdieplease.db.dao.ReportDAO;

/**
 * Database manager class(implementation of RoomDatabase).
 * It's also a singleton(GoF).
 */
@Database(entities={Report.class, Entry.class, Category.class, NotificationsSettings.class,
        Monitor.class},
        version=15)
@TypeConverters({DateConverter.class})
public abstract class DieDatabase extends RoomDatabase {
    /**
     * Singleton attribute.
     */
    static DieDatabase instance = null;
    /**
     * Singleton method implementation.
     * Initialize and cache the value in the 'instance' attribute if not initialized already.
     * @return The DieDatabase instance.
     */
    public static DieDatabase getInstance(Context context) {
        if (instance == null) {
            // Instantiate db with initial data(by design)
            instance = Room.databaseBuilder(context, DieDatabase.class, DieDatabase.NAME)
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/dont_die_please.db")
                    .build();
        }
        return instance;
    }

    public static final String NAME = "dont_die_please";

    public abstract EntryDAO entryDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract ReportDAO reportDAO();
    public abstract NotificationsDAO notificationsSettingsDAO();
    public abstract MonitorDAO monitorDAO();
}
