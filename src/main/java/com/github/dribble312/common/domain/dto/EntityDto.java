package com.github.dribble312.common.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author czx
 */
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@SuperBuilder(toBuilder = true, setterPrefix = "with")
public abstract class EntityDto {

    private final Long id;
}
