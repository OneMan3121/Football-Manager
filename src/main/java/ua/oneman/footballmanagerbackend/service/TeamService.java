package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.TeamReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.TeamRespDTO;
import ua.oneman.footballmanagerbackend.mapper.TeamMapper;
import ua.oneman.footballmanagerbackend.model.Team;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.repository.TeamRepository;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;

    public TeamRespDTO createTeam(String username, TeamReqDTO teamReqDTO) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Team team = teamMapper.toEntity(teamReqDTO);
        team.setOwner(owner);

        return teamMapper.toDTO(teamRepository.save(team));
    }

    public List<TeamRespDTO> getTeamsByOwner(String username) {
        return teamRepository.findByOwnerUsernameAndIsDeletedFalse(username).stream()
                .map(teamMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public TeamRespDTO updateTeam(Long teamId, TeamReqDTO updatedTeam) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));


        existingTeam.setName(updatedTeam.getName());
        existingTeam.setBalance(updatedTeam.getBalance());
        existingTeam.setCommissionPercentage(updatedTeam.getCommissionPercentage());

        return teamMapper.toDTO(existingTeam);
    }


    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("Team not found with ID: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }


    public TeamRespDTO removeTeam(Long teamId) {
        Team existingTeam = teamRepository.findByIdAndIsDeletedFalse(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));

        existingTeam.setIsDeleted(true);
        teamRepository.save(existingTeam);
        return teamMapper.toDTO(existingTeam);
    }
}