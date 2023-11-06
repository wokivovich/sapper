package com.wokivovich.sapper.db;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.*;

@Service
public class LeaderBoardService {

    public void addPlayer(Player player, int difficult) {
        Jedis jedis = new Jedis("redis", 6379);
        switch (difficult) {
            case 8:
                jedis.zadd("easy", player.getTime(), player.getName());
                break;
            case 12:
                jedis.zadd("medium", player.getTime(), player.getName());
                break;
            case 20:
                jedis.zadd("hard", player.getTime(), player.getName());
                break;
        }

        jedis.close();
    }


    // Need refactoring

    public List<List<Player>> getLeaders() {
        Jedis jedis = new Jedis("redis", 6379);
        List<Tuple> easy = jedis.zrangeWithScores("easy", 0, 10);
        List<Tuple> medium = jedis.zrangeWithScores("medium", 0, 10);
        List<Tuple> hard = jedis.zrangeWithScores("hard", 0, 10);

        List<List<Player>> leaders = new ArrayList<>();
        List<Player> easyTop = new ArrayList<>();
        List<Player> mediumTop = new ArrayList<>();
        List<Player> hardTop = new ArrayList<>();

        for (Tuple leader : easy) {
            easyTop.add(Player.builder()
                    .name(leader.getElement())
                    .time((int) leader.getScore())
                    .build());
            }

        for (Tuple leader : medium) {
            mediumTop.add(Player.builder()
                    .name(leader.getElement())
                    .time((int) leader.getScore())
                    .build());
        }

        for (Tuple leader : hard) {
            hardTop.add(Player.builder()
                    .name(leader.getElement())
                    .time((int) leader.getScore())
                    .build());
        }
        leaders.add(easyTop);
        leaders.add(mediumTop);
        leaders.add(hardTop);

        jedis.close();

        return leaders;
    }
}
