package ru.practicum.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.WrongStateArgumentException;

public class PageableBuilder {
    public static Pageable getPageable(Integer from, Integer size) {
        validateFrom(from);
        validateSize(size);
        if (from <= size - 1) {
            return PageRequest.of(0, size);
        } else {
            return PageRequest.of(from / size, size);
        }
    }

    private static void validateFrom(Integer from) {
        if (from < 0) {
            throw new WrongStateArgumentException("Не может быть меньше 0", new IllegalArgumentException());
        }
    }

    private static void validateSize(Integer size) {
        if (size < 1) {
            throw new WrongStateArgumentException("Должен быть больше 0", new IllegalArgumentException());
        }
    }
}
