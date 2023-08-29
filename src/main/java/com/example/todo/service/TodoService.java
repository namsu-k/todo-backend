package com.example.todo.service;

import com.example.todo.dto.CreateTodoRequest;
import com.example.todo.dto.TodoResponse;
import com.example.todo.dto.UpdateTodoRequest;
import com.example.todo.entity.Todo;

import java.util.List;

public interface TodoService {
    List<TodoResponse> getAllTodos();
    TodoResponse getTodoById(Long id);
    Todo createTodo(CreateTodoRequest request);
    TodoResponse updateTodo(Long id, UpdateTodoRequest request);
    void deleteTodo(Long id);
    void deleteAllTodos();
}
