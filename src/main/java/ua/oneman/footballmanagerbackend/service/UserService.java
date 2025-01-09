package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.exception.UserNotFoundException;
import ua.oneman.footballmanagerbackend.mapper.UserMapper;
import ua.oneman.footballmanagerbackend.model.User;
import ua.oneman.footballmanagerbackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User getOrCreateUser(Authentication authentication) {
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .filter(user -> !user.getIsDeleted())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);

                    String firstName = (String) authentication.getDetails();
                    String lastName = "...";
                    String email = "...";

                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setEmail(email);
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
    public Object getUserInfo(Long userId, Authentication authentication) {
        String currentUsername = authentication.getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (user.getUsername().equals(currentUsername)) {
            return userMapper.toPrivateDTO(user);
        }

        return userMapper.toPublicDTO(user);
    }
}