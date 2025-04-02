package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.TaskDTO;
import com.nbnp.trip_to_go.dto.mapping.TaskMapper;
import com.nbnp.trip_to_go.dto.mapping.UserMapper;
import com.nbnp.trip_to_go.model.Task;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);


    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public void testToDTO() {
        Task task = new Task(1,"Book tickets","Buy flight tickets", LocalDateTime.of(2025, 3, 1, 10, 0), true, null, null);

        TaskDTO dto = taskMapper.toDTO(task);

        assertNotNull(dto);
        assertEquals("Book tickets", dto.taskName());
        assertEquals("Buy flight tickets", dto.taskDescription());
        assertEquals(LocalDateTime.of(2025, 3, 1, 10, 0), dto.taskDeadline());
        assertTrue(dto.isDone());

        // Перевірка user (повинен бути null)
        assertNull(dto.user());
    }

    @Test
    public void testToEntity() {
        TaskDTO dto = new TaskDTO(1,"Book tickets", "Buy flight tickets", LocalDateTime.of(2025, 3, 1, 10, 0), true, null);
        Task task = taskMapper.toEntity(dto);
        assertNotNull(task);
        assertEquals("Book tickets", task.getTaskName());
        assertEquals("Buy flight tickets", task.getTaskDescription());
        assertEquals(LocalDateTime.of(2025, 3, 1, 10, 0), task.getTaskDeadline());
        assertTrue(task.isDone());
    }
}
