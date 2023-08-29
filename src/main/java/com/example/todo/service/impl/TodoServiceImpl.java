package com.example.todo.service.impl;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.dto.UpdateTodoRequest;
import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.exception.BadRequestException;
import com.example.todo.exception.NotFoundException;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Override
    public List<TodoResponse> getAllTodos() {
        List<Todo> todoList = todoRepository.findAll();
        List<TodoResponse> responseList = new ArrayList<>();
        for (Todo todo : todoList) {
            responseList.add(
                    TodoResponse
                    .builder()
                    .title(todo.getTitle())
                    .description(todo.getDescription())
                    .completed(todo.isCompleted())
                    .deadline(todo.getDeadline())
                    .userId(todo.getUser().getId())
                    .id(todo.getId())
                    .username(todo.getUser().getUsername())
                    .build()
            );
        }
        return responseList;
    }

    @Override
    public TodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Todo not found"));

        return TodoResponse
            .builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .completed(todo.isCompleted())
            .description(todo.getDescription())
            .deadline(todo.getDeadline())
            .userId(todo.getUser().getId())
            .username(todo.getUser().getUsername())
            .build();
    }

    @Override
    public Todo createTodo(CreateTodoRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User nor found"));

        if (request.getTitle() == null) {
            throw new BadRequestException("title is wrong");
        }
        Todo newTodo = Todo.builder()
            .title(request.getTitle())
            .completed(false)
            .deadline(request.getDeadline())
            .description(request.getDescription())
            .user(user)
            .build();

        return todoRepository.save(newTodo);
    }

    @Override
    public TodoResponse updateTodo(Long id, UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Todo not found")
        );

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(
                () -> new NotFoundException("User not found")
            );

        Todo updateTodo = Todo.builder()
            .id(request.getId())
            .title(request.getTitle())
            .description(request.getDescription())
            .completed(request.isCompleted())
            .deadline(request.getDeadline())
            .user(user)
            .build();

        Todo savedTodo = todoRepository.save(updateTodo);

        return TodoResponse.builder()
            .id(savedTodo.getId())
            .title(savedTodo.getTitle())
            .completed(savedTodo.isCompleted())
            .deadline(savedTodo.getDeadline())
            .description(savedTodo.getDescription())
            .userId(savedTodo.getUser().getId())
            .username(savedTodo.getUser().getUsername())
            .build();

    }

    @Override
    public void deleteTodo(Long id) {

        Optional<Todo> todo = todoRepository.findById(id);
        if(todo.isEmpty()) {
            throw new NotFoundException("Id를 찾을 수 없습니다.");
        }
        todoRepository.deleteById(id);
    }

    @Override
    public void deleteAllTodos() {
        todoRepository.deleteAll();
    }

}