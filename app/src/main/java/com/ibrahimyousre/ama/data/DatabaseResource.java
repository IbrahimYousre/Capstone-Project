package com.ibrahimyousre.ama.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class DatabaseResource {

    private final DataSnapshot data;
    private final DatabaseError error;

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

    public DataSnapshot getData() {
        return data;
    }

    public DatabaseError getError() {
        return error;
    }
}
