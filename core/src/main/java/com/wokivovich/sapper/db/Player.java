package com.wokivovich.sapper.db;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {

    private int time;
    private String name;
}
