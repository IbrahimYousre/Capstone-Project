package com.ibrahimyousre.ama.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class DatabaseResource {

    public final DataSnapshot data;
    public final DatabaseError error;

    private DatabaseResource(DataSnapshot data, DatabaseError error) {
        this.data = data;
        this.error = error;
    }

    public boolean isSuccessful() {
        return data != null;
    }

    public static DatabaseResource success(@NonNull DataSnapshot data) {
        return new DatabaseResource(data, null);
    }

    public static DatabaseResource error(@NonNull DatabaseError error) {
        return new DatabaseResource(null, error);
    }
}
