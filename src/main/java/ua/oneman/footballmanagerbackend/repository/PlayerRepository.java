package ua.oneman.footballmanagerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.oneman.footballmanagerbackend.model.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamId(Long teamId);

    List<Player> findByTeamIdAndIsDeletedFalse(Long teamId);

    List<Player> findByIsDeletedFalse();
}