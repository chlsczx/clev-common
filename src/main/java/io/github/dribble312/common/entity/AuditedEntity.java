package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class AuditedEntity extends BaseEntity {

    @Builder.Default()
    private ZoneId zoneId = ZoneId.systemDefault();

    /**
     * Clone and change the timezone.
     *
     * @param timezone timezone
     *
     * @return cloned this
     */
    public AuditedEntity cloneWithTimezone(@NonNull String timezone) {
        return this.cloneWithTimezone(ZoneId.of(timezone));
    }

    public AuditedEntity cloneWithTimezone(@NonNull ZoneId zoneId) {
        return this.toBuilder()
                .zoneId(zoneId)
                .build();
    }

    public AuditedEntity setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        this.alignTimezone();
        return this;
    }

    public AuditedEntity setZoneId(String zoneId) {
        this.zoneId = ZoneId.of(zoneId);
        this.alignTimezone();
        return this;
    }

    protected AuditedEntity alignTimezone() {
        // do nothing
        return this;
    }

}
