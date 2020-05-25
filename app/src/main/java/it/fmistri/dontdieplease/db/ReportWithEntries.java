package it.fmistri.dontdieplease.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReportWithEntries {
    @Embedded
    public Report report;

    @Relation(parentColumn = "r_id", entityColumn = "report_id", entity = Entry.class)
    public List<Entry> entries;
}
