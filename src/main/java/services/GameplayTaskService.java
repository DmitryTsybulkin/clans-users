package services;

import domains.Task;
import models.PageableRequest;

import java.util.List;

public interface GameplayTaskService {

    Task create(Task task);

    List<Task> getAll(PageableRequest pageableRequest);

    void delete();

}
