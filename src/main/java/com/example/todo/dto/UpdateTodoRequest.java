package com.example.todo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTodoRequest {
    private Long id;
    private String title;
    private boolean completed;
    private String description;
    private LocalDateTime deadline;
    private Long userId;
    private String username;
}
