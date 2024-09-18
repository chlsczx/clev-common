package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public class ImmutableAuditedEntity extends AuditedEntity {

    @NonNull
    private ZonedDateTime createdAt;

    private ZonedDateTime deletedAt;

    public void delete() {
        this.deletedAt = ZonedDateTime.now(ZoneId.systemDefault());
    }

    @Override
    public ImmutableAuditedEntity.ImmutableAuditedEntityBuilder<?, ?> withTimezone(ZoneId zoneId) {
        return this
                .toBuilder()
                .withCreatedAt(this.createdAt.withZoneSameInstant(zoneId))
                .withDeletedAt(this.deletedAt != null ? this.deletedAt.withZoneSameInstant(zoneId) : null);
    }

}
