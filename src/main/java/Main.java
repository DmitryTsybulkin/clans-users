import domains.Clan;
import domains.User;
import models.events.Reason;
import services.AuditService;
import services.ClanService;
import services.UserService;
import services.implementations.ClanServiceImpl;
import services.implementations.LogAuditServiceImpl;
import services.implementations.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final static Random RANDOM = new Random();

    private final static List<Clan> clans = new ArrayList<>();
    private final static List<User> users = new ArrayList<>();

    private final static ExecutorService executorService = Executors
            .newWorkStealingPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        AuditService auditService = new LogAuditServiceImpl();
        UserService userService = new UserServiceImpl(auditService);
        ClanService clanService = new ClanServiceImpl(auditService, userService);

        loadTestData(userService, clanService);

        while (!users.isEmpty()) {
            var user = users.get(RANDOM.nextInt(users.size()));
            if (user.getGold() <= 0) {
                users.remove(user);
                continue;
            }
            var gold = RANDOM.nextInt(user.getGold() + 1);
            Reason reason = Reason.randomReasonToGiveMoneyToClan(RANDOM);
            CompletableFuture
                    .runAsync(() ->
                            clanService.sendMoneyToClan(user.getId(), user.getClanId(), gold, reason), executorService)
                    .join();
        }
    }

    private static void loadTestData(UserService userService, ClanService clanService) {
        Clan bobry = clanService.create("Bobry");
        bobry.setGold(300);
        Clan enoty = clanService.create("Enoty");
        enoty.setGold(100);
        clans.add(bobry);
        clans.add(enoty);
        for (int i = 0; i < 500; i++) {
            User user = userService.create("bober_" + i, bobry.getId());
            user.setGold(RANDOM.nextInt(100));
            users.add(user);
        }
        for (int i = 0; i < 500; i++) {
            User user = userService.create("enot_" + i, bobry.getId());
            user.setGold(RANDOM.nextInt(100));
            users.add(user);
        }
    }

}
