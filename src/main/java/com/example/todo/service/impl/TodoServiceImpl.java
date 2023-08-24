package com.example.todo.service.impl;

import com.example.todo.dto.GetTodoResponse;
import com.example.todo.entity.Todo;
import com.example.todo.exception.BadRequestException;
import com.example.todo.exception.NotFoundException;
import com.example.todo.repository.TodoRepository;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public List<GetTodoResponse> getAllTodos() {
        List<Todo> todoList = todoRepository.findAll();
        List<GetTodoResponse> responseList = new ArrayList<>();
        for (Todo todo : todoList) {
            responseList.add(
                    GetTodoResponse
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
    public GetTodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Todo not found"));

        return GetTodoResponse
            .builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .description(todo.getDescription())
            .deadline(todo.getDeadline())
            .userId(todo.getUser().getId())
            .username(todo.getUser().getUsername())
            .build();
    }

    @Override
    public Todo createTodo(Todo todo) {
        String todoTitle = todo.getTitle();
        if (todoTitle == null) {
            throw new BadRequestException("할 일을 적어주세요.");
        }
        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Long id, Todo todo) {
        Optional<Todo> todoOptional = todoRepository.findById(id);

        if (todoOptional.isEmpty()) {
            throw new NotFoundException("id 없음");
        } else {
            if (todo.getTitle() == null) {
                throw new BadRequestException("타이틀 없음");
            } else {
                Todo existingTodo = todoOptional.get();
                existingTodo.setTitle(todo.getTitle());
                return todoRepository.save(existingTodo);
            }
        }
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