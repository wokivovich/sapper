package com.wokivovich.sapper;

import com.wokivovich.sapper.field.GameSession;
import org.springframework.stereotype.Service;

@Service
public class Reader {

    public String play(GameSession session, int level, int x, int y) {
            if (session.getField()[y][x].isHasBomb()) {
                return "LOOSE";
            } else {
                setVisibleNearestBlocks(session, x, y, level);
                System.out.println(session.getBlocksLost());
            }
            if (session.getBlocksLost() == 0) {
                return "WIN";
            }
        return "EMPTY";

    }

    private void setVisibleNearestBlocks(GameSession session, int x, int y, int level) {
        session.getField()[y][x].setVisible();
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
