package com.ghost.json.adapters;

import com.ghost.json.JsonSerializer;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;

/**
 * Converts all objects that extends java.util.Collections to JSONArray.
 */
public class CollectionAdapter implements JsonDataAdapter<Collection> {

    @Override
    public Object toJson(Collection collection) throws JSONException {
        JSONArray array = new JSONArray();
        for (Object o: collection) {
            array.put(JsonSerializer.serialize(o));
        }
        return array;
    }
}
