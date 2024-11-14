package fr.gassiotlepipe.devoir;

import fr.gassiotlepipe.devoir.todo.Todo;
import fr.gassiotlepipe.devoir.todo.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TodosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodosApplication.class, args);
    }

    public CommandLineRunner setUpBDD(TodoRepository todoRepository) {
        return (args) -> {
            List<Todo> todos = List.of(new Todo(1l, "Todo1", false),
                    new Todo(2L, "Todo2", true),
                    new Todo(3L, "Todo3", false));

            todoRepository.saveAll(todos);
        };
    }
}
