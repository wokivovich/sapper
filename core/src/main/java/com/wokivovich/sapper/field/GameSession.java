package com.wokivovich.sapper.field;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSession {
    private int sessionId;
    private FieldBlock[][] field;
    private String gameStatus;
    private int blocksLost;
    private int difficult;
}
