package ua.oneman.footballmanagerbackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // Захист на рівні класу: доступ тільки для адміністраторів
public class AdminUserController {

    private final UserService userService;

    /**
     * Отримати приватний профіль користувача (доступ тільки для адміністратора).
     * @param username - ім'я користувача
     * @return DTO з приватною інформацією користувача
     */
    @GetMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> getUserProfile(@PathVariable String username) {
        PrivateUserRespDTO userProfile = userService.getUserProfileByAdmin(username);
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Оновлення інформації користувача (доступ тільки для адміністратора).
     * @param username - ім'я користувача
     * @param userUpdateReqDTO - DTO з новими даними
     * @return DTO з оновленою інформацією про користувача
     */
    @PatchMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateReqDTO userUpdateReqDTO
    ) {
        PrivateUserRespDTO updatedUser = userService.updateUserByUsername(username, userUpdateReqDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Видалення облікового запису користувача (доступ тільки для адміністратора).
     * @param username - ім'я користувача
     * @return DTO з інформацією про видаленого користувача
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> deleteUser(@PathVariable String username) {
        PrivateUserRespDTO deletedUser = userService.deleteUserByUsername(username);
        return ResponseEntity.ok(deletedUser);
    }
}