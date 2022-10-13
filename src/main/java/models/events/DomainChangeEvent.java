package models.events;

import domains.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DomainChangeEvent extends Event {
    private Entity entityBefore;
    private Entity entityAfter;

    @Override
    public String toString() {
        return "DomainChangeEvent{" +
                "domainBefore=" + entityBefore +
                ", domainAfter=" + entityAfter +
                ", date=" + date +
                ", eventType=" + eventType +
                '}';
    }
}
