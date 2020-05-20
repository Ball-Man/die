package it.fmistri.dontdieplease.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"report_id", "category_name"},
        foreignKeys = {
            @ForeignKey(entity=Report.class, parentColumns = "r_id", childColumns = "report_id"),
            @ForeignKey(entity=Category.class, parentColumns = "name",
                    childColumns = "category_name")
        }
)
public class Entry {
    @ColumnInfo
    @NonNull
    public Integer report_id = 0;

    @ColumnInfo
    @NonNull
    public String category_name = "heart";

    @ColumnInfo
    public Double value;
}
