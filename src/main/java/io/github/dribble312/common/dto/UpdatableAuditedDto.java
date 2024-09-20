package io.github.dribble312.common.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * @author czx
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
@SuperBuilder(toBuilder = true)
public class UpdatableAuditedDto extends NonUpdatableAuditedDto {

    private final ZonedDateTime updatedAt;


}
