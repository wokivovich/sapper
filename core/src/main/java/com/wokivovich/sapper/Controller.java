package com.wokivovich.sapper;

import com.wokivovich.sapper.db.LeaderBoardService;
import com.wokivovich.sapper.db.Player;
import com.wokivovich.sapper.field.FieldGenerator;
import com.wokivovich.sapper.field.GameSession;
import com.wokivovich.sapper.field.SessionDto;
import com.wokivovich.sapper.field.SessionDtoConverter;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequestMapping(
        produces = "application/json")
@CrossOrigin(origins = {"http://localhost:3000", "http://ui:3000", "http://localhost", "http://192.168.1.12:3000"})
@RestController
public class Controller {

    FieldGenerator generator;
    View view;
    Reader reader;
    LeaderBoardService service;
    Map<Integer, GameSession> sessions = new HashMap();

    public Controller(FieldGenerator generator, View view, Reader reader, LeaderBoardService service) {
        this.generator = generator;
        this.view = view;
        this.reader = reader;
        this.service = service;
    }

    @GetMapping("/")
    public void greeting() {
    }

    @GetMapping("/api/start/{id}")
    public SessionDto startGame(@PathVariable("id") int difficult) {
        SessionDtoConverter converter = new SessionDtoConverter();
        GameSession gameSession = GameSession.builder()
                .sessionId((new Random()).nextInt())
                .gameStatus("EMPTY")
                .field(generator.createField(difficult))
                .blocksLost((difficult * difficult) - difficult)
                .difficult(difficult)
                .build();

        sessions.put(gameSession.getSessionId(), gameSession);

        return converter.sessionToDtoConverter(gameSession);
    }

    @PostMapping("/api/play")
    public SessionDto chooseBlock(@RequestBody int[] coordinates) {
        GameSession session = sessions.get(coordinates[0]);

        SessionDtoConverter converter = new SessionDtoConverter();

        session.setGameStatus(reader.play(session, session.getDifficult(), coordinates[1], coordinates[2]));
        sessions.put(session.getSessionId(), session);

        return converter.sessionToDtoConverter(session);
    }

    @PostMapping("/api/record")
    public void recordResult(@RequestBody PlayerRequest player) {

        service.addPlayer(Player.builder()
                .time(Integer.parseInt(player.time()))
                .name(player.name())
                .build()
        );
    }

    @GetMapping("/api/leaders")
    public List<Player> getLeaderBoard() {

        return service.getLeaders();
    }

}
