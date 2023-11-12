package com.wokivovich.sapper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionDto {
    int sessionId;
    String[][] field;
    String status;
}
