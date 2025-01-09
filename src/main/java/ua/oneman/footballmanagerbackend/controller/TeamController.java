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
    // TODO: get username from jwt token change when setup sec
    @PostMapping("/{username}")
    public ResponseEntity<TeamRespDTO> createTeam(
            @PathVariable String username,
            @Valid @RequestBody TeamReqDTO teamReqDTO) {
        return ResponseEntity.ok(teamService.createTeam(username, teamReqDTO));
    }
    // TODO: get username from jwt token change when setup sec
    @GetMapping("/{username}")
    public ResponseEntity<List<TeamRespDTO>> getTeamsByOwner(@PathVariable String username) {
        return ResponseEntity.ok(teamService.getTeamsByOwner(username));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamRespDTO> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamReqDTO teamReqDTO) {
        return ResponseEntity.ok(teamService.updateTeam(teamId, teamReqDTO));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{teamId}/soft-delete")
    public ResponseEntity<TeamRespDTO> softDeleteTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.removeTeam(teamId));
    }

    
}