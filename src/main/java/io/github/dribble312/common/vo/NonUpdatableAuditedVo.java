package io.github.dribble312.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * @author dribble312
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@SuperBuilder(toBuilder = true)
public class NonUpdatableAuditedVo extends BaseVo {

    private final ZonedDateTime createdAt;

    private final ZonedDateTime deletedAt;

}
