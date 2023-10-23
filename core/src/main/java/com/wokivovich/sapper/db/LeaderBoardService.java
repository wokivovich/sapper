package com.wokivovich.sapper.db;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.*;

@Service
public class LeaderBoardService {

    public void addPlayer(Player player) {
        Jedis jedis = new Jedis();
        jedis.zadd("leaderboard", player.getTime(), player.getName());
        jedis.close();
    }


    public List<Player> getLeaders() {
        Jedis jedis = new Jedis("172.17.0.2", 6379);
            List<Tuple> leaders = jedis.zrangeWithScores("leaderboard", 0, 10);
            List<Player> leaderBoard = new ArrayList<>();
            for (Tuple leader : leaders) {
                leaderBoard.add(Player.builder()
                        .name(leader.getElement())
                        .time((int) leader.getScore())
                        .build());
            }
            jedis.close();

            return leaderBoard;
    }
}
