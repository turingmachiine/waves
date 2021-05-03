package ru.bvb.waves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class PasswordDto {
    String old;
    String newPass;
}
