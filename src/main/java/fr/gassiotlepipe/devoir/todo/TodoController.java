package fr.gassiotlepipe.devoir.todo;

import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public List<Todo> getAll() {
        return todoService.getAll();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable("id") Long id) {
        return todoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        try {
            Todo createdTodo = todoService.create(todo);
            return ResponseEntity.created(URI.create("/todos/" + createdTodo.getId())).body(createdTodo);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Todo todo) {
        todoService.update(id, todo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
