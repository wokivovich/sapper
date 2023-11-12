package com.wokivovich.sapper.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldBlock {
    private boolean hasBomb;
    private boolean isVisible;
    private boolean isActive;
    private int countOfBombs;

    public FieldBlock(boolean hasBomb, boolean isVisible, boolean isActive, int countOfBombs) {
        this.hasBomb = hasBomb;
        this.isVisible = isVisible;
        this.isActive = isActive;
        this.countOfBombs = countOfBombs;
    }
}
