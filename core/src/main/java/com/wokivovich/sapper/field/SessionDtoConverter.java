package com.wokivovich.sapper.field;

public class SessionDtoConverter {

    public SessionDto sessionToDtoConverter(GameSession gameSession) {
        FieldDto dto = new FieldDto();
        return SessionDto.builder()
                .sessionId(gameSession.getSessionId())
                .field(dto.fieldToDigitsConverter(gameSession.getField()))
                .status(gameSession.getGameStatus())
                .build();
    }
}
