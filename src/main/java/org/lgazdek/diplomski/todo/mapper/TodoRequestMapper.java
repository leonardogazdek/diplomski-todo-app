package org.lgazdek.diplomski.todo.mapper;

import org.lgazdek.diplomski.todo.dto.TodoRequestDto;
import org.mapstruct.Mapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Mapper
public interface TodoRequestMapper {
    default HashMap<String, AttributeValue> toModel(TodoRequestDto source) {
        HashMap<String, AttributeValue> newMap = new HashMap<>();
        newMap.put("id", AttributeValue.fromS(UUID.randomUUID().toString()));
        newMap.put("content", AttributeValue.fromS(source.getContent()));
        if(source.getDueDate() != null) {
            newMap.put("dueDate", AttributeValue.fromS(source.getDueDate()));
        }
        newMap.put("createdAt", AttributeValue.fromS(Instant.now().toString()));
        return newMap;
    }

    default HashMap<String, AttributeValueUpdate> toUpdateModel(TodoRequestDto source) {
        HashMap<String, AttributeValueUpdate> newMap = new HashMap<>();
        newMap.put("content", AttributeValueUpdate.builder().value(AttributeValue.fromS(source.getContent())).build());
        if(source.getDueDate() != null) {
            newMap.put("dueDate", AttributeValueUpdate.builder().value(AttributeValue.fromS(source.getDueDate())).build());
        }
        return newMap;
    }
}
