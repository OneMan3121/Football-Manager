package ua.oneman.footballmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserInfo(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        // Забезпечуємо створення користувача при першому зверненні
        userService.getOrCreateUser(authentication);

        // Продовжуємо виконувати логіку для отримання інформації
        Object userInfo = userService.getUserInfo(userId, authentication);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Оновлення даних авторизованого користувача (лише для себе).
     * @param userId - ID користувача.
     * @param userUpdateReqDTO - Оновлення даних.
     * @param authentication - Дані авторизації.
     * @return Оновлений користувач у форматі DTO.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<PrivateUserRespDTO> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateReqDTO userUpdateReqDTO,
            Authentication authentication) {
        PrivateUserRespDTO updatedUser = userService.updateUser(userId, userUpdateReqDTO, authentication);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Видалення облікового запису авторизованим користувачем.
     * @param userId - ID користувача.
     * @param authentication - Дані авторизації.
     * @return HTTP 204 (No Content).
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            Authentication authentication) {
        userService.deleteUser(userId, authentication);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Soft-deletes a user account by marking it deleted instead of removing it.
     * @param userId - ID of the user to be soft deleted.
     * @param authentication - Authorization info.
     * @return HTTP 204 (No Content).
     */
    @DeleteMapping("/soft-delete/{userId}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable Long userId,
            Authentication authentication) {
        userService.softDeleteUser(userId, authentication);
        return ResponseEntity.noContent().build();
    }


}