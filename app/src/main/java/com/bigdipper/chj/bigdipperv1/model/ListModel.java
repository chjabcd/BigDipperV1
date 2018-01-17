package com.bigdipper.chj.bigdipperv1.model;

/**
 * Created by chj on 2018-01-12.
 */

public abstract class ListModel {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_EVENT = 1;

    abstract public int getType();


}
