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

    // Створення нової команди
    public TeamRespDTO createTeam(String username, TeamReqDTO teamReqDTO) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Team team = teamMapper.toEntity(teamReqDTO);
        team.setOwner(owner); // Додаємо власника команди

        return teamMapper.toDTO(teamRepository.save(team));
    }

    // Список команд
    public List<TeamRespDTO> getTeamsByOwner(String username) {
        return teamRepository.findByOwnerUsername(username).stream()
                .map(teamMapper::toDTO) // Перетворення команди у DTO
                .collect(Collectors.toList());
    }

    // Оновлення команди
    @Transactional
    public TeamRespDTO updateTeam(Long teamId, TeamReqDTO updatedTeam) {
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));

        // Оновлюємо поля
        existingTeam.setName(updatedTeam.getName());
        existingTeam.setBudget(updatedTeam.getBudget());
        existingTeam.setCommissionPercentage(updatedTeam.getCommissionPercentage());

        return teamMapper.toDTO(existingTeam);
    }

    // Видалення команди
    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("Team not found with ID: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }
}