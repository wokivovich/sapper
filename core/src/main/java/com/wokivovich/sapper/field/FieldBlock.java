package com.wokivovich.sapper.field;

import lombok.Data;

@Data
public class FieldBlock {
    boolean hasBomb;
    boolean isVisible;
    int countOfBombs;

    public FieldBlock(boolean hasBomb, boolean isVisible, int countOfBombs) {
        this.hasBomb = hasBomb;
        this.isVisible = isVisible;
        this.countOfBombs = countOfBombs;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    public void setVisible() {
        isVisible = true;
    }

    public void setCountOfBombs(int countOfBombs) {
        this.countOfBombs = countOfBombs;
    }


    public boolean isVisible() {
        return isVisible;
    }
}
