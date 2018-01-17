package com.bigdipper.chj.bigdipperv1.model;

/**
 * Created by chj on 2018-01-12.
 */

public class HeaderItem extends ListModel {

    private String header;

    // here getters and setters
    // for title and so on, built
    // using date


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

}
