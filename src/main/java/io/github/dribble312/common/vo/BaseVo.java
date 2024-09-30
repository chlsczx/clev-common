package io.github.dribble312.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author dribble312
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class BaseVo {

    private final Long id;

}
