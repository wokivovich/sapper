package com.wokivovich.sapper;

import com.wokivovich.sapper.field.FieldBlock;
import org.springframework.stereotype.Service;

@Service
public class View {
    public int[][] printField(FieldBlock[][] field) {
        int[][] arr = new int[field.length][field.length];

        System.out.println("  0123456789");
        for (int i = 0; i < 10; i++) {
            System.out.print("\n" + i + " ");
            for (int j = 0; j < 10; j++) {
                    arr[i][j] = field[i][j].getCountOfBombs();
            }
        }
        return arr;
    }

    public String convertFieldToString(FieldBlock[][] field) {
        StringBuilder sb = new StringBuilder();
        sb.append("  0123456789");
        for (int i = 0; i < 10; i++) {
            sb.append("\n" + i + " ");
            for (int j = 0; j < 10; j++) {
                if (field[i][j].isVisible()) {
                    sb.append(field[i][j].getCountOfBombs());
                } else {
                    sb.append("H");
                }

            }
        }
        return sb.toString();
    }
}
