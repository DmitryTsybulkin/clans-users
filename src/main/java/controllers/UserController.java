package controllers;

import domains.User;
import models.PageableRequest;
import services.UserService;

import java.util.List;

public class UserController {

    private UserService userService;

    public List<User> getUsers(Integer page, Integer size) {
        return userService.getAll(PageableRequest.builder().page(page).size(size).build());
    }

}
