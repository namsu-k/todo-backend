package com.example.todo.Service;

import com.example.todo.entity.Todo;
import com.example.todo.exception.BadRequestException;
import com.example.todo.exception.NotFoundException;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getTodoById(Long id) {
        Todo todoOptional = todoRepository.findById(id).orElse(null);
        if(todoOptional == null) {
            throw new NotFoundException("Id를 찾을 수 없습니다.");
        }
         return todoOptional;
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