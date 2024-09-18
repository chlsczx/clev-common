package io.github.dribble312.common.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * @author czx
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public class ImmutableAuditedEntityDto extends EntityDto {

    @NonNull
    private ZonedDateTime createdAt;

    private ZonedDateTime deletedAt;

}
