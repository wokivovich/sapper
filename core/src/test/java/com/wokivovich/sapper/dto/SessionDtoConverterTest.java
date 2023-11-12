package com.wokivovich.sapper.dto;

import com.wokivovich.sapper.domain.FieldBlock;
import com.wokivovich.sapper.domain.GameSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SessionDtoConverterTest {

    private final SessionDtoConverter converter = new SessionDtoConverter();

    @Test
    public void toStringArray_intArray_StringArray() {
        FieldBlock[][] field = {
                {FieldBlock.builder().isActive(true).build(), FieldBlock.builder().isActive(true).build()},
                {FieldBlock.builder().isActive(true).build(), FieldBlock.builder().isActive(true).build()}
        };

        GameSession session = GameSession.builder()
                .sessionId(1)
                .field(field)
                .gameStatus("EMPTY")
                .build();

        SessionDto expected = SessionDto.builder()
                .sessionId(1)
                .field(new String[][] {{null, null},{null, null}})
                .status("EMPTY")
                .build();

        SessionDto actual = converter.convert(session);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void toStringArray_oneFieldDisabled_StringArray() {
        FieldBlock[][] field = {
                {FieldBlock.builder().isActive(false).build(), FieldBlock.builder().isActive(true).build()},
                {FieldBlock.builder().isActive(true).build(), FieldBlock.builder().isActive(true).build()}
        };

        GameSession session = GameSession.builder()
                .sessionId(1)
                .field(field)
                .gameStatus("EMPTY")
                .build();

        SessionDto expected = SessionDto.builder()
                .sessionId(1)
                .field(new String[][] {{"disable", null},{null, null}})
                .status("EMPTY")
                .build();

        SessionDto actual = converter.convert(session);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void toStringArray_threeFieldsAreOpened_StringArray() {
        FieldBlock block = FieldBlock.builder().countOfBombs(3).isVisible(true).isActive(true).build();
        FieldBlock[][] field = {
                {block, FieldBlock.builder().isActive(true).build()},
                {block, block}
        };

        GameSession session = GameSession.builder()
                .sessionId(1)
                .field(field)
                .gameStatus("EMPTY")
                .build();

        SessionDto expected = SessionDto.builder()
                .sessionId(1)
                .field(new String[][] {{"3", null},{"3", "3"}})
                .status("EMPTY")
                .build();

        SessionDto actual = converter.convert(session);

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}