package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.exception.UserNotFoundException;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceService {

    private final UserRepository userRepository;

    /**
     * Retrieves the current balance of a user by their ID (with authentication check).
     *
     * @param userId         User's ID
     * @param authentication Authentication object
     * @return the current balance
     */
    public BigDecimal getBalance(Long userId, Authentication authentication) {
        validateSelfAccess(userId, authentication);
        User user = getUserById(userId);
        return user.getBalance();
    }

    /**
     * Increases the balance of a user.
     *
     * @param userId         User's ID
     * @param amount         the amount to increase
     * @param authentication Authentication object
     * @return the updated balance
     */
    public BigDecimal increaseBalance(Long userId, BigDecimal amount, Authentication authentication) {
        validateSelfAccess(userId, authentication);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than 0.");
        }

        User user = getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user); // Save changes
        return user.getBalance();
    }

    /**
     * Decreases the balance of a user.
     *
     * @param userId         User's ID
     * @param amount         the amount to decrease
     * @param authentication Authentication object
     * @return the updated balance
     */
    public BigDecimal decreaseBalance(Long userId, BigDecimal amount, Authentication authentication) {
        validateSelfAccess(userId, authentication);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than 0.");
        }

        User user = getUserById(userId);
        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds on the balance.");
        }

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
        return user.getBalance();
    }

    /**
     * Verifies the correspondence of the user to the authentication data.
     *
     * @param userId         User's ID
     * @param authentication Authentication object
     */
    private void validateSelfAccess(Long userId, Authentication authentication) {
        User user = getUserById(userId);
        String currentUsername = authentication.getName(); // Username from the token

        if (!user.getUsername().equals(currentUsername)) {
            throw new SecurityException("Access denied. You can only manage your own balance.");
        }
    }

    /**
     * Retrieves a user by their ID or throws an exception if not found.
     *
     * @param userId User's ID
     * @return the user object
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found."));
    }




}