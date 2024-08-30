package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CommentaryDto;
import org.example.dto.auth.UserDto;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.dto.task.Status;
import org.example.dto.task.TasksResult;
import org.example.entity.Commentary;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.exception.LockedException;
import org.example.exception.TaskNotFoundException;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final JwtService jwtService;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ResponseTaskDto getTaskById(int taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        List<CommentaryDto> commentaries = new ArrayList<>();
        task.getCommentaries().forEach(commentary -> {
            commentaries.add(CommentaryDto.builder()
                    .id(commentary.getId())
                    .commentary_body(commentary.getCommentary_body())
                    .user(UserDto.builder()
                            .id(commentary.getUser().getId())
                            .username(commentary.getUser().getUsername())
                            .email(commentary.getUser().getEmail())
                            .build())
                    .build());
        });

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
                .commentaries(commentaries)
                .build();
    }

    public TasksResult getAllTasks(int page) {

        return null;
    }

    public ResponseTaskDto createNewTask(String token, NewTaskDto newTaskDto) {
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

    public String setTaskExecutor(int taskId, String executorUsername) {
        User executor = userRepository.findByUsername(executorUsername).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username '%s' not found", executorUsername)));
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        task.setExecutor(executor);
        taskRepository.save(task);

        return String.format("user '%s' was appointed executor of task '%s'", executor.getUsername(), task.getTitle());
    }

    public String setTaskStatus(int taskId, Status status, String token) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        if (task.getExecutor() != user) {
            throw new LockedException("you are not the executor of this task");
        }

        task.setStatus(status.name());
        taskRepository.save(task);

        return String.format("in task with id '%s' now status is '%s'", taskId, status);
    }

    @Transactional
    public String writeAComment(String commentBody, int taskId, String token) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(String.format("Task with id '%s' not found", taskId)));

        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow();

        Commentary commentary = Commentary.builder()
                .commentary_body(commentBody)
                .task(task)
                .user(user)
                .build();

        List<Commentary> commentaries = task.getCommentaries();
        commentaries.add(commentary);

        task.setCommentaries(commentaries);
        taskRepository.save(task);

        return String.format("commentary '%s' successfully saved into task '%s'", commentBody, task.getTitle());
    }
}
