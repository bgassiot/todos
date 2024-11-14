package fr.gassiotlepipe.devoir.todo;

import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;
import fr.gassiotlepipe.devoir.utils.LocalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoLocalService extends LocalService<Todo, Long> implements TodoService {

    public TodoLocalService(){
        super();
    }

    public TodoLocalService(List<Todo> todos){
        super(todos);
    }

    @Override
    protected String getIdentifier() {
        return "id";
    }

    @Override
    public List<Todo> getAll() {
        return super.getAll();
    }

    @Override
    public Todo getById(Long id) {
        IndexAndValue<Todo> found = this.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException("Todo", id);
        }
        return found.value();
    }


    @Override
    public Todo create(Todo toCreate) throws ResourceAlreadyExistsException {
        try {
            this.findById(toCreate.getId());
            throw new ResourceAlreadyExistsException("Todo", toCreate.getId());
        } catch (ResourceNotFoundException e) {
            this.allValues.add(toCreate);
            return toCreate;
        }
    }

    @Override
    public void update(Long id, Todo updatedTodo) throws ResourceNotFoundException {
        IndexAndValue<Todo> found = this.findById(id);
        this.allValues.remove(found.index());
        this.allValues.add(found.index(), updatedTodo);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        IndexAndValue<Todo> found = this.findById(id);
        this.allValues.remove(found.index());
    }

    public IndexAndValue<Todo> findById(Long id) {
        return super.findByProperty(id);
    }
}
