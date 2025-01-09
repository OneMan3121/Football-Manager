package ua.oneman.footballmanagerbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.oneman.footballmanagerbackend.model.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // Знаходимо всі команди одного користувача за username
    List<Team> findByOwnerUsername(String ownerUsername);

    // Знаходимо команду за її унікальною назвою
    Team findByName(String name);
}