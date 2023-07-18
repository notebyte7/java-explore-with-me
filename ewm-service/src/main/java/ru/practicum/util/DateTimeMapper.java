package ru.practicum.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Format.DATA_FORMAT;

public class DateTimeMapper {
    public static LocalDateTime toLocalDateTime(String time) {
        if (time != null) {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATA_FORMAT));
        } else {
            return null;
        }
    }
}
