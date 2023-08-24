package com.example.todo.service;

import com.example.todo.dto.UpdateMyProfileRequest;
import com.example.todo.entity.User;

public interface UserService {

    User getMyProfile();
    User updateMyProfile(UpdateMyProfileRequest request);
}
