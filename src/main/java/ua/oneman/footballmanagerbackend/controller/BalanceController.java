package ua.oneman.footballmanagerbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserBalanceUpdateReqDTO;
import ua.oneman.footballmanagerbackend.service.BalanceService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        BigDecimal balance = balanceService.getBalance(userId, authentication);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{userId}/balance/increase")
    public ResponseEntity<BigDecimal> increaseBalance(
            @PathVariable Long userId,
            @Valid @RequestBody UserBalanceUpdateReqDTO balanceUpdateReqDTO,
            Authentication authentication
    ) {
        BigDecimal updatedBalance = balanceService.increaseBalance(userId, balanceUpdateReqDTO.getAmount(), authentication);
        return ResponseEntity.ok(updatedBalance);
    }

    @PostMapping("/{userId}/balance/decrease")
    public ResponseEntity<BigDecimal> decreaseBalance(
            @PathVariable Long userId,
            @Valid @RequestBody UserBalanceUpdateReqDTO balanceUpdateReqDTO,
            Authentication authentication
    ) {
        BigDecimal updatedBalance = balanceService.decreaseBalance(userId, balanceUpdateReqDTO.getAmount(), authentication);
        return ResponseEntity.ok(updatedBalance);
    }
}