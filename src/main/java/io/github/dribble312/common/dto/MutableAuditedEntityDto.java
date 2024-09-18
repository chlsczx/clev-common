package io.github.dribble312.common.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class MutableAuditedEntityDto extends ImmutableAuditedEntityDto {

    private ZonedDateTime updatedAt;

}
