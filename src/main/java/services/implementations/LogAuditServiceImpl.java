package services.implementations;

import exceptions.NotImplementedException;
import lombok.extern.slf4j.Slf4j;
import models.PageableEventRequest;
import models.events.Event;
import services.AuditService;

import java.util.List;

@Slf4j
public class LogAuditServiceImpl implements AuditService {

    @Override
    public void auditEvent(Event event) {
        log.info(event.toString());
    }

    @Override
    public List<Event> getAllEvents(PageableEventRequest pageableRequest) {
        throw new NotImplementedException();
    }
}
