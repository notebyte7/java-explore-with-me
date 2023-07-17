package ru.practicum.service.admin;

import ru.practicum.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminUserService {
    UserDto postNewUser(UserDto user);

    List<UserDto> getUsers(Set<Long> users, Integer from, Integer size);

    void deleteUser(Long userId);
}
