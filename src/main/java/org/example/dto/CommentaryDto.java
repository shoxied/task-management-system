package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.auth.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentaryDto {
    private int id;
    private String commentary_body;
    private UserDto user;
}
