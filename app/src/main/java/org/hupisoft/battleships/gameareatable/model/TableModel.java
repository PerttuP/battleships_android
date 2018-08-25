package org.hupisoft.battleships.gameareatable.model;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.ISquare;

import java.util.List;
import java.util.ArrayList;

public class TableModel {

    private List<RowHeaderModel> mRowHeaderModelList = null;
    private List<ColumnHeaderModel> mColumnHeaderModelList = null;
    private List<List<ISquare>> mCellModelList = null;

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<ColumnHeaderModel> getColumnHeaderModelList() {
        return  mColumnHeaderModelList;
    }

    public List<List<ISquare>> getCellModelList() {
        return mCellModelList;
    }

    public void setGameArea(IGameArea area) {
        if (area == null) {
            mRowHeaderModelList = null;
            mColumnHeaderModelList = null;
            mCellModelList = null;
        } else {
            mRowHeaderModelList = generateRowHeaderModelList(area);
            mColumnHeaderModelList = generateColumnHeaderModelList(area);
            mCellModelList = generateCellModelList(area);
        }
    }

    private List<RowHeaderModel> generateRowHeaderModelList(IGameArea area) {
        List<RowHeaderModel> models = new ArrayList<>();
        int rowCount = area.height();
        for (int row = 0; row < rowCount; ++row) {
            models.add(new RowHeaderModel(row));
        }
        return models;
    }

    private List<ColumnHeaderModel> generateColumnHeaderModelList(IGameArea area) {
        List<ColumnHeaderModel> models = new ArrayList<>();
        int columnCount = area.width();
        for (int column = 0; column < columnCount; ++ column) {
            models.add(new ColumnHeaderModel(column));
        }
        return models;
    }

    private List<List<ISquare>> generateCellModelList(IGameArea area) {
        int rowCount = area.height();
        int columnCount = area.width();
        List<List<ISquare>> models = new ArrayList<>();

        for (int row = 0; row < rowCount; ++row) {
            List<ISquare> squares = new ArrayList<>();
            for (int column = 0; column < columnCount; ++column) {
                squares.add(area.getSquare(new Coordinate(column, row)));
            }
            models.add(squares);
        }

        return  models;
    }
}
