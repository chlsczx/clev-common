package io.github.dribble312.common.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author czx
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class BasePo {

    private final Long id;

}
