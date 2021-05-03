package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bvb.waves.models.State;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateUpdateDto {
    Long userId;
    State state;
}
