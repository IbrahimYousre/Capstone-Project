package com.example.ibrahim.ama.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private final Query query;
    private final ValueEventListener valueEventListener;
    private final boolean singleEvent;

    public FirebaseQueryLiveData(@NonNull Query query) {
        this(query, false);
    }

    public FirebaseQueryLiveData(@NonNull Query query, boolean singleEvent) {
        this.query = query;
        this.valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseQueryLiveData.this.setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        this.singleEvent = singleEvent;
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
