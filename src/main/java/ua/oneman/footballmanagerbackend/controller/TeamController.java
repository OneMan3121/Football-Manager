package ua.oneman.footballmanagerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.TeamReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.TeamRespDTO;
import ua.oneman.footballmanagerbackend.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // Створення команди
    @PostMapping("/{username}")
    public ResponseEntity<TeamRespDTO> createTeam(
            @PathVariable String username,
            @Valid @RequestBody TeamReqDTO teamReqDTO) {
        return ResponseEntity.ok(teamService.createTeam(username, teamReqDTO));
    }

    // Отримання команд для конкретного користувача
    @GetMapping("/{username}")
    public ResponseEntity<List<TeamRespDTO>> getTeamsByOwner(@PathVariable String username) {
        return ResponseEntity.ok(teamService.getTeamsByOwner(username));
    }

    // Оновлення даних команди
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamRespDTO> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamReqDTO teamReqDTO) {
        return ResponseEntity.ok(teamService.updateTeam(teamId, teamReqDTO));
    }

    // Видалення команди
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}