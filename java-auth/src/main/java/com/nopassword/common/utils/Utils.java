package com.nopassword.common.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 *
 * @author NoPassword
 */
public class Utils {

    /**
     * Gets UTC
     *
     * @return UTC current time yyyy-mm-dd hh:mi:ssZ
     */
    public static String currentTime() {
        return ZonedDateTime.now(ZoneOffset.UTC).toString().substring(0, 19).replace("T", " ") + "Z";
    }

}
