package fr.gassiotlepipe.devoir.todo;

import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@Qualifier("Todos")
public class TodoJPAService implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            return todo.get();
        } else {
            throw new ResourceNotFoundException("Todo", id);
        }
    }

    @Override
    public Todo create(Todo newTodo) throws ResourceAlreadyExistsException {
        if (todoRepository.existsById(newTodo.getId())) {
            throw new ResourceAlreadyExistsException("Todo", newTodo.getId());
        }
        return todoRepository.save(newTodo);
    }

    @Override
    public void update(Long id, Todo updatedTodo) throws ResourceNotFoundException {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        updatedTodo.setId(id);  // Ensure the ID is set correctly
        todoRepository.save(updatedTodo);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", id);
        }
        todoRepository.deleteById(id);
    }
}
