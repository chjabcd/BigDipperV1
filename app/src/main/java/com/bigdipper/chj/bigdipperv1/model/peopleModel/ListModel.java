package com.bigdipper.chj.bigdipperv1.model.peopleModel;

/**
 * Created by chj on 2018-01-12.
 */

public abstract class ListModel {

    private int listId;
    private String name;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
