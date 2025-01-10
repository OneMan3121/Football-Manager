package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.PlayerReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PlayerRespDTO;
import ua.oneman.footballmanagerbackend.mapper.PlayerMapper;
import ua.oneman.footballmanagerbackend.model.Player;
import ua.oneman.footballmanagerbackend.model.Team;
import ua.oneman.footballmanagerbackend.repository.PlayerRepository;
import ua.oneman.footballmanagerbackend.repository.TeamRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    public PlayerRespDTO createPlayer(Long teamId, PlayerReqDTO playerReqDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));

        Player player = playerMapper.toEntity(playerReqDTO);
        player.setTeam(team);

        return playerMapper.toDTO(playerRepository.save(player));
    }

    public List<PlayerRespDTO> getPlayersByTeam(Long teamId) {
        return playerRepository.findByTeamIdAndIsDeletedFalse(teamId)
                .stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlayerRespDTO updatePlayer(Long playerId, PlayerReqDTO playerReqDTO) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        player.setFirstName(playerReqDTO.getFirstName());
        player.setLastName(playerReqDTO.getLastName());
        player.setAge(playerReqDTO.getAge());
        player.setExperienceMonths(playerReqDTO.getExperienceMonths());

        return playerMapper.toDTO(playerRepository.save(player));
    }

    @Transactional
    public void deletePlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        playerRepository.delete(player);
    }

    @Transactional
    public PlayerRespDTO softDeletePlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        player.setIsDeleted(true);
        player.setDeletedAt(LocalDateTime.now());

        return playerMapper.toDTO(playerRepository.save(player));
    }

    @Transactional
    public void transferPlayer(Long playerId, Long buyerTeamId, Authentication authentication) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        String ownerUsername = authentication.getName();

        Team sellerTeam = player.getTeam();
        if (sellerTeam == null) {
            throw new IllegalArgumentException("Player is not assigned to any team.");
        }

        Team buyerTeam = teamRepository.findById(buyerTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer team not found with ID: " + buyerTeamId));

        if (!buyerTeam.getOwner().getUsername().equals(ownerUsername)) {
            throw new SecurityException("You are not the owner of the buying team.");
        }

        if (sellerTeam.getOwner().getUsername().equals(ownerUsername)) {
            throw new IllegalArgumentException("You cannot buy from a team that you own.");
        }

        BigDecimal transferValue = calculateTransferValue(player);

        BigDecimal commissionValue = transferValue.multiply(BigDecimal.valueOf(sellerTeam.getCommissionPercentage() / 100));
        BigDecimal totalCost = transferValue.add(commissionValue);

        if (buyerTeam.getBalance().compareTo(totalCost) < 0) {
            throw new IllegalArgumentException("Buyer team has insufficient funds for this transfer.");
        }

        buyerTeam.setBalance(buyerTeam.getBalance().subtract(totalCost));
        sellerTeam.setBalance(sellerTeam.getBalance().add(totalCost));

        player.setTeam(buyerTeam);

        playerRepository.save(player);
        teamRepository.save(sellerTeam);
        teamRepository.save(buyerTeam);
    }

    private BigDecimal calculateTransferValue(Player player) {
        if (player.getAge() <= 0) {
            throw new IllegalArgumentException("Player age must be greater than zero.");
        }

        return BigDecimal.valueOf(player.getExperienceMonths())
                .multiply(BigDecimal.valueOf(100000))
                .divide(BigDecimal.valueOf(player.getAge()), 2, RoundingMode.HALF_UP);
    }

}