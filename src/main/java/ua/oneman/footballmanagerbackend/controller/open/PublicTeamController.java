package ua.oneman.footballmanagerbackend.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicTeamRespDTO;
import ua.oneman.footballmanagerbackend.service.TeamService;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class PublicTeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<Page<PublicTeamRespDTO>> getAllTeams(Pageable pageable) {
        Page<PublicTeamRespDTO> teams = teamService.getAllTeams(pageable);
        return ResponseEntity.ok(teams);
    }
}