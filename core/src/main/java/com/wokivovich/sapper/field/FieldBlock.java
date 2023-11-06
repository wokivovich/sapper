package com.wokivovich.sapper.field;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldBlock {
    boolean hasBomb;
    boolean isVisible;
    boolean isActive;
    int countOfBombs;

    public FieldBlock(boolean hasBomb, boolean isVisible, boolean isActive, int countOfBombs) {
        this.hasBomb = hasBomb;
        this.isVisible = isVisible;
        this.isActive = isActive;
        this.countOfBombs = countOfBombs;
    }
}
