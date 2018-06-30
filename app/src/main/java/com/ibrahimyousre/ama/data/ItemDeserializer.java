package com.ibrahimyousre.ama.data;

import android.arch.core.util.Function;

import com.google.firebase.database.DataSnapshot;
import com.ibrahimyousre.ama.data.model.Entity;

public class ItemDeserializer<T extends Entity> implements Function<DatabaseResource, T> {

    private final Class<T> myClass;

    public ItemDeserializer(Class<T> myClass) {
        this.myClass = myClass;
    }

    @Override
    public T apply(DatabaseResource databaseResource) {
        // TODO: handle error cases
        DataSnapshot dataSnapshot = databaseResource.getData();
        if (dataSnapshot == null) return null;
        T item = dataSnapshot.getValue(myClass);
        if (item == null) return null;
        item.setUid(dataSnapshot.getKey());
        return item;
    }
}
