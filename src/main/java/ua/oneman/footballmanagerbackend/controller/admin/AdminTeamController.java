package ua.oneman.footballmanagerbackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.resp.admin.AdminTeamRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.service.TeamService;

@RestController
@RequestMapping("/api/v1/admin/teams")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<Page<AdminTeamRespDTO>> getAllTeams(Pageable pageable) {
        Page<AdminTeamRespDTO> teams = teamService.getAllTeamsByAdmin(pageable);
        return ResponseEntity.ok(teams);
    }

    @PostMapping
    public ResponseEntity<AdminTeamRespDTO> createTeam(@RequestParam String name,
                                                        @RequestParam String ownerUsername) {
        AdminTeamRespDTO newTeam = teamService.createTeam(name, ownerUsername);
        return ResponseEntity.ok(newTeam);
    }

    @PutMapping("/{teamName}")
    public ResponseEntity<AdminTeamRespDTO> updateTeam(@PathVariable String teamName,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) Long ownerId) {
        // Виклик методу сервісу
        AdminTeamRespDTO updatedTeam = teamService.updateTeam(teamName, name, ownerId);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{teamName}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String TeamName) {
        teamService.deleteTeam(TeamName);
        return ResponseEntity.noContent().build();
    }

}