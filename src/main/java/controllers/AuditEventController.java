package controllers;

import models.PageableEventRequest;
import models.events.Event;
import models.events.EventType;
import services.AuditService;

import java.util.Date;
import java.util.List;

public class AuditEventController {

    private AuditService auditService;

    public List<Event> getAllEvents(Integer page, Integer size, Date from, Date to,
                                    EventType eventType, String sort) {
        var pageableRequest = PageableEventRequest.builder()
                .page(page)
                .size(size)
                .from(from)
                .to(to)
                .eventType(eventType)
                .sort(sort).build();
        return auditService.getAllEvents(pageableRequest);
    }

}
