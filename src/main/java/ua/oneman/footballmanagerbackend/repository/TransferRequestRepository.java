package ua.oneman.footballmanagerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.oneman.footballmanagerbackend.model.TransferRequest;

public interface TransferRequestRepository extends JpaRepository<TransferRequest, Long> {
}