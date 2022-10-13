package controllers;

import domains.Clan;
import models.PageableRequest;
import services.ClanService;

import java.util.List;

public class ClanController {

    public ClanService clanService;

    public List<Clan> getClans(int page, int size) {
        return clanService.getAll(PageableRequest.builder().page(page).size(size).build());
    }

}
