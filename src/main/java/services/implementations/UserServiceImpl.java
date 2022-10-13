package services.implementations;

import domains.User;
import exceptions.EntityNotFoundException;
import exceptions.UserNotEnoughBalanceException;
import lombok.extern.slf4j.Slf4j;
import models.PageableRequest;
import models.events.DomainChangeEvent;
import models.events.EventType;
import services.AuditService;
import services.UserService;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class UserServiceImpl implements UserService {

    private final List<User> users;
    private final AuditService auditService;
    private final Lock lock;

    private volatile Long lastId = 0L;

    public UserServiceImpl(AuditService auditService) {
        this.auditService = auditService;
        this.users = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public synchronized User create(String username, Long clanId) {
        log.info("Creating user with name: {}", username);
        User user = new User();
        user.setId(lastId + 1);
        user.setName(username);
        user.setClanId(clanId);
        user.setGold(0);
        lastId = user.getId();
        users.add(user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("User with id {} not found", id)));
    }

    @Override
    public List<User> getAll(PageableRequest pageableRequest) {
        return users;
    }

    @Override
    public void sendGold(Long senderUserId, Long recipientUserId, Integer gold) {
        lock.lock();
        try {
            User sender = getById(senderUserId);
            User recipient = getById(recipientUserId);
            if (sender.getGold() < gold) {
                throw new UserNotEnoughBalanceException(sender.getName());
            }
            User senderBefore = sender.copy();
            sender.setGold(sender.getGold() - gold);
            var sendMoneyEvent = DomainChangeEvent.builder()
                    .eventType(EventType.SEND_MONEY_TO_USER)
                    .date(ZonedDateTime.now())
                    .entityBefore(senderBefore)
                    .entityAfter(sender)
                    .build();
            auditService.auditEvent(sendMoneyEvent);

            User recipientBefore = recipient.copy();
            recipient.setGold(recipient.getGold() + gold);
            var receiveMoneyEvent = DomainChangeEvent.builder()
                    .eventType(EventType.RECEIVE_MONEY_FROM_USER)
                    .date(ZonedDateTime.now())
                    .entityBefore(recipientBefore)
                    .entityAfter(recipient)
                    .build();
            auditService.auditEvent(receiveMoneyEvent);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public User updateName(Long userId, String name) {
        User user = getById(userId);
        user.setName(name);
        log.info("User: {} update name to: {}", user.getId(), name);
        return user;
    }

}
