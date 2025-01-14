package ua.oneman.footballmanagerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.oneman.footballmanagerbackend.dto.req.UserUpdateReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PrivateUserRespDTO;
import ua.oneman.footballmanagerbackend.dto.resp.PublicUserRespDTO;
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

    // --- public service ---
    public PublicUserRespDTO getPublicUserProfile(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));
        return userMapper.toPublicDTO(user);
    }

    public Page<PublicUserRespDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toPublicDTO);
    }


    // --- protected service ---


    public PrivateUserRespDTO getPrivateUserProfile(String username, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            throw new IllegalArgumentException("Access denied: Username does not match authentication name.");
        }

        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));
        return userMapper.toPrivateDTO(user);
    }

    public PrivateUserRespDTO updateUserByUsername(String username, UserUpdateReqDTO userUpdateReqDTO, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            throw new IllegalArgumentException("Access denied: Username does not match authentication name.");
        }

        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));

        userMapper.updateEntity(user, userUpdateReqDTO);

        userRepository.save(user);

        // Повертаємо результат (змінений приватний профіль)
        return userMapper.toPrivateDTO(user);
    }

    public void softDeleteUserByUsername(String username, Authentication authentication) {
        if (!username.equals(authentication.getName())) {
            throw new IllegalArgumentException("Access denied: Username does not match authentication name.");
        }

        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));
        user.setIsDeleted(true);
        userRepository.save(user);
    }


    // --- admin service ---

    public PrivateUserRespDTO getUserProfileByAdmin(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.toPrivateDTO(user);
    }


    public PrivateUserRespDTO deleteUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));
        userRepository.delete(user);

        return userMapper.toPrivateDTO(user);
    }

    public PrivateUserRespDTO updateUserByUsername(String username, UserUpdateReqDTO userUpdateReqDTO) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username).orElseThrow(() -> new UserNotFoundException(username));
        userMapper.updateEntity(user, userUpdateReqDTO);
        return userMapper.toPrivateDTO(user);
    }


    // --- additional methods ---


}