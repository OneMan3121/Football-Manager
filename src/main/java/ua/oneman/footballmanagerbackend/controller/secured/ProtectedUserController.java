package ua.oneman.footballmanagerbackend.controller.secured;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/protected/users")
@RequiredArgsConstructor
public class ProtectedUserController {

    private final UserService userService;


    @GetMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> getUser(@PathVariable String username, Authentication authentication) {
        PrivateUserRespDTO userProfile = userService.getPrivateUserProfile(username, authentication);
        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<PrivateUserRespDTO> updateUser(
            @PathVariable String username,
            @RequestBody @Valid UserUpdateReqDTO userUpdateReqDTO,
            Authentication authentication
    ) {
        PrivateUserRespDTO updatedUser = userService.updateUserByUsername(username, userUpdateReqDTO, authentication);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable String username,
            Authentication authentication
    ) {
        userService.softDeleteUserByUsername(username, authentication);
        return ResponseEntity.noContent().build();
    }
}