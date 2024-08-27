package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.UserDto;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.exception.TaskNotFoundException;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ResponseTaskDto createNewTask(HttpServletRequest request, NewTaskDto newTaskDto) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        Task task = taskRepository.save(Task.builder()
                        .title(newTaskDto.getTitle())
                        .description(newTaskDto.getDescription())
                        .status("Created")
                        .priority(newTaskDto.getPriority())
                        .author_id(user)
                .build());

        return ResponseTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author_id(UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();
    }

    public void setTaskExecutor(int taskId, String executorUsername, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        User executor = userRepository.findByUsername(executorUsername).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username '%s' not found", executorUsername)));
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));
    }
}
