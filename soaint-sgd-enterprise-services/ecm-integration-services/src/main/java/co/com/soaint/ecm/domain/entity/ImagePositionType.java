package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public enum ImagePositionType implements Serializable {

    TOP_LEFT(20F, 710F),
    TOP_MIDDLE(195F, 710F),
    TOP_RIGHT(380F, 710F),

    MIDDLE_LEFT(20F, 355F),
    MIDDLE_MIDDLE(195F, 355F),
    MIDDLE_RIGHT(380F, 355F),

    BOTTOM_LEFT(20F, 30F),
    BOTTOM_MIDDLE(195F, 30F),
    BOTTOM_RIGHT(380F, 30F);

    private static final Long serialVersionUID = 188L;

    private final float absoluteX;
    private final float absoluteY;
}
