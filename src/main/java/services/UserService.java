package services;

import domains.User;
import models.PageableRequest;

import java.util.List;

public interface UserService {

    User create(String username, Long clanId);

    User getById(Long id);

    List<User> getAll(PageableRequest pageableRequest);

    void sendGold(Long senderUserId, Long recipientUserId, Integer gold);

    User updateName(Long userId, String name);

}
