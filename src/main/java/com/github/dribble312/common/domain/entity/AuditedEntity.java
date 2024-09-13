package com.github.dribble312.common.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public abstract class AuditedEntity extends BaseEntity {

    public AuditedEntity.AuditedEntityBuilder<?, ?> withTimezone(String timezone) {
        return this.withTimezone(ZoneId.of(timezone));
    }

    public abstract AuditedEntity.AuditedEntityBuilder<?, ?> withTimezone(ZoneId zoneId);

}
