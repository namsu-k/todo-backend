package com.example.todo.load;

import com.example.todo.entity.Role;
import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final TodoRepository todoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String ...args) throws Exception {
        User testUser = User.builder()
                .name("test")
                .role(Role.USER)
                .email("test@email.com")
                .username("test")
                .password(passwordEncoder.encode("1234"))
                .build();

        userRepository.save(testUser);
        User savedTestUser = userRepository.findByUsername("test")
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        for (int i = 0; i < 10; i++) {
            Todo todo = Todo.builder()
                .title("Test Todo " + (i + 1))
                .completed((i % 2 == 0) ? true : false)
                .deadline(LocalDateTime.now().plusDays(2))
                .description("test Description " + (i + 1))
                .user(savedTestUser)
                .build();

            todoRepository.save(todo);
        }
    }


}
