package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.UserDto;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.dto.task.Status;
import org.example.dto.task.TasksResult;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.exception.ApplicationError;
import org.example.exception.TaskNotFoundException;
import org.example.repo.PageableTaskRepository;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final PageableTaskRepository pageableTaskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ResponseTaskDto getTaskById(int taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        return ResponseTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserDto.builder()
                        .id(task.getAuthor().getId())
                        .username(task.getAuthor().getUsername())
                        .email(task.getAuthor().getEmail())
                        .build())
                .executor(UserDto.builder()
                        .id(task.getExecutor().getId())
                        .username(task.getExecutor().getUsername())
                        .email(task.getExecutor().getEmail())
                        .build())
                .commentaries(task.getCommentaries())
                .build();
    }

    public TasksResult getAllTasks(int page) {

        return null;
    }

    public ResponseTaskDto createNewTask(HttpServletRequest request, NewTaskDto newTaskDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        Task task = taskRepository.save(Task.builder()
                        .title(newTaskDto.getTitle())
                        .description(newTaskDto.getDescription())
                        .status(Status.CREATED.name())
                        .priority(newTaskDto.getPriority())
                        .author(user)
                .build());

        return ResponseTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();
    }

    public ResponseEntity<?> setTaskExecutor(int taskId, String executorUsername) throws RuntimeException {
        User executor = userRepository.findByUsername(executorUsername).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username '%s' not found", executorUsername)));
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        task.setExecutor(executor);
        taskRepository.save(task);

        return ResponseEntity.ok(String.format("user '%s' was appointed executor of task '%s'", executor.getUsername(), task.getTitle()));
    }

    public ResponseEntity<?> setTaskStatus(int taskId, Status status, HttpServletRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        if (task.getExecutor() != user) {
            return new ResponseEntity<>(new ApplicationError(HttpStatus.LOCKED.value(), "you are not the executor of this task"), HttpStatus.LOCKED);
        }

        task.setStatus(status.name());
        taskRepository.save(task);

        return ResponseEntity.ok(String.format("in task with id '%s' now status is '%s'", taskId, status));
    }
}
