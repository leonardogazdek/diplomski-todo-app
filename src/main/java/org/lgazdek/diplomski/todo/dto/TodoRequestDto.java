package org.lgazdek.diplomski.todo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoRequestDto {
    @NotNull
    private String content;
    private String dueDate;
}
