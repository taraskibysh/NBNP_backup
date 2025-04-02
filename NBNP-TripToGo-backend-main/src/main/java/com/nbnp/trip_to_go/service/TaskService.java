package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.TaskDTO;
import com.nbnp.trip_to_go.dto.TaskFilterDTO;
import com.nbnp.trip_to_go.dto.TaskRequestDTO;
import com.nbnp.trip_to_go.dto.mapping.TaskMapper;
import com.nbnp.trip_to_go.dto.mapping.TaskRequestMapper;
import com.nbnp.trip_to_go.exception.UserNotFoundException;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Task;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TaskRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.repository.specification.TaskSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TripRepository tripRepository;
    private final AppUserRepository appUserRepository;
    private final TaskMapper taskMapper;
    private final TaskRequestMapper taskRequestMapper;
    private final JwtService jwtService;
    private final UserTripService userTripService;

    public TaskService(TaskRepository taskRepository, TripRepository tripRepository,
                       TaskMapper taskMapper, TaskRequestMapper taskRequestMapper,
                       AppUserRepository appUserRepository,
                       JwtService jwtService, UserTripService userTripService) {
        this.taskRepository = taskRepository;
        this.tripRepository = tripRepository;
        this.taskMapper = taskMapper;
        this.taskRequestMapper = taskRequestMapper;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.userTripService = userTripService;
    }

    public List<Task> getAllTasks(TaskFilterDTO filter) {
        if (filter == null || filter.tripId() == null) {
            throw new IllegalArgumentException("Trip ID cannot be null and must be a valid integer.");
        }

        String userEmail = jwtService.getCurrentUsername();
        AppUser currentUser = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + userEmail));

        Specification<Task> spec = TaskSpecification.applyFilters(filter, currentUser.getId());
        return taskRepository.findAll(spec);
    }

    public TaskDTO getTaskById(int tripId, int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));
        if (task.getTrip().getId() != tripId) {
            throw new EntityNotFoundException("Task does not belong to trip " + tripId);
        }
        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskDTO createTask(int tripId, TaskRequestDTO taskDTO) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to create tasks");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        Task task = taskRequestMapper.toEntity(taskDTO);
        task.setTrip(trip);

        if (taskDTO.userId() != null) {
            AppUser user = appUserRepository.findById(taskDTO.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id " + taskDTO.userId()));
            task.setUser(user);
        }

        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    @Transactional
    public TaskDTO updateTask(int tripId, int taskId, TaskRequestDTO taskDTO) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to update tasks");
        }
        Task existing = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));

        if (existing.getTrip().getId() != tripId) {
            throw new EntityNotFoundException("Task does not belong to trip " + tripId);
        }

        existing.setTaskName(taskDTO.taskName());
        existing.setTaskDescription(taskDTO.taskDescription());
        existing.setTaskDeadline(taskDTO.taskDeadline());
        existing.setDone(taskDTO.isDone());

        AppUser user = appUserRepository.findById(taskDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + taskDTO.userId()));
        existing.setUser(user);

        Task updated = taskRepository.save(existing);
        return taskMapper.toDTO(updated);
    }

    @Transactional
    public void deleteTask(int tripId, int taskId) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to delete tasks");
        }
        Task existing = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));
        if (existing.getTrip().getId() != tripId) {
            throw new EntityNotFoundException("Task does not belong to trip " + tripId);
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public TaskDTO markTaskAsDone(int tripId, int taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));
        if (task.getTrip().getId() != tripId) {
            throw new EntityNotFoundException("Task does not belong to trip " + tripId);
        }
        task.setDone(true);
        return taskMapper.toDTO(taskRepository.save(task));
    }

    public TaskDTO assignUserToTask(int tripId, int taskId, int userId) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to assign users to tasks");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + taskId));
        if (task.getTrip().getId() != tripId) {
            throw new EntityNotFoundException("Task does not belong to trip " + tripId);
        }

        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        task.setUser(appUser);
        return taskMapper.toDTO(taskRepository.save(task));
    }
}
