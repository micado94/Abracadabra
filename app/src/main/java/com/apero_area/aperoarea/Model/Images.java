package com.apero_area.aperoarea.Model;

import io.realm.RealmObject;

/**
 * Created by micka on 11-Aug-17.
 */

public class Images extends RealmObject {

    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}