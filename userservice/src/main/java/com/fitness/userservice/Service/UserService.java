package com.fitness.userservice.Service;

import com.fitness.userservice.DTO.RegisterRequest;
import com.fitness.userservice.DTO.UserResponse;
import com.fitness.userservice.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fitness.userservice.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    public UserResponse getUserProfile(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        UserResponse newResponse=new UserResponse();
        newResponse.setId(user.getId());
        newResponse.setEmail(user.getEmail());
        newResponse.setPassword(user.getPassword());
        newResponse.setLastName(user.getLastName());
        newResponse.setFirstName(user.getFirstName());
        newResponse.setCreatedAt(user.getCreatedAt());
        newResponse.setUpdatedAt(user.getUpdatedAt());
        return newResponse;
    }

    public UserResponse register(@Valid RegisterRequest request) {

        if(repository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user =new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        User savedUser=repository.save(user);
        UserResponse newResponse=new UserResponse();
        newResponse.setId(savedUser.getId());
        newResponse.setEmail(savedUser.getEmail());
        newResponse.setPassword(savedUser.getPassword());
        newResponse.setLastName(savedUser.getLastName());
        newResponse.setFirstName(savedUser.getFirstName());
        newResponse.setCreatedAt(savedUser.getCreatedAt());
        newResponse.setUpdatedAt(savedUser.getUpdatedAt());
        return newResponse;

    }

    public Boolean existByUserId(String userId) {
        return repository.existsById(userId);
    }
}
