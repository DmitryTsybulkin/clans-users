package services;

import domains.Clan;
import models.PageableRequest;
import models.events.Reason;

import java.util.List;

public interface ClanService {

    Clan create(String name);

    Clan getById(Long clanId);

    boolean existsByName(String name);

    List<Clan> getAll(PageableRequest pageableRequest);

    Clan updateClanName(Long clanId, String clanName);

    Clan sendMoneyToClan(Long userId, Long clanId, Integer gold, Reason reason);

    Clan receiveMoneyFromClan(Long clanId, Integer gold, Reason reason);

    void deleteById(Long id);

}
