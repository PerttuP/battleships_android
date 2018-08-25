package org.hupisoft.battleships.gameareatable.model;

public class ColumnHeaderModel {

    private String mData;
    private static final String ALPHABET[] = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public ColumnHeaderModel(int columnNum) {
        mData = convertColumnNumberToString(columnNum);
    }

    public String getData() {
        return mData;
    }

    private String convertColumnNumberToString(int columnNum) {
        String letter = ALPHABET[columnNum % ALPHABET.length];
        if (columnNum >= ALPHABET.length){
            return convertColumnNumberToString(columnNum / ALPHABET.length - 1) + letter;
        } else {
            return letter;
        }
    }
}
