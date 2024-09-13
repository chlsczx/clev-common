package com.github.dribble312.common.domain.entity;

import com.github.dribble312.common.domain.dto.EntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public abstract class BaseEntity {

    private Long id;

    public EntityDto toDto() {
        throw new UnsupportedOperationException("abstract type: [EntityDto]");
    }

}
