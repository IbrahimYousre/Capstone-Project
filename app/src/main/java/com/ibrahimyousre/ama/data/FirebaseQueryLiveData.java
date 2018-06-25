package com.ibrahimyousre.ama.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DatabaseResource> {

    private final Query query;
    private final ValueEventListener valueEventListener;
    private final boolean singleEvent;

    public FirebaseQueryLiveData(@NonNull Query query) {
        this(query, false);
    }

    public FirebaseQueryLiveData(@NonNull Query query, boolean singleEvent) {
        this.query = query;
        this.singleEvent = singleEvent;
        this.valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setValue(DatabaseResource.success(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setValue(DatabaseResource.error(databaseError));
            }
        };
    }

    @Override
    protected void onActive() {
        if (singleEvent) {
            query.addListenerForSingleValueEvent(valueEventListener);
        } else {
            query.addValueEventListener(valueEventListener);
        }
    }

    @Override
    protected void onInactive() {
        query.removeEventListener(valueEventListener);
    }
}
