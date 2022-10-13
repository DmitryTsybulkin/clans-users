package models.events;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Reason {

    QUEST_COMPLETED,

    ARENA_BATTLE,

    USER_WISH,

    COLLECT_FOR_EPIC_MOUNT,

    NALOG;

    public static Reason randomReasonToGiveMoneyToClan(Random random) {
        return values()[random.nextInt(values().length)];
    }

}
