package io.github.dribble312.common.utils;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author dribble312
 */
public class ZoneDateTimes {

    public static ZonedDateTime nowOfUtc() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

}
