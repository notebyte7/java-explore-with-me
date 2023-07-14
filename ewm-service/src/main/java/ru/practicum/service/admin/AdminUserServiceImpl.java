package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.PageableBuilder;
import ru.practicum.util.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto postNewUser(UserDto user) {
        checkUserNameAndEmailValidation(user);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    private void checkUserNameAndEmailValidation(UserDto user) {
        if (user.getName().length() > 250 || user.getEmail().length() > 254) {
            throw new WrongStateArgumentException("Слишком длинное имя или емейл", new IllegalArgumentException());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(Set<Long> users, Integer from, Integer size) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        return userRepository.getUsers(users, pageable)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден", new NullPointerException()));
        userRepository.deleteById(user.getId());
    }
}
