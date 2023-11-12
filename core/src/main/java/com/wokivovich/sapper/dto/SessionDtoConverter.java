package com.wokivovich.sapper.dto;

import com.wokivovich.sapper.domain.FieldBlock;
import com.wokivovich.sapper.domain.GameSession;

public class SessionDtoConverter {

    public SessionDto convert(GameSession gameSession) {

        return SessionDto.builder()
                .sessionId(gameSession.getSessionId())
                .field(toStringArray(gameSession.getField()))
                .status(gameSession.getGameStatus())
                .build();
    }

    private String[][] toStringArray(FieldBlock[][] fieldBlocks) {
        String[][] stringField = new String[fieldBlocks.length][fieldBlocks.length];
        for (int i = 0; i < fieldBlocks.length; i++) {
            for (int j = 0; j < fieldBlocks.length; j++) {
                if (fieldBlocks[i][j].isVisible()) {
                    stringField[i][j] = fieldBlocks[i][j].getCountOfBombs() + "";
                } else if (!fieldBlocks[i][j].isActive()) {
                    stringField[i][j] = "disable";
                }

            }
        }
        return stringField;
    }
}
