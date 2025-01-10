package ua.oneman.footballmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.exception.UserNotFoundException;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<Object> getUserInfo(
            @PathVariable String username,
            Authentication authentication
    ) {
        try {
            Object userInfo = userService.getUserInfo(username, authentication);
            return ResponseEntity.ok(userInfo);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve user information.");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateReqDTO userUpdateReqDTO,
            Authentication authentication
    ) {
        PrivateUserRespDTO updatedUser = userService.updateUserByUsername(username, userUpdateReqDTO, authentication);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String username,
            Authentication authentication) {
        userService.deleteUserByUsername(username, authentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft-delete/{username}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable String username,
            Authentication authentication
    ) {
        userService.softDeleteUserByUsername(username, authentication);
        return ResponseEntity.noContent().build();
    }

}