package io.github.dribble312.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class UpdatableAuditedVo extends NonUpdatableAuditedVo {

    private final ZonedDateTime updatedAt;

}
