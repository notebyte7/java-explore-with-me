package ru.practicum.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.CompilationUtil;
import ru.practicum.util.StatsManager;
import ru.practicum.util.mapper.CompilationMapper;
import ru.practicum.util.mapper.EventMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationUtil compilationUtil;

    @Override
    public CompilationDto postNewCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> compilationEvents = getCompilationEvents(newCompilationDto.getEvents());
        compilation.setEvents(compilationEvents);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.setEvents(compilationUtil.getEventShortList(newCompilationDto.getEvents()));
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка не найдена", new NullPointerException()));
        compilationRepository.deleteById(compilation.getId());
    }


    private Set<Event> getCompilationEvents(Set<Long> eventIds) {
        if (eventIds != null) {
            return new HashSet<>(eventRepository.findAllById(eventIds));
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public CompilationDto patchCompilation(Long compId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка не найдена", new NullPointerException()));
        setCompilationFields(updateCompilationDto, compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
        compilationDto.setEvents(compilationUtil.getEventShortList(updateCompilationDto.getEvents()));
        return compilationDto;
    }

    private void setCompilationFields(UpdateCompilationDto updateCompilationDto, Compilation compilation) {
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(getCompilationEvents(updateCompilationDto.getEvents()));
        }
    }
}
