package fr.gassiotlepipe.devoir.todo;

import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TodoServiceTest {

    private TodoService todoService;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L, "Todo1", false));
            add(new Todo(2L, "Todo2", true));
            add(new Todo(3L, "Todo3", true));
        }};
        todoService = new TodoLocalService(todos);
    }

    @Test
    void whenGettingAll_shouldReturn3() {
        assertEquals(3, todoService.getAll().size());
    }

    @Test
    void whenGettingById_shouldReturnIfExists_andBeTheSame() {
        assertAll(
                () -> assertEquals(todos.get(0), todoService.getById(1L)),
                () -> assertEquals(todos.get(2), todoService.getById(3L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> todoService.getById(12L)),
                () -> assertThrows(ResourceNotFoundException.class, () -> todoService.getById(4L))
        );
    }

    @Test
    void whenCreating_ShouldReturnSame() {
        Todo toCreate = new Todo(5L, "Todo5", true);

        assertEquals(toCreate, todoService.create(toCreate));
    }

    @Test
    void whenCreatingWithSameId_shouldReturnEmpty() {
        Todo same_todo = todos.get(0);

        assertThrows(ResourceAlreadyExistsException.class, ()->todoService.create(same_todo));
    }

    @Test
    void whenUpdating_shouldModifyUser() {
        Todo initial_todo = todos.get(2);
        Todo new_todo = new Todo(initial_todo.getId(), "Updaté", initial_todo.isStatut());

        todoService.update(new_todo.getId(), new_todo);
        Todo updated_todo = todoService.getById(initial_todo.getId());
        assertEquals(new_todo, updated_todo);
        assertTrue(todoService.getAll().contains(new_todo));
    }

    @Test
    void whenUpdatingNonExisting_shouldThrowException() {
        Todo todo = todos.get(2);

        assertThrows(ResourceNotFoundException.class, ()->todoService.update(75L, todo));
    }

    @Test
    void whenDeletingExistingUser_shouldNotBeInUsersAnymore() {
        Todo todo = todos.get(1);
        Long id = todo.getId();

        todoService.delete(id);
        assertFalse(todoService.getAll().contains(todo));
    }

    @Test
    void whenDeletingNonExisting_shouldThrowException() {
        Long id = 68L;

        assertThrows(ResourceNotFoundException.class, ()->todoService.delete(id));
    }

}
