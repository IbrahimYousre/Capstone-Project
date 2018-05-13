package com.ibrahimyousre.ama.util;

import com.google.firebase.database.DatabaseError;

import timber.log.Timber;

public class DatabaseErrorUtils {

    public static void log(DatabaseError databaseError) {
        Timber.d(databaseError.toException(),
                "Database Error #%d, %s",
                databaseError.getCode(),
                databaseError.getMessage());
        Timber.v(databaseError.getDetails());
    }
}
