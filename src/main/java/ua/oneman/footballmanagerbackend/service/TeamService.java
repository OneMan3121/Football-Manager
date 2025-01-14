package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.resp.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.exception.TeamNotFoundException;
import ua.oneman.footballmanagerbackend.mapper.TeamMapper;
import ua.oneman.footballmanagerbackend.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    // --- public service ---

    public PublicTeamRespDTO getTeamByName(String name) {
        return teamRepository.findByNameAndIsDeletedFalse(name)
                .map(teamMapper::toPublicDTO)
                .orElseThrow(() -> new TeamNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public Page<PublicTeamRespDTO> getAllTeams(Pageable pageable) {
        return teamRepository.findAllByIsDeletedFalse(pageable)
                .map(teamMapper::toPublicDTO);
    }





    // --- protected service ---



    // --- admin service ---
}