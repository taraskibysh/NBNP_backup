package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.TaskDTO;
import com.nbnp.trip_to_go.dto.TaskFilterDTO;
import com.nbnp.trip_to_go.dto.TaskRequestDTO;
import com.nbnp.trip_to_go.dto.mapping.TaskMapper;
import com.nbnp.trip_to_go.dto.mapping.TaskRequestMapper;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Sex;
import com.nbnp.trip_to_go.model.Task;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TaskRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.service.TaskService;
import com.nbnp.trip_to_go.service.UserTripService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private TripRepository tripRepository;
    @Mock private AppUserRepository appUserRepository;
    @Mock private TaskMapper taskMapper;
    @Mock private TaskRequestMapper taskRequestMapper;
    @Mock private JwtService jwtService;
    @Mock private UserTripService userTripService;

    @InjectMocks private TaskService taskService;

    private Task task;
    private Trip trip;
    private AppUser user;
    private TaskRequestDTO taskRequestDTO;

    @BeforeEach
    void setUp() {
        trip = createTrip();
        user = createUser();
        task = createTask(trip, user);
        taskRequestDTO = new TaskRequestDTO("Visit museum", "Plan visit to Louvre", LocalDateTime.now().plusDays(2), false, 1);
    }

    @Test
    void givenValidTask_whenCreateTask_thenReturnCreatedTask() {
        TaskDTO taskDTO = new TaskDTO(1, "Visit museum", "Plan visit to Louvre", task.getTaskDeadline(), false, null);

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(taskRequestMapper.toEntity(any(TaskRequestDTO.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(appUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(taskMapper.toDTO(any(Task.class))).thenReturn(taskDTO);
        when(userTripService.isCurrentUserOwner(1)).thenReturn(true);
        TaskDTO result = taskService.createTask(1, taskRequestDTO);

        assertNotNull(result);
        assertEquals("Visit museum", result.taskName());
    }

    @Test
    void getAllTasks_WhenValidFilter_thenReturnTasks() {
        TaskFilterDTO filter = mock(TaskFilterDTO.class);
        when(filter.tripId()).thenReturn(1);
        when(jwtService.getCurrentUsername()).thenReturn("test@gmail.com");
        when(appUserRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        List<Task> expected = List.of(
                new Task(1, "rent house", "You must rent a house", LocalDateTime.of(2024, Month.JULY, 10, 10, 10), true, trip, user),
                new Task(2, "buy hotdogs", "You must buy hotdogs", LocalDateTime.of(2024, Month.AUGUST, 8, 12, 8), false, trip, user)
        );

        when(taskRepository.findAll(ArgumentMatchers.<Specification<Task>>any())).thenReturn(expected);

        List<Task> result = taskService.getAllTasks(filter);

        assertEquals(2, result.size());
        assertEquals("rent house", result.get(0).getTaskName());
    }

    @Test
    void getAllTasks_WhenFilterIsNull_thenThrowException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> taskService.getAllTasks(null));
        assertEquals("Trip ID cannot be null and must be a valid integer.", ex.getMessage());
    }

    @Test
    void getAllTasks_WhenTripIdIsNull_thenThrowException() {
        TaskFilterDTO filter = mock(TaskFilterDTO.class);
        when(filter.tripId()).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> taskService.getAllTasks(filter));
        assertEquals("Trip ID cannot be null and must be a valid integer.", ex.getMessage());
    }



    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setFullName("Test");
        user.setSex(Sex.male);
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setUserPassword("password");
        user.setPhoneNumber("1231412");
        return user;
    }

    private Trip createTrip() {
        return new Trip(1, "Europe Trip", null, null, null, null,
                LocalDateTime.of(2023, 7, 1, 12, 0),
                LocalDateTime.of(2023, 7, 10, 12, 0),
                true, Collections.emptyList());
    }

    private Task createTask(Trip trip, AppUser user) {
        return new Task(1, "Visit museum", "Plan visit to Louvre", LocalDateTime.now().plusDays(2), false, trip, user);
    }
}
