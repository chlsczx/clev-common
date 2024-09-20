package io.github.dribble312.common.po;

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
public class UpdatableAuditedPo extends NonUpdatableAuditedPo {

    private final ZonedDateTime updatedAt;

}
