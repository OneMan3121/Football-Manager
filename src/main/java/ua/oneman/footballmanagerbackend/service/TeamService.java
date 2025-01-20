package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.TeamReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.admin.AdminTeamRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.secured.PrivateTeamRespDTO;
import ua.oneman.footballmanagerbackend.exception.TeamNotFoundException;
import ua.oneman.footballmanagerbackend.exception.UsernameNotFoundException;
import ua.oneman.footballmanagerbackend.mapper.TeamMapper;
import ua.oneman.footballmanagerbackend.model.Team;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.repository.TeamRepository;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public Page<PrivateTeamRespDTO> getUserTeams(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return teamRepository.findAllByOwner(user, pageable)
                .map(teamMapper::toPrivateDTO);
    }

    @Transactional(readOnly = true)
    public PrivateTeamRespDTO getUserTeamByName(String username, String name) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з ім'ям " + username + " не знайдено."));

        Team team = teamRepository.findTeamByName(name)
                .filter(t -> t.getOwner().equals(user))
                .orElseThrow(() -> new SecurityException(name));

        return teamMapper.toPrivateDTO(team);
    }

    @Transactional
    public PrivateTeamRespDTO updateUserTeam(String username, String teamName, TeamReqDTO teamReqDTO) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Team team = teamRepository.findByNameAndIsDeletedFalse(teamName)
                .filter(t -> t.getOwner().equals(user))
                .orElseThrow(() -> new TeamNotFoundException(teamName));

        if (teamReqDTO.getName() != null && !teamReqDTO.getName().isBlank()) {
            team.setName(teamReqDTO.getName());
        }
        if (teamReqDTO.getLogoUrl() != null && !teamReqDTO.getLogoUrl().isBlank()) {
            team.setLogoUrl(teamReqDTO.getLogoUrl());
        }

        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toPrivateDTO(updatedTeam);
    }


    // --- admin service ---

    public AdminTeamRespDTO getTeamByNameByAdmin(String name) {
        return teamRepository.findTeamByName(name)
                .map(teamMapper::toAdminDTO)
                .orElseThrow(() -> new TeamNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public Page<AdminTeamRespDTO> getAllTeamsByAdmin(Pageable pageable) {
        return teamRepository.findAll(pageable)
                .map(teamMapper::toAdminDTO);
    }

    @Transactional
    public AdminTeamRespDTO createTeam(String teamName, String username) {
        User owner = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException(username + "create user before create team."));

        Team team = new Team();
        team.setName(teamName);
        team.setOwner(owner);
        team.setIsDeleted(false);

        Team savedTeam = teamRepository.save(team);
        return teamMapper.toAdminDTO(savedTeam);
    }

    @Transactional
    public AdminTeamRespDTO updateTeam(String teamName, String newName, Long newOwnerId) {

        Team team = teamRepository.findByNameAndIsDeletedFalse(teamName)
                .orElseThrow(() -> new TeamNotFoundException("Команду з ім'ям '" + teamName + "' не знайдено."));

        if (newName != null && !newName.isBlank()) {
            team.setName(newName);
        }

        if (newOwnerId != null) {
            User newOwner = userRepository.findById(newOwnerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Користувача з ID '" + newOwnerId + "' не знайдено."));
            team.setOwner(newOwner);
        }

        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toAdminDTO(updatedTeam);
    }

    @Transactional
    public void deleteTeam(String teamName) {
        Team team = teamRepository.findByNameAndIsDeletedFalse(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));

        team.setIsDeleted(true);

        teamRepository.save(team);
    }

}