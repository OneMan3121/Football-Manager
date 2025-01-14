package ua.oneman.footballmanagerbackend.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.oneman.footballmanagerbackend.model.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {


    Optional<Team> findByNameAndIsDeletedFalse(String name);

    Page<Team> findAllByIsDeletedFalse(Pageable pageable);



}