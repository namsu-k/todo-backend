package com.example.todo.load;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final TodoRepository todoRepository;

    public DataLoader(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String ...args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Todo todo = Todo.builder()
                .title("Test Todo " + (i + 1))
                .build();
            todoRepository.save(todo);
        }
    }


}
