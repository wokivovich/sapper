package com.wokivovich.sapper.field;

import lombok.Data;

@Data
public class FieldDto {
    String[][] fieldOfDigits;

    public String[][] fieldToDigitsConverter(FieldBlock[][] fieldBlocks) {
        String[][] res = new String[fieldBlocks.length][fieldBlocks.length];
        for (int i = 0; i < fieldBlocks.length; i++) {
            for (int j = 0; j < fieldBlocks.length; j++) {
                if (fieldBlocks[i][j].isVisible) {
                    res[i][j] = fieldBlocks[i][j].countOfBombs + "";
                }

            }
        }
        return res;
    }
}
