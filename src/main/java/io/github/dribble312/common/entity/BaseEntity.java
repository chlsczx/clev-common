package io.github.dribble312.common.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity {

    private Long id;

}
