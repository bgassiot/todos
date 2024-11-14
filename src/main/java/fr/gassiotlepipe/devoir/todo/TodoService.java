package fr.gassiotlepipe.devoir.todo;

import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    List<Todo> getAll();

    Todo getById(Long l);

    Todo create(Todo newTodo) throws ResourceAlreadyExistsException;

    void update(Long id, Todo updatedTodo) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;
}
