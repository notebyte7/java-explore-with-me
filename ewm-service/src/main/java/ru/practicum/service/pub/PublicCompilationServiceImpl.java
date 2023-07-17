package ru.practicum.service.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.util.CompilationUtil;
import ru.practicum.util.PageableBuilder;
import ru.practicum.util.StatsManager;
import ru.practicum.util.mapper.CompilationMapper;
import ru.practicum.util.mapper.EventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationUtil compilationUtil;


    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return mapCompilationPage(compilationRepository.findAll(pinned, PageableBuilder.getPageable(from, size)));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка не найдена", new NullPointerException()));
        return getCompilationDto(compilation);
    }

    private List<CompilationDto> mapCompilationPage(Page<Compilation> compilations) {
        return compilations
                .getContent()
                .stream()
                .map(this::getCompilationDto)
                .collect(Collectors.toList());
    }

    private CompilationDto getCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        Set<Long> eventIds = compilation.getEvents()
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        compilationDto.setEvents(compilationUtil.getEventShortList(eventIds));
        return compilationDto;
    }


}
