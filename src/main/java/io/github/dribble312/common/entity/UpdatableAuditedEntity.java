package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class UpdatableAuditedEntity extends NonUpdatableAuditedEntity {

    private ZonedDateTime updatedAt;

    public UpdatableAuditedEntity update() {
        if (this.getDeletedAt() != null) {
            throw new IllegalStateException("Entity already has a `deletedAt`, so it's can not be updated");
        }

        if (this.getCreatedAt() == null) {
            throw new IllegalStateException("Entity has not been created, so it's can not be updated");
        }

        this.updatedAt = ZonedDateTime.now().withZoneSameInstant(super.getZoneId());

        return this;
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

    @Override
    protected UpdatableAuditedEntity alignTimezone() {
        super.alignTimezone();

        ZonedDateTime updatedAt = this.getUpdatedAt();
        if (updatedAt != null) {
            this.setUpdatedAt(updatedAt.withZoneSameInstant(super.getZoneId()));
        }

        return this;
    }

    @Override
    public UpdatableAuditedEntity cloneWithTruncatingDateTime(ChronoUnit chronoUnit) {
        UpdatableAuditedEntity clone = (UpdatableAuditedEntity) super.cloneWithTruncatingDateTime(chronoUnit);

        ZonedDateTime updatedAt = clone.getUpdatedAt();

        if (updatedAt != null) {
            clone.setUpdatedAt(updatedAt.truncatedTo(chronoUnit));
        }

        return clone;
    }
}
