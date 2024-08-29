package org.example.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TasksResult {
    private int totalElements;
    private int totalPages;
    private int page;
    private List<ResponseTaskDto> tasks;
}
