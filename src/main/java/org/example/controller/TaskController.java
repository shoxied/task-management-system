package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.dto.task.Status;
import org.example.dto.task.TasksResult;
import org.example.service.TaskService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/write-a-comment")
    public String writeAComment(@NonNull HttpServletRequest request,
                                @RequestParam(name = "comment") String commentBody,
                                @RequestParam(name = "taskId") int taskId) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return taskService.writeAComment(commentBody, taskId, token);
    }

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
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return taskService.createNewTask(token, newTaskDto);
    }

    @GetMapping("/set-executor")
    public String setTaskExecutor(@RequestParam(name = "taskId") int taskId,
                                         @RequestParam(name = "executorUsername") String executorUsername) {
        return taskService.setTaskExecutor(taskId, executorUsername);
    }

    @GetMapping("/set-task-status")
    public String setTaskStatus( @RequestParam(name = "taskId") int taskId,
                                            @RequestParam(name = "status") Status status,
                                            @NonNull HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return taskService.setTaskStatus(taskId, status, token);
    }
}
