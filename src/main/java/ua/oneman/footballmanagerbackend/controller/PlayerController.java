package ua.oneman.footballmanagerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.PlayerReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PlayerRespDTO;
import ua.oneman.footballmanagerbackend.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/protected/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/{teamId}")
    public ResponseEntity<PlayerRespDTO> createPlayer(
            @PathVariable Long teamId,
            @Valid @RequestBody PlayerReqDTO playerReqDTO) {
        return ResponseEntity.ok(playerService.createPlayer(teamId, playerReqDTO));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<List<PlayerRespDTO>> getPlayersByTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(playerService.getPlayersByTeam(teamId));
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerRespDTO> updatePlayer(
            @PathVariable Long playerId,
            @Valid @RequestBody PlayerReqDTO playerReqDTO) {
        return ResponseEntity.ok(playerService.updatePlayer(playerId, playerReqDTO));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{playerId}/soft-delete")
    public ResponseEntity<PlayerRespDTO> softDeletePlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerService.softDeletePlayer(playerId));
    }

    @PatchMapping("/{playerId}/transfer/{buyerTeamId}")
    public ResponseEntity<Void> transferPlayer(
            @PathVariable Long playerId,
            @PathVariable Long buyerTeamId,
            Authentication authentication) {
        playerService.transferPlayer(playerId, buyerTeamId, authentication);
        return ResponseEntity.ok().build();
    }
}