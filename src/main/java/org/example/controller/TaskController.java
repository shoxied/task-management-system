package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.task.NewTaskDto;
import org.example.dto.task.ResponseTaskDto;
import org.example.entity.Task;
import org.example.service.TaskService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create-task")
    public ResponseTaskDto createNewTask(@NonNull HttpServletRequest request,
                                         @RequestBody NewTaskDto newTaskDto){
        return taskService.createNewTask(request, newTaskDto);
    }

    @GetMapping("/set-executor")
    public void setTaskExecutor(@RequestParam(name = "taskId") int taskId,
                                @RequestParam(name = "executorUsername") String executorUsername,
                                @NonNull HttpServletRequest request) throws Exception {
        try {
            taskService.setTaskExecutor(taskId, executorUsername, request);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
