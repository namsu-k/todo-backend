package com.example.todo.service.impl;

import com.example.todo.dto.GetMyProfileResponse;
import com.example.todo.dto.UpdateMyProfileRequest;
import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public GetMyProfileResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return GetMyProfileResponse.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .username(user.getUsername())
            .build();
    }

    @Override
    public User updateMyProfile(UpdateMyProfileRequest request) {
        return null;
    }
}
