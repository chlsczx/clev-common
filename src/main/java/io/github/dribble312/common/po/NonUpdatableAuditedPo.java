package io.github.dribble312.common.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * @author czx
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@SuperBuilder(toBuilder = true)
public class NonUpdatableAuditedPo extends BasePo {

    private final ZonedDateTime createdAt;

    private final ZonedDateTime deletedAt;

}
