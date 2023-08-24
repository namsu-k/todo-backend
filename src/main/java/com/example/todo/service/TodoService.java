package com.example.todo.service;

import com.example.todo.dto.GetTodoResponse;
import com.example.todo.entity.Todo;

import java.util.List;

public interface TodoService {
    List<GetTodoResponse> getAllTodos();
    GetTodoResponse getTodoById(Long id);
    Todo createTodo(Todo todo);
    Todo updateTodo(Long id, Todo todo);
    void deleteTodo(Long id);
    void deleteAllTodos();
}
