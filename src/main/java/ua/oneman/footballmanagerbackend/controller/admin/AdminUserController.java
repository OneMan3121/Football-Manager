package ua.oneman.footballmanagerbackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.secured.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> getUserProfile(@PathVariable String username) {
        PrivateUserRespDTO userProfile = userService.getUserProfileByAdmin(username);
        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateReqDTO userUpdateReqDTO
    ) {
        PrivateUserRespDTO updatedUser = userService.updateUserByUsername(username, userUpdateReqDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> deleteUser(@PathVariable String username) {
        PrivateUserRespDTO deletedUser = userService.deleteUserByUsername(username);
        return ResponseEntity.ok(deletedUser);
    }
}