package ru.romanov.pastbin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnedPostDTO {
    private String title;
    private String text;
}
