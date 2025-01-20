package ua.oneman.footballmanagerbackend.controller.open;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.resp.open.PublicUserRespDTO;
import ua.oneman.footballmanagerbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<PublicUserRespDTO> getUser(@PathVariable String username) {
        PublicUserRespDTO userProfile = userService.getPublicUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }


    @GetMapping
    public ResponseEntity<Page<PublicUserRespDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PublicUserRespDTO> users = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(users);
    }

}
