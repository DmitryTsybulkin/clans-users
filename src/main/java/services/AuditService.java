package services;

import models.PageableEventRequest;
import models.events.Event;

import java.util.List;

public interface AuditService {

    void auditEvent(Event event);

    List<Event> getAllEvents(PageableEventRequest pageableRequest);
}
