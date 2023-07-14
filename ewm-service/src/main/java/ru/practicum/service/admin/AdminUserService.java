package ru.practicum.service.admin;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminUserService {
    @Transactional
    UserDto postNewUser(UserDto user);

    @Transactional(readOnly = true)
    List<UserDto> getUsers(Set<Long> users, Integer from, Integer size);

    @Transactional
    void deleteUser(Long userId);
}
