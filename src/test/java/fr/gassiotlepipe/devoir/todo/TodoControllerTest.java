package fr.gassiotlepipe.devoir.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gassiotlepipe.devoir.exceptions.ExceptionHandlingAdvice;
import fr.gassiotlepipe.devoir.exceptions.ResourceAlreadyExistsException;
import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = TodoController.class)
@Import(ExceptionHandlingAdvice.class)
public class TodoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>() {{
            add(new Todo(1L, "Machin", false));
            add(new Todo(2L, "Chose", true));
            add(new Todo(3L, "Truc", true));
            add(new Todo(14L, "higher", false));
            add(new Todo(7L, "lower", true));
            add(new Todo(28L, "way higher", false));
        }};
        when(todoService.getAll()).thenReturn(todos);
        when(todoService.getById(7L)).thenReturn(todos.get(4));
        when(todoService.getById(49L)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void whenGettingAll_shouldGet6_andBe200() throws Exception {
        mockMvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasSize(6))
        ).andDo(print());
    }

    @Test
    void whenGettingId7L_shouldReturnSame() throws Exception{
        mockMvc.perform(get("/todos/7")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.id", is(7))
        ).andExpect(jsonPath("$.nom", is("lower"))
        ).andExpect(jsonPath("$.statut", is(true))
        ).andReturn();
    }

    @Test
    void whenGettingUnexistingId_should404() throws Exception {
        mockMvc.perform(get("/todos/49")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()
        ).andDo(print());
    }

    @Test
    void whenCreatingNew_shouldReturnLink_andShouldBeStatusCreated() throws Exception {
        Todo new_todo = new Todo(89L, "nouveau", false);
        ArgumentCaptor<Todo> todo_received = ArgumentCaptor.forClass(Todo.class);
        when(todoService.create(any())).thenReturn(new_todo);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new_todo))
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location", "/todos/"+new_todo.getId())
        ).andDo(print());

        verify(todoService).create(todo_received.capture());
        assertEquals(new_todo, todo_received.getValue());
    }

    @Test
    void whenCreatingWithExistingId_should404() throws Exception {
        when(todoService.create(any())).thenThrow(ResourceAlreadyExistsException.class);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(this.todos.get(2)))
        ).andExpect(status().isConflict()
        ).andDo(print());
    }

    @Test
    void whenUpdating_shouldReceivetodoToUpdate_andReturnNoContent() throws Exception {
        Todo initial_todo = todos.get(1);
        Todo updated_todo = new Todo(initial_todo.getId(), "updated", false);
        ArgumentCaptor<Todo> todo_received = ArgumentCaptor.forClass(Todo.class);

        mockMvc.perform(put("/todos/"+initial_todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updated_todo))
        ).andExpect(status().isNoContent());

        verify(todoService).update(anyLong(), todo_received.capture());
        assertEquals(updated_todo, todo_received.getValue());
    }

    @Test
    void whenDeletingExisting_shouldCallServiceWithCorrectId_andSendNoContent() throws Exception {
        Long id = 28L;

        mockMvc.perform(delete("/todos/"+id)
        ).andExpect(status().isNoContent()
        ).andDo(print());

        ArgumentCaptor<Long> id_received = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(todoService).delete(id_received.capture());
        assertEquals(id, id_received.getValue());
    }
}
