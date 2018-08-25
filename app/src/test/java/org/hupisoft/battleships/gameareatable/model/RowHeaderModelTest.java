package org.hupisoft.battleships.gameareatable.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class RowHeaderModelTest {

    @Test
    public void modelDataMatchesRowNumber() {
        for (int row = 0; row < 100; ++row) {
            RowHeaderModel model = new RowHeaderModel(row);
            assertEquals(Integer.toString(row+1), model.getData());
        }
    }
}