package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.TransferRequestReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.TransferRequestRespDTO;
import ua.oneman.footballmanagerbackend.exception.PlayerNotFoundException;
import ua.oneman.footballmanagerbackend.exception.TeamNotFoundException;
import ua.oneman.footballmanagerbackend.exception.UserNotFoundException;
import ua.oneman.footballmanagerbackend.mapper.TransferRequestMapper;
import ua.oneman.footballmanagerbackend.model.*;
import ua.oneman.footballmanagerbackend.repository.PlayerRepository;
import ua.oneman.footballmanagerbackend.repository.TeamRepository;
import ua.oneman.footballmanagerbackend.repository.TransferRequestRepository;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferRequestService {

    private final TransferRequestRepository transferRequestRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TransferRequestMapper transferRequestMapper;

    @Transactional
    public TransferRequestRespDTO createTransferRequest(TransferRequestReqDTO requestDTO, Authentication authentication) {
        User buyer = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Buyer not found"));

        Player player = playerRepository.findById(requestDTO.getPlayerId())
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        Team fromTeam = teamRepository.findById(requestDTO.getFromTeamId())
                .orElseThrow(() -> new TeamNotFoundException("Source team not found"));

        Team toTeam = teamRepository.findById(requestDTO.getToTeamId())
                .orElseThrow(() -> new TeamNotFoundException("Destination team not found"));

        BigDecimal transferFee = requestDTO.getTransferFee();
        if (toTeam.getBalance().compareTo(transferFee) < 0) {
            throw new IllegalArgumentException("Insufficient balance in destination team.");
        }

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPlayer(player);
        transferRequest.setFromTeam(fromTeam);
        transferRequest.setToTeam(toTeam);
        transferRequest.setTransferFee(transferFee);
        transferRequest.setBuyer(buyer);
        transferRequest.setSeller(fromTeam.getOwner());
        transferRequest.setStatus(TransferStatus.PENDING);
        transferRequest.setRequestedAt(LocalDateTime.now());

        TransferRequest savedRequest = transferRequestRepository.save(transferRequest);
        return transferRequestMapper.toDto(savedRequest);
    }

    @Transactional
    public TransferRequestRespDTO approveTransfer(Long transferId, Authentication authentication) {
        TransferRequest transferRequest = transferRequestRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer request not found"));

        if (!transferRequest.getBuyer().getUsername().equals(authentication.getName())) {
            throw new SecurityException("Only buyer can approve the transfer.");
        }

        transferRequest.setStatus(TransferStatus.APPROVED);
        transferRequest.setDecisionAt(LocalDateTime.now());

        Team toTeam = transferRequest.getToTeam();
        Team fromTeam = transferRequest.getFromTeam();

        toTeam.setBalance(toTeam.getBalance().subtract(transferRequest.getTransferFee()));
        fromTeam.setBalance(fromTeam.getBalance().add(transferRequest.getTransferFee()));

        transferRequest.getPlayer().setTeam(toTeam);

        transferRequestRepository.save(transferRequest);

        return transferRequestMapper.toDto(transferRequest);
    }

    @Transactional
    public TransferRequestRespDTO rejectTransfer(Long transferId, Authentication authentication) {
        TransferRequest transferRequest = transferRequestRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer request not found"));

        if (!transferRequest.getBuyer().getUsername().equals(authentication.getName())) {
            throw new SecurityException("Only buyer can reject the transfer.");
        }

        transferRequest.setStatus(TransferStatus.REJECTED);
        transferRequest.setDecisionAt(LocalDateTime.now());

        return transferRequestMapper.toDto(transferRequest);
    }
}