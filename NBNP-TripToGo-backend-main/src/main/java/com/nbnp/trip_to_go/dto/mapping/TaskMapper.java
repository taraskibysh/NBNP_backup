package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.TaskDTO;
import com.nbnp.trip_to_go.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "taskName", target = "taskName")
    @Mapping(source = "taskDescription", target = "taskDescription")
    @Mapping(source = "taskDeadline", target = "taskDeadline")
    @Mapping(source = "done", target = "isDone")
    @Mapping(source = "user", target = "user", qualifiedByName = "toDTO")
    TaskDTO toDTO(Task task);

    @Mapping(source = "taskName", target = "taskName")
    @Mapping(source = "taskDescription", target = "taskDescription")
    @Mapping(source = "taskDeadline", target = "taskDeadline")
    @Mapping(source = "isDone", target = "done")
    Task toEntity(TaskDTO dto);
}
