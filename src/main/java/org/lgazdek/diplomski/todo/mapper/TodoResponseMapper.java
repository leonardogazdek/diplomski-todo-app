package org.lgazdek.diplomski.todo.mapper;

import org.lgazdek.diplomski.todo.dto.TodoResponseDto;
import org.mapstruct.Mapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

@Mapper
public interface TodoResponseMapper {
    TodoResponseDto toDto(Map<String, AttributeValue> source);
    List<TodoResponseDto> toDtoCollection(List<Map<String, AttributeValue>> source);
    default String map(AttributeValue value) {
        return value.s();
    }
}
