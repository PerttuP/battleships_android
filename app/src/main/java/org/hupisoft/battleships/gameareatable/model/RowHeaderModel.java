package org.hupisoft.battleships.gameareatable.model;

/**
 * Model for single row header item in game area table.
 */
public class RowHeaderModel {

    private String mData;

    public RowHeaderModel(int rowNum) {
        mData = Integer.toString(rowNum+1);
    }

    public String getData() {
        return mData;
    }
}
