package it.fmistri.dontdieplease.db;

import androidx.room.Embedded;

import java.util.Date;

/**
 * Used as custom return value from queries for charts(basically, it's an entry with it's date
 * attached).
 */
public class StatisticEntry {
    @Embedded
    public Entry entry;

    public Date date;
}
