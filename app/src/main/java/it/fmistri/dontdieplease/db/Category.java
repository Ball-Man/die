package it.fmistri.dontdieplease.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    public Category(@NonNull String name, Integer importance) {
        this.name = name;
        this.importance = importance;
    }

    @PrimaryKey
    @NonNull
    public String name = "heart";

    @ColumnInfo
    public Integer importance;
}
