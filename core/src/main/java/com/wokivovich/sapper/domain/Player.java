package com.wokivovich.sapper.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {

    private int time;
    private String name;
}
