package io.github.dribble312.common.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public class MutableAuditedEntity extends ImmutableAuditedEntity {

    private ZonedDateTime updatedAt;

    public void update() {
        this.updatedAt = ZonedDateTime.now();
    }

    @Override
    public MutableAuditedEntity.MutableAuditedEntityBuilder<?, ?> withTimezone(ZoneId zoneId) {
        return ((MutableAuditedEntity.MutableAuditedEntityBuilder<?, ?>) super.withTimezone(zoneId))
                .withUpdatedAt(this.updatedAt != null ? this.updatedAt.withZoneSameInstant(zoneId) : null);
    }
}
