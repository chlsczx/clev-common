package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class NonUpdatableAuditedEntity extends AuditedEntity {

    private ZonedDateTime createdAt;

    private ZonedDateTime deletedAt;

    public NonUpdatableAuditedEntity delete() {
        if (this.getDeletedAt() != null) {
            throw new IllegalStateException("Entity already has a `deletedAt`");
        }

        if (this.getCreatedAt() == null) {
            throw new IllegalStateException("Entity has not been created, so it's can not be deleted");
        }

        this.deletedAt = ZonedDateTime.now(super.getZoneId());

        return this;
    }

    public NonUpdatableAuditedEntity create(@NonNull Long id) {
        if (super.getId() != null || this.getCreatedAt() != null) {
            if (super.getId() != null && this.getCreatedAt() != null) {
                throw new IllegalStateException("Entity already created.");
            }
            // or else
            throw new IllegalStateException("Entity already has a `id` or a `createdAt`.");
        }

        super.setId(id);
        this.createdAt = ZonedDateTime.now(super.getZoneId());

        return this;
    }


    /**
     * Recreate a new instance with new timezone audited properties (avoided concurrent problem).
     *
     * @param zoneId zoneId
     *
     * @return a new instance
     */
    @Override
    public NonUpdatableAuditedEntity cloneWithTimezone(@NonNull ZoneId zoneId) {
        NonUpdatableAuditedEntity entity = (NonUpdatableAuditedEntity) super.cloneWithTimezone(zoneId);

        return entity.alignTimezone();
    }

    @Override
    protected NonUpdatableAuditedEntity alignTimezone() {
        ZonedDateTime thisCreatedAt = this.getCreatedAt();
        ZonedDateTime thisDeletedAt = this.getDeletedAt();
        if (thisCreatedAt != null) {
            this.setCreatedAt(thisCreatedAt.withZoneSameInstant(super.getZoneId()));
        }
        if (thisDeletedAt != null) {
            this.setDeletedAt(thisDeletedAt.withZoneSameInstant(super.getZoneId()));
        }
        return this;
    }

    @Override
    public NonUpdatableAuditedEntity cloneWithTruncatingDateTime(ChronoUnit chronoUnit) {
        NonUpdatableAuditedEntity clone = (NonUpdatableAuditedEntity) super.cloneWithTruncatingDateTime(
                chronoUnit);
        ZonedDateTime createdAt = clone.getCreatedAt();

        ZonedDateTime deletedAt = clone.getDeletedAt();

        if (createdAt != null) clone.setCreatedAt(createdAt.truncatedTo(chronoUnit));

        if (deletedAt != null) clone.setDeletedAt(deletedAt.truncatedTo(chronoUnit));

        return clone;
    }
}
