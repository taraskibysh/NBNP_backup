package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.dto.TaskDTO;
import com.nbnp.trip_to_go.dto.TaskFilterDTO;
import com.nbnp.trip_to_go.dto.TaskRequestDTO;
import com.nbnp.trip_to_go.dto.mapping.TaskMapper;
import com.nbnp.trip_to_go.model.SortType;
import com.nbnp.trip_to_go.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks(@PathVariable Integer tripId,
                                     @RequestParam(required = false) Boolean isDone,
                                     @RequestParam(required = false) SortType sortType,
                                     @RequestParam(required = false) Boolean myTasks) {

        TaskFilterDTO filter = new TaskFilterDTO(tripId, isDone, sortType, myTasks);
        return taskService.getAllTasks(filter).stream().map(taskMapper::toDTO).toList();
    }

    @GetMapping("/{taskId}")
    public TaskDTO getTaskById(@PathVariable int tripId, @PathVariable int taskId) {
        return taskService.getTaskById(tripId, taskId);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@PathVariable int tripId,
                                              @Valid @RequestBody TaskRequestDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(tripId, taskDTO));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable int tripId,
                                              @PathVariable int taskId,
                                              @Valid @RequestBody TaskRequestDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(tripId, taskId, taskDTO));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable int tripId, @PathVariable int taskId) {
        taskService.deleteTask(tripId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/done")
    public ResponseEntity<TaskDTO> markTaskAsDone(@PathVariable int tripId, @PathVariable int taskId) {
        return ResponseEntity.ok(taskService.markTaskAsDone(tripId, taskId));
    }


    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskDTO> assignUserToTask(@PathVariable int tripId,
                                                    @PathVariable int taskId,
                                                    @PathVariable Integer userId) {
        return ResponseEntity.ok(taskService.assignUserToTask(tripId, taskId, userId));
    }
}
