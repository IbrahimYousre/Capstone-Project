package com.ibrahimyousre.ama.data;

import android.arch.core.util.Function;

import com.google.firebase.database.DataSnapshot;
import com.ibrahimyousre.ama.data.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class ListDeserializer<T extends Entity> implements Function<DatabaseResource, List<T>> {

    private final Class<T> myClass;

    public ListDeserializer(Class<T> myClass) {
        this.myClass = myClass;
    }

    @Override
    public List<T> apply(DatabaseResource databaseResource) {
        // TODO: handle error cases
        DataSnapshot dataSnapshot = databaseResource.getData();
        if (dataSnapshot == null) return null;
        List<T> list = new ArrayList<>((int) dataSnapshot.getChildrenCount());
        for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
            T item = childSnapShot.getValue(myClass);
            item.setUid(childSnapShot.getKey());
            list.add(item);
        }
        return list;
    }
}
