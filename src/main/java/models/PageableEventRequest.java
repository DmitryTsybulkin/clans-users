package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import models.events.EventType;

import java.util.Date;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PageableEventRequest extends PageableRequest {

    private Date from;
    private Date to;
    private EventType eventType;

}
