package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.dto.task.Status;
import org.example.dto.task.TasksResult;
import org.example.entity.Task;
import org.example.exception.ApplicationError;
import org.example.exception.TaskNotFoundException;
import org.example.service.TaskService;
import org.example.validator.EnumConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/get-task-by-id")
    public ResponseTaskDto getTaskById(@RequestParam(name = "taskId") int taskId) {
        return taskService.getTaskById(taskId);
    }

    @GetMapping("/get-all-tasks")
    public TasksResult getAllTasks(@RequestParam(name = "page") int page) {
        return taskService.getAllTasks(page);
    }

    @PostMapping("/create-task")
    public ResponseTaskDto createNewTask(@NonNull HttpServletRequest request,
                                         @RequestBody NewTaskDto newTaskDto){
        return taskService.createNewTask(request, newTaskDto);
    }

    @GetMapping("/set-executor")
    public ResponseEntity<?> setTaskExecutor(@RequestParam(name = "taskId") int taskId,
                                             @RequestParam(name = "executorUsername") String executorUsername) {
        return taskService.setTaskExecutor(taskId, executorUsername);
    }

    @GetMapping("/set-task-status")
    public ResponseEntity<?> setTaskStatus( @RequestParam(name = "taskId") int taskId,
                                            @RequestParam(name = "status") Status status,
                                            @NonNull HttpServletRequest request) {
        try {
            return taskService.setTaskStatus(taskId, status, request);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.NOT_FOUND.value(), String.format("task with id '%s' not found", taskId)), HttpStatus.NOT_FOUND);
        }
    }
}
