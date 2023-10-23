package com.wokivovich.sapper.field;

import com.wokivovich.sapper.field.FieldBlock;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FieldGenerator {

    public FieldBlock[][] createField(int level) {

        FieldBlock[][] field = calculateNearestBombs(generateFieldAndBombsPositions(level), level);

        return field;
    }

    private FieldBlock[][] generateFieldAndBombsPositions(int level) {
        FieldBlock[][] field = new FieldBlock[level][level];
        int countOfBombs = level;
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                if (countOfBombs > 0) {
                    int bomb = (new Random()).nextInt(0, level);
                    boolean hasBomb = bomb == 5;
                    field[i][j] = new FieldBlock(hasBomb, false, 0);

                    if (hasBomb) {
                        countOfBombs--;
                    }
                } else {
                    field[i][j] = new FieldBlock(false, false, 0);
                }

            }
        }

        return field;
    }

    public FieldBlock[][] calculateNearestBombs(FieldBlock[][] field, int level) {
        int tmp = level - 1;
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                int bombs = 0;

                if (j > 0) {
                    if (field[i][j - 1].hasBomb) {
                        bombs++;
                    }
                    if (i > 0) {
                        if (field[i - 1][j - 1].hasBomb) {
                            bombs++;
                        }
                    }
                }

                if (j < tmp) {
                    if (field[i][j + 1].hasBomb) {
                        bombs++;
                    }
                    if (i > 0) {
                        if (field[i - 1][j + 1].hasBomb) {
                            bombs++;
                        }
                    }
                }

                if (i > 0) {
                    if (field[i - 1][j].hasBomb) {
                        bombs++;
                    }
                }

                if (i < tmp) {
                    if (field[i + 1][j].hasBomb) {
                        bombs++;
                    }
                    if (j > 0) {
                        if (field[i + 1][j - 1].hasBomb) {
                            bombs++;
                        }
                    }
                    if (j < tmp) {
                        if (field[i + 1][j + 1].hasBomb) {
                            bombs++;
                        }
                    }
                }
                field[i][j].setCountOfBombs(bombs);
            }
        }

        return field;
    }
}
