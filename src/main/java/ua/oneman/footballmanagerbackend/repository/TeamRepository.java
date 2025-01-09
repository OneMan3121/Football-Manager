package ua.oneman.footballmanagerbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.oneman.footballmanagerbackend.model.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {


    List<Team> findByOwnerUsername(String ownerUsername);


    Team findByName(String name);

    List<Team> findByOwnerUsernameAndIsDeletedFalse(String username);


    Optional<Team> findByIdAndIsDeletedFalse(Long teamId);
}