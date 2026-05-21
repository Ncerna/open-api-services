package org.adminBo.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class DateUtil {

    private DateUtil() {}
    public static LocalDateTime parseDate(String date) {
        try {
            if (date == null || date.isBlank())  return LocalDateTime.now();
            return OffsetDateTime .parse(date) .toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
