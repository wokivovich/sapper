package com.wokivovich.sapper.service;

import com.wokivovich.sapper.domain.Player;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderBoardService {

    private final String REDIS_HOST = "redis";
    private final JedisPool pool = new JedisPool(REDIS_HOST, 6379);

    public void addPlayer(Player player, int difficult) {
        String level = convertDifficultToString(difficult);
        try (Jedis jedis = pool.getResource()) {
            jedis.zadd(level, player.getTime(), player.getName());
        }
    }

    public List<List<Player>> getLeaders() {
        List<List<Player>> leaders = new ArrayList<>();

        try (Jedis jedis = pool.getResource()) {
            List<Tuple> easy = jedis.zrangeWithScores("easy", 0, 10);
            List<Tuple> medium = jedis.zrangeWithScores("medium", 0, 10);
            List<Tuple> hard = jedis.zrangeWithScores("hard", 0, 10);

            leaders.addAll(List.of(
                    getLevelTop(easy),
                    getLevelTop(medium),
                    getLevelTop(hard))
            );
        }

        return leaders;
    }

    private List<Player> getLevelTop(List<Tuple> players) {
        List<Player> leaders = new ArrayList<>();

        players.forEach((leader ->
                leaders.add(Player.builder()
                        .name(leader.getElement())
                        .time((int) leader.getScore())
                        .build())
        ));

        return leaders;
    }

    private String convertDifficultToString(int difficult) {
        switch (difficult) {
            case 8:
                return "easy";
            case 12:
                return "medium";
            default:
                return "hard";
        }
    }

}
