package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class UpdatableAuditedEntity extends NonUpdatableAuditedEntity {

    private ZonedDateTime updatedAt;

    public void update() {
        this.updatedAt = ZonedDateTime.now().withZoneSameInstant(super.getZoneId());
    }

    @Override
    public UpdatableAuditedEntity cloneWithTimezone(ZoneId zoneId) {
        ZonedDateTime newUpdatedAt = Optional.ofNullable(updatedAt)
                .map(dt -> dt.withZoneSameInstant(zoneId))
                .orElse(null);

        return ((UpdatableAuditedEntity) super.cloneWithTimezone(zoneId))
                .toBuilder()
                .updatedAt(newUpdatedAt)
                .build();
    }
}
