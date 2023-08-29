package org.lgazdek.diplomski.todo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoResponseDto {
    @NotNull
    private String id;
    @NotNull
    private String content;
    private String dueDate;
    @NotNull
    private String createdAt;
}
