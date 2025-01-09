package ua.oneman.footballmanagerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.oneman.footballmanagerbackend.model.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Знаходимо гравців за командою
    List<Player> findByTeamId(Long teamId);
}