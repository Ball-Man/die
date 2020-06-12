package it.fmistri.dontdieplease.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"report_id", "category_name"},
        foreignKeys = {
            @ForeignKey(entity=Report.class, parentColumns = "r_id", childColumns = "report_id",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity=Category.class, parentColumns = "name",
                    childColumns = "category_name")
        }
)
public class Entry implements Categorized{
    @ColumnInfo
    @NonNull
    public Long report_id = 0L;

    @ColumnInfo
    @NonNull
    public String category_name = "heart";

    @ColumnInfo
    public Double value;

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String getCategoryName() {
        return category_name;
    }
}
