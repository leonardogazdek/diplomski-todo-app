package org.lgazdek.diplomski.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lgazdek.diplomski.todo.dto.TodoRequestDto;
import org.lgazdek.diplomski.todo.dto.TodoResponseDto;
import org.lgazdek.diplomski.todo.mapper.TodoRequestMapper;
import org.lgazdek.diplomski.todo.mapper.TodoResponseMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final DynamoDbClient dynamoDbClient;
    private final TodoResponseMapper todoResponseMapper;
    private final TodoRequestMapper todoRequestMapper;

    @Value("${aws.dynamoDb.todoTableName}")
    private String todoTableName;

    @Operation(summary = "Gets all todo items")
    @GetMapping("/api/todo")
    public List<TodoResponseDto> getTodos() {
        log.info("Primljen zahtjev getTodos");
        ScanRequest scanRequest = ScanRequest
            .builder()
            .tableName(todoTableName)
            .consistentRead(true)
            .build();
        ScanResponse data = dynamoDbClient.scan(scanRequest);
        return todoResponseMapper.toDtoCollection(data.items()).stream().sorted((a, b) -> {
            if(a.getDueDate() == null && b.getDueDate() == null) {
                return a.getCreatedAt().compareTo(b.getCreatedAt());
            }
            if(a.getDueDate() == null) return 1;
            if(b.getDueDate() == null) return -1;
            return a.getDueDate().compareTo(b.getDueDate());
        }).toList();
    }

    @Operation(summary = "Create a todo item")
    @PostMapping("/api/todo")
    public TodoResponseDto createTodo(@RequestBody @Valid TodoRequestDto reqDto) {
        log.info("Primljen zahtjev createTodo");
        Map<String, AttributeValue> insertItem = todoRequestMapper.toModel(reqDto);
        PutItemRequest putItemRequest = PutItemRequest
            .builder()
            .tableName(todoTableName)
            .item(insertItem)
            .build();
        dynamoDbClient.putItem(putItemRequest);
        return todoResponseMapper.toDto(insertItem);
    }

    @Operation(summary = "Delete a todo item")
    @DeleteMapping("/api/todo/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable String id) {
        log.info("Primljen zahtjev deleteTodo");
        DeleteItemRequest deleteItemRequest = DeleteItemRequest
            .builder()
            .tableName(todoTableName)
            .key(Map.of("id", AttributeValue.fromS(id)))
            .build();
        dynamoDbClient.deleteItem(deleteItemRequest);
    }

    @Operation(summary = "Update a todo item")
    @PatchMapping("/api/todo/{id}")
    public void updateTodo(@PathVariable String id, @RequestBody @Valid TodoRequestDto reqDto) {
        log.info("Primljen zahtjev updateTodo");
        Map<String, AttributeValueUpdate> updateItem = todoRequestMapper.toUpdateModel(reqDto);
        UpdateItemRequest updateItemRequest = UpdateItemRequest
            .builder()
            .tableName(todoTableName)
            .key(Map.of("id", AttributeValue.fromS(id)))
            .attributeUpdates(updateItem)
            .build();

        dynamoDbClient.updateItem(updateItemRequest);
    }
}
