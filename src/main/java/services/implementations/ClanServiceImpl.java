package services.implementations;

import domains.Clan;
import domains.User;
import exceptions.EntityDuplicateException;
import exceptions.EntityNotFoundException;
import exceptions.NotImplementedException;
import lombok.extern.slf4j.Slf4j;
import models.PageableRequest;
import models.events.DomainChangeEvent;
import models.events.EventType;
import models.events.Reason;
import services.AuditService;
import services.ClanService;
import services.UserService;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ClanServiceImpl implements ClanService {

    private final List<Clan> clans;
    private final AuditService auditService;
    private final UserService userService;
    private final Lock lock;

    private volatile Long lastId = 0L;

    public ClanServiceImpl(AuditService auditService, UserService userService) {
        this.clans = new ArrayList<>();
        this.auditService = auditService;
        this.userService = userService;
        this.lock = new ReentrantLock();
    }

    @Override
    public synchronized Clan create(String name) {
        log.info("Creating user with name: {}", name);
        if (existsByName(name)) {
            throw new EntityDuplicateException(MessageFormat
                    .format("Clan with name: {} already exists", name));
        }
        Clan clan = new Clan();
        clan.setId(lastId + 1);
        clan.setGold(0);
        clan.setName(name);
        lastId = clan.getId();
        clans.add(clan);
        return clan;
    }

    @Override
    public Clan getById(Long clanId) {
        return clans.stream().filter(clan -> clan.getId().equals(clanId)).findFirst().orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Clan with id: {} not found", clanId)));
    }

    @Override
    public boolean existsByName(String name) {
        return clans.stream().anyMatch(clan -> clan.getName().equals(name));
    }

    @Override
    public List<Clan> getAll(PageableRequest pageableRequest) {
        return clans;
    }

    @Override
    public Clan updateClanName(Long clanId, String clanName) {
        throw new NotImplementedException();
    }

    @Override
    public Clan sendMoneyToClan(Long userId, Long clanId, Integer gold, Reason reason) {
        lock.lock();
        Clan clan = null;
        try {
            clan = getById(clanId);
            User user = userService.getById(userId);
            if (user.getGold() < gold) {
                log.warn("User: {} don't have enough balance for operation", user.getName());
            }

            var userBefore = user.copy();
            user.setGold(user.getGold() - gold);

            var sendMoneyEvent = DomainChangeEvent.builder()
                    .eventType(EventType.SEND_MONEY_TO_CLAN)
                    .date(ZonedDateTime.now())
                    .entityBefore(userBefore)
                    .entityAfter(user)
                    .build();
            auditService.auditEvent(sendMoneyEvent);

            Clan clanBefore = clan.copy();
            clan.setGold(clan.getGold() + gold);
            var receiveMoneyEvent = DomainChangeEvent.builder()
                    .eventType(EventType.RECEIVE_MONEY_FROM_USER)
                    .date(ZonedDateTime.now())
                    .entityBefore(clanBefore)
                    .entityAfter(clan)
                    .build();
            auditService.auditEvent(receiveMoneyEvent);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }
        return clan;
    }

    @Override
    public Clan receiveMoneyFromClan(Long clanId, Integer gold, Reason reason) {
        Clan clan = getById(clanId);
        if (clan.getGold() < gold) {
            log.info("Clan not enough balance");
            return clan;
        }
        Clan clanBefore = clan.copy();
        clan.setGold(clan.getGold() - gold);
        var receiveMoneyFromClan = DomainChangeEvent.builder()
                .eventType(EventType.RECEIVE_MONEY_FROM_CLAN)
                .date(ZonedDateTime.now())
                .entityBefore(clanBefore)
                .entityAfter(clan)
                .reason(reason)
                .build();
        auditService.auditEvent(receiveMoneyFromClan);
        return clan;
    }

    @Override
    public void deleteById(Long id) {
        clans.removeIf(clan -> clan.getId().equals(id));
    }
}
