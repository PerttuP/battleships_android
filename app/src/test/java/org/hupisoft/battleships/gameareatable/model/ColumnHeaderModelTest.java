package org.hupisoft.battleships.gameareatable.model;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ColumnHeaderModelTest {

    static final String[] ALPHABET = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    @Test
    public void columns0to25HaveSingleCharacterData() {
        for (int col = 0; col < ALPHABET.length; ++col) {
            ColumnHeaderModel model = new ColumnHeaderModel(col);
            assertEquals(ALPHABET[col], model.getData());
        }
    }

    @Test
    public void columnsAbove25HaveMultipleCharacterData() {
        TreeMap<Integer, String> numberToLetters = new TreeMap<>();
        numberToLetters.put(26, "AA");
        numberToLetters.put(27, "AB");
        numberToLetters.put(51, "AZ");
        numberToLetters.put(52, "BA");
        numberToLetters.put(701, "ZZ");
        numberToLetters.put(702, "AAA");

        for (Map.Entry<Integer,String> entry : numberToLetters.entrySet()) {
            ColumnHeaderModel model = new ColumnHeaderModel(entry.getKey());
            assertEquals(entry.getValue(), model.getData());
        }
    }

}