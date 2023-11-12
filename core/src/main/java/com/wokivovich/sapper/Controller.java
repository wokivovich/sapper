package com.wokivovich.sapper;

import com.wokivovich.sapper.service.LeaderBoardService;
import com.wokivovich.sapper.domain.Player;
import com.wokivovich.sapper.dto.PlayerRequest;
import com.wokivovich.sapper.service.GameFieldService;
import com.wokivovich.sapper.domain.GameSession;
import com.wokivovich.sapper.dto.SessionDto;
import com.wokivovich.sapper.dto.SessionDtoConverter;
import com.wokivovich.sapper.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequestMapping(
        path = "/api",
        produces = "application/json")
@CrossOrigin(origins = {"http://localhost:3000", "http://ui:3000"})
@RestController
public class Controller {

    GameFieldService gameFieldService;
    GameService gameService;
    LeaderBoardService leaderBoardService;
    Map<Integer, GameSession> sessions = new HashMap();

    public Controller(GameFieldService gameFieldService, GameService gameService, LeaderBoardService leaderBoardService) {
        this.gameFieldService = gameFieldService;
        this.gameService = gameService;
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping("/start/{id}")
    public SessionDto startGame(@PathVariable("id") int difficult) {
        SessionDtoConverter converter = new SessionDtoConverter();
        GameSession gameSession = GameSession.builder()
                .sessionId((new Random()).nextInt())
                .gameStatus("EMPTY")
                .field(gameFieldService.createField(difficult))
                .blocksLost((difficult * difficult) - difficult)
                .difficult(difficult)
                .build();

        sessions.put(gameSession.getSessionId(), gameSession);

        return converter.convert(gameSession);
    }

    @PostMapping("/play")
    public SessionDto chooseBlock(@RequestBody int[] coordinates) {
        GameSession session = sessions.get(coordinates[0]);

        SessionDtoConverter converter = new SessionDtoConverter();

        session.setGameStatus(gameService.play(session, session.getDifficult(), coordinates[1], coordinates[2]));
        sessions.put(session.getSessionId(), session);

        return converter.convert(session);
    }

    @PostMapping("/flag")
    public SessionDto postFlag(@RequestBody int[] coordinates) {
        GameSession session = sessions.get(coordinates[0]);

        SessionDtoConverter converter = new SessionDtoConverter();

        gameService.flagPosting(session, coordinates[1], coordinates[2]);
        sessions.put(session.getSessionId(), session);

        return converter.convert(session);
    }

    @PostMapping("/record")
    public void recordResult(@RequestBody PlayerRequest player) {

        leaderBoardService.addPlayer(Player.builder()
                .time(Integer.parseInt(player.time()))
                .name(player.name())
                .build(),
                Integer.parseInt(player.difficult())
        );
    }

    @GetMapping("/leaders")
    public List<List<Player>> getLeaderBoard() {

        return leaderBoardService.getLeaders();
    }

}
