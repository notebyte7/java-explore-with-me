package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongStateArgumentException;
import ru.practicum.model.rating.Rating;
import ru.practicum.model.user.User;
import ru.practicum.repository.RatingRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.PageableBuilder;
import ru.practicum.util.RatingCalculator;
import ru.practicum.util.mapper.UserMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

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
    public List<UserDto> getUsers(Set<Long> users, Integer from, Integer size, Boolean asc) {
        Pageable pageable = PageableBuilder.getPageable(from, size);
        List<UserDto> userDtoList = userRepository.getUsers(users, pageable)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        List<Rating> ratings = ratingRepository.findAllByInitiatorIds(users);
        setUserRates(userDtoList, ratings);
        return getSortedUsers(userDtoList, asc);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден", new NullPointerException()));
        userRepository.deleteById(user.getId());
    }

    private void setUserRates(List<UserDto> userDtoList, List<Rating> rating) {
        for (UserDto userDto : userDtoList) {
            Set<Rating> userEventRatings = getUserEventRating(userDto.getId(), rating);
            userDto.setRating(RatingCalculator.calculateRating(userEventRatings));
        }
    }

    private Set<Rating> getUserEventRating(Long userId, List<Rating> ratings) {
        if (ratings != null && !ratings.isEmpty()) {
            return ratings
                    .stream()
                    .filter(rating -> rating.getEvent().getInitiator().getId().equals(userId))
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private List<UserDto> getSortedUsers(List<UserDto> userDtoList, Boolean asc) {
        if (asc == null) {
            return userDtoList;
        } else {
            if (asc.equals(Boolean.TRUE)) {
                return userDtoList
                        .stream()
                        .sorted(Comparator.comparing(UserDto::getRating, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                return userDtoList
                        .stream()
                        .sorted(Comparator.comparing(UserDto::getRating))
                        .collect(Collectors.toList());
            }
        }
    }
}
