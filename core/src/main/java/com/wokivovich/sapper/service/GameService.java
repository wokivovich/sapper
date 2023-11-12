package com.wokivovich.sapper.service;

import com.wokivovich.sapper.domain.GameSession;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public String play(GameSession session, int level, int x, int y) {
            if (session.getField()[y][x].isHasBomb()) {
                return "LOOSE";
            } else {
                setVisibleNearestBlocks(session, x, y, level);
            }
            if (session.getBlocksLost() == 0) {
                return "WIN";
            }
        return "EMPTY";

    }

    public void flagPosting(GameSession session, int x, int y) {
        if (!session.getField()[y][x].isVisible()) {
            if (session.getField()[y][x].isActive()) {
                session.getField()[y][x].setActive(false);
            } else {
                session.getField()[y][x].setActive(true);
            }
        }
    }

    private void setVisibleNearestBlocks(GameSession session, int x, int y, int level) {
        session.getField()[y][x].setVisible(true);
        session.setBlocksLost(session.getBlocksLost() - 1);
        if (session.getField()[y][x].getCountOfBombs() == 0) {

            if (x > 0 && !session.getField()[y][x-1].isVisible() && !session.getField()[y][x-1].isHasBomb()) {
                setVisibleNearestBlocks(session, x-1, y, level);
            }

            if (x < level - 1 && !session.getField()[y][x+1].isVisible() && !session.getField()[y][x+1].isHasBomb()) {
                setVisibleNearestBlocks(session, x+1, y, level);
            }

            if (y > 0 && !session.getField()[y - 1][x].isVisible() && !session.getField()[y - 1][x].isHasBomb()) {
                setVisibleNearestBlocks(session, x, y-1, level);
            }

            if (y < level - 1 && !session.getField()[y + 1][x].isVisible() && !session.getField()[y + 1][x].isHasBomb()) {
                setVisibleNearestBlocks(session, x, y+1, level);
            }
        }

    }
}
