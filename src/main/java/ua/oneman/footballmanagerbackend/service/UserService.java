package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.exception.UserNotFoundException;
import ua.oneman.footballmanagerbackend.mapper.UserMapper;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CognitoUserInfoService cognitoUserInfoService;

    @Transactional
    public User getOrCreateUser(Authentication authentication) {
        String accessToken = getAccessToken(authentication);
        String username = extractUsernameFromJwt(accessToken);

        Map<String, Object> userInfo = null;
        try {
            userInfo = cognitoUserInfoService.getUserInfo(accessToken);
        } catch (Exception e) {
            System.err.println("Failed to get user info from Cognito: " + e.getMessage());
        }

        String firstName = userInfo != null && userInfo.containsKey("given_name")
                ? (String) userInfo.get("given_name")
                : "Default First Name";

        String lastName = userInfo != null && userInfo.containsKey("family_name")
                ? (String) userInfo.get("family_name")
                : "Default Last Name";

        String email = userInfo != null && userInfo.containsKey("email")
                ? (String) userInfo.get("email")
                : "default@example.com";

        return userRepository.findByUsername(username)
                .filter(user -> !user.getIsDeleted())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setEmail(email);
                    newUser.setRegistrationDate(LocalDateTime.now());
                    return userRepository.save(newUser);
                });
    }

    @Transactional
    public PrivateUserRespDTO updateUser(Long userId, UserUpdateReqDTO userUpdateReqDTO, Authentication authentication) {
        User user = validateSelfAccess(userId, authentication);

        userMapper.updateEntity(user, userUpdateReqDTO);

        User updatedUser = userRepository.save(user);
        return userMapper.toPrivateDTO(updatedUser);
    }

    @Transactional
    public List<PrivateUserRespDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toPrivateDTO)
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId, Authentication authentication) {
        User user = validateSelfAccess(userId, authentication);
        userRepository.delete(user);
    }

    @Transactional
    public void softDeleteUser(Long userId, Authentication authentication) {
        User user = validateSelfAccess(userId, authentication);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    private User validateSelfAccess(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        String currentUsername = authentication.getName();
        if (!user.getUsername().equals(currentUsername)) {
            throw new SecurityException("Access denied. You can only manage your own data.");
        }

        return user;
    }

    @Transactional
    public Object getUserInfo(String username, Authentication authentication) {
        String currentUsername = extractUsernameFromJwt(getAccessToken(authentication));

        if (username.equals(currentUsername)) {
            return userMapper.toPrivateDTO(getOrCreateUser(authentication));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return userMapper.toPublicDTO(user);
    }

    @Transactional
    public Object getPublicUserInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapper.toPublicDTO(user);
    }

    @Transactional
    public Object getUserInfoByUsername(String username, Authentication authentication) {
        String currentUsername = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (user.getUsername().equals(currentUsername)) {
            return userMapper.toPrivateDTO(user);
        }

        return userMapper.toPublicDTO(user);
    }

    @Transactional
    public PrivateUserRespDTO updateUserByUsername(String username, UserUpdateReqDTO userUpdateReqDTO, Authentication authentication) {
        User user = validateSelfAccessByUsername(username, authentication);

        userMapper.updateEntity(user, userUpdateReqDTO);

        User updatedUser = userRepository.save(user);
        return userMapper.toPrivateDTO(updatedUser);
    }

    @Transactional
    public void deleteUserByUsername(String username, Authentication authentication) {
        User user = validateSelfAccessByUsername(username, authentication);
        userRepository.delete(user);
    }

    @Transactional
    public void softDeleteUserByUsername(String username, Authentication authentication) {
        User user = validateSelfAccessByUsername(username, authentication);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    private User validateSelfAccessByUsername(String username, Authentication authentication) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        String currentUsername = authentication.getName();
        if (!user.getUsername().equals(currentUsername)) {
            throw new SecurityException("Access denied. You can only manage your own data.");
        }

        return user;
    }

    private String getAccessToken(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String tokenValue = jwtToken.getToken().getTokenValue();
            return tokenValue;
        }

        throw new IllegalStateException("Access Token not found in Authentication.");
    }

    private String extractUsernameFromJwt(String accessToken) {
        if (accessToken == null || accessToken.split("\\.").length != 3) {
            throw new IllegalArgumentException("JWT token has incorrect structure");
        }

        try {
            String[] parts = accessToken.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

            Map<String, Object> claims = new com.fasterxml.jackson.databind.ObjectMapper().readValue(payload, Map.class);

            if (claims.containsKey("username")) {
                return (String) claims.get("username");
            } else if (claims.containsKey("sub")) {
                return (String) claims.get("sub");
            }

            throw new IllegalStateException("Field 'username' not found in token");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse JWT token: " + e.getMessage(), e);
        }
    }
}