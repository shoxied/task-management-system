package org.example.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.auth.UserDto;
import org.example.entity.Commentary;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTaskDto {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private UserDto author;
    private UserDto executor;
    private List<Commentary> commentaries = new ArrayList<>();
}
